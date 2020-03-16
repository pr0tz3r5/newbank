package newbank.server;

import java.util.ArrayList;

public class Customer {

	private ArrayList<Account> accounts;
	private String passwd;

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

	public void setPassword(String password) {
		passwd = password;
	}

	public Boolean changePassword(CustomerID customer, String password) {
		if (this.checkPasswdFormat(password)) {
			passwd = password;
			System.out.println("Password changed for " + customer.getKey());
			return true;
		} else {
			System.out.println("Password change attempt failed for " + customer.getKey());
		}
		return false;
	}

	public Boolean checkPassword(String password) {
		return this.passwd.equals(password);
	}

	public Boolean checkPasswdFormat(String str) {
		Boolean isNumber = false;
		Boolean isLetter = false;

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (Character.isDigit(c)) {
				isNumber = true;
			}
			if (Character.isLetter(c)) {
				isLetter = true;
			}
		}
		return isNumber && isLetter;
	}

	public void addAccount(Account account) {
		accounts.add(account);
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public boolean move(double amount, Account origAccount, Account destAccount) {
		if (origAccount.getBalance() >= amount) {
			origAccount.setBalance(origAccount.getBalance() - amount);
			destAccount.setBalance(destAccount.getBalance() + amount);
			return true;
		} else {
			return false;
		}
	}

	public Account findAccount(String accountName) {
		for (Account account : accounts) {
        if (account.getName().equals(accountName)) {
            return account;
        }
    }
    return null;
	}
}
