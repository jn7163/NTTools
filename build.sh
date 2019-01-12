for jar in ./libs/*.jar;do
 export CLASSPATH=$CLASSPATH:$jar
done

find -name "*.java" > sources.txt

javac -nowarn -classpath $CLASSPATH @sources.txt

rm -rf ./sources.txt