package tk.mctechniclp.devathlon2;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import tk.mctechniclp.devathlon2.listeners.JoinListener;

public class Main extends Plugin {
	
	public static Main INSTANCE;
	
	@Override
	public void onEnable() {
		INSTANCE = this;
		System.out.println(getDataFolder());
		registerListeners();
		registerCommands();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	
	private void registerListeners() {
		BungeeCord.getInstance().getPluginManager().registerListener(this, new JoinListener());
	}
	
	private void registerCommands() {
		
	}
}
