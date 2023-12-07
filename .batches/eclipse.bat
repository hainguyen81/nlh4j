@echo off

call "./setEnv.bat" /wait

rem clean-up projects first
call "%BATCHES_DIR%\_cleanup.bat" /wait

rem eclipse:eclipse
call "%BATCHES_DIR%\_eclipse.bat" /wait

SET PATH=%PREV_PATH%
CD /D %BATCHES_DIR%
