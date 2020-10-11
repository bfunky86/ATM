/**
 * Created by:      Brian Jackson
 */

package ATM;

import java.util.ArrayList;
import java.util.Random;

public class Bank {

    private String name;
    private ArrayList<User> user;
    private ArrayList<Account> accounts;

    /**
     * Create a new Bank object with empty lists of Users and Accounts
     * @param name  the name of the Bank
     */
    public Bank(String name){
        this.name = name;
        this.user = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();
    }

    /**
     * Generate a new universally unique ID for a user
     * @return uuid
     */
    public String getNewUserUUID() {
        String uuid;
        Random rng = new Random();
        int length = 6;
        boolean nonUnique;

        // Continue looping until we get a unique ID
        do {
            // Generate the number
            uuid = "";
            for (int c = 0; c < length; c++) {
                uuid += ((Integer) rng.nextInt(10)).toString();
            }
            // Check to make sure it is unique
            nonUnique = false;
            for (User user : this.user) {
                if (uuid.compareTo(user.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }

        } while (nonUnique);

        return uuid;

    }

    /**
     * Generate a new universally unique ID for an account
     *
     * @return uuid
     */
    public String getNewAccountUUID() {

        String uuid;
        Random rng = new Random();
        int length = 10;
        boolean nonUnique;

        // Continue looping until we get a unique ID
        do {
            // Generate the number
            uuid = "";
            for (int c = 0; c < length; c++) {
                uuid += ((Integer) rng.nextInt(10)).toString();
            }
            // Check to make sure it is unique
            nonUnique = false;
            for (Account a : this.accounts) {
                if (uuid.compareTo(a.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }

        } while (nonUnique);

        return uuid;
    }

    /**
     * Add an account
     *
     * @param account
     */
    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    /**
     *  Add a user
     * @param firstName
     * @param lastName
     * @param pin
     * @return
     */
    public User addUser(String firstName, String lastName, String pin){
        // Create new user object and add it to the list
        User newUser = new User(firstName, lastName, pin, this);
        this.user.add(newUser);

        // Create a savings account for the user
        Account newAccount = new Account("Savings", newUser, this);
        newUser.addAccount(newAccount);
        this.addAccount(newAccount);

        return newUser;
    }

    /**
     * Get the User object associated with a particular userID and pin, if they are valid
     * @param userID
     * @param pin
     * @return user
     */
    public User userLogin(String userID, String pin){

        // Search through the list of users
        for (User u:this.user) {

            // Check if userID is correct
            if(u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)){
                return u;
            }

        }

        // If we haven't found the user or the pin is incorrect
        return null;
    }

    /**
     * Return bank's name
     * @return bank name
     */
    public String getName() {
        return name;
    }
}
