@echo off

call "./setEnv.bat" /wait

echo Remove bin directories
FOR /d /r %PROJ_DIR% %%d IN (bin) DO @IF EXIST "%%d" rd /s /q "%%d"

echo Remove target directories
FOR /d /r %PROJ_DIR% %%d IN (target) DO @IF EXIST "%%d" rd /s /q "%%d"

echo Remove .settings directories
FOR /d /r %PROJ_DIR% %%d IN (.settings) DO @IF EXIST "%%d" rd /s /q "%%d"

echo Remove .classpath files
FOR /d /r %PROJ_DIR% %%d IN (.classpath) DO @IF EXIST "%%d" del /s /q "%%d"

echo Remove .project files
FOR /d /r %PROJ_DIR% %%d IN (.project) DO @IF EXIST "%%d" del /s /q "%%d"

echo Remove .log files
FOR /d /r %PROJ_DIR% %%d IN (.log) DO @IF EXIST "%%d" del /s /q "%%d"
