package newbank.server;

import java.util.HashMap;

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
		Customer bhagy = new Customer();
		bhagy.setPassword("Test1");
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);

		Customer christina = new Customer();
		christina.setPassword("Test2");
		christina.addAccount(new Account("Savings", 1500.0));
		christina.addAccount(new Account("Main", 5000.0));
		Account mainAccount = christina.getAccounts().get(1);
		Account savingAccount = christina.getAccounts().get(0);
		christina.move(100, mainAccount, savingAccount);
		customers.put("Christina", christina);

		Customer john = new Customer();
		john.setPassword("Test3");
		john.addAccount(new Account("Main", 250.0));
		customers.put("John", john);
	}

	public static NewBank getBank() {
		return bank;
	}

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

	// This function is easy to get NULL and loop if input the character not in exact format and looping
	public synchronized String processRequest(CustomerID customer, String request) {
		String[] requestWords = request.split(" ");
		if(customers.containsKey(customer.getKey())) {
			String userInput = requestWords[0].toUpperCase();//allow user inputs with lower cases, implemented by Chi
			switch(userInput) {
				//This is the case for showing account of particular customer
				case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
				// This is a adding new account implemented by Jane.
				// Input:account name & Output: Success or Fail
				case "NEWACCOUNT" : return newAccount(customer, requestWords[1]);
				// This is a "move" implemented by Long Ng
				// The Move will add amount ($) from either Main/Savings to another account
				// Input: MOVE 300 Main Savings & Output: SUCCESS or FAIL
				// Verified by SHOWMYACCOUNTS
				// However, the Main and Savings must follow exactly the capital letter and small letter in order to function
				case "MOVE" : return moveAccount(customer, requestWords[1], requestWords[2], requestWords[3]);
				// adding a "Move" comparing with "MOVE"
				case "Move" : return moveAccount(customer, requestWords[1], requestWords[2], requestWords[3]);
				case "PASSWD" : return changePasswd(customer, requestWords[1]);
				case "TRANSACTIONS": return transactions(customer, requestWords[1]);
				case "PAY": return payAccount(customer, requestWords[1], requestWords[2]);
				case "LOAN": return loan(customer, requestWords[1], requestWords[2]);
				case "LOGOUT": return "LOGOUT";
				case "EXIT": return "EXIT";//Exit function implemented by Chi
			default : return "FAIL";
			}
		}
		return "FAIL";
	}

	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	/*Method to add a new account. Customer types in NEWACCOUNT followed by name of account
	Example: NEWACCOUNT Savings.
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

	private String changePasswd(CustomerID customer, String passwd) {
		if(customers.get(customer.getKey()).changePassword(customer, passwd)){
			return "Password changed for " + customer.getKey();
		} else{
			return "FAIL: Password length should be at least 8 and contain letters and numbers";
		}
	}

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

	private String loan(CustomerID customerID, String recipient, String amount) {
		Customer loaner = customers.get(customerID.getKey());
		Customer loanee = customers.get(recipient);
		if (loaner.loan(loanee, Double.parseDouble(amount))){
			return "SUCCESS";
		}
		return "FAIL";
	}
}
