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

	public boolean addAccount(Account account) {
		Account isExistingAccount = findAccount(account.getName());
		if (isExistingAccount==null) {
			accounts.add(account);
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public boolean move(double amount, Account origAccount, Account destAccount) {
		try {
			origAccount.debit(amount, "Move to " + destAccount.getName());
			destAccount.credit(amount, "Move from " + origAccount.getName());
			return true;
		} catch (InsufficientBalanceException e) {
			return false;
		}
	}

	public boolean transfer(Customer sender, Customer receiver, Double amount) {
		Account origAccount = sender.findAccount("Main");
		Account destAccount = receiver.findAccount("Main");
		return move(amount, origAccount, destAccount);
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
