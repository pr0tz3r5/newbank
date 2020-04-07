package newbank.server;

import java.util.UUID;

public class Loan {
	private double loanAmount;
	private Customer loaner;
	private Customer loanee;
	private String id;
	private UUID uuid;

	public Loan(Customer sender, Customer receiver, double amount) {
		this.uuid = UUID.randomUUID();
		this.id = uuid.toString();
		this.loaner = sender;
		this.loanee = receiver;
		this.loanAmount = amount;
	}

	public String getId() {
		return id;
	}

	public Customer getLoaner(){
		return loaner;
	}

	public Customer getLoanee() { return loanee; }

	public double getLoanAmount(){
		return loanAmount;
	}

	public void updateLoanAmount(double amount){ this.loanAmount = amount; }
}
