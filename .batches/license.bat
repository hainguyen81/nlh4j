@echo off

call "./setEnv.bat" /wait

rem resolve dependencies first
call "%BATCHES_DIR%\_resolve.bat" /wait

rem run maven licenseTool profile
call "%BATCHES_DIR%\_license.bat" /wait

SET PATH=%PREV_PATH%
CD /D %BATCHES_DIR%
