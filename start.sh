export CLASSPATH=./classes

for jar in ./libs/*.jar;do
 export CLASSPATH=$CLASSPATH:$jar
done

java -classpath $CLASSPATH io.kurumi.nt.cmd.NTMain