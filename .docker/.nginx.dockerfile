# -------------------------------------------------
# *** ARGUMENTS ***
# -------------------------------------------------
ARG JDK_MAJOR_VERSION=11
ARG NGINX_HTTP_PORT=80
ARG SSL=true



# -------------------------------------------------
# *** Serve https://hub.docker.com/_/nginx ***
# -------------------------------------------------
FROM nginx:stable-alpine-slim
ARG SSL
ARG NGINX_HTTP_PORT

ARG JDK_MAJOR_VERSION
ENV JDK_MAJOR_VERSION=$JDK_MAJOR_VERSION

ENV SERVE_DIRECTORY=/usr/share/nginx/serve
ENV CERTS_DIRECTORY=/etc/nginx/certs
ENV CONF_DIRECTORY=/etc/nginx/conf.d
ENV DEFAULT_CONF=$CONF_DIRECTORY/default.conf

# -------------------------------------------------
# remove default conf
RUN rm /etc/nginx/conf.d/default.conf

# Copy httpd.conf if necessary
RUN mkdir -p .tmp
ONBUILD COPY --from=conf httpd.con[f] .tmp/

# Copy certificate for SSL
COPY --from=certificate jdk$JDK_MAJOR_VERSION/openssl/jdk$JDK_MAJOR_VERSION.org.nlh4j.keystore.crt-cabundle.crt $CERTS_DIRECTORY/jdk$JDK_MAJOR_VERSION.org.nlh4j.crt
COPY --from=certificate jdk$JDK_MAJOR_VERSION/openssl/jdk$JDK_MAJOR_VERSION.org.nlh4j.keystore.key $CERTS_DIRECTORY/jdk$JDK_MAJOR_VERSION.org.nlh4j.key

# create/copy conf from build context
RUN mkdir -p $SERVE_DIRECTORY
RUN if [ -f .tmp/httpd.conf ]; then \
		echo copy conf from host \
		&& cp .tmp/httpd.conf $DEFAULT_CONF; \

	elif [ "$SSL"=="true" ]; then \
		echo create default SSL conf \
		&& echo "server { \
				listen $NGINX_HTTP_PORT; \
				listen [::]:$NGINX_HTTP_PORT; \
				\
				resolver 127.0.0.1; \
				autoindex on; \
				\
				server_name _; \
				server_tokens off; \
				\
				root $SERVE_DIRECTORY; \
				gzip on; \
				gzip_static off; \
				\
				location / { \
					root $SERVE_DIRECTORY; \
					autoindex on; \
					autoindex_exact_size on; \
					autoindex_format html; \
					autoindex_localtime on; \
					expires -1; \
					default_type text/plain; \
				} \
				\
				location = /health { \
					types {} \
					default_type text/plain; \
					return 200 \"OK\"; \
				} \
			} \
			\
			server { \
				listen 443 ssl; \
				listen [::]:443; \
				\
				resolver 127.0.0.1; \
				autoindex on; \
				\
				server_name org.nlh4j.maven; \
				ssl_certificate $CERTS_DIRECTORY/jdk$JDK_MAJOR_VERSION.org.nlh4j.crt; \
				ssl_certificate_key $CERTS_DIRECTORY/jdk$JDK_MAJOR_VERSION.org.nlh4j.key; \
				\
				root $SERVE_DIRECTORY; \
				gzip on; \
				gzip_static off; \
				\
				location / { \
					root $SERVE_DIRECTORY; \
					autoindex on; \
					autoindex_exact_size on; \
					autoindex_format html; \
					autoindex_localtime on; \
					expires -1; \
					default_type text/plain; \
				} \
				\
				location = /health { \
					types {} \
					default_type text/plain; \
					return 200 \"OK\"; \
				} \
			}" \
			> $DEFAULT_CONF; \
	else \
		echo create default conf \
		&& echo "server { \
				listen $NGINX_HTTP_PORT; \
				listen [::]:$NGINX_HTTP_PORT; \
				\
				resolver 127.0.0.1; \
				autoindex on; \
				\
				server_name _; \
				server_tokens off; \
				\
				root $SERVE_DIRECTORY; \
				gzip on; \
				gzip_static off; \
				\
				location / { \
					root $SERVE_DIRECTORY; \
					autoindex on; \
					autoindex_exact_size on; \
					autoindex_format html; \
					autoindex_localtime on; \
					expires -1; \
					default_type text/plain; \
				} \
				\
				location = /health { \
					types {} \
					default_type text/plain; \
					return 200 \"OK\"; \
				} \
			}" \
			> $DEFAULT_CONF; \
	fi

# expose ports for using
EXPOSE $NGINX_HTTP_PORT
EXPOSE 443

# start nginx
CMD [ "nginx", "-g", "daemon off;" ]


