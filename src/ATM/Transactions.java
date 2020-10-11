/**
 * Created by:      Brian Jackson
 */

package ATM;

import java.util.Date;

public class Transactions {

    private double amount;
    private Date timestamp;
    private String memo;
    private Account account;

    /**
     * Create a new transaction
     * @param amount
     * @param account
     */
    public Transactions(double amount, Account account){
        this.amount = amount;
        this.account = account;
        this.timestamp = new Date();
        this.memo = "";
    }

    /**
     * Create a new transaction
     * @param amount
     * @param memo
     * @param account
     */
    public Transactions(double amount, String memo, Account account){
        // Call the two-arg constructor first
        this(amount, account);

        // Set the memo
        this.memo = memo;

    }

    /**
     * Get the amount of the transaction
     * @return
     */
    public double getAmount(){
        return this.amount;
    }

    /**
     * Get a string summarizing the transaction
     * @return  the summary string
     */
    public String getSummaryLine() {

        if(this.amount >= 0){
            return String.format("%s : $%.02f : %s",
                    this.timestamp.toString(),
                    this.amount, this.memo);
        } else {
            return String.format("%s : $(%.02f) : %s",
                    this.timestamp.toString(),
                    this.amount, this.memo);
        }

    }
}
