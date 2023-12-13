ARG GITHUB_USER=hainguyen81
ARG GITHUB_TOKEN=
ARG GIT_BRANCH=master
ARG JDK=11

# -------------------------------------------------
# PREPARATION
# -------------------------------------------------
FROM ubuntu as base
ARG GITHUB_USER
ARG GITHUB_TOKEN
ARG GIT_BRANCH
ARG JDK

RUN echo "[base] GITHUB_USER	- $GITHUB_USER"
RUN echo "[base] GITHUB_TOKEN	- $GITHUB_TOKEN"
RUN echo "[base] GIT_BRANCH		- $GIT_BRANCH"
RUN echo "[base] JDK			- $JDK"

# -------------------------------------------------
WORKDIR /data
RUN mkdir -p /root/.cert
COPY --from=certificate repo.maven.apache.org.crt .

# -------------------------------------------------
# Pull GIT https://hub.docker.com/r/alpine/git
# add `-c http.sslVerify=false` to git clone for fixing SSL error `SSL certificate problem: self signed certificate in certificate chain`
# details: https://github.com/docker/for-win/issues/4324
# -------------------------------------------------
FROM alpine/git as clone
ARG GITHUB_USER
ARG GITHUB_TOKEN
ARG GIT_BRANCH
ARG JDK

# -------------------------------------------------
WORKDIR /app

# Clone GIT source branch
RUN echo "[clone] Clone GIT with credenticals: $GITHUB_USER:$GITHUB_TOKEN"
RUN git -c http.sslVerify=false clone -b $GIT_BRANCH https://$GITHUB_USER:$GITHUB_TOKEN@github.com/hainguyen81/nlh4j.git

# -------------------------------------------------
# Pull MAVEN https://hub.docker.com/_/maven/tags?page=1&name=3.8.4
# -------------------------------------------------
# Import certificates
# -------------------------------------------------
FROM maven:3.8.4-openjdk-$JDK-slim as jdk
ARG JDK

# create maven settings.xml, toolchains.xml
RUN	mkdir -p /root/.m2 && mkdir -p /root/.m2/repository
RUN	mkdir -p /usr/share && mkdir -p /usr/share/maven && mkdir -p /usr/share/maven/conf
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
#RUN cat /usr/share/maven/conf/toolchains.xml

# -------------------------------------------------
WORKDIR /jdk
LABEL maintainer="Hai Nguyen (hainguyenjc@gmail.com)"
RUN echo "[jdk] JAVA_HOME: $JAVA_HOME"
COPY --from=base /data/repo.maven.apache.org.crt $JAVA_HOME/jre/lib/security/
RUN keytool -noprompt -trustcacerts -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -importcert -alias repo.maven.apache.org -file $JAVA_HOME/jre/lib/security/repo.maven.apache.org.crt

# -------------------------------------------------
# Maven Build
# -------------------------------------------------
FROM jdk as maven-build
ARG JDK

COPY --from=clone app/nlh4j /app
COPY --from=jdk /usr/share/maven/conf/settings.xml /app
COPY --from=jdk /usr/share/maven/conf/toolchains.xml /app

# -------------------------------------------------
WORKDIR /app
RUN	mkdir -p logs
RUN pwd && ls

# Build root first to create maven dependency reference
RUN echo "[maven-build - dev,jdk$JDK] Build parent POM before building modules - [-N] non-recusive for building only parent POM"
RUN --mount=type=cache,target=/root/.m2 \
	mvn -s settings.xml -t toolchains.xml -Dmaven.repo.local=/root/.m2/repository -X -q -N -T 7 -U -Dmaven.wagon.http.ssl.insecure=true -DskipTests=true -Dmaven.test.skip=true -P dev,jdk$JDK clean install \
	-o /logs/parent.build.log

# Build whole modules
RUN echo "[maven-build - dev,jdk$JDK,licenseTool] Build child modules after building only parent POM"
RUN --mount=type=cache,target=/root/.m2 \
	mvn -s settings.xml -t toolchains.xml -Dmaven.repo.local=/root/.m2/repository -X -q -T 7 -U -Dmaven.wagon.http.ssl.insecure=true -DskipTests=true -Dmaven.test.skip=true -P dev,jdk$JDK,licenseTool clean install \
	-o /logs/build.log

COPY logs/* .


