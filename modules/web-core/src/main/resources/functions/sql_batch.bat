@echo off
SET PGSQL_HOME=F:\Java.Working\1.tools\database\pgsql-9.4.4.1-x64
SET PGSQL_BIN=%PGSQL_HOME%\bin
SET PGHOST=localhost
SET PGPORT=5432
SET PGDATABASE=sansho
SET PGUSER=postgres
SET PGPASSWORD=postgres
SET PGCLIENTENCODING=utf-8

SET PGSCRIPT_HOME=F:\Java.Working\15.projects.4.5\<customer>\db
for /f %%f in ('dir /b /s "%%PGSCRIPT_HOME%%\\*.sql"') do %PGSQL_BIN%\psql -f %%f -v ON_ERROR_STOP=1 --pset pager=off -o --client-min-messages=warning

pause
