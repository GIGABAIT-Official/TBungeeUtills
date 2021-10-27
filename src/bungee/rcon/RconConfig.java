package bungee.rcon;

import java.io.IOException;
import java.util.Collection;
import bungee.main.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class RconConfig {
	
	private Configuration config;
	private String server;
	
	public RconConfig(String key) {
		  		
		try {
			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(Main.rconConf);
			this.server = key;
		} catch (IOException e) {
			return;
		}
	}
	
	public static Collection<String> getTab() throws IOException{
		Configuration tmp = ConfigurationProvider.getProvider(YamlConfiguration.class).load(Main.rconConf);
		return tmp.getKeys();
	}
	
	public Collection<String> getServers() {
		return this.config.getKeys();
	}
	
	public boolean serverExist()
	{
		if(this.config.contains(this.server)) {
			return true;
		} else {
			return false;
		}
		
	} 
	
	public String getIP() {
		return (String) this.config.get(this.server + ".ip");
	}
	
	public Integer getPort() {
		return (int) this.config.get(this.server + ".port");
	}
	
	public String getPass() {
		return (String) this.config.get(this.server + ".pass");
	}
	
	public String getPerms() {
		return (String) this.config.get(this.server + ".perms");
	}
}
