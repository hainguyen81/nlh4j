@echo off

SET MAVEN_VERSION=%1
SET JDK_MAJOR_VERSION=%2
SET BATCHES_DIR=%~dp0

rem build docker file [.jdk11.dockerfile] under context folder is current directory
echo [ MAVEN_VERSION: %MAVEN_VERSION% - JDK_MAJOR_VERSION: %JDK_MAJOR_VERSION% ]
echo.

docker buildx build ^
		--progress plain ^
		--force-rm ^
		--no-cache ^
		--tag hainguyen81/org.nlh4j:maven-%MAVEN_VERSION%-%JDK_MAJOR_VERSION% ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SIZE=-1 ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SPEED=-1 ^
		--build-arg MAVEN_VERSION=%MAVEN_VERSION% ^
		--build-arg JDK_MAJOR_VERSION=%JDK_MAJOR_VERSION% ^
		--build-context certificate=%BATCHES_DIR%.certificate ^
		--build-context maven=%BATCHES_DIR%.maven ^
		-f .maven.dockerfile ^
		%BATCHES_DIR%


