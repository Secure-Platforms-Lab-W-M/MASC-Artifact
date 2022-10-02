# Automated Analysis
You have already seen how to use MASC for mutant generation. In addition to that, you can also have MASC automatically compile and test the generated mutated apps from **main scope** with any static analysis tool of your choice. MASC can then analyze the report and tell you which mutants have been killed and which have not. This feature, automated analysis, is available for the Crypto-detectors that can output in SARIF format. With time, more and more Crypto-detectors are extending their support for SARIF format. But out of those that don't support SARIF format yet, MASC's automated analysis is available for CogniCrypt. 

## How it works
In order to use this feature, you have to - 
1. Write the necessary information in the properties file. 
2. Run MASC normally.

With the necessary information, MASC will - 

1. Generate mutated apps.
2. Compile the mutated apps. 
3. Run your specified static analysis tool for each mutated app and read the output.
4. Show a list showing which mutants have been killed and which have not. 

## 1. The extra info in the properties file
As mentioned earlier, MASC requires a some extra info in the properties file for this feature. The extra fields can be added at the end of the properties file. The extra fields are shown below - 

1. automatedAnalysis = true
1. toolName = Name of the tool
2. toolLocation = Folder directory of the tool's executable
3. toolRunCommand = Command line command that you use to run the tool manually from the command line. 
4. codeCompileCommand = Command to compile the mutated apps. The recommended value for this is `javac *.java`
5. outputReportDirectory = Directory of the folder where the output report will be generated after the tool runs successfully.
6. outputFileName = File name of the output report in outputReportDirectory.
7. stopCondition = When to stop. The possible values are (case insensitive):
    - OnError: Whenever an error such as compilation error, output report not found, wrong command or runtime exception of the tool is encountered. 
    - OnUnkilledMutant - Whenever the tool fails to kill a mutant
    - OnErrorOrUnkilled - Whenever an error is encountered or the tool fails to kill a mutant. 
    - Default (or any other value) - Only stop when the attempt to analyze all mutated apps have completed.

The values will be different for each tool. In addition, it will be different for each operating system since it contains commands that will ultimately be run by MASC in the command line. 

Sample `.properties` file (when run on Windows) -

```
mutantGeneration = true
type = StringOperator
outputDir = app/outputs
apiName = javax.crypto.Cipher
invocation = getInstance
secureParam = AES/GCM/NoPadding
insecureParam = AES
scope = MAIN
noise = ~
variableName = cryptoVariable
className = CryptoTest
propertyName = propertyName

automatedAnalysis = true
toolName = find-sec-bugs
toolLocation = D:/Downloads/Compressed/findsecbugs-cli-1.12.0
toolRunCommand = findsecbugs.bat -high -sarif -output out.json {}
codeCompileCommand = javac *.java
outputReportDirectory = D:/Downloads/Compressed/findsecbugs-cli-1.12.0
outputFileName = out.json
stopCondition = OnError
```
Sample `.properties` file (when run on Ubuntu) -

```
mutantGeneration = true
type = StringOperator
outputDir = app/outputs
apiName = javax.crypto.Cipher
invocation = getInstance
secureParam = AES/GCM/NoPadding
insecureParam = AES
scope = MAIN
noise = ~
variableName = cryptoVariable
className = CryptoTest
propertyName = propertyName

automatedAnalysis = true
toolName = find-sec-bugs
toolLocation = /home/yusuf/Downloads
toolRunCommand = bash findsecbugs.sh -high -sarif -output out.json {}
mutatedAppsLocation = app/outputs
codeCompileCommand = javac *.java
outputReportDirectory = /home/yusuf/Downloads
outputFileName = out.json
stopCondition = OnError
```

**About the `toolRunCommand`** - Running most tools require a command in this format - 
``` 
toolExecutable --parameters folderDirectoryOfTheAppToTest
```
Notice how `folderDirectoryOfTheAppToTest` has been replaced by `{}` in the properties file. This is needed because MASC will have the tool analyze tens of mutated apps one after another. So the value of `folderDirectoryOfTheAppToTest` can't be fixed. Instead, placing `{}` will tell MASC where to insert the folder directory for a particular mutated app. 

**About the keys `automatedAnalysis` and `mutantGeneration`**

More on this [here](#running-only-for-mutant-generation-without-automated-analysis) and [here](#running-only-for-automated-analysis-without-mutant-generation)

**Order of these items in the properties file DO NOT matter**

## 2. Running MASC with automated analysis
This is how you normally run MASC (replacing Cipher.properties with properties file of your choice) - 
```
java -jar MASC.jar Cipher.properties
```
And this is how you run MASC with automated analysis -
```
java -jar MASC.jar Cipher.properties FindSecBugs.properties
```

## Errors and missing values
Don't worry if you forget to include any necessary field in the properties file. MASC will ask for input if you do. 

Apart from that, don't panic if you face errors along the way. If an error is encountered, you will be shown what the error (as would be seen on the command line) was, and what command MASC was trying to execute which resulted in this error. Reading the error message and the attempted command will immensely help you write correct input in the second properties file. 

## Running only for mutant generation without automated analysis
If you want to only run mutant generation, do either of the following - 
 - Set automatedAnalysis = false in the properties file
 - Or, comment out or omit the value of automatedAnalysis in the properties file
 - OR, set the value of automatedAnalysis in the properties file anything other than 'true'. 
 
 If you are not running automated analysis, it does not matter whether you include or omit the other key-value pairs that are exclusively required for automated analysis. These include - toolName, toolLocation, toolRunCommand, mutatedAppsLocation, codeCompileCommand, outputReportDirectory, outputFileName,  stopCondition.

 We recommend that you only set automatedAnalysis = false and leave everything else as is. This way, the properties file will not require much change if you later choose to run automated analysis again. 

 ## Running only for automated analysis without mutant generation
If you think you have generated the mutants that you needed, and don't need to generate them again, then you can skip mutant generation and go directly to automated analysis. To do that, do either of the following - 
 - Set mutantGeneration = false in the properties file
 - Or, comment out or omit the value of mutantGeneration in the properties file
 - OR, set the value of mutantGeneration in the properties file anything other than 'true'. 
 
 If you are not running mutant generation, it does not matter whether you include or omit the other key-value pairs that are exclusively required for mutant generation. 

 We recommend that you only set mutantGeneration = false and leave everything else as is. This way, the properties file will not require much change if you later choose to run mutant generation again. 

 ## Support for CogniCrypt users
 At this moment, CogniCrypt does not output in the SARIF format. Fortunately, MASC's automated analysis extends its support for CogniCrypt too. Just add an extra field - 
 ```
 wrapper = CogniCrypt
 ```
 And you are done! Here is a sample properties file - 
```
mutantGeneration = true
type = StringOperator
outputDir = app/outputs
apiName = javax.crypto.Cipher
invocation = getInstance
secureParam = AES/GCM/NoPadding
insecureParam = AES
scope = MAIN
noise = ~
variableName = cryptoVariable
className = CryptoTest
propertyName = propertyName

automatedAnalysis = true
toolName = CogniCrypt
toolLocation = F:/IIT/Projects/SPL3/CryptoAnalysis-2.7.2/CryptoAnalysis-2.7.2/CryptoAnalysis/build
toolRunCommand = java -cp CryptoAnalysis-2.7.2-jar-with-dependencies.jar crypto.HeadlessCryptoScanner --rulesDir F:/IIT/Projects/SPL3/CryptoAnalysis-2.7.2/CryptoAnalysis-2.7.2/CryptoAnalysis/src/main/resources/JavaCryptographicArchitecture --applicationCp {} > out.txt
codeCompileCommand = javac *.java
outputReportDirectory = F:/IIT/Projects/SPL3/CryptoAnalysis-2.7.2/CryptoAnalysis-2.7.2/CryptoAnalysis/build
outputFileName = out.txt
stopCondition = OnError
wrapper = CogniCrypt
```