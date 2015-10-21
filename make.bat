@echo off
echo select evn : dev, prd
set /p evn=>nul 
echo evn=%evn%
cd %~dp0
call mvn clean package -Devn=%evn% -e
pause