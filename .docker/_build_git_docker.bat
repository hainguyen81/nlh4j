@echo off

SET LOCAL_PROJECT=
SET PROJECT_NAME=nlh4j
SET GITHUB_USER=%1
SET GITHUB_TOKEN=%2
SET GIT_BRANCH=%3
SET BATCHES_DIR=%~dp0

rem build docker file [.jdk11.dockerfile] under context folder is current directory
echo [ PROJECT_NAME: %PROJECT_NAME% ]

IF [%LOCAL_PROJECT%] == [] GOTO git-project

:local-project
echo - [ LOCAL_PROJECT: %LOCAL_PROJECT% ]
echo.
docker buildx build ^
		--progress plain ^
		--force-rm ^
		--no-cache ^
		--tag hainguyen81/org.nlh4j:git-%PROJECT_NAME% ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SIZE=-1 ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SPEED=-1 ^
		--build-context project=%LOCAL_PROJECT% ^
		-f .git.dockerfile ^
		%BATCHES_DIR%
goto:eof

:git-project
echo - [ GITHUB_USER: %GITHUB_USER% - GITHUB_TOKEN: %GITHUB_TOKEN% - GIT_BRANCH: %GIT_BRANCH% ]
echo.
docker buildx build ^
		--progress plain ^
		--force-rm ^
		--no-cache ^
		--tag hainguyen81/org.nlh4j:git-%PROJECT_NAME% ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SIZE=-1 ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SPEED=-1 ^
		-f .git.dockerfile ^
		%BATCHES_DIR%
goto:eof


