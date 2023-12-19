@echo off

SET BATCHES_DIR=%~dp0

SET JDK_MAJOR_VERSION=%1
SET NGINX_HTTP_PORT=%2
SET SSL=%3

if "%NGINX_HTTP_PORT%" == "" (
	SET NGINX_HTTP_PORT=80
)

if "%SSL%" == "t" (
	SET SSL=true
)

if "%SSL%" == "1" (
	SET SSL=true
)

if "%SSL%" == "f" (
	SET SSL=false
)

if "%SSL%" == "0" (
	SET SSL=false
)

if "%SSL%" NEQ "true" ( if "%SSL%" NEQ "t" ( if "%SSL%" NEQ "false" ( if "%SSL%" NEQ "f" ( if "%SSL%" NEQ "1" ( if "%SSL%" NEQ "0" (
	SET SSL=true
) ) ) ) ) )

rem build docker file [.jdk11.dockerfile] under context folder is current directory
echo [ JDK_MAJOR_VERSION: %JDK_MAJOR_VERSION% - NGINX_HTTP_PORT: %NGINX_HTTP_PORT% - SSL: %SSL% ]
echo.

docker buildx build ^
		--progress plain ^
		--force-rm ^
		--no-cache ^
		--tag hainguyen81/org.nlh4j:nginx-%JDK_MAJOR_VERSION% ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SIZE=-1 ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SPEED=-1 ^
		--build-arg JDK_MAJOR_VERSION=%JDK_MAJOR_VERSION% ^
		--build-arg NGINX_HTTP_PORT=%NGINX_HTTP_PORT% ^
		--build-arg SSL=%SSL% ^
		--build-context certificate=%BATCHES_DIR%.certificate ^
		--build-context conf=%BATCHES_DIR%.nginx ^
		-f .nginx.dockerfile ^
		%BATCHES_DIR%


