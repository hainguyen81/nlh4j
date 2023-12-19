@echo off

SET BATCHES_DIR=%~dp0

SET PROJECT_NAME=nlh4j
SET MAVEN_VERSION=%1
SET JDK_MAJOR_VERSION=%2

if "%GIT_BRANCH%" == "" (
	SET GIT_BRANCH=master
)

rem build docker file [.jdk11.dockerfile] under context folder is current directory
echo [ PROJECT_NAME: %PROJECT_NAME% - MAVEN_VERSION: %MAVEN_VERSION% - JDK_MAJOR_VERSION: %JDK_MAJOR_VERSION% ]
echo.

docker buildx build ^
		--progress plain ^
		--force-rm ^
		--no-cache ^
		--tag hainguyen81/org.nlh4j:dependencies-%MAVEN_VERSION%-%JDK_MAJOR_VERSION%-%PROJECT_NAME% ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SIZE=-1 ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SPEED=-1 ^
		--build-arg PROJECT_NAME=%PROJECT_NAME% ^
		--build-arg MAVEN_VERSION=%MAVEN_VERSION% ^
		--build-arg JDK_MAJOR_VERSION=%JDK_MAJOR_VERSION% ^
		--build-context certificate=%BATCHES_DIR%.certificate ^
		--build-context dep=%BATCHES_DIR%.dep ^
		-f .project.dependencies.dockerfile ^
		%BATCHES_DIR%