package bungee.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Config {
	
    private final File configFile;

    public Config(Plugin plugin) {
    	
    	Main.path = plugin.getDataFolder();
        if (!Main.path.exists()) {
            Main.path.mkdir();
        }
        configFile = new File(Main.path, "config.yml");
        if (!configFile.exists()) {
			try {
				configFile.createNewFile();
				Main.logger.info("§aCreate config file");
				try (InputStream is = plugin.getResourceAsStream("config.yml");
					OutputStream os = new FileOutputStream(configFile)) {
					ByteStreams.copy(is, os);
				}
			} catch (IOException e) {
				throw new RuntimeException("§cUnable to create configuration file config", e);
			}
        }
        
        Main.rconConf = new File(Main.path, "rcon.yml");
		if (!Main.rconConf.exists()) {
			try {
				Main.rconConf.createNewFile();
				try (InputStream is = plugin.getResourceAsStream("rcon.yml");
					OutputStream os = new FileOutputStream(Main.rconConf)) {
					ByteStreams.copy(is, os);
				}
			} catch (IOException e) {
				throw new RuntimeException("§cUnable to create configuration file rcon.yml", e);
			}
		}
        
		Main.shDir = new File(Main.path, "sh");
		if (!Main.shDir.exists()) {
			Main.shDir.mkdir();
		}
		Main.phpDir = new File(Main.path, "php");
		if (!Main.phpDir.exists()) {
			Main.phpDir.mkdir();
		}
		
		File logs = new File(Main.path, "logs");
		if (!logs.exists()) {
			logs.mkdir();
		}
		
		File logsRcon = new File(Main.path, "logs" + File.separator + "rcon");
		if (!logsRcon.exists()) {
			logsRcon.mkdir();
		}
    }
    
    public Configuration getConfig() throws IOException {
    	return ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
    }
}
