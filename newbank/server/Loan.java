/****************************************************************************
 *             Loan.java   													*
 * 																			*
 * This is the class for the object Loan.									*
 * The constructor is of the form 											*
 * Loan(Customer sender, Customer receiver, double amount).					*
 * It is used to store the lender, borrower and amount of each loan. 		*
 * Each loan is given a unique ID number.									*
 * The methods are getters for ID, lender, borrower and amount and a        *
 * setter for amount.														*
 * The list of loans for each customer is created in the class      		*
 * Customer.java. 		                                                	*
 * **************************************************************************/
package newbank.server;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class Loan {
	private double loanAmount;
	private double interest;
	private double repayable;
	private Customer loaner;
	private Customer loanee;
	private String id;
	private UUID uuid;

	public Loan(Customer sender, Customer receiver, double amount, double interest) {
		this.uuid = UUID.randomUUID();
		this.id = uuid.toString();
		this.loaner = sender;
		this.loanee = receiver;
		this.loanAmount = amount;
		this.interest = interest;
		repayable = round(amount*(1+interest/100),2);
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

	public double getInterest(){
		return interest;
	}

	public double getRepayable(){
		return repayable;
	}

	public void updateRepayable(double amount){ repayable = amount; }

	private static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
