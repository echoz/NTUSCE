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
public class Account1 extends Customer {

    // Stores static accessible values for positive and negative balances intererests
    private static double POSITIVE_BALANCE = 0.0003;
    private static double NEGATIVE_BALANCE = -0.002;

    /*
     * Constructors
     * 
     * */
    public Account1() {
        super();
        this.type = "Saving";
    }

    public Account1(int id, String name, String address, String dob, String phone, double bal) {
        super(id, name, address, dob, phone, bal, "Saving");
    }

    // abstract override from super class.

    public boolean update() {

        // setup update variables
        Transaction temp;
        boolean updated = false;
        double debit=0, credit=0;
        String newdate;

        // if today's date is the same as the last updated date, obviously,
        // there should not be any update.
        if (this.lastUpdated.compareTo(Calendar.getInstance()) > 0)  {
            return updated;
        } else {

            // increments the last updated month by 1
            if ((this.lastUpdated.get(Calendar.MONTH)+2) > 12) {
                newdate = "01-" + Integer.toString(this.lastUpdated.get(Calendar.YEAR)+2);
            } else {
                newdate = padNumber(this.lastUpdated.get(Calendar.MONTH)+2) +
                                    "-" + this.lastUpdated.get(Calendar.YEAR);
            }
            this.lastUpdated = this.presetDate("MM-yyyy", newdate);

            // go through every day of the month assume every month has only 30
            // days. and check every transaction.
            for (int i=0;i<30;i++) {
                debit = 0;
                credit = 0;

                // reset enumerator like pointer
                this.transactions.rewind();

                // go through ever transaction and match the date with the
                // the day that is represented by i.
                // on a match, check if the transaction type is ether a debit
                // or a credit and accumulate those values accordingly to the
                // corresponding variables.
                while (this.transactions.hasNext()) {
                    temp = (Transaction) this.transactions.next().getItem();

                    if ((i+1 == temp.getDateTime().get(Calendar.DAY_OF_MONTH))
                            && (temp.getDateTime().get(Calendar.MONTH) == this.lastUpdated.get(Calendar.MONTH))
                            && (temp.getDateTime().get(Calendar.YEAR) == this.lastUpdated.get(Calendar.YEAR))) {
                        if (temp.getType().toUpperCase().equals("CREDIT")) {
                            credit += temp.getAmount();
                        } else if (temp.getType().toUpperCase().equals("DEBIT")) {
                            debit += temp.getAmount();
                        }
                    }
                }

                // if a debit transaction exists, reduce the amount of money in
                // the account first
                this.balance -= debit;

                // apply interests accordingly if the balance is positive
                // or negative.
                if (this.balance > 0) {
                    this.balance += this.balance * POSITIVE_BALANCE;
                } else {
                    this.balance -= this.balance * NEGATIVE_BALANCE;
                }

                // finally add the credit amount. this is done this way to ensure
                // that only money that is in the account for the past 24 hours
                // will be credited with the interest
                this.balance += credit;

            }
            updated = true;
        }
        return updated;

    }
}
