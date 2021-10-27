package bungee.main;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Help extends Command {

	public Help(String name) {
		super(name, "tbutills", "tbutills help");
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender arg0, String[] arg1) {
		 CommandSender sender = arg0;
		 if (sender.hasPermission("tbutills")) {
			 if (arg1.length >= 0) {
				 String message = Main.lang.getFild("help", null);
				 sender.sendMessage(message);
			 } else {
				 sender.sendMessage("§cError. §aUsage: php {script name}");
			 } 
		 } else {
			 sender.sendMessage(Main.lang.getFild("no_perms", null));
		 }

	}

}
