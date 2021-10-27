package bungee.main;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class SaveLogs {

	private static String date;
	private static String time;
	
	public SaveLogs(String type, String data) 
	{
		DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		date = d.format(now);
		d = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		time = d.format(now);;
		
		if (type.equals("rcon_log")) {
			this.RconLog(data);
		} else if(type.equals("rcon_servers_log")){
			this.RconServersLog(data);
		}
	}
	
	public void RconLog(String data) 
	{
		if (Main.generalConf.getBoolean("Rcon.rcon_log")) {
			String logName = "log-" + date + ".log";
	        File logFile = new File(Main.path + File.separator + "logs" + File.separator + "rcon" , logName);
			if (!logFile.exists()) {
				try {
					logFile.createNewFile();
					
				} catch (IOException e) {
					throw new RuntimeException("§cUnable to create log file rcon.log", e);
				}
			}
			
	        try {
				BufferedWriter br = new BufferedWriter(new FileWriter(logFile, true));
				PrintWriter pw = new PrintWriter(br);
				pw.println("----------------------------------------------------Rcon connect log-------------------------------------------------------------");
				pw.println(time + data);
				pw.println("---------------------------------------------------------------------------------------------------------------------------------");
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void RconServersLog(String data) 
	{
		if (Main.generalConf.getBoolean("rcon_servers_log")) {
			String logName = "log-" + date + ".log";
	        File logFile = new File(Main.path + File.separator + "logs" + File.separator + "rcon" , logName);
			if (!logFile.exists()) {
				try {
					logFile.createNewFile();
					
				} catch (IOException e) {
					throw new RuntimeException("§cUnable to create log file rcon.log", e);
				}
			}
			
	        try {
				BufferedWriter br = new BufferedWriter(new FileWriter(logFile, true));
				PrintWriter pw = new PrintWriter(br);
				pw.println("--------------------------------------------------------Rcon server send log-----------------------------------------------------");
				pw.println(time + data);
				pw.println("---------------------------------------------------------------------------------------------------------------------------------");
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
