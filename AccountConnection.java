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

    public void closeAccount() {
        SQLConnection.deleteAccount(this.cardNumber);
    }

    public void addIncome(int amount) {
        SQLConnection.addIncome(this.cardNumber, amount);
    }

    public void checkTransferAmount(int amount) throws TransferException {
        if (amount > this.getBalance()) {
            throw new TransferException("\nNot enough money!\n");
        }
    }

    private boolean checkLuhn(long to) {
        String number = String.valueOf(to);
        int length = number.length() - 1;

        return Character.getNumericValue(number.charAt(length))
                == Account.checkSum(new StringBuilder(number.substring(0, length)));
    }

    public void checkTransferTo(long to) throws TransferException {
        if (to == this.cardNumber) {
            throw new TransferException("\nYou can't transfer money to the same " +
                    "account!\n");
        } else if (!checkLuhn(to)){
            throw new TransferException("\nProbably you made a mistake in the card " +
                    "number. Please try again!\n");
        } else if (!SQLConnection.checkIfNumberUsed(to)) {
            throw new TransferException("\nSuch a card does not exist.\n");
        }
    }

    public void transfer(long to, int amount) {
        SQLConnection.transferMoney(this.cardNumber, to, amount);
        System.out.println("Success!\n");
    }
}
