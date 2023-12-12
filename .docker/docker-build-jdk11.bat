@echo off

SET GITHUB_USER=%1
SET GITHUB_TOKEN=%2
SET GIT_BRANCH=%3
SET BATCHES_DIR=%~dp0

if "%GIT_BRANCH%" == "" (
	SET GIT_BRANCH=master
)

rem build docker file [.jdk11.dockerfile] under context folder is current directory
echo Docker build with [GITHUB_USER: %GITHUB_USER% - GITHUB_TOKEN: %GITHUB_TOKEN% - GIT_BRANCH: %GIT_BRANCH%]
echo [docker buildx build --build-arg GITHUB_USER=%GITHUB_USER% --build-arg GITHUB_TOKEN=%GITHUB_TOKEN% --build-arg GIT_BRANCH=%GIT_BRANCH% --build-context certificate=%BATCHES_DIR%.certificate --no-cache -f .jdk11.dockerfile %BATCHES_DIR%]
echo.
docker buildx build --progress plain --build-arg GITHUB_USER=%GITHUB_USER% --build-arg GITHUB_TOKEN=%GITHUB_TOKEN%  --build-arg GIT_BRANCH=%GIT_BRANCH% --build-context certificate=%BATCHES_DIR%.certificate --no-cache -f .jdk11.dockerfile %BATCHES_DIR%