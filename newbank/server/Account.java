package newbank.server;

public class Account {

	private String accountName;
	private double openingBalance;

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
	}

	public String toString() {
		return (accountName + ": " + openingBalance);
	}

	public double getBalance(){
		return openingBalance;
	}

	public void setBalance(double amount){
		this.openingBalance = amount;
	}

}
