package lab7;
import java.util.*;
import java.text.*;
/**
 *
 * @author jeremy foo jie you
 * @workstation C199
 * @user hp086541
 * @class fs1
 * @lab 7
 * @course csc105
 */
public class Customer implements Cloneable {

    // customer class attributes
    private int accountID;
    private String name;
    private String address;
    private Calendar dob;
    private String phoneNumber;
    private double balance;

    // default constructor
    public Customer() {
        this.accountID = -1;
        this.name = "";
        this.address = "";
        this.dob = Calendar.getInstance();
        this.phoneNumber = "";
        this.balance = 0;
    }

    // constructor to be used for cloning only, hence private access
    private Customer(int accountID, String name, String address, Calendar dob,
                        String phoneNumber, double balance) {
        this.accountID = accountID;
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
    }

    // getter and setter methods
    public int getAccountID() { return accountID; }
    public void setAccountID(int accountID) { this.accountID = accountID; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public Calendar getDob() { return dob; }
    public void setDob(Calendar dob) { this.dob = dob; }

    // set date of birth using a string that fits the dd-mm-yyyy format
    public void setDob(String ddmmyyyy) throws java.text.ParseException {
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(ddmmyyyy);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        this.dob = cal;
    }

    // return the date of birth in the format specified
    public String getDobDTString(String format) {
        if (format.length() == 0) {
            format = "dd-MM-yyyy";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(this.dob.getTime());
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }

    // ensures that the phonenumber is of the correct format using regular
    // expressions
    public void setPhoneNumber(String phoneNumber) throws java.text.ParseException {
        String regex = "\\+?[\\d\\-]+";
        if (phoneNumber.matches(regex)) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new java.text.ParseException("Phone number should be of " +
                                                "the correct format.",0);
        }
    }

    // clone it
    public Customer clone() {
        return new Customer(this.accountID,this.name, this.address,
                                this.dob, this.phoneNumber, this.balance);
    }

    
}
