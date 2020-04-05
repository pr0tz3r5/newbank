/********************************************************************
 *             Account.java   										*
 * 																	*
 * This is the class that is the object Account.					*
 * The constructor is of the form 									*
 * Account (String accountName, double openingBalance).				*
 * It contains a method to print to a String "accountName: Balance"	*
 * It contains getters to return the accountName or balance.		*
 * It also contains methods to change the balance.					*
 * ******************************************************************/

package newbank.server;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private String accountName;
	private double openingBalance;
	private List<Transaction> transactionList;


	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
		this.transactionList = new ArrayList<>();
	}

	public String toString() {
		return (accountName + ": " + openingBalance);
	}

	public double getBalance(){
		return openingBalance;
	}

	public String getName(){
		return accountName;
	}

	/**
	 * Credits the account with the specified amount
	 *
	 * @param amount The amount to credit the account
	 * @param description Description of the transaction
	 */
	public void credit(double amount, String description) {
		Transaction transaction = new Transaction(amount, description);
		this.applyTransaction(transaction);
	}

	public void debit(double amount, String description) throws InsufficientBalanceException {
		if (this.openingBalance - amount < 0) {
			throw new InsufficientBalanceException();
		}
		Transaction transaction = new Transaction(amount * -1, description);
		this.applyTransaction(transaction);

	}

	private void applyTransaction(Transaction transaction) {
		this.transactionList.add(transaction);
		this.openingBalance += transaction.getAmount();
	}

	public List<Transaction> getTransactionList() {
		return transactionList;
	}

}
