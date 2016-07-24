package tk.mctechniclp.devathlon2.listeners;

import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import tk.mctechniclp.devathlon2.Main;
import tk.mctechniclp.devathlon2.ServerManager;

public class QuitListener implements Listener {

	@EventHandler
	public void onDisconnect(ServerDisconnectEvent ev) {
		if(ev.getTarget().getPlayers().size() > 0 || ev.getTarget().getName().equals(Main.getConfig().get("fallbackServerName"))) return;
		ServerManager.unregisterServer(ev.getTarget().getName());
	}

}
