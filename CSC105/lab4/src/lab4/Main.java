package lab4;

/**
 *
 * @author jeremy
 * @workstation C199
 * @user hp086541
 * @class fs1
 * @course csc105
 * @lab 3
 */

import java.io.*;
import java.util.*;
import java.text.*;

public class Main {

    // records array to be preserved throughout the program using static type
    private static Customer[] records = null;
    private static DecimalFormat numberFormat = new DecimalFormat("################0.00");

    public static void main(String[] args) {
        /* initialise main() variables
         * - flag to control loop to display menu items and actions
         * - sc to be global scanner when parsing input
         * - size to store number of records read from readFile()
         * - command to store parsed command from input
         * - iTemp to store temporary integer variables
         * - dTemp to store temporary double variables
         */
        boolean flag = true;
        Scanner sc;
        int size = 0, i, iTemp;
        double dTemp;
        String command, result;

        while (flag) {

            // show menu and grab input command
            System.out.println(menu());
            sc = new Scanner(getInput());
            command = sc.next();

            // quit on Q or q.
            if (command.toUpperCase().equals("Q")) {
                flag = false;
            } else {
                try {
                    switch(Integer.parseInt(command)) {
                        case 1:
                            /* read accounts data from file and display records
                             * that have been read.
                             * will show error on FileNotFoundException for
                             * wrong filename input
                             */
                            System.out.print("Enter file to grab input: ");
                            try {
                                if ((size = readFile(new File(getInput()))) > 30) {
                                    System.out.println(size + " records read. Array has been resized to fit more than 30 records");
                                } else {
                                    System.out.println(size + " records read.");
                                }
                            } catch (FileNotFoundException e) {
                                System.out.println(e.toString());
                            }
                        break;

                        case 2:
                            // display records ensuring that if a empty record
                            // exists, it is not shown.
                            if (records != null) {
                                System.out.println(displayRecords(records));
                            } else {
                                System.out.println("No records.");
                            }
                        break;

                        case 3:
                            // write current records to file newCustomers.txt
                            try {
                                if ((size = writeFile(new File("newCustomers.txt"), records)) > 0) {
                                    System.out.println(size + " records written to file.");
                                }
                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }
                        break;

                        case 4:
                            try {
                                // grab account id to delete, hunt for it and
                                // delete using its position
                                System.out.print("Account ID to delete: ");
                                iTemp = Integer.parseInt(getInput());
                                if ((i = findRecord(iTemp, records)) > -1) {
                                    deleteRecord(i);
                                } else {
                                    System.out.println("No account with id " + iTemp + ".");
                                }
                                
                            } catch (Exception e) {
                                System.out.println("Please enter the correct value.");
                            }
                        break;

                        case 5:
                            try {

                                // does an update of all accounts within
                                // record set based upon daily interest
                                // specified.
                                // will iterate and notify if all records
                                // have been updated or not.

                                System.out.print("Amount of daily interest: ");
                                dTemp = Double.parseDouble(getInput());
                                result = "";

                                for (i=0;i<records.length;i++) {
                                    if (records[i].update(dTemp)) {
                                        result += "Name = " + records[i].getName() + "\nAccount Balance = " + numberFormat.format(records[i].getBalance()) + "\n\n";
                                    }
                                }

                                if (result.length() > 0) {
                                    System.out.println("\n\nThe updated balances are:\n");
                                    System.out.println(result);
                                } else {
                                    System.out.println("All records were updated for the month.");
                                }

                            } catch (Exception e) {
                                System.out.println("Please enter the correct value.");
                            }
                        break;
                    }
                } catch (NumberFormatException e) {
                    // catch erroroneous input.
                    System.out.println("Please enter the correct value for menu selection." + e.toString());
                }
            }
        }
        
    }

    // hunt for records in the haystack based upon id, return position.
    public static int findRecord(int id, Customer[] records) {
        for (int i=0;i<records.length;i++) {
            if (records[i].getAccountID() == id) {
                return i;
            }
        }
        return -1;
    }

    // delete and resizing method for records based upon position.
    public static void deleteRecord(int pos) {
        int i;
        Customer[] dest = new Customer[records.length-1];

        // copy src to destination
        for (i=0;i<pos;i++) {
            dest[i] = records[i];
        }

        // copy rest of data
        for (i=pos;i<dest.length;i++) {
            dest[i] = records[i+1];
        }
        records = dest;
    }

    public static int writeFile(File output, Customer[] records) throws java.io.IOException {
        /* initialise variables used
         * - written to count number of records written
         * - bw to provide interface to write to new file
         */
        int written=0;
        BufferedWriter bw = new BufferedWriter(new FileWriter(output));

        // grab each record and use bufferedwriter to dump toString value of
        // each customer object
        for (int i=0;i<records.length;i++) {
            if (records[i] != null) {
                bw.write(records[i].toString());
                bw.write("\n\n");
                written++;
            }
        }

        bw.close();

        return written;
    }

    public static int readFile(File phile) throws java.io.FileNotFoundException {

        /* initialise all variables used
         * -    sc initialised with delimter set as new line to grab every key/value
         *      pair from account data
         * -    pos to store position for insertion sort
         * -    added to count number of records added to records array
         * -    temp customer to store currently read customer data
         */
        Scanner sc = new Scanner(phile).useDelimiter("\n");
        int pos, added=0;
        Object temp;
        
        while (sc.hasNext()) {
            // pass sc to parseCustomerDetails to grab one account details from
            // file and store it in a temp customer class
            temp = parseCustomerDetails(sc);

            if (records == null) {
                // records will be null if its empty so initialise records with
                // space for one element and insert temp record into it.
                records = new Customer[1];
                records[0] = (Customer) temp;
                added++;
            } else {
                // if there are already records check to see if duplicate account IDs
                // exist. if it exsits, do not add and prompt for error.
                if (checkCustomerExist(((Customer) temp).getAccountID(), records)) {
                    System.out.println("Duplicate Account ID: " + ((Customer) temp).getAccountID());
                } else {

                    /* if not duplicate, hunt for insertion sort position.
                     * insertion sort position has 3 types of values, -1 means
                     * to insert the current customer object in front of the entire
                     * array. -2 means to add it to the back. the other values are
                     * positions that it should be inserted decremented by 1.
                     */
                    if ((pos = getInsertPosition((Customer) temp, records)) >= 0) {
                        // insert somewhere in the middle
                        records = cloneCustomerArrayAndInsert(records, (Customer) temp, pos + 1);
                    } else {
                        if (pos == -1) {
                            // insert in front of all records
                            records = cloneCustomerArrayAndInsert(records, (Customer) temp, 0);
                        } else if (pos == -2) {
                            // insert behind all records
                            records = cloneCustomerArrayAndInsert(records, (Customer) temp, records.length);
                        }
                    }
                    // increment added count
                    added++;
                }
                
            }
        }

        return added;
    }

    /* clones the entire customer array and inserts customer class passed
     * in position specified.
     */
    public static Customer[] cloneCustomerArrayAndInsert(Customer[] src, Customer inserting, int pos) {
        int i;
        // create destination array and increment size by 1 of the source
        Customer[] dest = new Customer[src.length+1];
        
        // copy src to destination
        for (i=0;i<pos;i++) {
            dest[i] = src[i];
            src[i] = null;
        }

        // insert in position
        dest[pos] = inserting;

        // copy rest of data
        for (i=pos;i<src.length;i++) {
            dest[i+1] = src[i];
            src[i] = null;
        }

        // return new customer array.
        return dest;
    }


    // build string of all records in array supplied and return it
    public static String displayRecords(Customer[] array) {
        String result = "";

        System.out.println(array.length);
        for (int i=0;i<array.length;i++) {
            if (array[i] != null) {
                /*
                result += "Name:\t\t" + array[i].getName();
                result += "\nBalance:\t" + array[i].getBalance();
                result += "\nAccount ID:\t" + array[i].getAccountID();
                result += "\nAddress:\t" + array[i].getAddress();
                result += "\nDate of Birth:\t" + array[i].getDOB();
                result += "\nPhone:\t\t" + array[i].getPhone() + "\n\n";
                */
                result += array[i].toString() + "\n\n";
            }
        }

        return result;
    }

    // loop through all elements of the array specified and compare its account ID
    // and the supplised account id.
    // return true if its found, false for everything else, even empty array.
    public static boolean checkCustomerExist(int id, Customer[] array) {
        for (int i=0;i<array.length;i++) {
            if (array[i] == null) {
                return false;
            } else if (array[i].getAccountID() == id) {
                return true;
            }
        }
        return false;
    }

    // look through all elements in supplied array and do a string lexicon compare
    // return position if supplied cust is a smaller string lexicon.
    public static int getInsertPosition(Customer cust, Customer[] records) {

        // checks to see if there is more than 1 record to make calculation for
        // middle insertion
        if (records.length > 1) {
            // hunt for lexicon comparison satisfaction, if found, return
            // position - 1 immediately.
            for (int i=0;i<records.length;i++) {
                if (cust.getName().compareTo(records[i].getName()) < 0) {
                    return i-1;
                }
            }
        } else {
            // if there is only 1 record, the insertion position can only either
            // be in the front or at the back, hence do a check and return
            // appropiate value
            if (cust.getName().compareTo(records[0].getName()) < 0) {
                return -1;
            } else {
                return 0;
            }
        }
        
        // if nothing is detected, means the insertion position is at the back
        // of the array, hence -2 to signify so.
        return -2;
    }

    // helper method to store all keyvalue pairs into a string array prior
    // to parsing to create appropiate account object.
    public static String[] addKeyvalueToArray(String keyvalue, String[] src) {

        String[] dest;
        
        if ((src.length == 1) && (src[0] == null)) {
            dest = src;
            dest[0] = keyvalue;
        } else {

            dest = dest = new String[src.length+1];

            for (int i=0;i<src.length;i++) {
                dest[i] = src[i];
            }

            dest[dest.length-1] = keyvalue;
        }

        return dest;
    }

    /* parses sc for values to read into customer class. will stop once
     * a double newline is discovered which signifies all data about that
     * customer has already been parsed.
     */
    public static Customer parseCustomerDetails(Scanner sc) {
        String keyvalue;
        String[] key = new String[1];
        String[] value = new String[1];
        int i;
        Scanner sd;
        Customer account = null;

        while (sc.hasNext()) {
            // grabs keyvalue pair from scanner
            keyvalue = sc.next().trim();

            if (keyvalue.length() > 0) {
                // split keyvalue pair using new scanner class based upon "="
                sd = new Scanner(keyvalue).useDelimiter("=");

                // extract key and value from keyvalue pair and store into
                // key and value arrays
                key = addKeyvalueToArray(sd.next().trim().toUpperCase(), key);
                value = addKeyvalueToArray(sd.next().trim(), value);

                // check if key is of the account type and instantiate a new
                // account object based upon account type.
                if (key[key.length-1].equals("ACCOUNT TYPE")) {
                    if (value[value.length-1].toUpperCase().equals("FIXED")) {
                        account = new Account3();
                    } else if (value[value.length-1].toUpperCase().equals("CHECKING")) {
                        account = new Account2();
                    } else if (value[value.length-1].toUpperCase().equals("SAVING")) {
                        account = new Account1();
                    }
                }

            } else {
                break;
            }

        }

        // parse through all key value arrays to push account information into
        // record object
        for (i=0;i<key.length;i++) {

            // checks keys and stores corresponding values in the correct
            // fields of the customer class.
            if (key[i].equals("ACCOUNT ID")) {
                account.setAccountID(Integer.parseInt(value[i]));
            }

            if (key[i].equals("NAME")) {
                try {
                    account.setName(value[i]);
                } catch (IndexOutOfBoundsException e) {
                    // truncate if over fixed length
                    account.setName(value[i].substring(0, 20));
                    System.out.println("Warning: Name length greater than 20, truncating...");
                }
            }

            if (key[i].equals("ADDRESS")) {
                try {
                    account.setAddress(value[i]);
                } catch (IndexOutOfBoundsException e) {
                    account.setAddress(value[i].substring(0, 80));
                    System.out.println("Warning: Address length greater than 80, truncating...");
                }
            }

            if (key[i].equals("DOB")) {
                try {
                    account.setDOB(value[i]);
                } catch (IndexOutOfBoundsException e) {
                    account.setDOB(value[i].substring(0, 10));
                    System.out.println("Warning: DOB length greater than 10, truncating...");
                }
            }

            if (key[i].equals("PHONE NUMBER")) {
                try {
                    account.setPhone(value[i]);
                } catch (IndexOutOfBoundsException e) {
                    account.setPhone(value[i].substring(0, 8));
                    System.out.println("Warning: Phone length greater than 8, truncating...");
                }
            }

            if (key[i].equals("ACCOUNT BALANCE")) {
                account.setBalance(Double.parseDouble(value[i]));
            }

            if (key[i].equals("FIXED DAILY INTEREST")) {
                ((Account3) account).setFixedDailyInterest(Double.parseDouble(value[i]));
            }
        }

        // return customer class if all else fails.
        return account;
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
    
    public static String menu() {
        String result = "";

        result += "(1) Input Data";
        result += "\n(2) Display Data";
        result += "\n(3) Output Data";
        result += "\n(4) Delete Record";
        result += "\n(5) Update records";
        result += "\n(Q) Quit";

        return result;
    }
}
