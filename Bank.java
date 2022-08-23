package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class Bank {
    List<Account> accounts = new ArrayList<>();
    Account currentLoggedIn;

    Scanner sc = new Scanner(System.in);

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
                logout();
                break;
        }
    }

    private void logout() {
        System.out.println("You have successfully logged out!");
        this.currentLoggedIn = null;
    }

    private void showBalance() {
        System.out.println("Balance: " + this.currentLoggedIn.getBalance());
    }

    private int accountMenuChoice() {
        int choice;
        do {
            System.out.println("1. Balance\n" +
                    "2. Log out\n" +
                    "0. Exit");
            choice = sc.nextInt();
        } while(choice != 0 && choice != 1 && choice != 2);
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
        System.out.println("Enter your card number:");
        long number = sc.nextLong();
        System.out.println("Enter your PIN:");
        int pin = sc.nextInt();
        for(Account account : accounts){
            if (account.getCardNumber() == number && account.getPin() == pin) {
                System.out.println("You have successfully logged in!");
                this.currentLoggedIn = account;
                break;
            } else {
                System.out.println("Wrong card number or PIN!");
            }
        }
    }

    private void createAccount() {
        this.accounts.add(new Account());
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
