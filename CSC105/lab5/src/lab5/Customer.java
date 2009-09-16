package lab5;

/**
 *
 * @author jeremy foo jie you
 * @workstation C199
 * @user hp086541
 * @class fs1
 * @lab 5
 * @course csc105
 */

import java.util.*;
import java.text.*;

public abstract class Customer extends Throwable {
    // Declare private attributes of customer class
    // protected attibutes of customer class to allow inherited childs direct
    // access to attributes.
    private int accountID;
    private String name;
    private String address;
    private String dob;
    private String phone;
    protected List transactions;
    protected String type;
    protected double balance;
    protected Calendar lastUpdated;

    // Default constructor to indicate an empty customer class
    public Customer() {
        this.accountID = -1;
        this.name = "";
        this.address = "";
        this.dob = "";
        this.phone = "";
        this.balance = -1.0;
        this.lastUpdated = null;
        this.type = "";
        this.transactions = new List();
        this.lastUpdated = presetDate("MM-yyyy", "08-2008");
    }

    // constructor to initialise class with all values
    public Customer(int id, String name, String address, String dob, String phone,
            double bal, String type) {
        this.accountID = id;
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.phone = phone;
        this.balance = bal;
        this.lastUpdated = null;
        this.type = type;
        this.transactions = new List();
        this.lastUpdated = presetDate("MM-yyyy", "08-2008");
    }

    // preset date that matches the match string. returns presetted calendar.
    protected Calendar presetDate(String match, String data) {
        try {
            Date date = new SimpleDateFormat(match).parse(data);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (java.text.ParseException e) {
            return null;
        }
    }

    // pad number with zero if it is only 1 digit.
    protected String padNumber(int i) {
        if (i <= 9) {
            return "0" + i;
        } else {
            return String.valueOf(i);
        }
    }

    /* Setter methods for fields
     * For fields with a fixed length, the setter method will throw an exception
     * should the input be more than its specified length
     */
    public void setAccountID(int id) { this.accountID = id; }
    public void setName(String name) {
        if (name.length() > 20) {
            throw new java.lang.IndexOutOfBoundsException();
        } else {
            this.name = name.toUpperCase();
        }
    }
    public void setAddress(String address) {
        if (address.length() > 80) {
            throw new java.lang.IndexOutOfBoundsException();            
        } else {
            this.address = address;
        }
    }
    public void setDOB(String dob) {
        if (dob.length() > 10) {
            throw new java.lang.IndexOutOfBoundsException();
        } else {
            this.dob = dob;
        }
    }
    public void setPhone(String phone) {
        if (phone.length() > 8) {
            throw new java.lang.IndexOutOfBoundsException();
        } else {
            this.phone = phone;
        }
    }
    public void setBalance(double bal) { this.balance = bal; }

    // Getter methods for fields of the class
    public int getAccountID() { return this.accountID; }
    public String getName() { return this.name; }
    public String getAddress() { return this.address; }
    public String getDOB() { return this.dob; }
    public String getPhone() { return this.phone; }
    public double getBalance() { return this.balance; }
    public Calendar getLastUpdated() { return this.lastUpdated; }

    // return human readable last updated date time format.
    public String getLastUpdatedString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
        return sdf.format(this.lastUpdated.getTime());
    }
    public List getTransactions() { return this.transactions; }

    // print all transactions of this particular customer object.
    public String printTransactions() {
        Transaction trans;
        String result = "";
        
        transactions.rewind();
        while (transactions.hasNext()) {
            trans = (Transaction) transactions.next().getItem();
            result += "\ntrans " + trans.getAccountID() + " " +
                    trans.getDateTimeString() + " " + trans.getType() + " " +
                    trans.getAmount() + " " + trans.getLocation();
        }

        return result;

    }
    public abstract boolean update();
    
    // override toString to display all propeties of the class
    @Override
    public String toString() {
        String result = "";

        result += "Account Id = " + this.accountID;
        result += "\nName = " + this.name;
        result += "\nAddress = " + this.address;
        result += "\nDOB = " + this.dob;
        result += "\nPhone Number = " + this.phone;
        result += "\nAccount Balance = " + this.balance;
        result += "\nAccount Type = " + this.type;

        return result;
    }
}
