# -------------------------------------------------
# *** ARGUMENTS ***
# -------------------------------------------------
ARG PROJECT_NAME=nlh4j
ARG MAVEN_VERSION=3.8.4
ARG JDK_MAJOR_VERSION=11
ARG NGINX_HTTP_PORT=80
ARG SSL=true



# -------------------------------------------------
# *** Built PROJECT https://hub.docker.com/repository/docker/hainguyen81 ***
# -------------------------------------------------
FROM hainguyen81/org.nlh4j:$PROJECT_NAME-$MAVEN_VERSION-$JDK_MAJOR_VERSION as maven-project

# Check maven repository including built project
RUN ls $MAVEN_REPOSITORY


# -------------------------------------------------
# *** Serve https://hub.docker.com/repository/docker/hainguyen81 ***
# -------------------------------------------------
FROM hainguyen81/org.nlh4j:nginx-$JDK_MAJOR_VERSION as nginx
ARG SSL
ARG NGINX_HTTP_PORT

ARG JDK_MAJOR_VERSION
ENV JDK_MAJOR_VERSION=$JDK_MAJOR_VERSION

# maven folders enviroment
ENV MAVEN_HOME=/usr/share/maven
ENV MAVEN_REF=$MAVEN_HOME/ref
ENV MAVEN_CONFIG=$MAVEN_HOME/conf
ENV MAVEN_REPOSITORY=$MAVEN_CONFIG/repository

# -------------------------------------------------
# Copy maven repository including built project to serve
COPY --from=maven-project $MAVEN_REPOSITORY $SERVE_DIRECTORY
RUN ls $SERVE_DIRECTORY

# Remove old maven repository for saving storage
RUN rm -rf $MAVEN_HOME
RUN rm -r .tmp


