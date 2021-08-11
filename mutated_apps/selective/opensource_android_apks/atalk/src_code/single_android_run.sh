#! /bin/bash

path=$1

java -cp CryptoAnalysis-Android-1.0.0-jar-with-dependencies.jar -Xmx1024M -Xss60m main.CogniCryptAndroid $path $ANDROID_HOME/platforms $(pwd)/jca_rules
		#java -cp CryptoAnalysis-2.0-jar-with-dependencies.jar crypto.HeadlessCryptoScanner --rulesDir=$(pwd)/jca_rules --applicationCp=$j > $i/crysl.md
		#sed -i '1s/^/```\n/' $i/crysl.md
		#echo '```' >> $i/crysl.md
