package banking;

import java.util.Random;

public class Account {
    private long cardNumber;
    private int pin;
    private long balance = 0;

    private Random random = new Random();

    public Account(){
        generateCardNumber();
        generatePin();
        printCreated();
    }

    private void printCreated() {
        System.out.println("\nYour card has been created\n" +
                "Your card number:\n" +
                this.cardNumber + "\n" +
                "Your card PIN:\n" +
                this.pin + "\n");
    }

    private void generatePin() {
        this.pin = random.nextInt(10000);
    }

    private void generateCardNumber() {
        StringBuilder number = new StringBuilder("400000");
        for (int i = 0; i < 9; i++){
            number.append(String.valueOf(random.nextInt(10)));
        }
        number.append(String.valueOf(checkSum(number)));
        this.cardNumber = Long.parseLong(number.toString());
    }

    private int checkSum(StringBuilder number) {
        return random.nextInt(10);
    }

    public long getCardNumber() {
        return this.cardNumber;
    }

    public int getPin() {
        return this.pin;
    }

    public long getBalance() {
        return this.balance;
    }
}
