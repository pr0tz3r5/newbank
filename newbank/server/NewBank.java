/****************************************************************************
 *             NewBank.java   												*
 * 																			*
 * This is the class that creates the bank and adds test data for 3			*
 * customers.																*
 * It contains the method to check the login details for the customer.		*
 * It also contains the methods to process the commands input by the		*
 * customer.														 		*
 * More detail for each of these methods is shown below with the methods.	*
 * **************************************************************************/
package newbank.server;

import java.util.HashMap;
import java.util.List;

import static newbank.server.Customer.*;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;

	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}

	//Test comment
	private void addTestData() {
		Customer bhagy = new Customer("Bhagy");
		bhagy.setPassword("Test1");
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);

		Customer christina = new Customer("Christina");
		christina.setPassword("Test2");
		christina.addAccount(new Account("Savings", 1500.0));
		christina.addAccount(new Account("Main", 5000.0));
		Account mainAccount = christina.getAccounts().get(1);
		Account savingAccount = christina.getAccounts().get(0);
		christina.move(100, mainAccount, savingAccount);
		customers.put("Christina", christina);

		Customer john = new Customer("John");
		john.setPassword("Test3");
		john.addAccount(new Account("Main", 250.0));
		customers.put("John", john);
	}

	public static NewBank getBank() {
		return bank;
	}
	/* Method to check the customer is an authorised user and has the correct password */
	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName)) {
			if (customers.get(userName).checkPassword(password)) {
				System.out.println("Login succeeded for " + userName);
				return new CustomerID(userName);
			} else {
				System.out.println("Login failed for " + userName + ".");
			}
		}
		return null;
	}
	/*********************************************************************************
	 * commands from the NewBank customer are processed in this method               *
	 *********************************************************************************
	  Example: The customer enters a request such as NEWACCOUNT Savings.
	  This is read into the variable request. This is then split into separate
	  words NEWACCOUNT and Savings and stored as Strings in the array requestWords. */

	public synchronized String processRequest(CustomerID customer, String request) {
		String[] requestWords = request.split(" ");//Input command is split into words and stored in an array.
		if(customers.containsKey(customer.getKey())) {
			String userInput = requestWords[0].toUpperCase();//allow user inputs with lower cases.
			//More detail of the methods in the switch is shown with the methods at the bottom of the page.
			switch(userInput) {

				case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
				case "NEWACCOUNT" : if (requestWords.length==2){return newAccount(customer, requestWords[1]);}else{ return "FAIL";}
				case "MOVE" : if (requestWords.length==4){return moveAccount(customer, requestWords[1], requestWords[2], requestWords[3]);}else{ return "FAIL";}
				case "PASSWD" : if (requestWords.length==2){return changePasswd(customer, requestWords[1]);}else{ return "FAIL";}
				case "TRANSACTIONS": if (requestWords.length==2){return transactions(customer, requestWords[1]);}else{ return "FAIL";}
				case "PAY": if (requestWords.length==3){return payAccount(customer, requestWords[1], requestWords[2]);}else{ return "FAIL";}
				case "SHOWMYLOANS" : return showMyLoans(customer);
				case "LOAN": if (requestWords.length==4){return loan(customer, requestWords[1], requestWords[2], requestWords[3]);}else{ return "FAIL";}
				case "PAYMYLOAN": if (requestWords.length==3){return payLoan(customer, requestWords[1], requestWords[2]);}else{ return "FAIL";}
				case "WITHDRAW": if (requestWords.length==3){return withdraw(customer, requestWords[1], requestWords[2]);}else{ return "FAIL";}
				case "LOGOUT": return "LOGOUT";
				case "EXIT": return "EXIT";
			default : return "FAIL";
			}
		}
		return "FAIL";
	}
	/* Method for showing all the accounts held by the customer. */
	private String showMyAccounts(CustomerID customer) { return (customers.get(customer.getKey())).accountsToString(); }

	/*Method to add a new account. Customer types in NEWACCOUNT followed by name of account
	The account is created with a zero balance.
	The customer should use MOVE to put money in the account.
	Method prevents duplicate Account being created.*/
	private String newAccount(CustomerID customer, String accountName) {
		Customer currentCustomer = customers.get(customer.getKey());
		if (currentCustomer.addAccount(new Account(accountName, 0.0))) {
			return "SUCCESS";
		} else {
			return "FAIL";
		}
	}

	/* Method to change the customer's password to a new one */
	private String changePasswd(CustomerID customer, String passwd) {
		if(customers.get(customer.getKey()).changePassword(customer, passwd)){
			return "Password changed for " + customer.getKey();
		} else{
			return "FAIL: Password length should be at least 8 and contain letters and numbers";
		}
	}

	/* Method to move amount from one account to another account of the same customer. */
	private String moveAccount(CustomerID customer, String amount, String account1, String account2) {
		Customer currentCustomer = customers.get(customer.getKey());
		Account origAccount = currentCustomer.findAccount(account1);
		Account destAccount = currentCustomer.findAccount(account2);
		Double amountDouble = Double.parseDouble(amount);
		if(origAccount != null && destAccount != null && currentCustomer.move(amountDouble, origAccount, destAccount)) {
			return "SUCCESS";
		} else {
			return "FAIL";
		}
	}

	/* Method to display all the transactions on a chosen account of the customer. */
	private String transactions(CustomerID customerId, String accountName) {
		Customer customer = customers.get(customerId.getKey());
		Account selectedAccount = customer.findAccount(accountName);
		if (selectedAccount == null) {
			return "Account not found.";
		}
		if (selectedAccount.getTransactionList().size() == 0) {
			return "No transactions.";
		}
		StringBuilder response = new StringBuilder();
		int transactionIndex = 1;
		for (Transaction transaction : selectedAccount.getTransactionList()) {
			response.append(transactionIndex);
			response.append(".\t");
			response.append(transaction.getDescription());
			response.append("\t");
			response.append(transaction.getAmount());
			response.append("\n");
			transactionIndex++;
		}
		return response.toString();
	}

	/* Method to move amount from Main account of one customer to Main account of another customer. */
	private String payAccount(CustomerID customerID, String recipient, String amount) {
		try {
			Double.parseDouble(amount.trim());
		} catch(NumberFormatException e) {
			return "FAIL";
		}
		Customer sender = customers.get(customerID.getKey());
		Customer receiver = customers.get(recipient);

		if (sender.transfer(sender, receiver, Double.parseDouble(amount))) {
			return "SUCCESS";
		} else {
			return "FAIL";
		}
	}

	/* Method for showing all the loans held by the customer. */
	private String showMyLoans(CustomerID customerID) {
		{ return (customers.get(customerID.getKey())).loansToString(); }
	}

	/* Method to set up loan to another customer */
	private String loan(CustomerID customerID, String recipient, String amount, String interest) {
		Customer loaner = customers.get(customerID.getKey());
		Customer loanee = customers.get(recipient);
		if (loaner.loan(loanee, Double.parseDouble(amount), Double.parseDouble(interest))){
			return "SUCCESS";
		}
		return "FAIL";
	}

	/* Method to either part pay or pay back in full a loan */
	private String payLoan(CustomerID customerID, String recipient, String amount) {
		Customer loanee = customers.get(customerID.getKey());
		Customer loaner = customers.get(recipient);
		if (loanee.payLoan(loaner, Double.parseDouble(amount))){
			return "SUCCESS";
		}
		return "FAIL";
	}

	/* Method to withdraw cash from a chosen account */
	private String withdraw(CustomerID customerID, String amount, String accountName) {
		Customer customer = customers.get(customerID.getKey());
		Account account = customer.findAccount(accountName);
		if (account == null) {
			return "FAIL";
		}
		try {
			double withdrawal = Double.parseDouble(amount);
			account.debit(withdrawal, "Cash withdrawal");
			// Dispense cash here
			return "SUCCESS";
		} catch (Exception e) {
			return "FAIL";
		}
	}

	public HashMap<String,Customer> getCustomers(){
		return customers;
	}
}
