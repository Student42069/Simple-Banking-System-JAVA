package banking;

import java.util.Random;

/**
 * Class used to create a new bank Account
 */
public class Account {
    private final static String MAJOR_INDUSTRY_IDENTIFIER = "400000";

    private final static Random random = new Random(100);

    public Account(){
        int pin = generatePin();
        long cardNumber = generateCardNumber();
        insertInDB(cardNumber, pin);
        printCreated(cardNumber, pin);
    }

    private static void insertInDB(long cardNumber, int pin) {
        SQLConnection.insertInDB(cardNumber, pin);
    }

    private void printCreated(long cardNumber, int pin) {
        System.out.println("\nYour card has been created\n" +
                "Your card number:\n" +
                cardNumber + "\n" +
                "Your card PIN:\n" +
                pin + "\n");
    }

    private static int generatePin() {
        return random.nextInt(10000 - 1000) + 1000;
    }

    private static long generateCardNumber() {
        StringBuilder number;
        do {
            number = new StringBuilder(MAJOR_INDUSTRY_IDENTIFIER);
            for (int i = 0; i < 9; i++) {
                number.append(random.nextInt(10));
            }
            number.append(checkSum(number));
        } while(checkIfNumberUsed(Long.parseLong(number.toString())));

        return Long.parseLong(number.toString());
    }

    private static boolean checkIfNumberUsed(Long number) {
        return SQLConnection.checkIfNumberUsed(number);
    }

    private static int checkSum(StringBuilder number) {
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

    private static int removeNine(int num) {
        if (num > 9) {
            return num - 9;
        }
        else {
            return num;
        }
    }
}
