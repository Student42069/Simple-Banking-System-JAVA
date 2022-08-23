package banking;

import java.util.Random;

public class Account {
    private long cardNumber;
    private int pin;
    private long balance = 0;

    private final Random random = new Random();

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
        int sum = 0;
        for (int i = 1; i <= number.length(); i++) {
            if (i%2==1) {
                sum += removeNine(Integer.parseInt(number.charAt(i-1) + "") * 2);
            } else {
                sum += Integer.parseInt(number.charAt(i-1) + "");
            }
        }
        return ((int) Math.ceil(sum/10.0)*10) - sum;
    }

    private int removeNine(int num) {
        if (num > 9) {
            return num - 9;
        }
        else {
            return num;
        }
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
