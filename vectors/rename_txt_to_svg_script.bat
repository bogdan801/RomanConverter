@echo off
setlocal enabledelayedexpansion

REM Specify the source extension
set "source_extension=.txt"

REM Specify the target extension
set "target_extension=.svg"

REM Loop through all files with the source extension
for %%F in (*%source_extension%) do (
    REM Get the file name without extension
    set "filename=%%~nF"
    
    REM Rename the file with the new extension
    ren "%%F" "!filename!!target_extension!"
)

echo All .txt files have been renamed to .svg.
pause
