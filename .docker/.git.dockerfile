# -------------------------------------------------
# *** ARGUMENTS ***
# -------------------------------------------------
ARG GITHUB_USER=hainguyen81
ARG GITHUB_TOKEN=
ARG GIT_BRANCH=master
ARG PROJECT_NAME=nlh4j



# -------------------------------------------------
# *** Pull GIT https://hub.docker.com/r/alpine/git
# add `-c http.sslVerify=false` to git clone for fixing SSL error `SSL certificate problem: self signed certificate in certificate chain`
# details: https://github.com/docker/for-win/issues/4324 ***
# -------------------------------------------------
FROM alpine/git as clone
ARG GITHUB_USER
ARG GITHUB_TOKEN
ARG GIT_BRANCH
ARG PROJECT_NAME

ENV GIT_CLONE_URL=https://github.com/$GITHUB_USER/$PROJECT_NAME.git
ENV GIT_CLONE_URL_WITH_CREDENTICALS=https://$GITHUB_USER:$GITHUB_TOKEN@github.com/$GITHUB_USER/$PROJECT_NAME.git

# -------------------------------------------------
WORKDIR /git

# -------------------------------------------------
# Copy host project if necessary
RUN mkdir -p .tmp
ONBUILD COPY --from=project [.] .tmp/

# Clone GIT source branch
RUN mkdir -p $PROJECT_NAME
RUN if [ -f .tmp/pom.xml ]; then \
		cp .tmp $PROJECT_NAME; \
	elif [ "$GITHUB_TOKEN" == "" ]; then \
		echo [clone] Clone GIT without credenticals: $PROJECT_NAME \
		&& git -c http.sslVerify=false clone -b $GIT_BRANCH $GIT_CLONE_URL $PROJECT_NAME; \
	else \
		echo [clone] Clone GIT with credenticals: $GITHUB_USER:$GITHUB_TOKEN:$PROJECT_NAME \
		&& git -c http.sslVerify=false clone -b $GIT_BRANCH $GIT_CLONE_URL_WITH_CREDENTICALS $PROJECT_NAME; \
	fi

# Remove temporary
RUN rm -rf .tmp/

# Create symbolic link to project
RUN ln -s /git/$PROJECT_NAME $PROJECT_NAME

# # Create volume for accessing from another
# VOLUME /git/$PROJECT_NAME


