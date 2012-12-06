Intelligent Lock Pattern Project
================================

This project is part of a bigger project named Intelligent Lock Pattern that has the goal of build a behavioral biometric system on Android based on the lock patterns authentication system present on the OS.

The project started as a university course conclusion work in 2012 and now it's open for new ideas and improvements.

The basic idea is make an artificial intelligence that learns how the user draws his pattern on the screen. Using the knowledge learned, the system verifies if the user that is drawing the pattern is authentic through the inserted characteristics during the drawing.

ILP Network Training
--------------------

This software was made to facilitate the training with all the different configurations of neural networks needed.

The [Encog Framework](http://www.heatonresearch.com/encog) is used to build all the networks' configurations tested.

The database used is a [SQLite](http://www.sqlite.org/), extracted from the phone where the characteristics were collected.

The ORM used to facilitate the communication with the database was [ORMLite](http://ormlite.com/).

All the libraries used on the project are located on the libs folder.

In the folder **db** is an database example with some samples collected during the research. 

The others sub projects:
*	[ILP Models Generator](https://github.com/lucasandre/ilp-models-generator)
*	[ILP Network Models](https://github.com/lucasandre/ilp-network-models)
*	[Intelligent Lock Pattern](https://github.com/lucasandre/intelligent-lock-pattern)