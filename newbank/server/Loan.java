package newbank.server;

import java.util.UUID;

public class Loan {
	private double loanAmount;
	private Customer loanCustomer;
	private String id;

	public Loan(Customer customer, double amount) {
		UUID uuid = UUID.randomUUID();
		id = uuid.toString();
		loanCustomer = customer;
		loanAmount = amount;
	}

	public String getId() {
		return id;
	}

	public Customer getLoanCustomer(){
		return loanCustomer;
	}

	public double getLoanAmount(){
		return loanAmount;
	}
}
