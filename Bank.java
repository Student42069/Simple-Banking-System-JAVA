package banking;

import java.util.Scanner;

import static java.lang.System.exit;

public class Bank {
    AccountConnection currentLoggedIn;

    Scanner sc = new Scanner(System.in);

    public Bank(String arg) {
        SQLConnection.connectSQLite(arg);
    }

    public void start() {
        while(true){
            if(currentLoggedIn != null) {
                accountMenu();
            } else {
                mainMenu();
            }
        }
    }

    private void accountMenu() {
        int accountMenuChoice = accountMenuChoice();
        switch (accountMenuChoice){
            case 0:
                System.out.println("Bye!");
                exit(0);
                break;
            case 1:
                showBalance();
                break;
            case 2:
                addIncome();
                break;
            case 3:
                transfer();
                break;
            case 4:
                closeAccount();
                break;
            case 5:
                logout();
                break;
        }
    }

    private void closeAccount() {
        this.currentLoggedIn.closeAccount();
        System.out.println("\nThe account has been closed!\n");
        this.currentLoggedIn = null;
    }

    private void transfer() {
        try {
            System.out.println("\nTransfer\nEnter card number:");
            long to = sc.nextLong();
            this.currentLoggedIn.checkTransferTo(to);

            System.out.println("Enter how much money you want to transfer:");
            int amount = sc.nextInt();
            this.currentLoggedIn.checkTransferAmount(amount);

            this.currentLoggedIn.transfer(to, amount);
        } catch (TransferException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addIncome() {
        System.out.println("\nEnter income:");
        this.currentLoggedIn.addIncome(sc.nextInt());
        System.out.println("Income was added!\n");
    }

    private void logout() {
        System.out.println("\nYou have successfully logged out!");
        this.currentLoggedIn = null;
    }

    private void showBalance() {
        System.out.println("\nBalance: " + this.currentLoggedIn.getBalance() + "\n");
    }

    private int accountMenuChoice() {
        int choice;
        do {
            System.out.println("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit");
            choice = sc.nextInt();
        } while(choice != 0 && choice != 1 && choice != 2
                && choice != 3 && choice != 4 && choice != 5);
        return choice;
    }

    private void mainMenu() {
        int mainMenuChoice = mainMenuChoice();
        switch (mainMenuChoice){
            case 0:
                exit(0);
                break;
            case 1:
                createAccount();
                break;
            case 2:
                logIn();
                break;
        }
    }

    private void logIn() {
        System.out.println("\nEnter your card number:");
        long number = sc.nextLong();
        System.out.println("Enter your PIN:");
        int pin = sc.nextInt();
        if (checkIfExists(number, pin)) {
            System.out.println("\nYou have successfully logged in!\n");
            this.currentLoggedIn = new AccountConnection(number);
        } else {
            System.out.println("Wrong card number or PIN!\n");
        }
    }

    private boolean checkIfExists(long number, int pin) {
        return SQLConnection.checkIfExists(number, pin);
    }

    private void createAccount() {
        new Account();
    }

    private int mainMenuChoice() {
        int choice;
        do {
            System.out.println("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");
            choice = sc.nextInt();
        } while(choice != 0 && choice != 1 && choice != 2);
        return choice;
    }
}
