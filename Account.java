package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class Account {
    private long cardNumber;
    private int pin;

    private final static Random random = new Random(100);

    public Account(){
        generateCardNumber();
        generatePin();
        insertInDB();
        printCreated();
    }

    private void insertInDB() {
        String url = "jdbc:sqlite:" + Bank.DBFileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution
                statement.executeUpdate("INSERT INTO card (number, pin) " +
                        "VALUES " +
                        "("+this.cardNumber+", "+this.pin+")");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account(long number) {
        this.cardNumber = number;
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
        StringBuilder number;
        do {
            number = new StringBuilder("400000");
            for (int i = 0; i < 9; i++) {
                number.append(random.nextInt(10));
            }
            number.append(checkSum(number));
        } while(checkIfNumberUsed(Long.parseLong(number.toString())));

        this.cardNumber = Long.parseLong(number.toString());
    }

    private boolean checkIfNumberUsed(Long number) {
        String url = "jdbc:sqlite:" + Bank.DBFileName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
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

    public long getBalance() {
        String url = "jdbc:sqlite:" + Bank.DBFileName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

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
