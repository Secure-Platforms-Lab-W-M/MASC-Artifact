# Setting Up Masc-Core on Intelli J IDEA

While other editors exist and can be used to edit and contribute to Masc, this set up will focus on the popular Intelli J IDE.  Those that stray from the path do so at their own risk.

## Installing Intelli J
The Intelli J Community Edition can be downloaded for free from [this link](https://www.jetbrains.com/idea/download/#section=windows).

Follow the instructions of the on screen wizard until installation is successful.

## Cloning Masc-Core
Now that our IDE is installed, we need to clone the [Masc Repository](https://github.com/WM-SEMERU/MASC-Spring21-635) from GitHub.  You can do this from Intelli J, Github Desktop, or the command line.  Take note of where the cloned repository is saved on your machine.

## Opening Masc
Once you have successfully cloned the entire Masc Repository, open Intelli J.  Select the open option, to open an existing project.  A file explorer window should open.  Navigate to the directory where you cloned the Masc Repo.  Do NOT open the root level of the repository!  Instead, navigate down the nested project folders until you reach masc-core.  Open the masc-core folder as your project.

## Configuring the project
If all goes well, you will be asked if you want to trust the Gradle project.  Press 'Trust Project.'  Pay attention to the event log in the lower-right corner.  An error concerning the JDK / SDK should appear.  This prevents the successful build of the project.  The solution is simple.  

---
#### On Windows

>Press CTRL+ALT+S to open the settings dialog.  Note: this may also open system information.  If this happens, close the Windows pop-up.


#### On Mac
>Go to the navigation bar at the top of your screen. Click on IntelliJ IDEA and then select Preferences or use the shortcut COMMAND, to get to Preferences.

---
On the left-hand side, click the 'Build, Execution, Deployment' drop-down.  From the sub-menu, select 'Build Tools' and then 'Gradle.'  In the right pane, at the bottom, you want to supply a Gradle JVM.  You can download one straight from Intelli J. We use the most recent LTS version, which is JDK 11.
Hit Apply and OK.  Intelli J should automatically begin building your project.

## Running Masc-Core
Open MASC.java.  It can be found at `masc-core > app > src > main > java > edu.wm.cs.masc > MASC.java.`
You should see a green play button beside the main method of the file.  Press this button.  Select the first option from the menu.  Congratulations, you just ran Masc-Core, but wait, Nothing really happened, except for a message saying "No properties file supplied". Continue to the next section to resolve these.

## Resolving Initial Errors

If you have made it this far in the setup process, then you aren't seeing any useful outputs yet.  Do not fear, the solution is simple.  Note that the program needs exactly one parameter, i.e., the name of the properties file. The default `Cipher.properties` file is given in src > main > resources.  Find the file in the resources folder and open it.  Notice that the `outputDir` field specifies a file path. Ensure that the file exists on your system. Then click on "edit build configuration" from the toolbar at the top, and add Cipher.properties in program arguments.  The program should now run without errors.  Look in the specified directory, and you will find all the mutations.

## Building JAR

If you want to create a JAR file for MASC, just run `gradlew shadowJar`. The output JAR can be found at `masc-core > app > build > libs > app-all.jar`.
To run the JAR, place Cipher.properties in the same directory as the JAR and run `java -jar app-all.jar Cipher.properties`