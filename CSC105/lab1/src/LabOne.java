/**
 *
 * @author jeremy foo jie you
 * @workstation c199
 * @user hp086541
 * @class fs1
 * @course csc105
 * @lab 1
 */

import java.io.*;

public class LabOne {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {

        // declare all variables used.
        // - m1 to m3 for Matrix class.
        // - command to store selected main menu selection.
        // - matrixdisplay to hold value of matrix to display for command 4.
        // - i and j are counters to loop through elements in Matrix.
        // - result string to store and assemble output for display.
        Matrix m1 = new Matrix();
        Matrix m2 = new Matrix();
        Matrix m3 = new Matrix();
        int command, matrixdisplay, i, j;
        String result = "";

        // loop until program exits.
        while (true) {

            // reset command to default
            command = 0;

            // show main menu and ensure input is valid.
            try {
                MainMenu();
                command = Integer.parseInt(getInput());
                
            } catch (Exception e) {
                //System.out.println(e.toString()); for debugging.
            }
            
            switch (command) {

                // command to add 2 matrix from file.
                case 1:
                    try {

                        // load matrix
                        m1 = null;
                        m1 = loadAndInitMatrixFromFileSelection("Enter Matrix 1 File: ");
                        m2 = null;
                        m2 = loadAndInitMatrixFromFileSelection("Enter Matrix 2 File: ");

                        // check if they are valid to be added by comparing rows and cols are equal.
                        if ((m1.getCols() == m2.getCols()) && (m1.getRows() == m2.getRows())) {

                            // construct result matrix by initialising it with rows and colums
                            m3 = new Matrix(m1.getRows(),m1.getCols());

                            // begin adding matrix together by adding corresponding rows and cols to each other.
                            for (i = 0;i<m3.getRows();i++) {
                                for (j = 0;j<m3.getCols();j++) {
                                    m3.setElement(i, j, (m1.getElement(i, j)+m2.getElement(i, j)));
                                }
                            }

                            // display results.
                            System.out.println("\nMatrix 1");
                            System.out.println(m1.toString());

                            System.out.println("\nMatrix 2");
                            System.out.println(m2.toString());

                            System.out.println("\nThe Summation Result is");
                            System.out.println(m3.toString());
                        } else {
                            System.out.println("\nMatrix not of same sizes. Cannot add.\n");
                        }
                    } catch (Exception e) {
                        // catch and notify a likely exception the file name entered is wrong and thus a file not found error.
                        System.out.println("Error: " + e.toString() + "\n\n");
                    }
                break;

                // command to multiply matrix together
                case 2:
                    try {
                        // ask for input of 2 matrix to be multiplied.
                        m1 = null;
                        m1 = loadAndInitMatrixFromFileSelection("Enter Matrix 1 File: ");
                        m2 = null;
                        m2 = loadAndInitMatrixFromFileSelection("Enter Matrix 2 File: ");

                        // ensure both matrix can be multiplied together by checking if matrix 1's columns are equal to matrix 2's rows.
                        if (m1.getCols() == m2.getRows()) {

                            // construct and initialise matrix 3 with correct rows and colums.
                            m3 = new Matrix(m1.getRows(), m2.getCols());

                            // loop through each row of matrix 1
                            for (i=0;i<m3.getRows();i++) {

                                // loop through each colum of matrix 2
                                for (j=0;j<m3.getCols();j++) {

                                    // use current row of matrix 1 and current column of matrix 2 for summation of multiplication
                                    m3.setElement(i, j, addMatrixRowCol(m1,m2,i,j));
                                }
                            }

                            // show results
                            System.out.println("\nMatrix 1");
                            System.out.println(m1.toString());

                            System.out.println("\nMatrix 2");
                            System.out.println(m2.toString());

                            System.out.println("\nThe Multiplication Result is");
                            System.out.println(m3.toString());
                        } else {
                            System.out.println("Matrix 1 Columns and Matrix 2 Rows are not equal. Cannot multiply.");
                        }
                    } catch (Exception e) {

                        // notify on exception that file entered for input is wrong.
                        System.out.println("Error: " + e.toString() + "\n\n");                    
                    }
                break;

                // transpose matrix command
                case 3:
                    try {

                        // ask for matrix to transpose
                        m1 = null;
                        m1 = loadAndInitMatrixFromFileSelection("Enter Matrix 1 File: ");

                        // construct and initialise result matrix for transpose
                        m3 = new Matrix(m1.getCols(), m1.getRows());

                        // transpose matrix by inverting row with column.
                        for (i=0;i<m1.getRows();i++) {
                            for (j=0;j<m1.getCols();j++) {
                                m3.setElement(j, i, m1.getElement(i, j));
                            }
                        }

                        // show results
                        System.out.println("\nMatrix 1");
                        System.out.println(m1.toString());

                        System.out.println("\nTranspose results is");
                        System.out.println(m3.toString());
                    } catch (Exception e) {
                        // catch and display error that the file input is not valid
                        System.out.println("Error: " + e.toString() + "\n\n");
                    }

                break;

                // show matrix selected
                case 4:
                    try {

                        // ask for matrix to show
                        System.out.print("Enter Matrix to display: ");
                        matrixdisplay = Integer.parseInt(getInput());

                        // show matrix
                        result = "\nm" + matrixdisplay + " is ";

                        switch (matrixdisplay) {
                            case 1:
                                System.out.println(result + m1.toString());
                            break;

                            case 2:
                                System.out.println(result + m2.toString());
                            break;

                            case 3:
                                System.out.println(result + m3.toString());
                            break;

                            default:
                                // ensure matrix selected is valid, m1, m2, m3 only.
                                System.out.println("\n\nNo such selection\n");
                            break;
                        }
                    } catch (Exception e) {
                        // catch wrong input and display error.
                        System.out.println("Error: Enter the correct value for matrix display.");
                    }
                break;

                // exit program
                case 5:
                    System.exit(0);
                break;

                // catch wrong value for main menu selection.
                default:
                    System.out.println("\n\nError: Please enter the correct value for main menu selection\n");
                break;
            }
        }
    }

    // grab input from the System IO and return a string of input.
    public static String getInput() {
        try {
            String result="";
            int ch;
            boolean flag = true;

            // grab everything and store in result buffer until a newline or tab character is entered
            while (flag) {
                ch = System.in.read();
                if ((ch == '\t') || (ch == '\n')) {
                    flag = false;
                } else {
                    result += (char) ch;
                }
            }
            return result.trim();
        } catch (IOException e) {

            // returns a null object on error. very unlikely to happen.
            return null;
        }
    }

    // helper function to prompt for file containing matrix data and return a new matrix object with initialised values.
    // throws error when file is not found to report exception
    public static Matrix loadAndInitMatrixFromFileSelection(String msg) throws java.io.FileNotFoundException {
        System.out.print(msg);
        Matrix m = new Matrix(new File(getInput()));
        return m;
    }

    // sums the product of specified row and column of matrix m1 and m2 and returns result.
    public static double addMatrixRowCol(Matrix m1, Matrix m2, int row1, int col2) {
        double sum = 0;      
        for (int i=0;i<m1.getCols();i++) {
            sum += m1.getElement(row1, i) * m2.getElement(i, col2);
        }
        return sum;
    }

    // prints main menu
    public static void MainMenu() {
        System.out.println("(1) Add two matrices");
        System.out.println("(2) Multiply two matrices");
        System.out.println("(3) Take transpose of a matrix");
        System.out.println("(4) Display a Matrix");
        System.out.println("(5) Exit");
    }

}
