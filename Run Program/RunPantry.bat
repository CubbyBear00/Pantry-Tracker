@echo off
:: 1. Check if Java is installed
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java is not installed.
    pause
    exit /b 1
)

echo Starting Smart Pantry Manager...

:: 2. Launch the browser after a short delay [cite: 4]
start /b cmd /c "timeout /t 3 >nul && start http://localhost:7070"

:: 3. Start the Java app in the background of THIS window [cite: 4]
:: This allows the script to continue to the "pause" command below.
start /b java --enable-native-access=ALL-UNNAMED -jar target/smart-pantry-1.0-SNAPSHOT-jar-with-dependencies.jar

:: 4. Wait for the server to log some info, then clear the screen for your message
timeout /t 4 >nul
cls

echo ======================================================
echo   SMART PANTRY MANAGER IS RUNNING
echo ======================================================
echo.
echo The server logs are running in the background.
echo.
echo [TO STOP]: PRESS ANY KEY IN THIS WINDOW
echo.
echo ======================================================

:: 5. This stays at the bottom and waits for you
pause >nul

echo.
echo Stopping server...
:: 6. This kills the Java process we started 
taskkill /F /IM java.exe >nul 2>&1

echo Done!
timeout /t 2 >nul
exit