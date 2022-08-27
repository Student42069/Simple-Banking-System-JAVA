package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

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
                try (ResultSet account = statement.executeQuery("SELECT * FROM card " +
                        "WHERE number = "
                        + number + " AND pin = "
                        + pin + ";")) {
                    return account.next();
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

    public static void deleteAccount(long cardNumber) {
        try (Connection con = dataSource.getConnection()) {
            String delete = "DELETE FROM card WHERE number = ?";

            try (PreparedStatement preparedStatement = con.prepareStatement(delete)) {
                preparedStatement.setLong(1, cardNumber);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addIncome(long cardNumber, int amount) {
        try (Connection con = dataSource.getConnection()) {
            String delete = "UPDATE card SET balance = balance + ? WHERE number = ?";

            try (PreparedStatement preparedStatement = con.prepareStatement(delete)) {
                preparedStatement.setLong(1, amount);
                preparedStatement.setLong(2, cardNumber);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void transferMoney(long from, long to, int amount) {
        String updateFromSQL = "UPDATE card SET balance = balance - ? WHERE number = ?";
        String updateToSQL = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection con = dataSource.getConnection()) {
            // Disable auto-commit mode
            con.setAutoCommit(false);

            try (PreparedStatement updateFrom = con.prepareStatement(updateFromSQL);
                 PreparedStatement updateTo = con.prepareStatement(updateToSQL)) {

                updateFrom.setInt(1, amount);
                updateFrom.setLong(2, from);
                updateFrom.executeUpdate();

                updateTo.setInt(1, amount);
                updateTo.setLong(2, to);
                updateTo.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
