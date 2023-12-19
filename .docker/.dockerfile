# -------------------------------------------------
# *** ARGUMENTS ***
# -------------------------------------------------
ARG GITHUB_PROJECT=hainguyen81/nlh4j.git
ARG GITHUB_USER=hainguyen81
ARG GITHUB_TOKEN=
ARG GIT_BRANCH=master
ARG MAVEN_VERSION=3.8.4
ARG JDK_MAJOR_VERSION=11
ARG NGINX_PORT=80


# -------------------------------------------------
# *** Pull MAVEN https://hub.docker.com/_/maven/tags?page=1&name=3.8.4 ***
# -------------------------------------------------
# Import certificates
# -------------------------------------------------
FROM maven:$MAVEN_VERSION-eclipse-temurin-$JDK_MAJOR_VERSION-alpine as maven

ARG JDK_MAJOR_VERSION
ENV JDK_MAJOR_VERSION=$JDK_MAJOR_VERSION

# maven folders enviroment
ENV MAVEN_HOME=/usr/share/maven
ENV MAVEN_REF=$MAVEN_HOME/ref
ENV MAVEN_REPOSITORY=$MAVEN_REF/repository
ENV MAVEN_CONFIG=$MAVEN_HOME/conf

# -------------------------------------------------
RUN mkdir -p $MAVEN_REPOSITORY
COPY --from=certificate repo.maven.apache.org.crt $JAVA_HOME/jre/lib/security/
RUN echo [jdk] JAVA_HOME: $JAVA_HOME \
	&& keytool -noprompt -trustcacerts \
	-keystore $JAVA_HOME/jre/lib/security/cacerts \
	-storepass changeit -importcert \
	-alias repo.maven.apache.org \
	-file $JAVA_HOME/jre/lib/security/repo.maven.apache.org.crt

# create maven settings.xml, toolchains.xml
RUN echo \
    "<settings xmlns='http://maven.apache.org/SETTINGS/1.0.0' \
    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
    xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd'> \
        <localRepository>$MAVEN_REPOSITORY</localRepository> \
        <interactiveMode>true</interactiveMode> \
		<pluginGroups> \
			<pluginGroup>com.sonatype.maven.plugins</pluginGroup> \
			<pluginGroup>org.sonatype.plugins</pluginGroup> \
			<pluginGroup>org.apache.maven.plugins</pluginGroup> \
			<pluginGroup>org.eclipse.m2e</pluginGroup> \
		</pluginGroups> \
    </settings>" \
    > $MAVEN_CONFIG/settings.xml
RUN	echo \
    "<toolchains xmlns='http://maven.apache.org/TOOLCHAINS/1.1.0' \
	xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
	xsi:schemaLocation='http://maven.apache.org/TOOLCHAINS/1.1.0 http://maven.apache.org/xsd/toolchains-1.1.0.xsd'> \
        <toolchain> \
			<type>jdk</type> \
			<provides> \
				<version>$JDK_MAJOR_VERSION</version> \
				<vendor>openjdk</vendor> \
			</provides> \
			<configuration> \
				<jdkHome>$JAVA_HOME</jdkHome> \
			</configuration> \
		</toolchain> \
    </toolchains>" \
    > $MAVEN_CONFIG/toolchains.xml



# -------------------------------------------------
# *** Pull GIT https://hub.docker.com/r/alpine/git
# add `-c http.sslVerify=false` to git clone for fixing SSL error `SSL certificate problem: self signed certificate in certificate chain`
# details: https://github.com/docker/for-win/issues/4324 ***
# -------------------------------------------------
FROM alpine/git as clone
ARG GITHUB_PROJECT
ARG GITHUB_USER
ARG GITHUB_TOKEN
ARG GIT_BRANCH

# -------------------------------------------------
WORKDIR /git

# Clone GIT source branch
RUN echo [clone] Clone GIT with credenticals: $GITHUB_USER:$GITHUB_TOKEN \
	&& git -c http.sslVerify=false clone -b $GIT_BRANCH https://$GITHUB_USER:$GITHUB_TOKEN@github.com/$GITHUB_PROJECT project



# -------------------------------------------------
# *** Maven build ***
# -------------------------------------------------
FROM maven as maven-build

# -------------------------------------------------
WORKDIR /app

# Copy project from git clone
RUN mkdir -p project
COPY --from=clone git/project project

# Copy maven settings
COPY --from=maven $MAVEN_CONFIG/settings.xml project
COPY --from=maven $MAVEN_CONFIG/toolchains.xml project

# -------------------------------------------------
# [maven-build - dev,jdk$JDK_MAJOR_VERSION] Build root and resolve dependencies offline for publish repository later
RUN mkdir -p logs
RUN echo [maven-build - dev,jdk$JDK_MAJOR_VERSION] Build root and resolve dependencies offline for publish repository later \
	&& mvn \
	-s project/settings.xml -t project/toolchains.xml \
	-Dmaven.repo.local=$MAVEN_REPOSITORY \
	-X -N -B -U -ntp -q -T 7 \
	--fail-never \
	-P dev,jdk$JDK_MAJOR_VERSION \
	dependency:resolve dependency:go-offline install \
	-Dmaven.wagon.http.ssl.insecure=true \
	-DskipTests=true -Dmaven.test.skip=true \
	--log-file logs/maven.resolve.dependencies.log \
	-f project/pom.xml

# -------------------------------------------------
# Build project offline
RUN echo Repository $MAVEN_REPOSITORY - JDK_MAJOR_VERSION $JDK_MAJOR_VERSION - Profiles dev,jdk$JDK_MAJOR_VERSION \
	&& mvn \
	-s project/settings.xml -t project/toolchains.xml \
	-Dmaven.repo.local=$MAVEN_REPOSITORY \
	-X -B -U -q -T 7 \
	--fail-never \
	-P dev,jdk$JDK_MAJOR_VERSION,issuedDep,licenseTool \
	clean install \
	-Dmaven.wagon.http.ssl.insecure=true \
	-DskipTests=true -Dmaven.test.skip=true \
	--log-file logs/maven.build.log \
	-f project/pom.xml



# -------------------------------------------------
# *** Serve repository ***
# -------------------------------------------------
FROM nginx:stable-alpine-slim
ARG JDK_MAJOR_VERSION
ENV JDK_MAJOR_VERSION=$JDK_MAJOR_VERSION
ARG NGINX_PORT

# remove default conf
RUN rm /etc/nginx/conf.d/default.conf

# copy repository for serving
COPY --from=maven-build /app/logs /usr/share/nginx/logs
COPY --from=maven-build /usr/share/maven/ref/repository /usr/share/nginx/repository
COPY --from=certificate jdk$JDK_MAJOR_VERSION/openssl/jdk$JDK_MAJOR_VERSION.org.nlh4j.keystore.crt-cabundle.crt /etc/nginx/certs/jdk$JDK_MAJOR_VERSION.org.nlh4j.crt
COPY --from=certificate jdk$JDK_MAJOR_VERSION/openssl/jdk$JDK_MAJOR_VERSION.org.nlh4j.keystore.key /etc/nginx/certs/jdk$JDK_MAJOR_VERSION.org.nlh4j.key

# copy conf from build context
RUN echo "server { \
		listen $NGINX_PORT; \
		listen [::]:$NGINX_PORT; \
		\
		resolver 127.0.0.1; \
		autoindex on; \
		\
		server_name _; \
		server_tokens off; \
		\
		root /usr/share/nginx/repository; \
		gzip on; \
		gzip_static off; \
		\
		location / { \
			root /usr/share/nginx/repository; \
			autoindex on; \
			autoindex_exact_size on; \
			autoindex_format html; \
			autoindex_localtime on; \
			expires -1; \
			default_type text/plain; \
		} \
		\
		location = /health { \
			types {} \
			default_type text/plain; \
			return 200 \"OK\"; \
		} \
	} \
	\
	server { \
		listen 443 ssl; \
		listen [::]:443; \
		\
		resolver 127.0.0.1; \
		autoindex on; \
		\
		server_name org.nlh4j.maven; \
		ssl_certificate /etc/nginx/certs/jdk$JDK_MAJOR_VERSION.org.nlh4j.crt; \
		ssl_certificate_key /etc/nginx/certs/jdk$JDK_MAJOR_VERSION.org.nlh4j.key; \
		\
		root /usr/share/nginx/repository; \
		gzip on; \
		gzip_static off; \
		\
		location / { \
			root /usr/share/nginx/repository; \
			autoindex on; \
			autoindex_exact_size on; \
			autoindex_format html; \
			autoindex_localtime on; \
			expires -1; \
			default_type text/plain; \
		} \
		\
		location = /health { \
			types {} \
			default_type text/plain; \
			return 200 \"OK\"; \
		} \
	}" \
	> /etc/nginx/conf.d/default.conf

EXPOSE $NGINX_PORT
EXPOSE 443
CMD [ "nginx", "-g", "daemon off;" ]

