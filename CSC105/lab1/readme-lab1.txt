@title			README FOR CSC105 LAB 1
@author			Jeremy Foo
@user			hp086541
@workstation		C199
@class			FS1
@course			CSC105
@lab			1

COMPILE
To compile the Lab, load the NetBeans project in NetBeans and do a clean and build.

RUNNING
The lab can be run either from NetBeans or from the command line. It accepts input from the command line either via manual keyboard entry or through a typescript file as show in the "test" directory of the project. The typescript files are "commands" and "commands2" respectively.

They represent 2 formats that are accepted by the program input. One which is a generic command followed by a newline before the next command format. The other has each command and the sub commands tab delimited. The program detects tabs and newline characters alike and treats them as whitespace separating each command.

IDEA
The basic premise for this lab is based upon 2 files. The main driver with the main() method in LabOne.java and the Matrix class with its associated methods and properties in the Matrix class file.

** MATRIX CLASS
The matrix class holds basic properties of the matrix namely its stipulated rows and columns as well as all the elements in the matrix. It has getter methods for the rows and columns as well as the elements. Setter methods are only available for the elements of the Matrix. This keeps and holds the integrity of whatever instantiated Matrix object. Consequently, this forces the creation of Matrix objects to be done solely via Constructors which a variety is provided for. 

You can construct the Matrix from a Matrix file which has all the properties of the matrix embedded or a generic matrix which only has its rows and columns set and all elements set to zero. You can also create an empty Matrix by using its default constructor.

The toString() method is overridden to provide a way to describe whichever instantiated Matrix object.

** LABONE MAIN DRIVER
The LabOne class provides all the main functions of the lab namely, addition, multiplication, transposition and display of the 3 Matrixes embedded in the program. Each of the functions provide a check to ensure the corresponding matrix operation will actually work with the exception of Transposition.

Matrix operations are done using setter and getter methods for elements of the Matrix object.

The driver uses exception handling to deal with erroneous input.

The driver also provides helper methods to load the Matrix file automatically from selections as well as a better way to grab input from the standard system IO.

It additionally implements a method to help with matrix multiplication.