@echo off

SET BATCHES_DIR=%~dp0

rem project folder path
rem SET LOCAL_PROJECT=%BATCHES_DIR%..\
rem not project folder path
SET LOCAL_PROJECT=%BATCHES_DIR%
SET PROJECT_NAME=nlh4j
SET GITHUB_USER=%1
SET GITHUB_TOKEN=%2
SET GIT_BRANCH=%3
SET GITHUB_ORG=%4

if "%GITHUB_ORG%" == "" (
	SET GITHUB_ORG=%GITHUB_USER%
)

rem build docker file [.jdk11.dockerfile] under context folder is current directory
echo [ PROJECT_NAME: %PROJECT_NAME% - LOCAL_PROJECT: %LOCAL_PROJECT% - GITHUB_ORG: %GITHUB_ORG% ]
echo.

docker buildx build ^
		--progress plain ^
		--force-rm ^
		--no-cache ^
		--tag hainguyen81/org.nlh4j:git-%PROJECT_NAME% ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SIZE=-1 ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SPEED=-1 ^
		--build-arg PROJECT_NAME=%PROJECT_NAME% ^
		--build-arg GITHUB_ORG=%GITHUB_ORG% ^
		--build-arg GITHUB_USER=%GITHUB_USER% ^
		--build-arg GITHUB_TOKEN=%GITHUB_TOKEN% ^
		--build-arg GIT_BRANCH=%GIT_BRANCH% ^
		--build-context project=%LOCAL_PROJECT% ^
		-f .git.dockerfile ^
		%BATCHES_DIR%
goto:eof


