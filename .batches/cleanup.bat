@echo off

call "./setEnv.bat" /wait

call "%BATCHES_DIR%\_cleanup.bat" /wait

echo -------------------------------------------------
echo Clear physical files/directories from modules
echo -------------------------------------------------
echo 1. Remove bin directories
FOR /d /r %PROJ_DIR% %%d IN (bin) DO @IF EXIST "%%d" rd /s /q "%%d"

echo 2. Remove target directories
FOR /d /r %PROJ_DIR% %%d IN (target) DO @IF EXIST "%%d" rd /s /q "%%d"

echo 3. Remove .eclipse directories
FOR /d /r %PROJ_DIR%\modules %%d IN (.eclipse) DO @IF EXIST "%%d" rd /s /q "%%d"

echo 4. Remove .license directory
rd /s /q %PROJ_DIR%\.license

echo 5. Remove .profiles directories
FOR /d /r %PROJ_DIR%\modules %%d IN (.profiles) DO @IF EXIST "%%d" rd /s /q "%%d"

echo 6. Remove .settings directories
FOR /d /r %PROJ_DIR%\modules %%d IN (.settings) DO @IF EXIST "%%d" rd /s /q "%%d"

echo 7. Remove .classpath files
FOR /d /r %PROJ_DIR%\modules %%d IN (.classpath) DO @IF EXIST "%%d" del /s /q "%%d"

echo 8. Remove .factorypath files
FOR /d /r %PROJ_DIR%\modules %%d IN (.factorypath) DO @IF EXIST "%%d" del /s /q "%%d"

echo 9. Remove .project files
FOR /d /r %PROJ_DIR%\modules %%d IN (.project) DO @IF EXIST "%%d" del /s /q "%%d"

echo 10. Remove .log files
FOR /d /r %PROJ_DIR%\modules %%d IN (.log) DO @IF EXIST "%%d" del /s /q "%%d"

SET PATH=%PREV_PATH%
CD /D %BATCHES_DIR%
