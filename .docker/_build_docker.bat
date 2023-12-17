@echo off

SET GITHUB_PROJECT=hainguyen81/nlh4j.git
SET GITHUB_USER=%1
SET GITHUB_TOKEN=%2
SET GIT_BRANCH=%3
SET JDK_MAJOR_VERSION=%4
SET BATCHES_DIR=%~dp0

if "%GIT_BRANCH%" == "" (
	SET GIT_BRANCH=master
)

rem build docker file [.jdk11.dockerfile] under context folder is current directory
echo [ GITHUB_PROJECT: %GITHUB_PROJECT% - GITHUB_USER: %GITHUB_USER% - GITHUB_TOKEN: %GITHUB_TOKEN% - GIT_BRANCH: %GIT_BRANCH% - JDK_MAJOR_VERSION: %JDK_MAJOR_VERSION% ]
echo.
REM docker buildx build ^
	REM --progress plain ^
	REM --force-rm ^
	REM --no-cache ^
	REM --tag nlh4j:1.0-jre%JDK_MAJOR_VERSION% ^
	REM --build-arg BUILDKIT_STEP_LOG_MAX_SIZE=-1 ^
	REM --build-arg BUILDKIT_STEP_LOG_MAX_SPEED=-1 ^
	REM --build-arg GITHUB_PROJECT=%GITHUB_PROJECT% ^
	REM --build-arg GITHUB_USER=%GITHUB_USER% ^
	REM --build-arg GITHUB_TOKEN=%GITHUB_TOKEN% ^
	REM --build-arg GIT_BRANCH=%GIT_BRANCH% ^
	REM --build-arg JDK_MAJOR_VERSION=%JDK_MAJOR_VERSION% ^
	REM --build-context certificate=%BATCHES_DIR%.certificate ^
	REM --build-context dep=%BATCHES_DIR%.dep ^
	REM --output type=local,dest=%BATCHES_DIR%logs ^
	REM -f .dockerfile ^
	REM %BATCHES_DIR%

docker buildx build ^
	--progress plain ^
	--force-rm ^
	--no-cache ^
	--tag hainguyen81/org.nlh4j:1.0-jre%JDK_MAJOR_VERSION% ^
	--build-arg BUILDKIT_STEP_LOG_MAX_SIZE=-1 ^
	--build-arg BUILDKIT_STEP_LOG_MAX_SPEED=-1 ^
	--build-arg GITHUB_PROJECT=%GITHUB_PROJECT% ^
	--build-arg GITHUB_USER=%GITHUB_USER% ^
	--build-arg GITHUB_TOKEN=%GITHUB_TOKEN% ^
	--build-arg GIT_BRANCH=%GIT_BRANCH% ^
	--build-arg JDK_MAJOR_VERSION=%JDK_MAJOR_VERSION% ^
	--build-context certificate=%BATCHES_DIR%.certificate ^
	--build-context dep=%BATCHES_DIR%.dep ^
	-f .dockerfile ^
	%BATCHES_DIR%