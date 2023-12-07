@echo off

call "./setEnv.bat" /wait

rem resolve dependencies
call "%BATCHES_DIR%\_resolve.bat" /wait

SET PATH=%PREV_PATH%
CD /D %BATCHES_DIR%
