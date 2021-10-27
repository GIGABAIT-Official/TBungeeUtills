package bungee.php;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import bungee.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class CommandsPHP extends Command implements TabExecutor
{
   public CommandsPHP(String name) {
     super(name, "tbutills.run.php");
  }
   
  
 @SuppressWarnings("deprecation")
 public void execute(CommandSender arg0, String[] arg1) {
	 CommandSender sender = arg0;
	 
	 if (sender.hasPermission("tbutills.run.php")) {
		 if (arg1.length >= 1) {
			 boolean successfully = runCommand(sender, arg1);
			 String message = successfully ? Main.lang.getFild("succsess", null) : Main.lang.getFild("error", null);
			 sender.sendMessage(message);
		 } else {
			 sender.sendMessage("§cError. §aUsage: php {script or folder}");
		 } 
	 } else {
		 sender.sendMessage(Main.lang.getFild("no_perms", null));
	 } 
  	}
  
  	@SuppressWarnings("deprecation")
	private boolean runCommand(CommandSender sender, String[] args) {

  		try {

  			Process process = Runtime.getRuntime().exec(this.command(args), (String[])null, Main.phpDir);
  			if (process.isAlive()) {
  	  			String line;
  	  	      	StringBuilder output = new StringBuilder();
  	  			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
  	  			      while ((line = input.readLine()) != null) {
  	  			          output.append(line);
  	  			      }
  	  			if (output.length() > 1) {
  	  				sender.sendMessage(output.toString());
  	  			}
  				return true;	
  			}
    
  		} catch (IOException iOException) {}
  			return false;
  	}
  	
	public Iterable<String> onTabComplete(CommandSender arg0, String[] arg1) {
		if (arg1.length <= 1) {
			String[] d = Main.phpDir.list();
			ArrayList<String> Collection = new ArrayList<String>(Arrays.asList(d));
			return  Collection;
		} else {
			ArrayList<String> Collection = new ArrayList<String>();
			return  Collection;
		}
	}
	
	
	private String command(String[] args) {
		String file  = args[0].replace("[", "").replace("]", "");
		File path = new File(Main.phpDir, file);
		if(path.isDirectory()) {
			args[0] = "";
			file = file + File.separator + "index.php";

  			StringBuilder b = new StringBuilder("php " + file); byte b1; int i; String[] arrayOfString;
  			for (i = (arrayOfString = args).length, b1 = 0; b1 < i; ) { String s = arrayOfString[b1];
  			b.append(s).append(" "); b1++; }
  			String command = b.toString();
  			
			return command;
		} else {
			
  			StringBuilder b = new StringBuilder("php "); byte b1; int i; String[] arrayOfString;
  			for (i = (arrayOfString = args).length, b1 = 0; b1 < i; ) { String s = arrayOfString[b1];
  			b.append(s).append(" "); b1++; }
  			String command = b.toString();
  			
  			return command;
		}
	}
}
