#Prerequisites
In order to build the source files, the target system must have Java(>=1.7) installed and available in the system path.
The project makes use of maven as the build tool and hence, maven must also be in the path.
The project files require Windows environment.

#Compiling
In the root directory of the project, a batch file by the name "AutoBuildTest.bat" is present. 
Run the batch file in the command line and it will compile the sources and execute the test cases.
Once the batch file has sucessfully executed, you should be able to see a "build successful" message.

#Executing
Once the batch file has been run, a binary will be put in the target folder and it will have the name "SARC-1.0-with-dependencies.jar".
In order to run the application, open the commandline and enter "java -jar SARC-1.0-with-dependencies.jar".