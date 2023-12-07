@echo off

call "./setEnv.bat" /wait

rem clean-up projects first
call "%BATCHES_DIR%\_cleanup.bat" /wait

rem run maven eclipseSetup profile
call "%BATCHES_DIR%\_eclipseSetup.bat" /wait

SET PATH=%PREV_PATH%
CD /D %BATCHES_DIR%
