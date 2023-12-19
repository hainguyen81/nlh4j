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
ENV PG_HOME=$USER_SHARE/postgresql/$PG_MAJOR
ENV PG_SHARE=$USER_SHARE/postgresql
ENV PG_LOCALE_ALIAS=$USER_SHARE/locale/locale.alias
ENV PG_INITDB_ENTRY=/docker-entrypoint-initdb.d
ENV PG_DATA=/var/lib/postgresql/data

# -------------------------------------------------
WORKDIR .db

COPY --from=db . .
RUN ls && echo ------- && ls scripts

# -- including pg_hba.conf
RUN if [ -d conf ]; then cp -a conf/. $PG_HOME; fi
# -- including postgresql.conf
RUN if [ -d conf ]; then cp -a conf/. $PG_SHARE; fi
# scripts for initializing DB
RUN if [ -d scripts ]; then cp -a scripts/. $PG_INITDB_ENTRY; fi

# Locale configuration
RUN localedef -i $PG_LANG -c -f UTF-8 -A $PG_LOCALE_ALIAS $LOCALE_ALIAS

# authentication method
RUN if [ ! -f conf/pg_hba.conf ]; then \
		echo configure default pg_hba.conf by argument \
		&& echo "host all all all $POSTGRES_HOST_AUTH_METHOD" \
			> $PG_HOME/pg_hba.conf \
		&& cp $PG_HOME/pg_hba.conf $PG_SHARE/pg_hba.conf; \
	fi


