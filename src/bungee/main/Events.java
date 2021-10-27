package bungee.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Events implements Listener {
	
	private void sendCommand(ProxiedPlayer player, String cmd)
	{ 

		if(cmd.contains("[console]")) {
			ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), cmd.replace("[console]", "").trim());
		} else {
			ProxyServer.getInstance().getPluginManager().dispatchCommand(player, cmd.trim());
		}
	}
	
	private List<String> commandsPrepare (String event, String player, String server)
	{
		List<String> cmd = new ArrayList<String>();
		List<String> events = Main.generalConf.getStringList("Events." + event + ".commands");

		for(String tmp : events) {
			cmd.add(tmp.replace("&", "§").replace("{player}", player).replace("{server}", server));
		}
		return cmd;
	}
	
	
    
	
    @EventHandler
    public void onJoin(final PostLoginEvent e)
    {
        Thread t = new Thread(new Runnable(){

            @Override
            public void run()
            {
            	String event = "on_join_commands";
            	
            	if (Main.generalConf.getBoolean("Events." + event + ".enabled")) {
                	ProxiedPlayer player = e.getPlayer();
                	String server = "";
                	
                	
					for(String tmp : commandsPrepare(event, player.getName(), server)) 
                	{
                		if(tmp.contains("[delay]")) {
                		    int i = Integer.parseInt(tmp.replace("[delay]", "").trim());
                		    try {
								TimeUnit.SECONDS.sleep(i);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
                		    continue;
                		}
                		sendCommand(player, tmp);
                	}
            	}
            }});
            t.setPriority(Thread.MAX_PRIORITY);
            t.start();
    }
    
    
    @EventHandler
    public void onLeave(final PlayerDisconnectEvent e)
    {
        Thread t = new Thread(new Runnable(){

            @Override
            public void run()
            {
            	String event = "on_leave_commands";
            	
            	if (Main.generalConf.getBoolean("Events." + event + ".enabled")) {
                	ProxiedPlayer player = e.getPlayer();
                	String server = player.getServer().getInfo().getName();
                	
                	
					for(String tmp : commandsPrepare(event, player.getName(), server)) 
                	{
                		if(tmp.contains("[delay]")) {
                		    int i = Integer.parseInt(tmp.replace("[delay]", "").trim());
                		    try {
								TimeUnit.SECONDS.sleep(i);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
                		    continue;
                		}
                		sendCommand(player, tmp);
                	}
            	}
            }});
            t.setPriority(Thread.MAX_PRIORITY);
            t.start();
    }
    
    @EventHandler
    public void ServerSwitch(final ServerSwitchEvent e)
    {
        Thread t = new Thread(new Runnable(){

            @Override
            public void run()
            {
            	String event = "on_server_switch";
            	
            	if (Main.generalConf.getBoolean("Events." + event + ".enabled")) {
                	ProxiedPlayer player = e.getPlayer();
                	String server = player.getServer().getInfo().getName();
                	
                	
					for(String tmp : commandsPrepare(event, player.getName(), server)) 
                	{
                		if(tmp.contains("[delay]")) {
                		    int i = Integer.parseInt(tmp.replace("[delay]", "").trim());
                		    try {
								TimeUnit.SECONDS.sleep(i);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
                		    continue;
                		}
                		sendCommand(player, tmp);
                	}
            	}
            }});
            t.setPriority(Thread.MAX_PRIORITY);
            t.start();
    } 
    

//    @EventHandler
//    public void onKick(final ServerKickEvent serverKickEvent) {}
//
//    @EventHandler
//    public void onStop(final ServerStopEvent serverStopEvent) {}
//
//    @EventHandler
//    public void onServerJoin(final ServerConnectEvent serverConnectEvent) {}
//
//   @EventHandler
//    public void onServerLeave(final ServerDisconnectEvent serverDisconnectEvent) {}
//
//    @EventHandler
//    public void onServerConnected(final ServerConnectedEvent serverConnectedEvent) {}

}


