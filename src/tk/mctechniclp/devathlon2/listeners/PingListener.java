package tk.mctechniclp.devathlon2.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import tk.mctechniclp.devathlon2.Main;

public class PingListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPing(ProxyPingEvent ev) {
		String[] parts = ev.getConnection().getVirtualHost().getHostString().split("\\.");
		if (parts.length != 3) return;
		
		ServerPing ping = ev.getResponse();
		ping.setDescription(ChatColor.translateAlternateColorCodes('&', Main.getConfig().getString("MOTD").replace("{serverName}", parts[0])));
	}

}
