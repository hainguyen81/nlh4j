@echo off

call "./setEnv.bat" /wait

SET "MAVEN_GOALS=clean eclipse:clean"

SET MAVEN_CLEAN_PROFILES=%MAVEN_PROFILES%,eclipseSetup

CD /D %PROJ_DIR%

echo -------------------------------------------------
echo mvn -s %MAVEN_SETTINGS% -t %PROJ_TOOLCHAINS% -P %MAVEN_CLEAN_PROFILES% -T 5 -U -up -X -DskipTests=%SKIP_TESTS% -Dmaven.test.skip=%SKIP_TESTS% -Dmaven.repo.local=%MAVEN_REPO% %MAVEN_GOALS%
echo -------------------------------------------------
echo.
if "%DEBUG%" == "true" (
	mvn -s %MAVEN_SETTINGS% -t %PROJ_TOOLCHAINS% -P %MAVEN_CLEAN_PROFILES% -T 5 -U -up -X -DskipTests=%SKIP_TESTS% -Dmaven.test.skip=%SKIP_TESTS% -Dmaven.repo.local=%MAVEN_REPO% %MAVEN_GOALS%
) else (
	mvn -s %MAVEN_SETTINGS% -t %PROJ_TOOLCHAINS% -P %MAVEN_CLEAN_PROFILES% -T 5 -U -up -X -DskipTests=%SKIP_TESTS% -Dmaven.test.skip=%SKIP_TESTS% -Dmaven.repo.local=%MAVEN_REPO% %MAVEN_GOALS% 1> build.log 2>&1
)

SET PATH=%PREV_PATH%

echo -------------------------------------------------
echo Clear physical files/directories from modules
echo -------------------------------------------------
echo 1. Remove bin directories
FOR /d /r %PROJ_DIR%\modules %%d IN (bin) DO @IF EXIST "%%d" rd /s /q "%%d"

echo 2. Remove target directories
FOR /d /r %PROJ_DIR%\modules %%d IN (target) DO @IF EXIST "%%d" rd /s /q "%%d"

echo 3. Remove .eclipse directories
FOR /d /r %PROJ_DIR%\modules %%d IN (.eclipse) DO @IF EXIST "%%d" rd /s /q "%%d"

echo 4. Remove .settings directories
FOR /d /r %PROJ_DIR%\modules %%d IN (.settings) DO @IF EXIST "%%d" rd /s /q "%%d"

echo 5. Remove .classpath files
FOR /d /r %PROJ_DIR%\modules %%d IN (.classpath) DO @IF EXIST "%%d" del /s /q "%%d"

echo 6. Remove .project files
FOR /d /r %PROJ_DIR%\modules %%d IN (.project) DO @IF EXIST "%%d" del /s /q "%%d"

echo 7. Remove .log files
FOR /d /r %PROJ_DIR%\modules %%d IN (.log) DO @IF EXIST "%%d" del /s /q "%%d"

CD /D %BATCHES_DIR%
