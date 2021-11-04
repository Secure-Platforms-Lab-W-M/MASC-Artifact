# Setting Up Muse on Intelli J IDEA

While other editors exist and can be used to edit and contribute to Muse, this setup will focus on the popular Intelli J IDE.  Those that stray from the path do so at their own risk.  You have been warned.

## Installing Intelli J
The Intelli J Community Edition can be downloaded for free from [this link](https://www.jetbrains.com/idea/download/#section=windows).

Follow the instructions of the on screen wizard until installation is successful.

## Cloning Muse
Now that our IDE is installed, we need to clone the [Masc Repository](https://github.com/WM-SEMERU/CSci435-Fall21-MASC) from Github.  You can do this from Intelli J, Github Desktop, or the command line.  Take note of where the cloned repository is saved on your machine.

## Opening Muse
Once you have successfully cloned the entire Masc Repository, open Intelli J.  Select the open option, to open an existing project.  A file explorer window should open.  Navigate to the directory where you cloned the Masc Repo.  Do NOT open the root level of the repository!  Masc is currently comprised of three separate projects: Masc-Core, Muse, and MDroid.  Instead navigate down the nested project folders until you see Muse.  Open the Muse folder as your project.

## Configuring the project
The window will ask if you want to build the project with Eclipse or Maven.  Select Maven.  Next, you will be asked if you want to trust the Maven project.  Press 'Trust Project.'  A pop-up concerning legal issues may appear at the lower-right corner.  In order to continue, you must agree to the terms.  Once everything is built correctly, you are ready to continue.

## Running Muse
Open Muse.java.  It can be found at `muse > src > edu.wm.cs.masc > muse > Muse.java.`
You should see a green play button beside the main method of the file.  Note: The main method is located near the bottom of the file.  Press the button.  Select the first option from the menu.  Congratulations, you just ran Muse.  

If you look at the console, you should see output that looks like:

```
******* ERROR: INCORRECT USAGE *******
Values need to be defined in config.properties in order to run Muse:
lib4ast: -----------
appSrc: -----------
appName: -----------
output: -----------
operatorType: TAINTSINK, SCOPESINK, REACHABILITY or COMPLEXREACHABILITY
source: -----------
sink: -----------
varDec: -----------
Please provide the path to the config.properties file on command line
```

To run Muse successfully, you will need to provide a command-line argument to Muse's main method.  Let's look at how to do that in Intelli J.

## Supplying Command Line Parameters in Intelli J
1. Click the green play button beside the main method.
2. Instead of selecting the first option, click on the last one (Modify Run Configuration).
3. Type `.\src\edu\wm\cs\muse\sample_config.properties` into the text field labeled `Program Arguments`.  You need to supply the path where the file can be found by the system.
4. Press `Apply` and hit `OK`.

Now click the play button again to run the main method.

You will see the same error as before.  However, this time, there is a different cause.  (Note: the error messages should be updated for clarity).  The issue now stems from the contents of the sample_config.properties file.  Let's address those issues now.


## Supplying Arguments through the Properties file
Before running Muse again, open the `sample_config.properties`.  Notice that the properties file has multiple fields, but all of them are of the form *<something\>*.  To start applying mutations, you need to supply path arguments for the `lib4ast` `appSrc` and `output` fields.  Next, supply the operator type.  We recommend starting with the `REACHABILITY` operator type.  Lastly you need to choose an `appName`.  This will be the name of the folder that the mutationed files are saved in.  Below you will find descriptions of the fields you should complete before continuing.

- lib4ast: path to MDroid directory
- appSrc: path to input directory (where are your files stored?)
- operatorType: type of operator (how should mutations be applied?)
- appName: name of the app folder (what folder should mutations be put in?)
- output: path to output directory (where should the mutations be saved?)

If you want to use the default input and output directories, you can find them at: `muse > test > input` and `muse > test > output` respectively.  Make sure you supply the full file paths in the .properties file.

## Running Muse
Now you should be all set.  If you hit the green play button again, the console should now be filled with stats on the mutation successes.  This tutorial will end here, but know there is still much more to explore in Muse.
