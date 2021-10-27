package bungee.main;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import bungee.php.CommandsPHP;
import bungee.rcon.RunRcon;
import bungee.sh.CommandsSH;
import fr.orblazer.bungeeRcon.RconServer;
import io.netty.channel.Channel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
  
public class Main extends Plugin
{

    static final Logger logger = ProxyServer.getInstance().getLogger();
    private RconServer rconServer;
    
	public static String name = "TBungeeUtills";
	public static File path;
	public static File shDir;
	public static File phpDir;
	public static File rconConf;
	public static Configuration generalConf;
	public static Language lang;
	
	public void onEnable() {
				
		try {
			loadConfig();
		} catch (IOException e) {
			logger.info("§cError load configuration files.");
			e.printStackTrace();
			return;
		}
		
		if(generalConf.getBoolean("Rcon.enabled")) {
	        SocketAddress address = new InetSocketAddress(generalConf.getInt("Rcon.port"));
	        rconServer = new RconServer(this.getProxy());

	        logger.log(Level.INFO, Main.lang.getFild("rcon_bind", null) + " {0}...", address);
	        Channel channel = rconServer.bind(address).awaitUninterruptibly().channel();
	        
	        if (!channel.isActive()) {
	            logger.warning(Main.lang.getFild("rcon_error_bind", null));
	        }
		}

		getProxy().getPluginManager().registerListener(this, new Events());
		getProxy().getPluginManager().registerCommand(this, new Help("tbutills"));
		getProxy().getPluginManager().registerCommand(this, new RunRcon("rcon"));
		getProxy().getPluginManager().registerCommand(this, new CommandsPHP("php"));
		getProxy().getPluginManager().registerCommand(this, new CommandsSH("sh"));

    }
	
	public void onDisable() {
		
		if(generalConf.getBoolean("Rcon.enabled")) {
	        logger.info(Main.lang.getFild("rcon_server_stop", null));
	        rconServer.shutdown();
		}
		logger.info("§a"+ name + " disabled");
	}
	
	public void loadConfig() throws IOException 
	{
		generalConf = new Config(this).getConfig();
		lang = new Language(generalConf.getString("language"), this);
	}
}