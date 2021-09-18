# Muse - Log Analyzer
The Log Analyzer extension for the Muse tool helps the user remove certain leaks from an application with ease. If the user wants to adjust the leaks in a mutated application, Muse can remove all leaks specified by the user through a universal log format. This feature can be utilized to recheck static analysis tools with a subset of leaks in a mutated application.

## Usage
To run the Log Analyzer Muse extension, the `logAnalyze` keyword should be specified on the command line. The `config.properties` file path should also define all values that Muse will use during execution. `(arg)` specifies required arguments:

```
java -jar Muse-1.0.0.jar logAnalyze (<ConfigFilePath>)
```

If running Muse within an IDE like Eclipse, import only the Muse folder within the code subdirectory, or else you might get a java.lang.SecurityException error when running Muse.java

### Arguments

Provide the following list of required arguments when running Muse: 
1. ``ConfigFilePath``: This is the path to the config.properties file that Muse uses to read arguments. These arguments defined in the config.properties file include:
- ``appSrc``: Path of the mutated Android app source code folder, which you want to remove leaks from;
- `operatorType`: Type of operator used when creating mutants. Currently supported arguments are: TAINTSINK, REACHABILITY, SCOPESINK, and COMPLEXREACHABILITY.
- ``insertionLog``: Path to a log file listing all of the leaks inserted by Muse. This information is output to the console when running "Mutate".
- ``executionLog``: Path to a log file containing all executed leaks that will should remain in the mutant app.

If custom data leaks are being used, the following arguments are also required:
- ``source``: Value of the custom source to be used in Muse
- ``sink``: Value of the custom sink to be used in Muse
- ``varDec``: Value of the custom variable declaration to be used in Muse

### Example
```
java -jar Muse-1.0.0.jar logAnalyze C:/config/config.properties
```

The `config.properties` file is defined as:
```
appSrc: C:/app/sample_multilevelclass
operatorType: TAINTSINK
insertionLog: C:/log/insertion.txt
executionLog: C:/log/execution.txt
```
Leaks inside of the `execution log` file should be formatted as one of the following strings:
  ```
leak-0
leak-0-0
  ``` 
  Any leaks that are present in the insertion log but not in the execution log will be identified by the Log Analyzer and removed.
