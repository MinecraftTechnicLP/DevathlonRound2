package tk.mctechniclp.devathlon2.listeners;

import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import tk.mctechniclp.devathlon2.ServerManager;

public class JoinListener implements Listener {
	
	
	@EventHandler
	public void onJoin(PreLoginEvent ev) {
		ServerManager.startServer("test", "test server");
	}
	
}
