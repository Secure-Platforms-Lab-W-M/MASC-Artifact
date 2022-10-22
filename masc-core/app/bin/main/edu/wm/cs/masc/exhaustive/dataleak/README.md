# Muse - Custom Leaks

The custom leak feature in Muse allows for the user to define their own leaks for application mutation. Muse will use the custom source, sinks, and/or leak variable declarations when mutating the Android application.

## Usage
To define custom leaks in Muse execution, use the keyword `mutate` on the command line. 

```
java -jar Muse-1.0.0.jar mutate (<ConfigFilePath>)
```

### Arguments
The only arguments on the command line is the path to the `config.properties` file where all runtime values will be defined. To utilize the custom leak strings feature of Muse, the `source`, `sink`, and `varDec` values should be define in the properties file.

1. ``ConfigFilePath``: This is the path to the config.properties file that Muse uses to read arguments. These arguments defined in the config.properties file include:
- ``libs4ast``:  Path of the lib4ast folder, from [MDroidPlus](https://gitlab.com/SEMERU-Code-Public/Android/Mutation/MDroidPlus/tree/master/libs4ast);
- ``appSrc``: Path of the Android app source code folder, which you want to apply mutation on;
- ``appName``:  Name of the App;
- ``output``: Path of the folder where the mutants will be created;
- `operatorType`: Type of operator to be used while creating mutants. Currently supported arguments are: TAINTSINK, REACHABILITY, SCOPESINK, and COMPLEXREACHABILITY.

These values are required for using custom leak string in Muse

   - ``source``: Value of the custom source to be used in Muse
   - ``sink``: Value of the custom sink to be used in Muse
   - ``varDec``: Value of the custom variable declaration to be used in Muse


### Example

```
java -jar Muse-1.0.0.jar mutate /config.properties
```

The `config.properties` file is defined as:
```
lib4ast: MDroidPlus/libs4ast/
appSrc: /tmp/AppFoo/src/
appName: AppFoo
output: /tmp//mutants/
operatorType: SCOPESINK
source: sourceString
sink: sinkString
varDec: varDecString
```

This will create a folder called `AppFoo` under `/tmp/mutants` where the mutated source files will be stored. 

This will also mutate the app using the custom leak strings defined by the user in the properties file.

# Muse - PlacementChecker
The PlacementChecker feature allows muse to not include any leaks that might create a non-compilable application. Since muse analyzes source files that are taken out of context, there may be errors that PlacementChecker will ignore. 

## Usage
PlacementChecker is called by operators before a leak is placed. It accepts the source file being worked on and creates a temporary file which will be checked for compilability. 
If this temporary file is deemed compilable, the leak will remain in the source file. If the temporary file is not compilable, the leak will be removed. 

### Example

```
package com.markuspage.android.atimetracker; 
import android.Manifest;      // An import like this would be seen as an error from eclipse, 
                              // however PlacementChecker would ignore it
public Class Example{
   String dataleak;
   
   public static methodA(){   
      dataleak = "leak";   // This leak would be caught by the PlacementChecker because a non-static variable 
      int a = 5;           // is being referenced in a static contet
   }
}
```
