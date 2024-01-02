@echo off

SET BATCHES_DIR=%~dp0

REM -------------------------------------------------
REM Rebuild GIT project
REM -------------------------------------------------
REM Pull source from GIT
echo -------------------------------------------------
echo 1. Pull project from GIT
echo -------------------------------------------------
echo.
rem due to public repository, so no need GIT user and token to pull code
call my_build_git_docker.bat
echo -------------------------------------------------
echo ^>^>^>^>^>^>^> Finished: 1. Pull project from GIT
echo -------------------------------------------------
echo.
timeout /t 10 /nobreak >nul

REM Build project dependencies image
echo -------------------------------------------------
echo 2. Build project dependencies docker image
echo -------------------------------------------------
echo.
call my_build_project_dependencies_docker.bat
echo -------------------------------------------------
echo ^>^>^>^>^>^>^> Finished: 2. Build project dependencies docker image
echo -------------------------------------------------
echo.
timeout /t 10 /nobreak >nul

REM Build project docker image
echo -------------------------------------------------
echo 3. Build project docker image
echo -------------------------------------------------
echo.
call build.deploy.dockerfile.bat
echo -------------------------------------------------
echo ^>^>^>^>^>^>^> Finished: 3. my_build_project_build_docker.bat
echo -------------------------------------------------
echo.
timeout /t 10 /nobreak >nul

REM Build project NGINX docker image
echo -------------------------------------------------
echo 4. Build project NGINX docker image
echo -------------------------------------------------
echo.
call my_build_project_nginx_docker.bat
echo -------------------------------------------------
echo ^>^>^>^>^>^>^> Finished: 4. Build project NGINX docker image
echo -------------------------------------------------
echo.


