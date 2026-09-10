package bankingsystem;

public class Main {

    public static void main(String[] args) {

        SavingsAccount s1 =
                new SavingsAccount("Hanzala",5000);

        CheckingAccount c1 =
                new CheckingAccount("Ayan",7000);

        Transaction t = new Transaction();

        t.performTransaction(s1,"deposit",1000);

        t.performTransaction(c1,"withdraw",500);

        System.out.println();
        System.out.println("--------SAVINGS ACCOUNT--------");

        s1.displayAccount();
        System.out.println("Current Balance : " + s1.getBalance());

        System.out.println();
        System.out.println("--------Checking Account--------");

        c1.displayAccount();
        System.out.println("Current Balance : " + c1.getBalance());

        System.out.println();

        System.out.println("Total Accounts : "
                +Bank.getTotalAccounts());
    }
}