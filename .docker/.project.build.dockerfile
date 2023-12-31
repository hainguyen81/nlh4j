# -------------------------------------------------
# *** ARGUMENTS ***
# -------------------------------------------------
ARG PROJECT_NAME=nlh4j
ARG MAVEN_VERSION=3.8.4
ARG JDK_MAJOR_VERSION=11



# -------------------------------------------------
# *** Pull MAVEN https://hub.docker.com/repository/docker/hainguyen81 ***
# -------------------------------------------------
# Import certificates
# -------------------------------------------------
FROM hainguyen81/org.nlh4j:dependencies-$MAVEN_VERSION-$JDK_MAJOR_VERSION-$PROJECT_NAME as maven
ARG PROJECT_NAME

ENV LOG_FILE=$PROJECT_NAME.log

# -------------------------------------------------
# Build project offline
RUN echo [maven-build[$PROJECT_NAME] - dev,jdk$JDK_MAJOR_VERSION - MAVEN_REPOSITORY: $MAVEN_REPOSITORY] Build project offline - Profiles dev,jdk$JDK_MAJOR_VERSION,issuedDep,licenseTool \
	&& mvn \
	-s $PROJECT_NAME/settings.xml -t $PROJECT_NAME/toolchains.xml \
	-Dmaven.repo.local=$MAVEN_REPOSITORY \
	-X -B -U -q -T 7 \
	-P dev,jdk$JDK_MAJOR_VERSION,issuedDep,licenseTool \
	clean install \
	-Dmaven.wagon.http.ssl.insecure=true \
	-DskipTests=true -Dmaven.test.skip=true \
	--log-file $LOG_FILE \
	-f $PROJECT_NAME/pom.xml

# Remove cloned project after build
RUN rm -rf /.tmp/$PROJECT_NAME

# -------------------------------------------------
# Turn off CMD/ENTRYPOINT from maven
# VOLUME $MAVEN_REPOSITORY
ENTRYPOINT	echo ------------------------------------------------- \
			&& echo MAVEN REPOSITORY: $MAVEN_REPOSITORY \
			&& echo ------------------------------------------------- \
			&& ls $MAVEN_REPOSITORY \
			&& echo ------------------------------------------------- \
			&& echo Results: \
			&& echo ------------------------------------------------- \
			&& cat $LOG_FILE


