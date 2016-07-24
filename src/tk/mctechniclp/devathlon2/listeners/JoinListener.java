package tk.mctechniclp.devathlon2.listeners;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import tk.mctechniclp.devathlon2.ServerManager;

public class JoinListener implements Listener {
	
	
	@EventHandler
	public void onJoin(PostLoginEvent ev) {
		String[] parts = ev.getPlayer().getPendingConnection().getVirtualHost().getHostString().split("\\.");
		
		if(parts.length == 3) {
			if(ServerManager.hasRAMForNextServer() || ServerManager.isRunningServer(parts[0])) {
				ServerManager.reconnectPlayer(ev.getPlayer(), parts[0].toLowerCase());
			} else {
				ev.getPlayer().disconnect();
			}
		}
	}
	
}
