# -------------------------------------------------
# *** ARGUMENTS ***
# -------------------------------------------------
ARG TC_USER=admin
ARG TC_PWD=admin
ARG JDK_MAJOR_VERSION=11



# -------------------------------------------------
# *** Pull TOMCAT https://hub.docker.com/_/tomcat ***
# -------------------------------------------------
FROM tomcat:jre$JDK_MAJOR_VERSION-temurin
ARG TC_USER
ARG TC_PWD

ARG JDK_MAJOR_VERSION
ENV JDK_MAJOR_VERSION=$JDK_MAJOR_VERSION

ENV USER_LOCAL=/usr/local
ENV CATALINA_BASE=$USER_LOCAL/tomcat
ENV CATALINA_HOME=$CATALINA_BASE
ENV CATALINA_WEBAPPS=$CATALINA_HOME/webapps
ENV CATALINA_WEBAPPS_DIST=$CATALINA_HOME/webapps.dist
ENV CATALINA_WEBAPPS_MANAGER=$CATALINA_WEBAPPS/manager
ENV CATALINA_CONF=$CATALINA_BASE/conf

# -------------------------------------------------
WORKDIR .tc

COPY --from=tomcat . .
RUN ls

# authentication method
RUN if [ ! -f conf/tomcat-users.xml ]; then \
		echo configure default tomcat-users.xml user: $TC_USER/$TC_PWD \
		&& echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \
				<tomcat-users xmlns=\"http://tomcat.apache.org/xml\" \
				xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \
				xsi:schemaLocation=\"http://tomcat.apache.org/xml tomcat-users.xsd\" \
				version=\"1.0\"> \
					<role rolename=\"admin-gui\"/> \
					<role rolename=\"manager-gui\"/> \
					<user username=\"$TC_USER\" password=\"$TC_PWD\" roles=\"admin-gui,manager-gui\"/> \
				</tomcat-users>" \
			> $CATALINA_CONF/tomcat-users.xml; \
	else \
		cp -a conf/. $CATALINA_CONF; \
	fi

# Distribution embedded webapps tomcat
RUN cp -a $CATALINA_WEBAPPS_DIST/. $CATALINA_WEBAPPS
# Overide distribution webapps by host
RUN cp -a webapps/. $CATALINA_WEBAPPS

# configure tomcat manager webapps
RUN if [ ! -f webapps/manager/META-INF/context.xml ]; then \
		echo configure default context.xml user: $TC_USER/$TC_PWD for $CATALINA_WEBAPPS_MANAGER \
		&& echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \
				<Context antiResourceLocking=\"false\" privileged=\"true\"> \
					<CookieProcessor className=\"org.apache.tomcat.util.http.Rfc6265CookieProcessor\" \
					sameSiteCookies=\"strict\" /> \
					<Manager sessionAttributeValueClassNameFilter=\"java\\.lang\\.(?:Boolean|Integer|Long|Number|String)|org\\.apache\\.catalina\\.filters\\.CsrfPreventionFilter\\$LruCache(?:\\$1)?|java\\.util\\.(?:Linked)?HashMap\"/> \
				</Context>" \
			> $CATALINA_WEBAPPS_MANAGER/META-INF/context.xml; \
	fi

