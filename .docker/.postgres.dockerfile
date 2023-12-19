# -------------------------------------------------
# *** ARGUMENTS ***
# -------------------------------------------------
ARG PG_USER=postgres
ARG PG_PWD=postgres
ARG PG_HOST_AUTH_METHOD=trust
ARG PG_LANG=en_US



# -------------------------------------------------
# *** Pull POSTGRES https://hub.docker.com/_/postgres ***
# -------------------------------------------------
# Import certificates
# -------------------------------------------------
FROM postgres:latest
ARG PG_USER
ARG PG_PWD
ARG PG_HOST_AUTH_METHOD
ARG PG_LANG

ENV POSTGRES_USER=$PG_USER
ENV POSTGRES_PASSWORD=$PG_PWD
ENV POSTGRES_HOST_AUTH_METHOD=$PG_HOST_AUTH_METHOD
ENV LOCALE=$PG_LANG.utf8
ENV LOCALE_ALIAS=$PG_LANG.UTF-8
ENV LANG=$LOCALE

ENV USER_SHARE=/usr/share
ENV PG_SHARE=$USER_SHARE/postgresql
ENV PG_LOCALE_ALIAS=$USER_SHARE/locale/locale.alias
ENV PG_INITDB_ENTRY=/docker-entrypoint-initdb.d

# -------------------------------------------------
WORKDIR .db

# Copy configuration if necessary
RUN mkdir -p conf
ONBUILD COPY --from=db conf/[.] conf
RUN cp conf $PG_SHARE

# Locale configuration
RUN localedef -i $PG_LANG -c -f UTF-8 -A $PG_LOCALE_ALIAS $LOCALE_ALIAS

# authentication method
RUN if [ -f conf/pg_hba.conf ]; then \
		echo copy pg_hba.conf from host \
		&& cp conf/pg_hba.conf pg_hba.conf; \
	else \
		echo configure pg_hba.conf by argument \
		&& echo "host all all all $POSTGRES_HOST_AUTH_METHOD" >> pg_hba.conf; \
	fi

# Copy database scripts
RUN mkdir -p scripts
ONBUILD COPY --from=db scripts/*.sql scripts
RUN cp scripts $PG_INITDB_ENTRY


