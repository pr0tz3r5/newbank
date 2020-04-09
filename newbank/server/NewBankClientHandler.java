/********************************************************************
 *             NewBankClientHandler.java   							*
 * 																	*
 * This is the class that takes in the requests from the client		*
 * and processes them.												*
 * 																	*
 * ******************************************************************/

package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class NewBankClientHandler extends Thread{

	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;
	private static final int MAXIMUM_LOGIN_ATTEMPTS = 3;


	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);

	}

	public void run() {
		// keep getting requests from the client and processing them
		try {
			// ask for user name
			out.println("Enter Username");
			String userName = in.readLine();
			// authenticate user and get customer ID token from bank for use in subsequent requests
			int loginAttempts = 0;
			CustomerID customer = null;
			while (customer == null && loginAttempts < MAXIMUM_LOGIN_ATTEMPTS) {
				out.println("Enter Password");
				String password = in.readLine();
				out.println("Checking Details...");
				customer = bank.checkLogInDetails(userName, password);
				if (customer == null) {
					out.println("Invalid credentials.");
				}
				loginAttempts++;
			}
			// If still null after 3 attempts
			if (customer == null) {
				out.println("Maximum login attempts reached.");
				return;
			} else {

				out.println("Log In Successful. What do you want to do?");
				Display display = new Display();
				//out.println(display.availableCommands());
				while(true) {
					String request = in.readLine();
					System.out.println("Request from " + customer.getKey());
					String response = bank.processRequest(customer, request);

					if (response == "LOGOUT") {
						customer = null;
						out.println("LOGOUT SUCCESS.");
						run();
					}
					else if(response == "EXIT"){//exit function implemented by Chi
						out.println("Terminal has been closed.");
						return;//exit function implemented by Chi
					}

					out.println(response);
					//out.println(display.availableCommands());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
}
