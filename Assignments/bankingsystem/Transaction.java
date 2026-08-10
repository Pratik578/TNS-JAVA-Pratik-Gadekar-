package bankingsystem;

public class Transaction {

    final double transactionFee = 10;

    public final void performTransaction(Account account,
                                         String type,
                                         double amount){

        if(type.equalsIgnoreCase("deposit")){
            account.deposit(amount-transactionFee);
        }

        else if(type.equalsIgnoreCase("withdraw")){
            account.withdraw(amount+transactionFee);
        }

        System.out.println("Transaction Fee = "+transactionFee);
    }
}