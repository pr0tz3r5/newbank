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
        commands.add("LOAN <Person/Company> <Amount> <Interest%>");
        commands.add("PAYMYLOAN <Person/Company> <Amount>");
        commands.add("LOGOUT");
        commands.add("EXIT");
    }

    public String introMessage(){
        return "The commands available to you are:";
    }

    public String endMessage(){
        return "(Customer Names and Account Names are case sensitive. Example: Main)";
    }

    public String availableCommands(){
        String s = "" + "\n" + introMessage();
        for(String command : commands) {
            s += command + "\n";
        }
        s += endMessage() + "\n";
        return s;
    }

}
