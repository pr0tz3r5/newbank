package newbank.server;

import java.util.ArrayList;
import java.util.List;

public class Customer {

	private ArrayList<Account> accounts;
	private List<Loan> toLoanList;
	private List<Loan> fromLoanList;
	private String passwd;

	public Customer() {
		accounts = new ArrayList<>();
		toLoanList = new ArrayList<>();
		fromLoanList = new ArrayList<>();
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
		if (password.length()<=8) {  // add to check the password length
			System.out.println("Password length should be at least 8");
			return false;
		}
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

	public List<Loan> getToLoanList() { return toLoanList; }
	public List<Loan> getFromLoanList() { return fromLoanList; }

	public boolean loan(Customer loanee, double amount) {
		Loan newLoan = new Loan(loanee, amount);
		if (transfer(this, loanee, amount)) {
			Customer.addLoanTo(this, newLoan, "TO");
			Customer.addLoanTo(loanee, newLoan, "FROM");
			return true;
		} else {
			return false;
		}
	}

	public void addToLoanList(Loan loan) {
		this.toLoanList.add(loan);
	}

	public void addFromLoanList(Loan loan) {
		this.fromLoanList.add(loan);
	}

	public static void addLoanTo(Customer customer, Loan loan, String type) {
		if(type == "TO") {
			customer.addToLoanList(loan);
		} else if (type == "FROM") {
			customer.addFromLoanList(loan);
		}
	}
}
