package bungee.rcon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import bungee.main.Main;
import bungee.main.SaveLogs;
import bungee.rcon.library.AuthenticationException;
import bungee.rcon.library.Rcon;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;


public class RunRcon extends Command implements TabExecutor{
	
	public RunRcon(String name) {
		super(name);
	}
	
	@SuppressWarnings("deprecation")
	public void execute(CommandSender arg0, String[] args) {
		
		CommandSender sender = arg0;
		Rcon rcon = null;
		String cmd = "";
		String result = null;
		
		if(args.length < 2)
    	{
    		sender.sendMessage(Main.lang.getFild("no_rcon_args", null));
        	return;
    	}
		
		String server = args[0];
		RconConfig rconfig = new RconConfig(server);
		if (server.equals("all")) {
			if (sender.hasPermission("tbutills.rcon.*") | sender.hasPermission("tbutills.rcon.all")) {
				
				for(String tmp : args)
				{
					if(tmp.equals(args[0])){continue;}
		    		cmd += " " + tmp;
				}
				
				sender.sendMessage(Main.lang.getFild("rcon_send_all", new String[] {"{command}", cmd}));
				
				for(String serv : rconfig.getServers())
				{
					RconConfig rconConfig = new RconConfig(serv);
					try {
						rcon = new Rcon(rconConfig.getIP(), rconConfig.getPort(), rconConfig.getPass().getBytes());
						result = rcon.command(cmd);
						new SaveLogs("rcon_servers_log", " Server: " + serv + " || Command: " + cmd + System.lineSeparator() +"Response: "+ System.lineSeparator() + result);
						
						sender.sendMessage(Main.lang.getFild("rcon_server_response", new String[] {"{server}", serv}));
						sender.sendMessage(result);
					} catch (IOException | AuthenticationException e1) {
					}
				}
				return;
				
			} else {
				sender.sendMessage(Main.lang.getFild("no_perms", null));
				return;
			}

		} else {
		
			if(rconfig.serverExist() == false) 
			{
				sender.sendMessage(Main.lang.getFild("rcon_server_not_exist", new String[] {"{server}", server}));
				return;
			}
				
			if (sender.hasPermission(rconfig.getPerms()) | sender.hasPermission("tbutills.rcon.*") | sender.hasPermission("tbutills.rcon.all")) 
			{
			
				for(String tmp : args)
				{
					if(tmp == args[0])
					{
						continue;
					}
		    		cmd += " " + tmp;
				}
			
				try {
					rcon = new Rcon(rconfig.getIP(), rconfig.getPort(), rconfig.getPass().getBytes());
				} catch (IOException | AuthenticationException e1) {
					sender.sendMessage(Main.lang.getFild("rcon_server_not_exist", new String[] {"{server}", server}));
					return;
				}

		  		
		  		try {
		  			result = rcon.command(cmd);
		  			new SaveLogs("rcon_servers_log", " Server: " + server + " || Command: " + cmd + System.lineSeparator() +"Response: "+ System.lineSeparator() + result);
		  		} catch (IOException e) {
					e.printStackTrace();
				}
		  		sender.sendMessage(Main.lang.getFild("rcon_server_send", new String[] {"{server}", server, "{command}", cmd}));
				sender.sendMessage(result);
				return;
			
			} else {
				sender.sendMessage(Main.lang.getFild("no_perms", null));
				return;
			}
		}	
	}

	@Override
	public Iterable<String> onTabComplete(CommandSender arg0, String[] arg1) {
		ArrayList<String> Collection = new ArrayList<String>();
		if (arg1.length <= 1) {
			try {
				Collection<String> d = RconConfig.getTab();
				d.add("all");
				return d;
			} catch (IOException e) {
				return Collection;
			}
		} else {
			return Collection;
		}
	}

	
	
	
	
	
	
	
	
}
