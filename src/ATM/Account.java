/**
 * Created by:      Brian Jackson
 */

package ATM;

import java.util.ArrayList;

public class Account {

    private String name;
    private String uuid;
    private User holder;
    private ArrayList<Transactions> transactions;

    /**
     * Create an account with empty list of Transactions
     * @param name
     * @param holder
     * @param bankAccount
     */
    public Account(String name, User holder, Bank bankAccount){

        // set the account name and holder
        this.name = name;
        this.holder = holder;

        // get new account UUID
        this.uuid = bankAccount.getNewAccountUUID();
        this.transactions = new ArrayList<Transactions>();
    }

    /**
     * Get the account ID
     * @return
     */
    public String getUUID() {
        return this.uuid;
    }

    /**
     * Get summary line for the account
     * @return  the string summary
     */
    public String getSummaryLine(){

        // get the accounts balance
        double balance = this.getBalance();

        // format the summary line, depending on whether the balance is negative
        if(balance >= 0){
            return String.format("%s : $%.02f : %s", this.uuid, balance, this.name);
        } else {
            return String.format("%s : $(%.02f) : %s", this.uuid, balance, this.name);
        }
    }

    /**
     * Returns the balance of this account by adding the amounts of the transactions
     * @return  the balance value
     */
    public double getBalance(){

        double balance = 0;

        for(Transactions t: this.transactions){
            balance += t.getAmount();
        }
        return balance;
    }

    /**
     * Print the transaction history of this account
     */
    public void printTransHistory(){

        System.out.printf("\nTransaction history for account %s\n", this.uuid);
        for (int t = this.transactions.size() - 1; t >= 0; t--) {
            System.out.printf(this.transactions.get(t).getSummaryLine());
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Add a new transaction in this account
     * @param amount    the amount transacted
     * @param memo      the transaction memo
     */
    public void addTransaction(double amount, String memo){

        // create a new Transaction object and add it to the list
        Transactions newTrans = new Transactions(amount, memo, this);
        this.transactions.add(newTrans);
    }


}
