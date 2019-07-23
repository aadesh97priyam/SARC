# SARC
Static Analysis Report Combiner (SARC) merges reports from various tools (primarily but not limited to static code analysis tools).

## Index
1. [Prerequisites](#Prerequisites)
2. [Compiling](#Compiling)
3. [Executing](#Executing)
4. [Usage](#usage)
5. [Design Reports](#DesignReports)
    1. [Class Diagram](#ClassDiagram)
    2. [Sequence Diagram](#SequenceDiagram)
6. [Issues](#Issues)

## Prerequisites
First off, the project makes use of a batch file in order to build the source files and hence, the target system must have a Windows environment.
Furthermore, the target system must have Java (>=1.7) installed and available in the system path.
The project makes use of Maven as the build tool and hence, Maven must also be in the system path.

## Compiling
In the root directory of the project, a batch file by the name "AutoBuildTest.bat" is present. 
Run the batch file in the command line and it will compile the sources and execute the test cases.  
```
./../SARC> AutoBuildTest.bat
```  
Once the batch file has sucessfully executed, you should be able to see a `"build successful"` message.

## Executing
Once the batch file has been run, a binary will be put in the target folder and it will have the name "SARC-1.0-with-dependencies.jar".
In order to run the application, open the commandline and copy paste the below line.  
```
java -jar SARC-1.0-with-dependencies.jar
```  
Check the [Usage](#Usage) section for more details.

## Usage
TODO

## Design reports
### Class Diagram
![Class Diagram](/resources/classDiagram.png "Class Diagram")
### Sequence diagram
![Sequence Diagram](/resources/sequenceDiagram.png "Sequence Diagram")