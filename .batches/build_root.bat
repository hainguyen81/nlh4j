@echo off

call "./setEnv.bat" /wait

rem resolve dependencies first
call "%BATCHES_DIR%\_resolve.bat" /wait

rem run maven build
call "%BATCHES_DIR%\_build_root.bat" /wait

SET PATH=%PREV_PATH%
CD /D %BATCHES_DIR%
