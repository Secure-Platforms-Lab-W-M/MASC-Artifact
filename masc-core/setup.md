# Setting Up Masc-Core on Intelli J IDEA

While other editors exist and can be used to edit and contribute to Masc, this set up will focus on the popular Intelli J IDE.  Those that stray from the path do so at their own risk.

## Installing Intelli J
The Intelli J Community Edition can be downloaded for free from [this link](https://www.jetbrains.com/idea/download/#section=windows).

Follow the instructions of the on screen wizard until installation is successful.

## Cloning Masc-Core
Now that our IDE is installed, we need to clone the [Masc Repository](https://github.com/WM-SEMERU/CSci435-Fall21-MASC) from Github.  You can do this from Intelli J, Github Desktop, or the command line.  Take note of where the cloned repository is saved on your machine.

## Opening Masc-corresponding
Once you have successfully cloned the entire Masc Repository, open Intelli J.  Select the open option, to open an existing project.  A file explorer window should open.  Navigate to the directory where you cloned the Masc Repo.  Do NOT open the root level of the repository!  Instead navigate down the nested project folders until you reach masc-core.  Open the masc-core folder as your project.

## Configuring the project
If all goes well, you will be asked if you want to trust the Gradle project.  Press 'Trust Project.'  Pay attention to the event log in the lower-right corner.  An error concerning the JDK / SDK should appear.  This prevents the successful build of the project.  The solution is simple.  

---
#### On Windows

>Press CTRL+ALT+S to open the settings dialog.  Note: this may also open system information.  If this happens, close the Windows pop-up.


#### On Mac
>Go to the navigation bar at the top of your screen. Click on IntelliJ IDEA and then select Prefrences or use the shortcut COMMAND, to get to Prefrences.

---
On the left-hand side, click the 'Build, Execution, Deployment' drop-down.  From the sub-menu, select 'Build Tools' and then 'Gradle.'  In the right pane, at the bottom, you want to supply a Gradle JVM.  You can download one straight from Intelli J.  I am using and recommend corretto-15.  Hit Apply and OK.  Intelli J should automatically begin building your project.

## Running Masc-Core
Open MASCBarebone.java.  It can be found at `masc-core > app > src > main > java > masc > edu.wm.cs.masc > barebone > MASCBarebone.java.`
You should see a green play button beside the main method of the file.  Press this button.  Select the first option from the menu.  Congratulations, you just ran Masc-Core, but wait, you got lots of runtime errors... Continue to the next section to resolve these.

## Resolving Initial Errors
If you have made it this far in the setup process, then you are seeing lots of runtime errors in your Intelli J console.  Do not fear, the solution is simple.  Note that the program takes the `Cipher.properties` file as input by default.  Find the file in the resources folder and open it.  Notice that the `outputDir` field specifies a file path that is not on your system.  Change this line to an appropriate file path.  Make sure that it exists!  Save the file and rerun the project.  The program should now run without error.  Look in the specified directory and you will find all of the mutations.
