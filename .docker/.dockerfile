# -------------------------------------------------
# *** ARGUMENTS ***
# -------------------------------------------------
ARG GITHUB_PROJECT=hainguyen81/nlh4j.git
ARG GITHUB_USER=hainguyen81
ARG GITHUB_TOKEN=
ARG GIT_BRANCH=master
ARG JDK=11


# -------------------------------------------------
# *** PREPARATION ***
# -------------------------------------------------
FROM alpine:latest as base
LABEL maintainer="Hai Nguyen (hainguyenjc@gmail.com)"

ARG GITHUB_PROJECT
ARG GITHUB_USER
ARG GITHUB_TOKEN
ARG GIT_BRANCH
ARG JDK

RUN echo "[base] GITHUB_PROJECT	- $GITHUB_PROJECT"
RUN echo "[base] GITHUB_USER	- $GITHUB_USER"
RUN echo "[base] GITHUB_TOKEN	- $GITHUB_TOKEN"
RUN echo "[base] GIT_BRANCH		- $GIT_BRANCH"
RUN echo "[base] JDK			- $JDK"

# -------------------------------------------------
WORKDIR /data
RUN mkdir -p .cert && mkdir -p .cert/jdk$JDK
RUN mkdir -p .dep
COPY --from=certificate repo.maven.apache.org.crt .cert/
COPY --from=certificate jdk$JDK .cert/jdk$JDK
COPY --from=dep . .dep/


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
ARG JDK

# -------------------------------------------------
WORKDIR /app

# Clone GIT source branch
RUN echo "[clone] Clone GIT with credenticals: $GITHUB_USER:$GITHUB_TOKEN"
RUN git -c http.sslVerify=false clone -b $GIT_BRANCH https://$GITHUB_USER:$GITHUB_TOKEN@github.com/$GITHUB_PROJECT


# -------------------------------------------------
# *** Pull MAVEN https://hub.docker.com/_/maven/tags?page=1&name=3.8.4 ***
# -------------------------------------------------
# Import certificates
# -------------------------------------------------
#FROM maven:3.8.4-openjdk-$JDK-slim as jdk
FROM maven:3.8.4-eclipse-temurin-$JDK-alpine as jdk
ARG JDK

# The $MAVEN_CONFIG dir (default to /root/.m2) could be configured as a volume so anything copied there in a Dockerfile at build time is lost.
# For that reason the dir /usr/share/maven/ref/ exists, and anything in that directory will be copied on container startup to $MAVEN_CONFIG.
ENV USER_SHARE=/usr/share
ENV MAVEN_HOME=$USER_SHARE/maven
ENV MAVEN_CONFIG=$MAVEN_HOME/conf
ENV MAVEN_REPOSITORY=$MAVEN_HOME/repository
ENV MAVEN_CERT=$MAVEN_HOME/.cert
ENV MAVEN_DEP=$MAVEN_HOME/.dep
RUN	mkdir -p $USER_SHARE \
	&& mkdir -p $MAVEN_HOME \
	&& mkdir -p $MAVEN_CONFIG \
	&& mkdir -p $MAVEN_REPOSITORY \
	&& mkdir -p $MAVEN_CERT \
	&& mkdir -p $MAVEN_DEP

# create maven settings.xml, toolchains.xml
RUN echo \
    "<settings xmlns='http://maven.apache.org/SETTINGS/1.0.0\' \
    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' \
    xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd'> \
        <localRepository>$MAVEN_REPOSITORY</localRepository> \
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
	xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance\' \
	xsi:schemaLocation='http://maven.apache.org/TOOLCHAINS/1.1.0 http://maven.apache.org/xsd/toolchains-1.1.0.xsd\'> \
        <toolchain> \
			<type>jdk</type> \
			<provides> \
				<version>$JDK</version> \
				<vendor>openjdk</vendor> \
			</provides> \
			<configuration> \
				<jdkHome>$JAVA_HOME</jdkHome> \
			</configuration> \
		</toolchain> \
    </toolchains>" \
    > /usr/share/maven/conf/toolchains.xml

# -------------------------------------------------
WORKDIR /jdk
RUN echo "[jdk] JAVA_HOME: $JAVA_HOME"
COPY --from=base /data/.cert/repo.maven.apache.org.crt $JAVA_HOME/jre/lib/security/
RUN keytool -noprompt -trustcacerts -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -importcert -alias repo.maven.apache.org -file $JAVA_HOME/jre/lib/security/repo.maven.apache.org.crt


# -------------------------------------------------
# *** Maven Build (FINAL) ***
# -------------------------------------------------
FROM jdk as maven-build
ARG JDK

# -------------------------------------------------
RUN	mkdir -p logs
COPY --from=clone app/* .
COPY --from=jdk $MAVEN_CONFIG/settings.xml .
COPY --from=jdk $MAVEN_CONFIG/toolchains.xml .
COPY --from=base /data/.cert/jdk$JDK $MAVEN_CERT

# Build project: solve dependencies > build project
RUN echo "[maven-build - dev,jdk$JDK] Build root and resolve dependencies offline for publish repository later" \
	&& mvn \
	-s settings.xml -t toolchains.xml \
	-Dmaven.repo.local=$MAVEN_REPOSITORY \
	-X -N -B -U -ntp -q -T 7 \
	-Dmaven.wagon.http.ssl.insecure=true -DskipTests=true -Dmaven.test.skip=true \
	-P dev,jdk$JDK \
	clean dependency:resolve dependency:go-offline install \
	&& echo "[maven-build - dev,jdk$JDK,licenseTool,issuedDep] Build modules based on offline respository - use [issuedDep] profile to unzip dependencies due to issue on docker build" \
	&& mvn \
	-s settings.xml -t toolchains.xml \
	-Dmaven.repo.local=$MAVEN_REPOSITORY \
	-B -U -npu -ntp -q -T 7 \
	-Dmaven.wagon.http.ssl.insecure=true -DskipTests=true -Dmaven.test.skip=true \
	-P dev,jdk$JDK,licenseTool,issuedDep \
	clean install


# -------------------------------------------------
# *** SERVE MAVEN AS REPOSITORY ***
# -------------------------------------------------
#FROM svenstaro/miniserve:latest
#ENTRYPOINT [ "miniserve", "--tls-cert", "jdk$JDK.org.nlh4j.keystore.cer", "--tls-key", "jdk$JDK.org.nlh4j.keystore.jks", "/root/.m2/repository" ]