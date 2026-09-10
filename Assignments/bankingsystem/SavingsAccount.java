package bankingsystem;

public class SavingsAccount extends Account {

    public SavingsAccount(String accountHolder,double balance){
        super(accountHolder,balance);
    }

    @Override
    public void deposit(double amount){
        balance+=amount;
    }

    @Override
    public void withdraw(double amount){
        if(balance>=amount){
            balance-=amount;
        }
        else{
            System.out.println("Insufficient Balance");
        }
    }

    @Override
    public double getBalance(){
        return balance;
    }
}