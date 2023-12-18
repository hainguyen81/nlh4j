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
FROM maven:$MAVEN_VERSION-eclipse-temurin-$JDK_MAJOR_VERSION-alpine as maven

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
ONBUILD COPY --from=settings settings.xml .tmp/

# -------------------------------------------------
RUN mkdir -p $MAVEN_REPOSITORY
COPY --from=certificate repo.maven.apache.org.crt $JAVA_HOME/jre/lib/security/
RUN echo [jdk] JAVA_HOME: $JAVA_HOME \
	&& keytool -noprompt -trustcacerts \
	-keystore $JAVA_HOME/jre/lib/security/cacerts \
	-storepass changeit -importcert \
	-alias repo.maven.apache.org \
	-file $JAVA_HOME/jre/lib/security/repo.maven.apache.org.crt

# create/copy maven settings.xml, toolchains.xml
RUN if [ -f .tmp/settings.xml ]; then \
		cp .tmp/settings.xml $MAVEN_CONFIG/settings.xml \
		&& cp .tmp/settings.xml $MAVEN_CONFIG/settings-docker.xml \
		&& cp .tmp/toolchains.xml $MAVEN_CONFIG/toolchains.xml \
	else \
		echo \
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
			> $MAVEN_CONFIG/toolchains.xml \
		&& cp $MAVEN_CONFIG/toolchains.xml $MAVEN_CONFIG/settings-docker.xml; \
	fi

# Remove temporary
RUN rm -rf .tmp/
