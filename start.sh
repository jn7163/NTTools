export CLASSPATH=./classes

for jar in ./libs/hutool//*.jar;do
 export CLASSPATH=$CLASSPATH:$jar
done

for jar in ./libs/twitter4j/*.jar;do
 export CLASSPATH=$CLASSPATH:$jar
done

java -classpath $CLASSPATH io.kurumi.nt.cmd.NTMain