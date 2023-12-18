# -------------------------------------------------
# *** ARGUMENTS ***
# -------------------------------------------------
ARG NGINX_HTTP_PORT=80
ARG SSL=true



# -------------------------------------------------
# *** Serve ***
# -------------------------------------------------
FROM nginx:stable-alpine-slim
ARG SSL
ARG NGINX_HTTP_PORT

ENV SERVE_DIRECTORY=/usr/share/nginx/repository

# -------------------------------------------------
# remove default conf
RUN rm /etc/nginx/conf.d/default.conf

# Copy httpd.conf if necessary
RUN mkdir -p .tmp
ONBUILD COPY --from=conf httpd.con[f] .tmp/

# copy certificate for SSL
COPY --from=certificate jdk$JDK_MAJOR_VERSION/openssl/jdk$JDK_MAJOR_VERSION.org.nlh4j.keystore.crt-cabundle.crt /etc/nginx/certs/jdk$JDK_MAJOR_VERSION.org.nlh4j.crt
COPY --from=certificate jdk$JDK_MAJOR_VERSION/openssl/jdk$JDK_MAJOR_VERSION.org.nlh4j.keystore.key /etc/nginx/certs/jdk$JDK_MAJOR_VERSION.org.nlh4j.key

# create/copy conf from build context
RUN if [ -f .tmp/httpd.conf ]; then \
		echo copy conf from host \
		&& cp .tmp/httpd.conf /etc/nginx/conf.d/default.conf; \

	elif [ "$SSL"=="true" ]; then \
		echo create default SSL conf \
		&& echo "server { \
				listen $NGINX_PORT; \
				listen [::]:$NGINX_PORT; \
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
					root /usr/share/nginx/repository; \
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
				ssl_certificate /etc/nginx/certs/jdk$JDK_MAJOR_VERSION.org.nlh4j.crt; \
				ssl_certificate_key /etc/nginx/certs/jdk$JDK_MAJOR_VERSION.org.nlh4j.key; \
				\
				root /usr/share/nginx/repository; \
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
			> /etc/nginx/conf.d/default.conf; \
	else \
		echo create default conf \
		&& echo "server { \
				listen $NGINX_PORT; \
				listen [::]:$NGINX_PORT; \
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
					root /usr/share/nginx/repository; \
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
			> /etc/nginx/conf.d/default.conf; \
	fi

# expose ports for using
EXPOSE $NGINX_HTTP_PORT
EXPOSE 443

# start nginx
CMD [ "nginx", "-g", "daemon off;" ]


