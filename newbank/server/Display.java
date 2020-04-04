/****************************************************************************
 *             Display.java   												*
 * 																			*
 * This class contains all the commands available to the customer that  	*
 * are displayed when the customer logs in and at the end of each 			*
 * transaction.														        *
 * **************************************************************************/

package newbank.server;
import java.util.ArrayList;

public class Display {
    private ArrayList<String> commands;
    public Display(){
        commands = new ArrayList<>();
        commands.add("SHOWMYACCOUNTS");
        commands.add("NEWACCOUNT NewAccountName");
        commands.add("PAY RecipientName Amount");
        commands.add("MOVE amount FromAccountName ToAccountName");
        commands.add("PASSWD NewPassword");
        commands.add("TRANSACTIONS AccountName");
        commands.add("SHOWMYLOANS");
        commands.add("LOAN RecipientName Amount");
        commands.add("PAYMYLOAN RecipientName Amount");
        commands.add("LOGOUT");
        commands.add("EXIT");
    }
    public ArrayList<String> availableCommands(){
        return commands;
    }

}
