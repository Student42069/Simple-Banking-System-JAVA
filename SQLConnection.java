package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for all SQL calls
 */
public class SQLConnection {
    static SQLiteDataSource dataSource = new SQLiteDataSource();

    static void connectSQLite(String arg) {
        String url = "jdbc:sqlite:" + arg;
        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER PRIMARY KEY," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static boolean checkIfExists(long number, int pin) {
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                try (ResultSet greatHouses = statement.executeQuery("SELECT * FROM card WHERE number = "
                        + number + " AND pin = "
                        + pin + ";")) {
                    return greatHouses.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    static void insertInDB(long cardNumber, int pin) {
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution
                statement.executeUpdate("INSERT INTO card (number, pin) VALUES " +
                        String.format("(%d, %d)", cardNumber, pin));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static long getBalance(long cardNumber) {
        long cardBalance = 0;
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution
                try (ResultSet balance = statement.executeQuery("SELECT balance " +
                        "FROM card WHERE number = " + cardNumber)) {
                    balance.next();
                    // Retrieve column values
                    cardBalance = balance.getLong("balance");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cardBalance;
    }

    static boolean checkIfNumberUsed(Long number) {
        try (Connection con = dataSource.getConnection()) {
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
}
