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
        commands.add("NEWACCOUNT <Name>");
        commands.add("PAY <Person/Company> <Amount>");
        commands.add("MOVE <Amount> <From> <To>");
        commands.add("WITHDRAW <Amount> <From>");
        commands.add("PASSWD <newPassword>");
        commands.add("TRANSACTIONS <Account>");
        commands.add("SHOWMYLOANS");
        commands.add("LOAN <Person/Company> <Amount>");
        commands.add("PAYMYLOAN <Person/Company> <Amount>");
        commands.add("LOGOUT");
        commands.add("EXIT");
    }
    public ArrayList<String> availableCommands(){
        return commands;
    }

}
