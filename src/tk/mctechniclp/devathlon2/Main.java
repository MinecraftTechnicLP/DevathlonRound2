package tk.mctechniclp.devathlon2;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import tk.mctechniclp.devathlon2.listeners.ChannelListener;
import tk.mctechniclp.devathlon2.listeners.JoinListener;
import tk.mctechniclp.devathlon2.listeners.PingListener;
import tk.mctechniclp.devathlon2.listeners.QuitListener;

public class Main extends Plugin {
	
	private static Main instance;
	private static Configuration config;
	
	@Override
	public void onEnable() {
		instance = this;
		initConfig();
		registerListeners();
		registerCommands();
		
		
		/*try {
			Field f = ProxyServer.class.getDeclaredField("instance");
			f.setAccessible(true);
			f.set(null, new CustomBungeeCord());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}*/
	}
	
	@Override
	public void onDisable() {
		
	}
	
	
	private void registerListeners() {
		BungeeCord.getInstance().getPluginManager().registerListener(this, new JoinListener());
		BungeeCord.getInstance().getPluginManager().registerListener(this, new PingListener());
		BungeeCord.getInstance().getPluginManager().registerListener(this, new ChannelListener());
		BungeeCord.getInstance().getPluginManager().registerListener(this, new QuitListener());
	}
	
	private void registerCommands() {
		
	}
	
	private void initConfig() {
		try {
			if(!getDataFolder().exists()) getDataFolder().mkdir();
			
			File file = new File(getDataFolder().getPath() + "/config.yml");
			if(!file.exists()) {
				file.createNewFile();
				config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
				
				config.set("minPort", 25668);
				config.set("maxPort", 30000);
				config.set("blockedPorts", Arrays.asList(new int[] {12345, 23456}));
				config.set("socketPort", 1337);
				config.set("host", "localhost");
				config.set("maxRAMPerServer", 512);
				config.set("maxTotalRAM", 2048);
				config.set("MOTD", "&bSelected Server: &6&l{serverName}");
				config.set("errorMOTD", "&4The selected Server: &l&c{serverName} &4is not online\nand no new server can be run due lag of memory");
				config.set("errorVersion", "Server is offline");
				config.set("fallbackServerName", "lobby");
				
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
			} else {
				config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public static Configuration getConfig() {
		return config;
	}
}
