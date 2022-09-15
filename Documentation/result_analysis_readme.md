# Automatic Result Analysis
With some simple setup, you can have MASC automatically compile and test the generated mutated apps from **main scope** with any static analysis tool of your choice.  If your tool supports SARIF format for its output report, MASC can also analyze the report and tell you which mutants have been killed and which have not. 

## How it works
In order to use this feature, you have to - 
1. Write the necessary information in a second properties file. 
2. Supply the second properties file as command line argument.

With the necessary information, MASC will - 

1. Generate mutated apps.
2. Compile the mutated apps. 
3. Run your specified static analysis tool for each mutated app and read the output.
4. Show a list showing which mutants have been killed and which have not. 

## 1. The second properties file
As mentioned earlier, MASC requires a second properties file for this feature. This properties file must have the following fields - 

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

The `.properties` file will be different for each tool. In addition, it will be different for each operating system since it contains commands that will ultimately be run by MASC in the command line. 

Sample `.properties` file (when run on Windows) -

```
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

## 2. Running MASC with result analysis
This is how you normally run MASC (replacing Cipher.properties with properties file of your choice) - 
```
java -jar MASC.jar Cipher.properties
```
And this is how you run MASC with result analysis -
```
java -jar MASC.jar Cipher.properties FindSecBugs.properties
```

## Errors and missing values
Don't worry if you forget to include any necessary field in the properties file. MASC will ask for input if you do. 

Apart from that, don't panic if you face errors along the way. If an error is encountered, you will be shown what the error (as would be seen on the command line) was, and what command MASC was trying to execute which resulted in this error. Reading the error message and the attempted command will immensely help you write correct input in the second properties file. 