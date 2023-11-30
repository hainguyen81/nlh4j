@echo off

call "./setEnv.bat" /wait

CD /D %PROJ_DIR%

if "%DEBUG%" == "true" (
	mvn --settings %MAVEN_SETTINGS% clean install -P %MAVEN_PROFILES% -T 5 -X -DskipTests=%SKIP_TESTS% -Dmaven.test.skip=%SKIP_TESTS% -Dmaven.repo.local=%MAVEN_REPO%
) else (
	mvn --settings %MAVEN_SETTINGS% clean install -P %MAVEN_PROFILES% -T 5 -X -DskipTests=%SKIP_TESTS% -Dmaven.test.skip=%SKIP_TESTS% -Dmaven.repo.local=%MAVEN_REPO% 1> build.log 2>&1
)

CD /D %BATCHES_DIR%

SET PATH=%PREV_PATH%