/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lab4;
import java.util.*;

/**
 *
 * @author jeremy
 * @workstation C199
 * @user hp086541
 * @class fs1
 * @course csc105
 */
public class Account1 extends Customer {

    public Account1() {
        super();
        this.lastUpdated = null;
    }

    public Account1(int id, String name, String address, String dob, String phone, double bal) {
        super(id, name, address, dob, phone, bal);
        this.lastUpdated = null;
    }

    public boolean update(double dailyInterest) {

        // check to see if account has been updated for the current month
        // if it is, return a false statement to notify that the update was not
        // completed.
        // if not, update the account using a cumulative function.

        if ((this.lastUpdated.MONTH == Calendar.getInstance().MONTH) && (this.lastUpdated != null))  {
            return false;
        } else {
            this.lastUpdated = Calendar.getInstance();

            for (int i=0;i<30; i++) {
                this.balance += this.balance * dailyInterest;
            }
            
            return true;
        }
    }

    @Override
    public String toString() {
        String result = super.toString();

        result += "\nAccount Type = Saving";

        return result;
    }
}
