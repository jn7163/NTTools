set CLASSPATH=./classes

for %%i in (.\libs\*.jar) do set CLASSPATH=!CLASSPATH!;%%i

java -classpath "%CLASSPATH%" io.kurumi.nt.cmd.NTMain