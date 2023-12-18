# -------------------------------------------------
# *** ARGUMENTS ***
# -------------------------------------------------
ARG PROJECT_NAME=nlh4j
ARG MAVEN_VERSION=3.8.4
ARG JDK_MAJOR_VERSION=11


# -------------------------------------------------
# *** Pull GIT https://hub.docker.com/repository/docker/hainguyen81 ***
# -------------------------------------------------
FROM hainguyen81/org.nlh4j:git-$PROJECT_NAME as project
ARG PROJECT_NAME

# Clone GIT source branch
RUN ls $PROJECT_NAME



# -------------------------------------------------
# *** Pull MAVEN https://hub.docker.com/repository/docker/hainguyen81 ***
# -------------------------------------------------
# Import certificates
# -------------------------------------------------
FROM hainguyen81/org.nlh4j:maven-$MAVEN_VERSION-$JDK_MAJOR_VERSION as maven
ARG PROJECT_NAME

# maven folders enviroment
ENV MAVEN_HOME=/usr/share/maven
ENV MAVEN_REF=$MAVEN_HOME/ref
ENV MAVEN_REPOSITORY=$MAVEN_REF/repository
ENV MAVEN_CONFIG=$MAVEN_HOME/conf

ENV APP=/app

# -------------------------------------------------
VOLUME [ "$APP", "$MAVEN_REPOSITORY" ]
WORKDIR $APP

# Copy project from GIT to build
RUN mkdir -p logs
RUN mkdir -p $PROJECT_NAME
COPY --from=project /git/$PROJECT_NAME $PROJECT_NAME

# Copy maven settings to project
RUN cp $MAVEN_REF/settings.xml $PROJECT_NAME
RUN cp $MAVEN_REF/toolchains.xml $PROJECT_NAME

# -------------------------------------------------
# [maven-build - dev,jdk$JDK_MAJOR_VERSION] Build root and resolve dependencies offline for publish repository later
RUN mkdir -p logs
RUN echo [maven-build[$PROJECT_NAME] - dev,jdk$JDK_MAJOR_VERSION] Build root and resolve dependencies offline for publish repository later \
	&& mvn \
	--fail-never \
	-s $PROJECT_NAME/settings.xml -t $PROJECT_NAME/toolchains.xml \
	-Dmaven.repo.local=$MAVEN_REPOSITORY \
	-X -N -B -U -ntp -q -T 7 \
	-P dev,jdk$JDK_MAJOR_VERSION \
	dependency:resolve dependency:go-offline install \
	-Dmaven.wagon.http.ssl.insecure=true \
	-DskipTests=true -Dmaven.test.skip=true \
	--log-file logs/$PROJECT_NAME.resolve.dependencies.log \
	-f $PROJECT_NAME/pom.xml

# -------------------------------------------------
# Turn off CMD/ENTRYPOINT from maven
ENTRYPOINT []
CMD []


