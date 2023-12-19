@echo off

SET PG_USER=%1
SET PG_PWD=%2
SET PG_HOST_AUTH_METHOD=%3
SET PG_LANG=%4
SET BATCHES_DIR=%~dp0

if "%PG_USER%" == "" (
	SET PG_USER=postgres
)

if "%PG_PWD%" == "" (
	SET PG_PWD=postgres
)

if "%PG_HOST_AUTH_METHOD%" == "" (
	SET PG_HOST_AUTH_METHOD=md5
)

if "%PG_LANG%" == "" (
	SET PG_LANG=ja_JP
)

rem build docker file [.jdk11.dockerfile] under context folder is current directory
echo [ PG_USER: %PG_USER% - PG_PWD: %PG_PWD% - PG_HOST_AUTH_METHOD: %PG_HOST_AUTH_METHOD% - PG_LANG: %PG_LANG% ]
echo.


docker buildx build ^
		--progress plain ^
		--force-rm ^
		--no-cache ^
		--tag hainguyen81/org.nlh4j:postgres ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SIZE=-1 ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SPEED=-1 ^
		--build-arg PG_USER=%PG_USER% ^
		--build-arg PG_PWD=%PG_PWD% ^
		--build-arg PG_HOST_AUTH_METHOD=%PG_HOST_AUTH_METHOD% ^
		--build-arg PG_LANG=%PG_LANG% ^
		--build-context certificate=%BATCHES_DIR%.certificate ^
		--build-context db=%BATCHES_DIR%.db ^
		-f .postgres.dockerfile ^
		%BATCHES_DIR%


