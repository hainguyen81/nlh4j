@echo off

SET MAVEN_SETTINGS=
SET MAVEN_VERSION=%1
SET JDK_MAJOR_VERSION=%2
SET BATCHES_DIR=%~dp0

rem build docker file [.jdk11.dockerfile] under context folder is current directory
echo [ MAVEN_VERSION: %MAVEN_VERSION% - JDK_MAJOR_VERSION: %JDK_MAJOR_VERSION% ]

IF [%MAVEN_SETTINGS%] == [] GOTO default-settings

:custom-settings
echo - [ MAVEN_SETTINGS: %MAVEN_SETTINGS% ]
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
		--build-context settings=%MAVEN_SETTINGS% ^
		-f .maven.dockerfile ^
		%BATCHES_DIR%
goto:eof

:default-settings
echo - [ Default Maven Settings ]
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
		-f .maven.dockerfile ^
		%BATCHES_DIR%
goto:eof


