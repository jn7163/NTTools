for jar in ./libs/*.jar;do
 export CLASSPATH=$CLASSPATH:$jar
done

javac -classpath $CLASSPATH -sourcepath ./src