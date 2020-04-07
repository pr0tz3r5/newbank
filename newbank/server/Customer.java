/****************************************************************************
 *             Customer.java   												*
 * 																			*
 * This is the class for the object Customer.								*
 * The constructor is of the form 											*
 * Customer ().																*
 * It creates the list of accounts and the list of loans the customer has.	*
 * It contains all the methods to change the customer's loans and 			*
 * accounts.																*
 * It contains the methods to set and change the customer's password.		*
 * **************************************************************************/

package newbank.server;

import java.util.ArrayList;
import java.util.List;

public class Customer {

	private ArrayList<Account> accounts;
	private List<Loan> toLoanList;
	private List<Loan> fromLoanList;
	private String passwd;

	public Customer() {
		this.accounts = new ArrayList<>();
		this.toLoanList = new ArrayList<>();
		this.fromLoanList = new ArrayList<>();
	}

	public String accountsToString() {
		String s = "";
		for(Account a : this.accounts) {
			s += a.toString();
		}
		return s;
	}

	public void setPassword(String password) {
		passwd = password;
	}

	public Boolean changePassword(CustomerID customer, String password) {
		if (password.length()>=8 && this.checkPasswdFormat(password)) {
			passwd = password;
			return true;
		} else {
			return false;
		}
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

	private boolean loanExists(Customer customer){
		if(findLoan(customer, this.fromLoanList) != null) {
			return true;
		}
  		return false;
	}
	
	//this method should only be called if loanExits method returns true.
	private Loan findLoan(Customer customer, List<Loan> loanList){
		for(Loan l : loanList){
			Customer loaner = l.getLoaner();
			String loanerName = loaner.accountsToString();
			if(loanerName.equals(customer.accountsToString())){
				return l;
			}
		}
		return null;
	}

	public boolean loan(Customer loanee, double amount) {
		Loan newLoan = new Loan(this, loanee, amount);
		if (transfer(this, loanee, amount)) {
			Customer.addLoanTo(this, newLoan, "TO");
			Customer.addLoanTo(loanee, newLoan, "FROM");
			return true;
		} else {
			return false;
		}
	}
	
	private void updateLoan(Loan loan, double amount, List<Loan> loanList){
		int i = loanList.indexOf(loan);
		loanList.remove(i);
		double loanAmount = loan.getLoanAmount();
		loan.updateLoanAmount(loanAmount - amount);
		loanList.add(loan);
	}

	public boolean payLoan(Customer loaner, double amount){
		if(loanExists(loaner)){
			Loan loanToBePaid = findLoan(loaner, this.fromLoanList);
			Loan loanersLoan = findLoan(loaner, loaner.toLoanList);
			double loanAmount = loanToBePaid.getLoanAmount();
			if(loanAmount >= amount) {//only allow to pay amount which is less or equal to loanAmount.
				if (transfer(this,loaner,amount)) {
					updateLoan(loanToBePaid, amount, this.fromLoanList);
					updateLoan(loanersLoan, amount, loaner.toLoanList);
					return true;

				} else {
					return false;
				}
			} else{
				System.out.println("FAIL:The amount is greater than the loan.");
				return false;
			}
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
