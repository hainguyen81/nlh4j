@echo off

SET "MAVEN_GOALS=clean install"

CD /D %PROJ_DIR%

echo -------------------------------------------------
echo mvn -s %MAVEN_SETTINGS% -t %PROJ_TOOLCHAINS% -P %MAVEN_PROFILES% -N -T 5 -U -up -X -DskipTests=%SKIP_TESTS% -Dmaven.test.skip=%SKIP_TESTS% -Dmaven.repo.local=%MAVEN_REPO% %MAVEN_GOALS%
echo -------------------------------------------------
echo.
if "%DEBUG%" == "true" (
	mvn -s %MAVEN_SETTINGS% -t %PROJ_TOOLCHAINS% -P %MAVEN_PROFILES% -N -T 5 -U -up -X -DskipTests=%SKIP_TESTS% -Dmaven.test.skip=%SKIP_TESTS% -Dmaven.repo.local=%MAVEN_REPO% %MAVEN_GOALS%
) else (
	mvn -s %MAVEN_SETTINGS% -t %PROJ_TOOLCHAINS% -P %MAVEN_PROFILES% -N -T 5 -U -up -X -DskipTests=%SKIP_TESTS% -Dmaven.test.skip=%SKIP_TESTS% -Dmaven.repo.local=%MAVEN_REPO% %MAVEN_GOALS% 1> build.log 2>&1
)
