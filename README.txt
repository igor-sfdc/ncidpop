Windows/Linux/Mac OS X: Builds with Eclipse (recommend Kepler or above).Here is how to create a deployment.

Note: This program has been designed to work on Java 1.5 and above.  When modifying this program, it is BEST to 
compile using the Java 1.5 JDK compiler to ensure optimum compatibility.  

Additional Note: In addition to building the below, there is a Macintosh Xcode project under the folder “libaddressbooklookup”.
This library has been built and added to the project, but should you need to build it again, please refer to the 
README.txt under that directory. If you plan to rebuild this library, it must be rebuilt prior to performing the below steps.

All Operating Systems:
1) From eclipse, Import the project by choosing Import then selecting General | Existing Projects into Workspace.
Select the directory that contains this README.txt file as your base.
2) Build the NCIDpop project (Eclipse typically auto builds for you)
3) Run the NCID project as a Java Application.  Choose NCIDpop (default package)

For Windows:
4) For deployment, we will create a runnable JAR.  
Select the ncidpop project.  Choose Export, then select Java | Runnable JAR File. 
Next select the launch configuration to be NCIDpop - NCIDpop
Next ensure that the export destination is NCIDpop/install/NCIDpop.jar
When prompted about including referenced libraries, click "OK"
5) On the file system, navigate to the install/Windows directory and locate the NCIDpop.nsi file.  
Make any changes needed for the new version numbers to this file, if needed.
Right click on NCIDpop.nsi and choose Compile NSIS Script.
6) You will now have a deployed version!

For Macintosh (java packager method - post Java 6):
4) For deployment, we will create a runnable JAR.  
Select the ncidpop project.  Choose Export, then select Java | Runnable JAR File. 
Next select the launch configuration to be NCIDpop - NCIDpop
Next ensure that the export destination is NCIDpop/install/NCIDpop.jar
When prompted about including referenced libraries, click "OK"
5) From a new terminal window, navigate to NCIDpop/install/Macintosh and locate the packagee-NCIDpop-javapackager.sh file.
Make any changes needed for the new version numbers to this file, if needed.
6) Execute package-NCIDpop-javapackager.sh
7) You will now have a deployed version!


For Macintosh-Compat (legacy JAR Bundler method - pre Java 6):

Note: It appears that using the JAR bundler on a PowerPC-based Mac has resulted in the deployment 
working on BOTH PowerPC and Intel-based Macs (a.k.a. "Universal Binary").  
If you make the deployment on a newer Intel-based Mac, it will NOT be compatible with PowerPC-based Macs. 
So, best to find an older Mac (minimum Mac OS X 10.4 "Tiger") for greatest compatibility.

4) For deployment, we will create a non-executable JAR.  
Select the ncidpop project.  Choose Export, then select Java | JAR File. 
For resources to export, click on NCIDpop to deselect ALL resources.  
Expand NCIDpop
Select src as a resource to export (our end objective: we only want to export src as a resource)
Next ensure that the export destination is NCIDpop/install/Macintosh/NCIDpop-nonexe.jar
5) From a new terminal window, launch Jar Bundler on the system: 
open /usr/share/java/Tools/Jar\ Bundler.app
or perhaps
open /Developer/Applications/Java Tools/Jar\ Bundler.app
or maybe even somewhere else.  Google it.
6) Next to main class, click the Choose... button.  
Navigate to NCIDpop/install/Macintosh/NCIDpop-nonexe.jar
After selecting NCIDpop.jar, the Main Class should populate as NCIDpop
Click "Choose Icon..."
Select NCIDpop/install/Macintosh/package/macosx/NCIDpop.icns
Change the JVM Version to 1.5+
7) Click the "Classpaths and Files" tab
8) Click the "add button"
9) Navigate to NCIDpop/lib. Select all items that end with *.jar.  Select choose.
10) Click the "add button" again
11) Navigate to NCIDpop/install.  Select CIDRing.wav.  Select choose.
12) Click the "add button" again
13) Navigate to NCIDpop/src/com/lenderman/ncidpop/resources.  Select libaddressbooklookup.dylib.  Select choose.
14) Click the "Properties" tab
For Version, enter the version number (i.e. 0.10.10.0)
For Get-Info String, enter something like this: Version 0.10.10.0, Copyright 2017 Chris Lenderman
For Bundle Name, enter NCIDpop
For identifier, put in com.lenderman.ncidpop.NCIDpop
15) Click "Create Application..."
For file, choose NCIDpop/install/Macintosh/NCIDpop
16) Give the application a test run and make sure it functions
Navigate to NCIDpop/install/Macintosh
double click on NCIDpop
17) To create a DMG install file
Using the Terminal, navigate to NCIDpop/install/Macintosh
execute: package-NCIDpop-JarBundler.sh
18) Test the installer
19) You will now have a deployed version!


For Linux
4) For deployment, we will create a runnable JAR.  
Select the ncidpop project.  Choose Export, then select Java | Runnable JAR File. 
Next select the launch configuration to be NCIDpop - NCIDpop
Next ensure that the export destination is NCIDpop/install/NCIDpop.jar
When prompted about including referenced libraries, click "OK"
5) Using the Terminal, navigate to NCIDpop/install/Linux
execute: tarball-NCIDpop.sh
6) You will now have a deployed version!