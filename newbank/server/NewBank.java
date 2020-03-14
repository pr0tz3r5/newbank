package newbank.server;

import java.util.HashMap;

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
		bhagy.addAccount(new Account("Main", 1000.0));
		customers.put("Bhagy", bhagy);

		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		christina.addAccount(new Account("Main", 5000.0));
		Account mainAccount = christina.getAccounts().get(1);
		Account savingAccount = christina.getAccounts().get(0);
		christina.move(100, mainAccount, savingAccount);
		customers.put("Christina", christina);

		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		customers.put("John", john);
	}

	public static NewBank getBank() {
		return bank;
	}

	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if(customers.containsKey(userName)) {
			return new CustomerID(userName);
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
		String[] requestWords = request.split(" ");
		if(customers.containsKey(customer.getKey())) {
			switch(requestWords[0]) {
			case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
			case "NEWACCOUNT" : return newAccount(customer, requestWords[1]);
			case "MOVE" : return moveAccount(customer, requestWords[1], requestWords[2], requestWords[3])
			default : return "FAIL";
			}
		}
		return "FAIL";
	}

	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	/*Method to add a new account. Customer types in NEWACCOUNT followed by name of account
	Example: NEWACCOUNT Savings. The account is created with a zero balance.
	The customer should use MOVE to put money in the account.*/
	private String newAccount(CustomerID customer, String accountName) {
		customers.get(customer.getKey()).addAccount(new Account(accountName, 0.0));
		return "SUCCESS";
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

}
