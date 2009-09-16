package lab5;
import java.util.*;
import java.text.*;
/**
 *
 * @author jeremy foo jie you
 * @workstation C199
 * @user hp086541
 * @class fs1
 * @lab 5
 * @course csc105
 */
public class Transaction {
    // attributes of transaction class
    private int accountID;
    private Calendar dateTime;
    private String type;
    private int location;
    private double amount;

    /*
     * Constructors
     * */

    public Transaction() {
        this.accountID = 0;
        this.dateTime = null;
        this.type = "";
        this.location = -1;
        this.amount = 0;
    }

    public Transaction(int acc, Calendar dt, String typ, double amt, int loc) {
        this.accountID = acc;
        this.dateTime = dt;
        this.type = typ;
        this.location = loc;
        this.amount = amt;
    }

    public Transaction(int acc, String ddmmyyyy, String hhmm, String typ,
            double amt, int loc) throws java.text.ParseException {

        this.setDateTime(ddmmyyyy, hhmm);
        this.accountID = acc;
        this.type = typ;
        this.location = loc;
        this.amount = amt;
        
    }

    // getter setter methods
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }


    public int getAccountID() { return accountID; }
    public void setAccountID(int accountID) { this.accountID = accountID; }

    public Calendar getDateTime() { return dateTime; }

    // returns human readable format for date time
    public String getDateTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
        return sdf.format(this.dateTime.getTime());
    }
    public void setDateTime(Calendar dateTime) { this.dateTime = dateTime; }

    // sets date and time based upon date and time of format ddmmyyyy and hhmm
    public void setDateTime(String ddmmyyyy, String hhmm) throws java.text.ParseException {
        Date date = new SimpleDateFormat("dd-MM-yyyy kkmm").parse(ddmmyyyy + " " + hhmm);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        this.dateTime = cal;
    }

    public int getLocation() { return location; }
    public void setLocation(int location) { this.location = location; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        String result = "";
        
        result += "Account ID = " + this.accountID;
        result += ", Date Time = " + this.getDateTimeString();
        result += ", Type = " + this.type;
        result += ", Amount = " + this.amount;
        result += ", Location = " + this.location;

        return result;
    }

}
