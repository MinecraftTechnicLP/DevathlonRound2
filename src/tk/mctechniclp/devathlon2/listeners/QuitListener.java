package tk.mctechniclp.devathlon2.listeners;

import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class QuitListener implements Listener {

	@EventHandler
	public void onKick(ServerKickEvent ev) {
		System.out.println("KICKED: " + ev.getKickReason());
	}

}
