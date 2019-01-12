for %%i in (.\libs\*.jar) do set CLASSPATH=!CLASSPATH!;%%i

javac -classpath "%CLASSPATH%" -sourcepath ".\src"