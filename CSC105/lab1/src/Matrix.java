/**
 *
 * @author jeremy foo jie you
 * @workstation C199
 * @user hp086541
 * @class fs1
 * @course csc105
 * @lab 1
 */

import java.io.*;
import java.util.*;

public class Matrix {
    // private propeties of MAtrix class
    private double[][] element;
    private int rows, cols;

    // default constructor to initalise an "empty" matrix
    public Matrix () {
        this.rows = 0;
        this.cols = 0;
    }

    // uses values from a Matrix file to construct class.
    // throws error to notify exception.
    public Matrix (File file) throws java.io.FileNotFoundException {

        // initalise variables
        int row, col, currentCol = 0;

        // start file for reading and push it into scanner class to parse input.
        FileReader phileReader = new FileReader(file);
        Scanner phileScanner = new Scanner(phileReader);

        // ensures its a valid matrix file with open <matrix> tag.
        
        if (phileScanner.nextLine().equals("<matrix>")) {

            // skip over parsed input of "rows" and "="
            phileScanner.next();
            phileScanner.next();

            // grab row data
            row = Integer.parseInt(phileScanner.next());

            // skip over parsed input of "cols" and "="
            phileScanner.next();
            phileScanner.next();

            // grab column data
            col = Integer.parseInt(phileScanner.next());

            // construct and initialise element storage based upon row 
            // and col data and set Matrix class propeties of row and cols
            // respectively.
            element = new double[row][col];
            this.rows = row;
            this.cols = col;

            // set row to 0 to begin parsing element data of Matrix from file.
            row = 0;

            // loop through subsequent parsed input
            // (doubles of matrix element data)
            while (phileScanner.hasNextDouble()) {

                // push parsed data into correct element based upon row and col
                element[row][currentCol] = phileScanner.nextDouble();
                currentCol++;
                // sets counters to correct row and colum.
                if (currentCol >= col) {
                    row++;
                    currentCol = 0;
                }
            }

        } else {

            // initliase matrix as "empty" on error that matrix file does not
            // have <matrix> tag therefore it is likely it is not a matrix file
            this.rows = 0;
            this.cols = 0;
        }

    }

    // construtor for Matrix class based upon rows and columns
    public Matrix (int row, int col) {
        element = new double[row][col];
        this.rows = row;
        this.cols = col;
        for (int i=0;i<this.cols;i++) {
            for (int j=0;j<this.rows;j++) {
                element[i][j] = 0.0;
            }
        }
    }

    // public method set individual element of row and col with specified value
    public void setElement (int row, int col, double value) {
        element[row][col] = value;
    }

    // public method to grab element and row and col
    public double getElement (int row, int col) {
        return element[row][col];
    }

    // publi method to get Matrix propeties of row/col
    public int getRows() { return this.rows; }
    public int getCols() { return this.cols; }

    // override default toString() method to display Matrix. Will say "Empty"
    // if matrix is empty. otherwise it will show Matrix data
    @Override
    public String toString() {
        String result = "";

        if ((this.rows == 0) || (this.cols == 0)) {
            result = "Empty Matrix";
        } else {
            // result += this.rows + " by " + this.cols + " Matrix:";

            for (int i=0;i<this.rows;i++) {
                result += "\n";
                for (int j=0;j<this.cols;j++) {
                    result += element[i][j] + "\t";
                }
            }
        }
        
        return result + "\n";
    }
}
