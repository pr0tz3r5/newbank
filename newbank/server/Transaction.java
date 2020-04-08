/****************************************************************************
 *             Transaction.java   											*
 * 																			*
 * This is the class for the object Transaction.							*
 * The constructor is of the form 											*
 * Transaction(double amount, String description).							*
 * It is used to store the amount and description of each transaction.      *
 * The methods are getters and setters for amount and description           *
 * The list of transactions for each account is created in the class        *
 * Account.java. 		                                                	*
 * **************************************************************************/

package newbank.server;

public class Transaction {
    private double amount;
    private String description;

    public Transaction(double amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
