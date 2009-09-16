package lab6;

/**
 *
 * @author jeremy foo jie you
 * @workstation C199
 * @user hp086541
 * @class fs1
 * @course csc105
 * @lab 6
 */

import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;

public class Main {

    // records array to be preserved throughout the program using static type
    private static List customers = new List();

    private static List transactions = new List();

    protected static final long MILLISECS_PER_DAY = 24L * 60L * 60L * 1000L;
    
    // default decimal format for doubles
    private static DecimalFormat numberFormat = new DecimalFormat("############0.00");

    // set static current date.
    private static Calendar CurrentDate = Calendar.getInstance();

    public static void main(String[] args) {
        /* initialise main() variables
         * - flag to control loop to display menu items and actions
         * - sc to be global scanner when parsing input
         * - size to store number of records read from readFile()
         * - command to store parsed command from input
         * - iTemp to store temporary integer variables
         */
        boolean flag = true;
        Scanner sc;
        int size = 0, i, iTemp;
        String command;

        // set current date to the one that is presented in the scenario.
        try {
            CurrentDate.setTime(new SimpleDateFormat("MM-yyyy").parse("09-2008"));
        } catch(Exception e) {
            System.out.println("Error setting current date. Defaulting to current" +
                    " date time.");
        }
        
        while (flag) {
            try {
                // show menu and grab input command
                System.out.println(menu());
                sc = new Scanner(getInput());
                command = sc.next().trim();

                // quit on Q or q.
                if (command.toUpperCase().equals("Q")) {
                    System.exit(0);
                } else {

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
                                    System.out.println(size + " records read. " +
                                            "Array has been resized to fit more " +
                                            "than 30 records");
                                } else {
                                    System.out.println(size + " records read.");
                                }
                            } catch (java.util.NoSuchElementException nseex) {
                                System.out.println("Wrong format");
                            } catch (FileNotFoundException fnfe) {
                                System.out.println("File does not exist. " +
                                        fnfe.toString());
                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }
                        break;

                        case 2:
                            // display records ensuring that if a empty record
                            // exists, it is not shown.
                            if (customers.size() > 0) {
                                System.out.println(displayRecords(customers));
                            } else {
                                System.out.println("No records.");
                            }
                        break;

                        case 3:
                            // write current records to file newCustomers.txt
                            try {
                                if ((size = writeFile(new File("newCustomers.txt"), customers)) > 0) {
                                    System.out.println(size + " records written to file.");
                                }
                            } catch (java.util.NoSuchElementException nseex) {
                                System.out.println("Wrong format");
                            } catch (IOException e) {
                                System.out.println(e.toString());
                            }
                        break;

                        case 4:
                            try {
                                // grab account id to delete, hunt for it and
                                // delete using its position
                                System.out.print("Account ID to delete: ");
                                iTemp = Integer.parseInt(getInput());
                                if ((i = findRecord(iTemp, customers)) > -1) {
                                    customers.remove(i);
                                } else {
                                    System.out.println("No account with id " + iTemp + ".");
                                }

                            } catch (Exception e) {
                                System.out.println("Please enter the correct value.");
                            }
                        break;

                        case 5:
                            // computes the balance of each customer record
                            compute_balance();
                        break;

                        case 6:
                            // parsed and associate transactions with customer
                            // records.
                            System.out.print("Enter file to grab transactions: ");
                            try {
                                size = insert_trans(new File(getInput()));
                                System.out.println(size + " records read.");
                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }
                        break;

                        case 7:
                            System.out.print(mean(customers) + "\n\n");
                        break;

                        case 8:
                            try {
                                // grab account id to delete, hunt for it and
                                // delete using its position
                                System.out.print("Account ID to search: ");
                                iTemp = Integer.parseInt(getInput());
                                if (findRecord(iTemp, customers) > -1) {
                                    System.out.println(partners(customers, iTemp));
                                } else {
                                    System.out.println("No account with id " + iTemp + ".");
                                }
                            } catch (Exception e) {
                                System.out.println("Please enter the correct value." + e.toString());
                            }

                            break;

                        default:
                            // any other option selected will get them here.
                            System.out.println("I don't think there's such an option.");
                        break;
                    }

                }
            } catch (Exception e) {
                // catch erroroneous input.
                System.out.println("Please enter the correct value for menu selection. " + e.toString());
            }
        }

    }

    /* original work done at lab based upon assumption
    public static String partners(List transactions, int id) {

        Transaction temp, a = null;
        long diff;
        String result = "";

        transactions.rewind();
        while (transactions.hasNext()) {
            temp = (Transaction) transactions.next().getItem();
            if ((temp.getAccountID() == id) && (temp.getType().toUpperCase().equals("DEBIT"))) {
                a = temp;
            } else {
                if (a != null) {
                    if ((a.getLocation() == temp.getLocation())
                        && (dateDiff(a.getDateTime(), temp.getDateTime()) < 3)
                        && (temp.getType().toUpperCase().equals("DEBIT"))) {
     
                        result += temp.toString() + "\n";
                    }
                }
            }
        }
        
        return result;
    }
     * */

    public static String partners(List customers, int id) {

        // setup target customer class and initialise list of
        // target dates.
        Customer target = (Customer) customers.getItem(findRecord(id,customers));
        List targetDates = new List();

        // setup variables
        String result = "", custresult = "";
        Customer cust;
        Transaction temp, compare;
        int i=0;

        // setup consecutive 3 days storage
        Transaction[] threedays = new Transaction[3];

        // formatting
        System.out.println("\n");

        // go through all transactions in target customer and extract groups 3
        // consecutive days and store it within the targetdates array.
        for (i=0;i<target.transactions.size();i++) {
            temp = (Transaction) target.transactions.getItem(i);

            // set time data to nothing to have better detection.
            temp.getDateTime().clear(Calendar.MINUTE);
            temp.getDateTime().clear(Calendar.HOUR);
            temp.getDateTime().clear(Calendar.HOUR_OF_DAY);
            temp.getDateTime().clear(Calendar.MILLISECOND);

            // check if tranactions pulled out is of debit type and not at home city
            if ((temp.getType().toUpperCase().equals("DEBIT")) && (temp.getLocation() != 1)) {
                if (threedays[0] == null) {
                    threedays[0] = temp;
                    
                } else {
                    // since transactions are stored chronologically, grab dates
                    // that are within 3 days and of the same location
                    // store them in an associative array. keep doing this until
                    // next read date is greater than 3 which means its a possible
                    // new group
                    if ((dateDiff(threedays[0].getDateTime(), temp.getDateTime()) < 3)
                            && (threedays[0].getLocation() == temp.getLocation())) {
                        threedays[(int) dateDiff(threedays[0].getDateTime(), temp.getDateTime())] = temp;
                    } else {

                        // check if 3 days stored are consecutive. if they are,
                        // write to targetdates. if not prepare new group for
                        // storage
                        if ((threedays[0] != null) && (threedays[1] != null) && (threedays[2] != null)) {
                            targetDates.add(threedays[0]);
                            targetDates.add(threedays[1]);
                            targetDates.add(threedays[2]);
                        } 
                        threedays[0] = temp;
                        threedays[1] = null;
                        threedays[2] = null;

                    }
                }
            }
        }

        // check if last dates gotten are of a group of 3 consecutive and
        // store them.
        if ((threedays[0] != null) && (threedays[1] != null) && (threedays[2] != null)) {
            targetDates.add(threedays[0]);
            targetDates.add(threedays[1]);
            targetDates.add(threedays[2]);
        }


        // time to look through and find similar customers!
        customers.rewind();

        // iterate through each customer's tranactions and compare. if there
        // is a match, push it to the results string to prepare for output.
        while (customers.hasNext()) {
            cust = (Customer) customers.next().getItem();

            // ensure customer is not target
            if (cust.getAccountID() != target.getAccountID()) {
                custresult = "";

                // iterate through target dates.
                targetDates.rewind();

                while (targetDates.hasNext()) {
                    // prepare 3 date mask
                    threedays[0] = (Transaction) targetDates.next().getItem();
                    threedays[1] = (Transaction) targetDates.next().getItem();
                    threedays[2] = (Transaction) targetDates.next().getItem();

                    // go through all transactions within current customer.
                    cust.transactions.rewind();

                    while (cust.transactions.hasNext()) {
                        compare = (Transaction) cust.transactions.next().getItem();

                        // check if location is similiar and date mask parses,
                        // correctly.
                        if ((threedays[0].getLocation() == compare.getLocation())
                                && (hasDate(threedays, compare))) {

                            // store transaction data in result string.
                            custresult += compare.toString() + "\n";
                        }
                    }
                }

                // if results string for current customer is not empty, obviously
                // a possible touring partner has been found, so store meta data
                // in final result string.
                if (custresult.length() > 0) {
                    result += "Possible Touring partner: " + cust.getName() + "\n";
                    result += "Related Transactions: \n" + custresult + "\n\n";
                    custresult = "";
                }
            }
        }

        return result;
    }

    // date matching using a mask of dates and a target.
    private static boolean hasDate(Transaction[] mask, Transaction target) {
        for (int i=0; i< mask.length; i++) {
            if ((mask[i].getDateTime().get(Calendar.DAY_OF_MONTH) == target.getDateTime().get(Calendar.DAY_OF_MONTH))
                                && (mask[i].getDateTime().get(Calendar.MONTH) == target.getDateTime().get(Calendar.MONTH))
                                && (mask[i].getDateTime().get(Calendar.YEAR) == target.getDateTime().get(Calendar.YEAR))) {
                                return true;
            }
        }

        return false;
    }

    // calculation of difference between dates using UNIX epoch elapsed msecs.
    private static long dateDiff(Calendar start, Calendar end) {
        long endL = end.getTimeInMillis() + end.getTimeZone().getOffset( end.getTimeInMillis() );
        long startL = start.getTimeInMillis() + start.getTimeZone().getOffset( start.getTimeInMillis() );
        return (endL - startL) / MILLISECS_PER_DAY;
    }

    /* depreciated totally
    private static Calendar checkConsectDatesExist(Customer a) {

        a.transactions.rewind();

        Calendar result = ((Transaction) a.transactions.next().getItem()).getDateTime();
        Calendar temp;
        long diff;
        boolean consec[] = new boolean[3];

        while (a.transactions.hasNext()) {
            temp = ((Transaction) a.transactions.next().getItem()).getDateTime();
            diff = result.getTime().getTime() - temp.getTime().getTime();
            if ((diff / (1000 * 60 * 60 * 24)) > 2) {
                if ((consec[0] == false) || (consec[1] == false) || (consec[2] == false)) {
                    result = temp;
                } else {
                    return result;
                }
            } else  {
                consec[((int)diff / (1000 * 60 * 60 * 24))] = true;
            }
        }

        return null;
    }
     * */

    public static String mean(List customers) {
        // setup variables!
        String result = "";
        double mean = 0;
        Customer[] nearestBal = new Customer[2];
        Customer temp;

        // ensures if there are actually customer records to compute
        if (customers.size() > 0) {

            // go through all records to compute mean of balance
            customers.rewind();

            while (customers.hasNext()) {
                mean += ((Customer) customers.next().getItem()).getBalance();
            }

            mean = mean / customers.size();

            // check if there really is a need to go through the customers
            // records to find the nearest. if there is (meaning there are more
            // than 2 customer records, do it!
            if (customers.size() > 2) {

                customers.rewind();
                //  set initial values. doesn't matter if its big or small, because
                // we are finding the smallest.
                nearestBal[0] = (Customer) customers.next().getItem();
                nearestBal[1] = (Customer) customers.next().getItem();

                // now go through the reamining customer records and check
                // if they are smaller in size compared to thsoe 2 that are,
                // already there.
                while (customers.hasNext()) {
                    temp = (Customer) customers.next().getItem();

                    // check and store appropiately.
                    if (Math.abs(temp.getBalance() - mean) < Math.abs(nearestBal[0].getBalance() - mean)) {
                        nearestBal[0] = temp;
                    } else if (Math.abs(temp.getBalance() - mean) < Math.abs(nearestBal[1].getBalance() - mean)) {
                        nearestBal[1] = temp;
                    }
                }

                // dump to result string
                result += nearestBal[0].toString() + "\n\n";
                result += nearestBal[1].toString();
            } else {

                // this will work for exactly 2 or 1 customers so just dump
                // to results string accordingly.
                customers.rewind();
                while (customers.hasNext()) {
                    temp = (Customer) customers.next().getItem();
                    result += temp.toString() + "\n";
                }
            }
        } else {
            result = "No Customer Records!";
        }

        // add result headers! 
        result = "\n\nAccounts with nearest balance: " + result + "\n\n";
        result = "\nMean of all accounts = " + mean + result + "\n\n";

        return result;
    }

    public static void compute_balance() {

        // goes through each customer record and apply transaction and interest
        boolean result = false;
        Customer tempcust;

        // reset pseudo enumerator pointer
        customers.rewind();

        // goes through each customer record and check if it has been last updated
        // in this month. if it hasn't, update it and print its balance.
        while (customers.hasNext()) {
            tempcust = (Customer) customers.next().getItem();

            if (tempcust.lastUpdated.compareTo(CurrentDate) < 0) {
                System.out.println("Account ID = " + tempcust.getAccountID());
                if (tempcust.update())
                    result = true;
                System.out.println("Balance = " +
                        numberFormat.format(tempcust.getBalance()) + "\n");
            }
        }

        // if no balances were updated, prompt user.
        if (!result) {
            System.out.println("No balances were updated.");
        }
    }

    // hunt for records in the haystack based upon id, return position.
    public static int findRecord(int id, List records) {
        Customer temp;
        int i=0;
        
        records.rewind();

        while (records.hasNext()) {
            temp = (Customer)records.next().getItem();
            if (temp.getAccountID() == id) {
                return i;
            }
            i++;
        }

        return -1;
    }

    public static int writeFile(File output, List records) throws java.io.IOException {
        /* initialise variables used
         * - written to count number of records written
         * - bw to provide interface to write to new file
         */
        int written=0;
        BufferedWriter bw = new BufferedWriter(new FileWriter(output));

        // grab each record and use bufferedwriter to dump toString value of
        // each customer object
        records.rewind();

        while (records.hasNext()) {
            bw.write(records.next().getItem().toString());
            bw.write("\n\n");
            written++;
        }

        bw.close();

        return written;
    }

    public static int readFile(File phile) throws Exception {

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


            if (temp != null) {
                // if there are already records check to see if duplicate account IDs
                // exist. if it exsits, do not add and prompt for error.
                if (checkCustomerExist(((Customer) temp).getAccountID(), customers)) {
                    System.out.println("Duplicate Account ID: " +
                            ((Customer) temp).getAccountID());
                } else {

                    // get position, insert into position.
                    pos = getInsertPosition((Customer) temp, customers);
                    customers = insertHelper(customers, temp, pos);

                    // increment added count
                    added++;
                }
            }
        }

        return added;
    }

    public static int insert_trans(File phile) throws Exception {
        /* setup variables
         * - sc scanner class with new line set as its delimiter
         * - pos for index
         * - added to count the number of records added
         * - temp for temporary object storage
         * - transcations for temporary transactions storage in a list
         * - cust for temporary storage of customer
         * - trans for temporary storage of transaction
         * */

        Scanner sc = new Scanner(phile).useDelimiter("\n");
        int pos, added=0;
        Object temp;
        Customer cust;
        Transaction trans;

        // skip header and emptyline
        sc.next();
        sc.next();

        // look through each line and parse it using parseTransactionDetails method
        while (sc.hasNext()) {
            temp = parseTransactionDetails(sc);

            // if details is not null, means it exists, then check if the associated
            // account id exists. if it exists, put it into the transcations list
            // using an insertion sort so it can be chronological
            if (temp != null) {
                if (checkCustomerExist(((Transaction) temp).getAccountID(),customers)) {
                    if (!checkTransactionExist(transactions, (Transaction) temp)) {
                        pos = getInsertPosition((Transaction) temp, transactions);
                        transactions = insertHelper(transactions, temp, pos);

                        // increment added count
                        added++;
                    }
                } else {
                    System.out.println("Error: cannot find account number for " +
                            "transaction \"" + temp.toString() + "\".");
                }
            }
        }

        // reset customer pointer
        customers.rewind();

        // go through each customer and associated the respective transcations
        // with the correct customer object.
        while (customers.hasNext()) {
            cust = (Customer) customers.next().getItem();

            transactions.rewind();
            while (transactions.hasNext()) {
                trans = (Transaction) transactions.next().getItem();
                if ((trans.getAccountID() == cust.getAccountID()) && (!checkTransactionExist(cust.transactions, trans))) {
                    cust.getTransactions().add(trans);
                }
            }
        }

        return added;
    }

    private static boolean checkTransactionExist(List transaction, Transaction compare) {
        transaction.rewind();

        while (transaction.hasNext()) {
            if (compare.equalsTo((Transaction) transaction.next().getItem())) {
                return true;
            }
        }
        return false;
    }

    // this mehtod parse teh customer details and returns a transcation object
    // with the attributes all filled up.
    public static Transaction parseTransactionDetails(Scanner sc) {

        // setup variables
        String transaction;
        Scanner sd;
        Transaction trans = new Transaction();

        while (sc.hasNext()) {
            // grab one line from the transcation file. one line here is one
            // record
            transaction = sc.next().trim();

            try {
                // use another scanner class to parse through each field,
                // storing it appropiately. then return the transcation object.
                sd = new Scanner(transaction);

                trans.setAccountID(sd.nextInt());
                trans.setDateTime(sd.next().trim(), sd.next().trim());
                trans.setType(sd.next().trim());
                trans.setAmount(sd.nextDouble());
                trans.setLocation(sd.nextInt());
            } catch (Exception e) {
                return null;
            }
            break;
        }

        return trans;
    }

    // this insert helper just allows casscading.
    public static List insertHelper(List records, Object temp, int pos) {
        records.insert(temp, pos);
        return records;
    }
    
    // build string of all records in array supplied and return it
    public static String displayRecords(List records) {
        String result = "";
        Customer cust;

        records.rewind();

        while (records.hasNext()) {
            cust = (Customer) records.next().getItem();
            
            result += cust.toString();
            result += cust.printTransactions();
            
            result += "\n\n";
        }
                
        return result;
    }

    // loop through all elements of the array specified and compare its account ID
    // and the supplised account id.
    // return true if its found, false for everything else, even empty array.
    public static boolean checkCustomerExist(int id, List records) {
        if (records.size() == 0) {
            return false;
        } else {
            records.rewind();
            while (records.hasNext()) {
                if (((Customer)records.next().getItem()).getAccountID() == id) {
                    return true;
                }
            }
            return false;
        }
    }

    // look through all elements in supplied array and do a string lexicon compare
    // return position if supplied cust is a smaller string lexicon.
    public static int getInsertPosition(Customer cust, List records) {

        int i=0;
        
        // checks to see if there is more than 1 record to make calculation for
        // middle insertion
        records.rewind();
        
        if (records.size() > 1) {
            // hunt for lexicon comparison satisfaction, if found, return
            // position - 1 immediately.
            while (records.hasNext()) {
                if (cust.getName().compareTo(((Customer)records.next().getItem()).getName()) < 0) {
                    return i;
                }
                i++;
            }

        } else if (records.size() == 1) {

            // if there is only 1 record, the insertion position can only either
            // be in the front or at the back, hence do a check and return
            // appropiate value
            if (cust.getName().compareTo(((Customer)records.next().getItem()).getName()) < 0) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
        
        // if nothing is detected, means the insertion position is at the back
        // of the array, 
        return records.size();
    }

    // look through all elements in supplied array and do a string lexicon compare
    // return position if supplied transaction chronologically smaller.
    public static int getInsertPosition(Transaction trans, List records) {

        int i=0;

        // checks to see if there is more than 1 record to make calculation for
        // middle insertion
        records.rewind();

        if (records.size() > 1) {
            // hunt for chronological comparison satisfaction, if found, return
            // position - 1 immediately.
            while (records.hasNext()) {
                if (trans.getDateTime().compareTo(((Transaction)records.next().getItem()).getDateTime()) < 0) {
                    return i;
                }
                i++;
            }

        } else if (records.size() == 1){

            // if there is only 1 record, the insertion position can only either
            // be in the front or at the back, hence do a check and return
            // appropiate value
            if (trans.getDateTime().compareTo(((Transaction)records.next().getItem()).getDateTime()) < 0) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 0;
        }

        // if nothing is detected, means the insertion position is at the back
        // of the array, hence -2 to signify so.
        return records.size();
    }

    // helper method to store all keyvalue pairs into a string array prior
    // to parsing to create appropiate account object.
    public static String[] addKeyvalueToArray(String keyvalue, String[] src) {

        String[] dest;
        
        if ((src.length == 1) && (src[0] == null)) {
            dest = src;
            dest[0] = keyvalue;
        } else {

            // resize the array to allow for a variable sized array.
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
        result += "\n(6) Insert Transactions";
        result += "\n(7) Balance Mean";
        result += "\n(8) Touring Partners";
        result += "\n(Q) Quit";

        return result;
    }
}
