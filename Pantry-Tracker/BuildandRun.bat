@echo off
:: 1. Build the project
echo ========================================
echo   BUILDING SMART PANTRY MANAGER...
echo ========================================
:: Use CALL to ensure the script continues after Maven finishes [cite: 3]
call mvn clean install assembly:single

:: Check if the build failed (e.g., due to a code typo) [cite: 3]
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Build failed! Check the error messages above. [cite: 3]
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo ========================================
echo   STARTING SERVER...
echo ======================================== 

:: 2. Launch the browser after a 4-second delay in the background
start /b cmd /c "timeout /t 4 >nul && start http://localhost:7070"

:: 3. Run the "Fat JAR" in the background of THIS window [cite: 4]
:: We use the security flag to fix the SQLite warning
start /b java --enable-native-access=ALL-UNNAMED -jar target/smart-pantry-1.0-SNAPSHOT-jar-with-dependencies.jar

:: 4. Wait for initial server logs, then clear the screen for a clean UI
timeout /t 5 /nobreak >nul
cls

echo ======================================================
echo   BUILD SUCCESSFUL - SYSTEM IS RUNNING 
echo ======================================================
echo.
echo Dashboard: http://localhost:7070
echo.
echo [TO STOP]: PRESS ANY KEY IN THIS WINDOW [cite: 6]
echo ======================================================

:: 5. Wait for user input 
pause >nul

echo.
echo Shutting down server...
:: 6. Clean up the background process on exit 
:: Since this is a dev script, we'll use the reliable image name kill
taskkill /F /IM java.exe >nul 2>&1

echo Done!
timeout /t 2 >nul
exit