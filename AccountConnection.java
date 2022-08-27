package banking;

/**
 * Class used for possible operation in an account
 * add, remove, trasfer funds.
 */
public class AccountConnection {
    private final long cardNumber;

    public AccountConnection(long number) {
        this.cardNumber = number;
    }

    public long getBalance() {
        return SQLConnection.getBalance(this.cardNumber);
    }
}
