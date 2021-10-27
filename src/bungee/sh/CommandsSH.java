package bungee.sh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import bungee.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class CommandsSH extends Command implements TabExecutor
{
   public CommandsSH(String name) {
     super(name, "tbutills.run.sh");
  }
  
 @SuppressWarnings("deprecation")
 public void execute(CommandSender arg0, String[] arg1) {
	 CommandSender sender = arg0;
	 if (sender.hasPermission("tbutills.run.sh")) {
      
		 if (arg1.length >= 0) {
			 boolean successfully;
			try {
				successfully = runCommand(arg1, sender);
				 String message = successfully ? Main.lang.getFild("succsess", null) : Main.lang.getFild("error", null);
				 sender.sendMessage(message);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		 } else {
			 sender.sendMessage("§cError. §aUsage: sh {script name} ");
		 } 
	 } else {
		 sender.sendMessage(Main.lang.getFild("no_perms", null));
	 } 
  	}
  

	private boolean runCommand(String[] args, CommandSender sender) throws InterruptedException {    
  		try {
  			StringBuilder b = new StringBuilder("sh "); byte b1; int i; String[] arrayOfString;
  			for (i = (arrayOfString = args).length, b1 = 0; b1 < i; ) { String s = arrayOfString[b1];
  			b.append(s).append(" "); b1++; }
      
  			Process process = Runtime.getRuntime().exec(b.toString(), (String[])null, Main.shDir);
  			process.waitFor();
  			BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
  			String line = "";
  			while ((line=buf.readLine())!=null) {
  			System.out.println(line);
  			}
//  			OutputStream out = process.getOutputStream();  			
//  			sender.sendMessage(out);
  			if (process.isAlive()) return true;
    
  		} catch (IOException iOException) {}
  			return false;
  	}
  	
  	
	public Iterable<String> onTabComplete(CommandSender arg0, String[] arg1) {
		if (arg1.length <= 1) {
			String[] d = Main.shDir.list();
			ArrayList<String> Collection = new ArrayList<String>(Arrays.asList(d));
			return  Collection;
		} else {
			ArrayList<String> Collection = new ArrayList<String>();
			return  Collection;
		}
	}
}
