package bankingsystem;

public class CheckingAccount extends Account {

    private final double overdraftLimit = 1000;

    public CheckingAccount(String accountHolder, double balance) {
        super(accountHolder, balance);
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited ₹" + amount + " into Checking Account.");
    }

    @Override
    public void withdraw(double amount) {

        if (balance + overdraftLimit >= amount) {
            balance -= amount;
            System.out.println("Withdrawn ₹" + amount + " from Checking Account.");
        } else {
            System.out.println("Withdrawal failed! Overdraft limit exceeded.");
        }

    }

    @Override
    public double getBalance() {
        return balance;
    }
}