/**
 * Created by:      Brian Jackson
 */

package ATM;

import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {

        try( // init Scanner
             Scanner sc = new Scanner(System.in)) {

            // init Bank
            Bank aBank = new Bank("Bank Of Jackson");

            // add a User, which also creates a savings account
            User aUser = aBank.addUser("John", "Doe", "1234");

            // add a checking Account for the User
            Account newAccount = new Account("Checking", aUser, aBank);
            aUser.addAccount(newAccount);
            aBank.addAccount(newAccount);

            User currentUser;

            while (true) {
                // stay in the login prompt until successful login
                currentUser = ATM.mainMenuPrompt(aBank, sc);

                // stay in main menu until User quits
                ATM.printUserMenu(currentUser, sc);
            }
        } catch (Exception e){
            System.out.println("Unknown Error");
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * Display the UserID/Pin prompt
     *
     * @param bank
     * @param sc
     * @return
     */
    public static User mainMenuPrompt(Bank bank, Scanner sc) {

        // inits
        String userID;
        String pin;
        User authUser;

        do {

            // prompt the user for userID and pin until a correct one is reached
            System.out.printf("\n\nWelcome to %s\n\n", bank.getName());
            System.out.print("Enter User ID: ");
            userID = sc.nextLine();
            System.out.print("Enter PIN: ");
            pin = sc.nextLine();

            // try to get the user object corresponding to the userID and pin combo entered
            authUser = bank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Incorrect User ID/pin combination. " + "Please try again.");
            }

        } while (authUser == null); // continue looping until successful login

        return authUser;
    }

    /**
     * Display the ATM menu
     *
     * @param aUser
     * @param sc
     */
    public static void printUserMenu(User aUser, Scanner sc) {

        // print summary of user's account
        aUser.printAccountsSummary();

        // init
        int choice;

        // user menu
        do {
            System.out.printf("Welcome %s, what would you like to do?\n",
                    aUser.getFirstName());
            System.out.println("  1)  Show account transaction history");
            System.out.println("  2)  Withdraw");
            System.out.println("  3)  Deposit");
            System.out.println("  4)  Transfer");
            System.out.println("  5)  Quit");
            System.out.println();
            System.out.print("Enter a choice: ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice. Please choose 1-5");
            }
        } while (choice < 1 || choice > 5);

        // process the choice
        switch (choice) {

            case 1:
                ATM.showTransHistory(aUser, sc);
                break;
            case 2:
                ATM.withdrawFunds(aUser, sc);
                break;
            case 3:
                ATM.depositFunds(aUser, sc);
                break;
            case 4:
                ATM.transferFunds(aUser, sc);
                break;
            case 5:
                // gather up the rest of user input
                sc.nextLine();
                break;
        }

        // redisplay this menu unless the user wants to quit
        if (choice != 5) {
            printUserMenu(aUser, sc);
        }
    }

    /**
     * Show the transaction history of the account
     *
     * @param user the logged-in User object
     * @param sc   the Scanner object used for user input
     */
    public static void showTransHistory(User user, Scanner sc) {

        int theAccount;

        // get account transaction history
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "whose transactions you want to see: ", user.numAccounts());
            theAccount = sc.nextInt() - 1;
            if (theAccount < 0 || theAccount >= user.numAccounts()) {
                System.out.println("Invalid Account. Please try again.");
            }
        } while (theAccount < 0 || theAccount >= user.numAccounts());

        // print the transaction history
        user.printAcctTransHistory(theAccount);
    }

    /**
     * Process transferring funds from one account to another
     *
     * @param user the logged-in User object
     * @param sc   the Scanner object used for user input
     */
    public static void transferFunds(User user, Scanner sc) {

        // inits
        int fromAccount;
        int toAccount;
        double amount;
        double acctBalance;

        // get the Account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer from: ", user.numAccounts());
            fromAccount = sc.nextInt() - 1;
            if (fromAccount < 0 || fromAccount >= user.numAccounts()) {
                System.out.println("Invalid Account. Please try again.");
            }

        } while (fromAccount < 0 || fromAccount >= user.numAccounts());
        acctBalance = user.getAcctBalance(fromAccount);

        // get the Account to transfer to
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer to: ", user.numAccounts());
            toAccount = sc.nextInt() - 1;
            if (toAccount < 0 || toAccount >= user.numAccounts()) {
                System.out.println("Invalid Account. Please try again.");
            }
        } while (toAccount < 0 || toAccount >= user.numAccounts());

        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f): $",
                    acctBalance);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBalance) {
                System.out.printf("Amount must not be greater than\n" +
                        "balance of $.02f.\n", acctBalance);
            }
        } while (amount < 0 || amount > acctBalance);

        // finally, do the transfer
        user.addAcctTransaction(fromAccount, -1 * amount, String.format(
                "Transfer to account %s", user.getAcctUUID(toAccount)));
        user.addAcctTransaction(toAccount, amount, String.format(
                "Transfer to account %s", user.getAcctUUID(toAccount)));
    }

    /**
     * Process a fund withdraw from an Account
     *
     * @param user the logged-in User object
     * @param sc   the Scanner object used for user input
     */
    public static void withdrawFunds(User user, Scanner sc) {

        int fromAccount;
        double amount;
        double acctBalance;
        String memo;

        // get the Account to withdraw from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to withdraw from: ", user.numAccounts());
            fromAccount = sc.nextInt() - 1;
            if (fromAccount < 0 || fromAccount >= user.numAccounts()) {
                System.out.println("Invalid Account. Please try again.");
            }

        } while (fromAccount < 0 || fromAccount >= user.numAccounts());
        acctBalance = user.getAcctBalance(fromAccount);

        // get the amount to withdraw
        do {
            System.out.printf("Enter the amount to withdraw (max $%.02f): $",
                    acctBalance);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBalance) {
                System.out.printf("Amount must not be greater than\n" +
                        "balance of $.02f.\n", acctBalance);
            }
        } while (amount < 0 || amount > acctBalance);

        // get the rest of the user input
        sc.nextLine();

        // get the memo
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        // do the withdrawal
        user.addAcctTransaction(fromAccount, -1 * amount, memo);

    }

    /**
     * Process a fund deposit to an account
     *
     * @param user the logged-in User object
     * @param sc   the Scanner object used for user input
     */
    public static void depositFunds(User user, Scanner sc) {

        int toAccount;
        double amount;
        double acctBalance;
        String memo;

        // get the Account to deposit in
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to deposit in: ", user.numAccounts());
            toAccount = sc.nextInt() - 1;
            if (toAccount < 0 || toAccount >= user.numAccounts()) {
                System.out.println("Invalid Account. Please try again.");
            }

        } while (toAccount < 0 || toAccount >= user.numAccounts());
        acctBalance = user.getAcctBalance(toAccount);

        // get the amount to deposit
        do {
            System.out.printf("Enter the amount to deposit (max $%.02f): $",
                    acctBalance);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            }
        } while (amount < 0);

        // get the rest of the user input
        sc.nextLine();

        // get the memo
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        // do the deposit
        user.addAcctTransaction(toAccount, amount, memo);
    }
}
