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
public class Account3 extends Customer {
    private double fixedDailyInterest;

    public Account3() {
        super();
        this.fixedDailyInterest = 0.0;
    }

    public Account3(int id, String name, String address, String dob, String phone, double bal, double fixedDailyInterest) {
        super(id, name, address, dob, phone, bal);
        this.fixedDailyInterest = fixedDailyInterest;
    }

    public boolean update(double dailyInterest) {

        // checks if account has previously been updated. if it has, return
        // false to notify.
        // if not, add all fixeddailyinterests for the month to the balance.

        if ((this.lastUpdated.MONTH == Calendar.getInstance().MONTH) && (this.lastUpdated != null)) {
            return false;
        } else {
            this.lastUpdated = Calendar.getInstance();

            this.balance += this.balance * fixedDailyInterest * 30;

            return true;
        }
    }

    // getter setter methods for fixed daily interest
    public void setFixedDailyInterest(double fdi) { this.fixedDailyInterest = fdi; }
    public double getFixedDailyInterest() { return this.fixedDailyInterest; }

    @Override
    public String toString() {
        String result = super.toString();

        result += "\nAccount Type = Fixed";
        result += "\nFixed Daily Interest = " + this.fixedDailyInterest;

        return result;
    }
}
