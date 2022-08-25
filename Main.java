package banking;

public class Main {
    public static void main(String[] args) {
        if (args[0].equals("-fileName")) {
            Bank bank = new Bank(args[1]);
            bank.start();
        }
    }
}