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
public class Account2 extends Customer {

    public Account2() {
        super();
    }

    public Account2(int id, String name, String address, String dob, String phone, double bal) {
        super(id, name, address, dob, phone, bal);
    }
    
    public boolean update(double dailyInterest) {

        // returns the appropiate behavior if it has been updated witin the
        // same month.
        // no business logic as this is just a checking account

        if ((this.lastUpdated.MONTH == Calendar.getInstance().MONTH) && (this.lastUpdated != null)) {
            return false;
        } else {
            this.lastUpdated = Calendar.getInstance();
            return true;
        }
    }

    @Override
    public String toString() {
        String result = super.toString();

        result += "\nAccount Type = Checking";

        return result;
    }
}
