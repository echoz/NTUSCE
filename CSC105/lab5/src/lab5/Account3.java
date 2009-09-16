package lab5;
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
public class Account3 extends Customer {

    // fixed daily interest for this particular account
    private double fixedDailyInterest;

    /*
     * Constructors
     * */
    
    public Account3() {
        super();
        this.fixedDailyInterest = 0.0;
        this.type = "Fixed";
    }

    public Account3(int id, String name, String address, String dob, String phone, double bal, double fixedDailyInterest) {
        super(id, name, address, dob, phone, bal, "Fixed");
        this.fixedDailyInterest = fixedDailyInterest;
    }

    // abstract override
    
    public boolean update() {

        // checks if account has previously been updated. if it has, return
        // false to notify.
        // if not, add all fixeddailyinterests for the month to the balance.
        Transaction temp;
        boolean updated = false;
        String newdate;
        double credit;


        // ensure that the last updated month is not this month.
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

            // go through every day of the month matching all transactions related
            // to that day.
            for (int i=0;i<30;i++) {
                credit = 0;
                this.transactions.rewind();

                // look through all transactions matchin the current day in this
                // loop. store all the credit tranaction amounts and error out
                // those that are debit.
                while (this.transactions.hasNext()) {
                    temp = (Transaction) this.transactions.next().getItem();
                    if ((i+1 == temp.getDateTime().get(Calendar.DAY_OF_MONTH)) 
                            && (temp.getDateTime().get(Calendar.MONTH) == this.lastUpdated.get(Calendar.MONTH))
                            && (temp.getDateTime().get(Calendar.YEAR) == this.lastUpdated.get(Calendar.YEAR))) {
                        if (temp.getType().toUpperCase().equals("CREDIT")) {
                            credit += temp.getAmount();
                            updated = true;
                        } else if (temp.getType().toUpperCase().equals("DEBIT")) {
                            System.out.println("ERROR: Cannot apply transaction " +
                                    "to fixed deposit account. (" + temp.toString() + ")");
                        }
                    }                    
                }

                // apply fixed daily interest and add credit to account.
                this.balance += (this.balance * fixedDailyInterest) + credit;
            }
            
            return updated;
        }
    }

    // getter setter methods for fixed daily interest
    public void setFixedDailyInterest(double fdi) { this.fixedDailyInterest = fdi; }
    public double getFixedDailyInterest() { return this.fixedDailyInterest; }

    @Override
    public String toString() {
        String result = super.toString();
        result += "\nFixed Daily Interest = " + this.fixedDailyInterest;

        return result;
    }
}
