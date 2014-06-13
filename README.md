#TMC-Eclipse project
<<<<<<< HEAD
=======

####Master  
>>>>>>> DEV

[![Build Status](https://travis-ci.org/tmc-eclipse/tmc-eclipse.svg?branch=master)](https://travis-ci.org/tmc-eclipse/tmc-eclipse)
[![Coverage Status](https://coveralls.io/repos/tmc-eclipse/tmc-eclipse/badge.png?branch=master)](https://coveralls.io/r/tmc-eclipse/tmc-eclipse?branch=master)

<<<<<<< HEAD
=======
=======
####DEV  

[![Build Status](https://travis-ci.org/tmc-eclipse/tmc-eclipse.svg?branch=DEV)](https://travis-ci.org/tmc-eclipse/tmc-eclipse)
[![Coverage Status](https://coveralls.io/repos/tmc-eclipse/tmc-eclipse/badge.png?branch=DEV)](https://coveralls.io/r/tmc-eclipse/tmc-eclipse?branch=DEV)

>>>>>>> DEV
TMC-Eclipse project is a [University of Helsinki](http://helsinki.fi/university) [Department of Computer Science](http://cs.helsinki.fi) project for porting the [Netbeans](https://netbeans.org/) [TestMyCode plugin](https://github.com/testmycode/tmc-netbeans) to the [Eclipse IDE](http://www.eclipse.org/).

##Project structure
The project consists of two discrete components: a TestMyCode plugin core and a TestMyCode Eclipse plugin.

###Plugin core
The plugin core is a Java Maven project that contains the platform independent parts of the project. It interfaces with other TestMyCode components like the [server](https://github.com/testmycode/tmc-server) and the [test runner](https://github.com/testmycode/tmc-junit-runner).

###Eclipse plugin
The Eclipse plugin is an Ant project that binds the core to the Eclipse IDE allowing users to retrieve, complete, test and return exercises for courses utilizing the testMyCode enviroment.

The plugin component supports both Eclipse 4.3 (Kepler) and Eclipse 4.4 (Luna).

##Building the project
To build the project, follow the following process:

1. Clone this repository
2. Run the "clean" and "verify" Maven goals for the plugin component ("plugin-logic")
3. Open the Eclipse IDE
4. Make sure you have the following components installed from the marketplace:
 * [m2e](https://www.eclipse.org/m2e/) - Provides the required Maven support to the Eclipse IDE
 * [CDT](https://www.eclipse.org/cdt/) - C and C++ development tools for Eclipse IDE
<<<<<<< HEAD
 * [PDE](https://www.eclipse.org/pde/) - Plug-in Development Environment for Eclipse IDE
=======
>>>>>>> DEV
5. Restart the IDE if you had to install any of the above components
6. Import the Eclipse plugin component ("eclipse-plugin") using the "General > Existing Projects into Workspace" settings
7. Import the plugin core component using the "Maven > Existing Maven Projects" settings
8. Right click the Eclipse plugin component from the project browser and Export it using "Deployable plug-ins and fragments" settings

To use the built plugin, place it in the /plugins folder within your eclipse installation.

You can also run the project as "Eclipse Application" to run an instance of Eclipse with the plugin within the Eclipse instance you are developing on.

The plugin component utilizes the core component via a shaded .jar file that must be present in the plugin's /lib folder. When running the "verify" Maven goal for the core a shaded .jar will be built and moved to the plugin components /lib folder. For this reason, you **must** run the "clean verify" goals for any changes in the core to be visible within the plugin. Changes that only affect the plugin components code do not require running the "clean verify" goal.

##Using the plugin
###Installation
Installing the plugin consists of the following steps:

1. Move the plugin's .jar file to the /plugins folder within your Eclipse folder.
2. Start Eclipse
3. Make sure you have the following components installed from the marketplace:
 * [m2e](https://www.eclipse.org/m2e/) - Provides the required Maven support to the Eclipse IDE
 * [CDT](https://www.eclipse.org/cdt/) - C and C++ development tools for Eclipse IDE
4. Restart the IDE if you had to install any of the above components

###Using the plugin
See the instructions for using the Netbeans version.

##Credits
### TMC-Eclipse team:
<<<<<<< HEAD
=======

#### Developers

>>>>>>> DEV
* Juhani Heliö ([jphelio](https://github.com/jphelio))
* Ville-Pekka Hämäläinen ([veepee](https://github.com/veepee))
* Nikke Kostiainen ([nkostiai](https://github.com/nkostiai))
* Erkka Kääriä ([valtis](https://github.com/valtis))
* Leo Leppänen ([ljleppan](https://github.com/ljleppan/))
* Joel Nummelin ([numppa](https://github.com/numppa))
<<<<<<< HEAD
=======

#### Instructor

* Jarmo Isotalo ([jamox](https://github.com/jamox))

#### Client

* Arto Vihavainen ([avihavai](https://github.com/avihavai))
>>>>>>> DEV
