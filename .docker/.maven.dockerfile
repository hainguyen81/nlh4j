# -------------------------------------------------
# *** ARGUMENTS ***
# -------------------------------------------------
ARG MAVEN_VERSION=3.8.4
ARG JDK_MAJOR_VERSION=11


# -------------------------------------------------
# *** Pull MAVEN https://hub.docker.com/_/maven/tags?page=1&name=3.8.4 ***
# -------------------------------------------------
# Import certificates
# -------------------------------------------------
FROM maven:$MAVEN_VERSION-eclipse-temurin-$JDK_MAJOR_VERSION-alpine

ARG JDK_MAJOR_VERSION
ENV JDK_MAJOR_VERSION=$JDK_MAJOR_VERSION

# maven folders enviroment
ENV MAVEN_HOME=/usr/share/maven
ENV MAVEN_REF=$MAVEN_HOME/ref
ENV MAVEN_REPOSITORY=$MAVEN_REF/repository
ENV MAVEN_CONFIG=$MAVEN_HOME/conf

# -------------------------------------------------
# Copy maven settings if necessary
RUN mkdir -p .tmp
COPY --from=maven settings.xm[l] .tmp/

# # -------------------------------------------------
# # here to install some packages that be needed for later
# # if issue, then checking it out here: https://github.com/alpinelinux/docker-alpine/issues/98
# # install inotifywait for watching hot deploy using apk due to alpine OS
# RUN sed -i 's/https/http/' /etc/apk/repositories
# RUN apk add --no-cache inotify-tools

# -------------------------------------------------
RUN mkdir -p $MAVEN_REPOSITORY

# Copy repository certificate
# to fix `PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target`
COPY --from=certificate *.crt $JAVA_HOME/jre/lib/security/
RUN find $JAVA_HOME/jre/lib/security/ -type f -iname "*.crt" -exec echo {} \;
RUN echo [jdk] JAVA_HOME: $JAVA_HOME \
	&& find $JAVA_HOME/jre/lib/security/ -type f -iname "*.crt" -exec \
	keytool -noprompt -trustcacerts \
	-keystore $JAVA_HOME/jre/lib/security/cacerts \
	-storepass changeit -importcert \
	-file {} \;

# create/copy maven settings.xml, toolchains.xml
RUN if [ -f .tmp/settings.xml ]; then \
		echo copy settings from host \
		&& cp .tmp/settings.xml $MAVEN_CONFIG/settings.xml \
		&& cp .tmp/settings.xml $MAVEN_CONFIG/settings-docker.xml \
		&& cp .tmp/toolchains.xml $MAVEN_CONFIG/toolchains.xml; \
	else \
		echo create default settings \
		&& echo \
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
			> $MAVEN_CONFIG/settings.xml \
		&& echo \
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
			> $MAVEN_CONFIG/toolchains.xml; \
	fi
RUN	cp $MAVEN_CONFIG/settings.xml $MAVEN_REF/settings-docker.xml \
	&& cp $MAVEN_CONFIG/settings.xml $MAVEN_REF/settings.xml \
	&& cp $MAVEN_CONFIG/toolchains.xml $MAVEN_REF/toolchains.xml

# Remove temporary
RUN rm -rf .tmp/

# # Create volume for accessing from another
# VOLUME $JAVA_HOME
# VOLUME $MAVEN_HOME


