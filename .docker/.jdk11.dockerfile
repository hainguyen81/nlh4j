# -------------------------------------------------
# PREPARATION
# -------------------------------------------------
FROM ubuntu as base
WORKDIR /data
RUN mkdir -p /root/.cert
COPY --from=certificate repo.maven.apache.org.crt .
RUN file="$(ls -1 /root/.cert)" && echo $file
RUN echo "Done preparation"

# -------------------------------------------------
# Pull GIT https://hub.docker.com/r/alpine/git
# add `-c http.sslVerify=false` to git clone for fixing SSL error `SSL certificate problem: self signed certificate in certificate chain`
# details: https://github.com/docker/for-win/issues/4324
# -------------------------------------------------
FROM alpine/git as clone
WORKDIR /app
ARG GITHUB_USER # use ARG below `FROM` for applying arguments from outside
ARG GITHUB_TOKEN # use ARG below `FROM` for applying arguments from outside
ARG GIT_BRANCH # use ARG below `FROM` for applying arguments from outside
RUN echo "Clone GIT with credenticals: $GITHUB_USER:$GITHUB_TOKEN https://$GITHUB_USER:$GITHUB_TOKEN@github.com/hainguyen81/nlh4j.git"
RUN git -c http.sslVerify=false clone -b $GIT_BRANCH https://$GITHUB_USER:$GITHUB_TOKEN@github.com/hainguyen81/nlh4j.git
RUN echo "Done GIT clone"

# -------------------------------------------------
# Pull MAVEN https://hub.docker.com/_/maven/tags?page=1&name=3.8.4
# -------------------------------------------------
# Import certificates
# -------------------------------------------------
FROM maven:3.8.4-openjdk-11-slim as jdk
LABEL maintainer="Hai Nguyen (hainguyenjc@gmail.com)"
RUN echo "JAVA_HOME: $JAVA_HOME"
COPY --from=base /data/repo.maven.apache.org.crt $JAVA_HOME/jre/lib/security/
RUN keytool -trustcacerts -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -importcert -alias repo.maven.apache.org -file $JAVA_HOME/jre/lib/security/repo.maven.apache.org.crt

# -------------------------------------------------
# Maven Settings
# -------------------------------------------------
FROM jdk as maven-settings
RUN echo \
    "<settings xmlns='http://maven.apache.org/SETTINGS/1.0.0\' \
    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
    xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd'> \
        <localRepository>/root/.m2/repository</localRepository> \
		<pluginGroups> \
			<pluginGroup>com.sonatype.maven.plugins</pluginGroup> \
			<pluginGroup>org.sonatype.plugins</pluginGroup> \
			<pluginGroup>org.apache.maven.plugins</pluginGroup> \
			<pluginGroup>org.eclipse.m2e</pluginGroup> \
		</pluginGroups> \
    </settings>" \
    > /usr/share/maven/conf/settings.xml
RUN	echo \
    "<toolchains xmlns='http://maven.apache.org/TOOLCHAINS/1.1.0\' \
	xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
	xsi:schemaLocation='http://maven.apache.org/TOOLCHAINS/1.1.0 http://maven.apache.org/xsd/toolchains-1.1.0.xsd'> \
        <toolchain> \
			<type>jdk</type> \
			<provides> \
				<version>11</version> \
				<vendor>openjdk</vendor> \
			</provides> \
			<configuration> \
				<jdkHome>/usr/local/openjdk-11</jdkHome> \
			</configuration> \
		</toolchain> \
    </toolchains>" \
    > /usr/share/maven/conf/toolchains.xml
RUN	mkdir -p /root/.m2 && mkdir /root/.m2/repository
RUN echo \
    "<settings xmlns='http://maven.apache.org/SETTINGS/1.0.0\' \
    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
    xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd'> \
        <localRepository>/root/.m2/repository</localRepository> \
		<pluginGroups> \
			<pluginGroup>com.sonatype.maven.plugins</pluginGroup> \
			<pluginGroup>org.sonatype.plugins</pluginGroup> \
			<pluginGroup>org.apache.maven.plugins</pluginGroup> \
			<pluginGroup>org.eclipse.m2e</pluginGroup> \
		</pluginGroups> \
    </settings>" \
    > /root/.m2/settings.xml
RUN	echo \
    "<toolchains xmlns='http://maven.apache.org/TOOLCHAINS/1.1.0\' \
	xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
	xsi:schemaLocation='http://maven.apache.org/TOOLCHAINS/1.1.0 http://maven.apache.org/xsd/toolchains-1.1.0.xsd'> \
        <toolchain> \
			<type>jdk</type> \
			<provides> \
				<version>11</version> \
				<vendor>openjdk</vendor> \
			</provides> \
			<configuration> \
				<jdkHome>/usr/local/openjdk-11</jdkHome> \
			</configuration> \
		</toolchain> \
    </toolchains>" \
    > /root/.m2/toolchains.xml

# -------------------------------------------------
# Build Project
# -------------------------------------------------
FROM maven-settings as maven-build
WORKDIR /app

COPY --from=clone /app/nlh4j /app
RUN echo "Build parent POM before building modules - [-N] non-recusive for building only parent POM"
RUN --mount=type=cache,target=/root/.m2 \
	mvn -P dev,jdk11 -N -T 5 -U -X -Dmaven.wagon.http.ssl.insecure=true -DskipTests=true -Dmaven.test.skip=true clean install

RUN echo "Build child modules after building only parent POM"
RUN --mount=type=cache,target=/root/.m2 \
	mvn -P dev,jdk11,licenseTool -T 5 -U -X -Dmaven.wagon.http.ssl.insecure=true -DskipTests=true -Dmaven.test.skip=true clean install

