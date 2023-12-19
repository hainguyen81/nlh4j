@echo off

SET JDK_MAJOR_VERSION=%1
SET TOMCAT_USER=%2
SET TOMCAT_PASSWORD=%3
SET BATCHES_DIR=%~dp0

rem build docker file [.jdk11.dockerfile] under context folder is current directory
echo [ JDK_MAJOR_VERSION: %JDK_MAJOR_VERSION% - TOMCAT_USER: %TOMCAT_USER% - TOMCAT_PASSWORD: %TOMCAT_PASSWORD% ]
echo.

docker buildx build ^
		--progress plain ^
		--force-rm ^
		--no-cache ^
		--tag hainguyen81/org.nlh4j:tomcat-%JDK_MAJOR_VERSION% ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SIZE=-1 ^
		--build-arg BUILDKIT_STEP_LOG_MAX_SPEED=-1 ^
		--build-arg TC_USER=%TOMCAT_USER% ^
		--build-arg TC_PWD=%TOMCAT_PASSWORD% ^
		--build-arg JDK_MAJOR_VERSION=%JDK_MAJOR_VERSION% ^
		--build-context certificate=%BATCHES_DIR%.certificate ^
		--build-context tomcat=%BATCHES_DIR%.tomcat ^
		-f .tomcat.dockerfile ^
		%BATCHES_DIR%


