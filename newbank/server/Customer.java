package newbank.server;

import java.util.ArrayList;

public class Customer {

	private ArrayList<Account> accounts;

	public Customer() {
		accounts = new ArrayList<>();
	}

	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);
	}

	public boolean move(double amount, Account origAccount, Account destAccount) {
		if (origAccount.balance >= amount) {
			origAccount.updateBalance(origAccount.balance - amount);
			destAccount.updateBalance(destAccount.balance + amount);
			return true;
		} else {
			return false;
		}
	}
}
