@echo off
for /f "tokens=1" %%i in ('date /t') do set DATE_DOW=%%i
for /f "tokens=2" %%i in ('date /t') do set DATE_DAY=%%i
for /f %%i in ('echo %date_day:/=-%') do set DATE_DAY=%%i
for /f %%i in ('time /t') do set DATE_TIME=%%i
for /f %%i in ('echo %date_time::=-%') do set DATE_TIME=%%i
C:\Program Files\MySQL\MySQL Server 5.5\bin\mysqldump --hex-blob  -u root -proot1 -h localhost --default-character-set=utf8  --max_allowed_packet=64M  JPOS>"D:\SANGUINE\Projects\POS\Source Code\prjSPOSStartUp/DBBackup/1-1-2018_13-18_JPOS.sql" 