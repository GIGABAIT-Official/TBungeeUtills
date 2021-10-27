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

public class Language {
	
	Configuration lang = null;
	
	public Language(String file, Plugin plugin) throws IOException 
	{
		File langDir = new File(Main.path, "language");
		if (!langDir.exists()) {
			langDir.mkdir();
			Main.logger.info("§aCreate language files");
			
			File langEN = new File(Main.path, "language" + File.separator + "en.yml");
			if (!langEN.exists()) {
				try {
					langEN.createNewFile();
					try (InputStream is = plugin.getResourceAsStream("language/en.yml");
						OutputStream os = new FileOutputStream(langEN)) {
						ByteStreams.copy(is, os);
					}
				} catch (IOException e) {
					throw new RuntimeException("§cUnable to create configuration file language", e);
				}
			}
		}
		File langFile = new File(Main.path, "language" + File.separator + file.trim() + ".yml");
		if(!langFile.exists()) {
			langFile = new File(Main.path, "language" + File.separator + "en.yml");
		}
		this.lang = ConfigurationProvider.getProvider(YamlConfiguration.class).load(langFile);
	}
	
	public String getFild(String key, String[] args)
	{	
		String value = this.lang.getString(key.trim()).replace("&", "§");
		
		if (args != null) {
			int f = args.length / 2;
			int i = 0; int a = 1;
			for (int x = 0; x < f; x++) {
				value = value.replace(args[i].trim(), args[a].trim());
				i = i + 2; a = a + 2;
			}
		}
		return value;
	}
}