/************************************************************************
 *             CustomerID.java   										*
 * 																		*
 * This is the class that for the object CustomerID.					*
 * The constructor is of the form 										*
 * CustomerID (String key).												*
 * It contains a getter to return the key.								*
 * It is used in NewBank.java to authorise the customer with the server	*
 * when logging in.														*
 * **********************************************************************/

package newbank.server;

public class CustomerID {
	private String key;
	
	public CustomerID(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}
