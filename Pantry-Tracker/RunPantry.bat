@echo off
java -version >nul 2>&1 || (echo [ERROR] Java is not installed. & pause & exit /b 1)
start /b cmd /c "timeout /t 3 >nul && start http://localhost:7070"
start /b java --enable-native-access=ALL-UNNAMED -jar target/smart-pantry-1.0-SNAPSHOT-jar-with-dependencies.jar
timeout /t 4 >nul & cls
echo ======================================================
echo   SMART PANTRY MANAGER IS RUNNING
echo ======================================================
echo The server logs are running in the background.
echo [TO STOP]: PRESS ANY KEY IN THIS WINDOW
pause >nul
echo Stopping server...
taskkill /F /IM java.exe >nul 2>&1
echo Done!
timeout /t 2 >nul & exit
