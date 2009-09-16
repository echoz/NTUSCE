package lab6;
import java.util.*;
/**
 *
 * @author jeremy foo jie you
 * @workstation C199
 * @user hp086541
 * @class fs1
 * @lab 5
 * @course csc105
 */
public class Account2 extends Customer {

    /*
     * Constructors
     * */
    
    public Account2() {
        super();
        this.type = "Checking";
    }

    public Account2(int id, String name, String address, String dob, String phone, double bal) {
        super(id, name, address, dob, phone, bal, "Checking");
    }

    // abstract overriden.
    public boolean update() {

        // setup update variables
        Transaction temp;
        boolean updated = false;
        String newdate;

        // check if the current month is the same as the last updated month.
        // quit if it is true because this account has already been updated.
        if (this.lastUpdated.compareTo(Calendar.getInstance()) > 0)  {
            return updated;
        } else {

            // increment last updated month by 1
            if ((this.lastUpdated.get(Calendar.MONTH)+2) > 12) {
                newdate = "01-" + Integer.toString(this.lastUpdated.get(Calendar.YEAR)+2);
            } else {
                newdate = padNumber(this.lastUpdated.get(Calendar.MONTH)+2) +
                                    "-" + this.lastUpdated.get(Calendar.YEAR);
            }
            this.lastUpdated = this.presetDate("MM-yyyy", newdate);

            // reset psuedo enumerator pointer
            this.transactions.rewind();

            // go through all items in this accounts transaction and match those
            // that are the same as the current month and year. if they are the
            // same credit the credit amount and debit the debit amount only
            // and only if the debit amount doens't bring the account to below
            // 0. if it does, print an error saying the tranaction will not
            // work.
            while (this.transactions.hasNext()) {
                temp = (Transaction) this.transactions.next().getItem();
                
                if ((temp.getDateTime().get(Calendar.MONTH) == this.lastUpdated.get(Calendar.MONTH))
                        && (temp.getDateTime().get(Calendar.YEAR) == this.lastUpdated.get(Calendar.YEAR))) {
                    
                    if (temp.getType().toUpperCase().equals("DEBIT")) {
                        if ((this.balance - temp.getAmount()) > 0) {
                            this.balance -= temp.getAmount();
                            updated = true;
                        } else {
                            System.out.println("ERROR: Cannot apply transaction " +
                                    "to checking account. (" + temp.toString() + ")");
                        }
                    } else if (temp.getType().toUpperCase().equals("CREDIT")) {
                        this.balance += temp.getAmount();
                        updated = true;
                    }
                }
            }
            
            return updated;
        }
    }
}
