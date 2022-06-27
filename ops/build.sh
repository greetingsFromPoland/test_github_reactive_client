#!/usr/bin/env bash

cd ..
./mvnw clean package

cd target

export fullJarFileName=$(ls *.jar)
charCountInJarFileName=$(echo $fullJarFileName | wc -c )
echo "fullName $fullJarFileName"
let charCountInJarFileName=charCountInJarFileName-5
echo "charCountInJarFileName $charCountInJarFileName"

jarFileName=${fullJarFileName:0:charCountInJarFileName};
echo "jarFileName $jarFileName"

IFS='-'
read -ra fileNameArray <<< "$jarFileName"

echo ${fileNameArray[0]}
echo ${fileNameArray[1]}
echo ${fileNameArray[2]}

echo "mv $fullJarFileName app.jar"

cp *.jar ../ops/image/app.jar
cd ../ops/image/
docker build . -t jarekwasowski:${fileNameArray[1]}
docker build . -t jarekwasowski:latest
rm app.jar
