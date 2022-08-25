package banking;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import static banking.Bank.dataSource;

public class Account {
    private long cardNumber;

    private final static Random random = new Random(100);

    public Account(){
        int pin = generatePin();
        long cardNumber = generateCardNumber();
        insertInDB(cardNumber, pin);
        printCreated(cardNumber, pin);
    }

    public Account(long number) {
        this.cardNumber = number;
    }

    private static void insertInDB(long cardNumber, int pin) {
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution
                statement.executeUpdate("INSERT INTO card (number, pin) " +
                        "VALUES " +
                        "(" + cardNumber + ", " + pin + ")");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            number = new StringBuilder("400000");
            for (int i = 0; i < 9; i++) {
                number.append(random.nextInt(10));
            }
            number.append(checkSum(number));
        } while(checkIfNumberUsed(Long.parseLong(number.toString())));

        return Long.parseLong(number.toString());
    }

    private static boolean checkIfNumberUsed(Long number) {
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                try (ResultSet greatHouses = statement.executeQuery("SELECT * FROM card WHERE number = "
                        + number + ";")) {
                    return greatHouses.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

    public long getBalance() {
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution
                try (ResultSet balance = statement.executeQuery("SELECT balance " +
                        "FROM card WHERE number = " + this.cardNumber)) {
                    balance.next();
                    // Retrieve column values
                    return balance.getLong("balance");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 69;
    }
}
