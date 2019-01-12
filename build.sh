for jar in ./libs/*.jar;do
 export CLASSPATH=$CLASSPATH:$jar
done

javac -classpath $CLASSPATH -sourcepath $(dirname $(readlink -f $0))/src