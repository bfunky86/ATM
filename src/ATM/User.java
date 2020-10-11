/**
 * Created by:      Brian Jackson
 */

package ATM;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {

    private String firstName;
    private String lastName;
    private String uuid;
    private byte pinHash[];
    private ArrayList<Account> accounts;

    /**
     * Create a new User object using a Message Digest object to validate pinHash
     * @param firstName
     * @param lastName
     * @param pin
     * @param bankAccount
     */
    public User(String firstName, String lastName, String pin, Bank bankAccount) {

        // set the User's name
        this.firstName = firstName;
        this.lastName = lastName;
        try {
            // store the pin's MD5 hash, for security reasons
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error caught: No Such Algorithm Exception");
            e.printStackTrace();
            System.exit(1);
        }
        this.uuid = bankAccount.getNewUserUUID();
        this.accounts = new ArrayList<Account>();

        System.out.printf("New user %s, %s with ID %s created.\n", lastName, firstName, this.uuid);

    }

    /**
     *  Add an account
     * @param account
     */
    public void addAccount(Account account){
        this.accounts.add(account);
    }

    /**
     * Return the users ID
     * @return  the uuid
     */
    public String getUUID() {
        return this.uuid;
    }

    /**
     * Check whether the given pin matches the true users pin
     * @param pin
     * @return
     */
    public boolean validatePin(String pin){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(pin.getBytes()),this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error caught: No Such Algorithm Exception");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    /**
     * Return the user's first name
     * @return
     */
    public String getFirstName(){
        return this.firstName;
    }

    /**
     * Print the account summary for this user
     */
    public void printAccountsSummary(){
        System.out.printf("\n\n%s's account summary:\n", this.firstName);
        for(int i = 0; i < accounts.size(); i++){
            System.out.printf("%d) %s\n", i+1,
                    this.accounts.get(i).getSummaryLine());
        }
        System.out.println();
    }

    /**
     * Get the number of accounts of the user
     * @return the number of accounts
     */
    public int numAccounts(){
        return this.accounts.size();
    }

    /**
     * Print the transaction history of a particular account
     * @param accountIndex  the index of the account to use
     */
    public void printAcctTransHistory(int accountIndex){
        this.accounts.get(accountIndex).printTransHistory();
    }

    /**
     * Get the account balance of a particular Account
     * @param accountIndex  the index of the Account to use
     * @return  the balance of the account
     */
    public double getAcctBalance(int accountIndex){
        return this.accounts.get(accountIndex).getBalance();
    }

    /**
     * Get the UUID of a particular Account
     * @param accountIndex  the index of the Account to use
     * @return  the UUID of the Account
     */
    public String getAcctUUID(int accountIndex){
        return this.accounts.get(accountIndex).getUUID();
    }

    /**
     * Add a transaction to a particular Account
     * @param accountIndex  the index of the Account
     * @param amount    the amount of the transaction
     * @param memo      the memo of the transaction
     */
    public void addAcctTransaction(int accountIndex, double amount, String memo) {
        this.accounts.get(accountIndex).addTransaction(amount, memo);
    }
}
