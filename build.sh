for jar in ./libs/*.jar;do
 export CLASSPATH=$CLASSPATH:$jar
done

find -name "*.java" > sources.txt

rm -rf ./classes

mkdir /classes

javac -d classes -nowarn -classpath $CLASSPATH @sources.txt

rm -rf ./sources.txt