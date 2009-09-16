package lab2;

/**
 *
 * @author jeremy
 * @workstation C199
 * @user hp086541
 * @class fs1
 * @course csc105
 * @lab 2
 */

public class Customer extends Throwable {
    // Declare private attributes of customer class
    private int accountID;
    private String name;
    private String address;
    private String dob;
    private String phone;
    private double balance;

    // Default constructor to indicate an empty customer class
    public Customer() {
        accountID = -1;
        name = "";
        address = "";
        dob = "";
        phone = "";
        balance = -1.0;
    }

    // constructor to initialise class with all values
    public Customer(int id, String name, String address, String dob, String phone, double bal) {
        this.accountID = id;
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.phone = phone;
        this.balance = bal;
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
    public int getAccountID() { return accountID; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getDOB() { return dob; }
    public String getPhone() { return phone; }
    public double getBalance() { return balance; }

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

        return result;
    }
}
