package tk.mctechniclp.devathlon2.listeners;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import tk.mctechniclp.devathlon2.Main;
import tk.mctechniclp.devathlon2.ServerManager;

public class PingListener implements Listener {
	
	@EventHandler
	public void onPing(ProxyPingEvent ev) {
		String[] parts = ev.getConnection().getVirtualHost().getHostString().split("\\.");
		if (parts.length != 3) return;
		
		ServerPing ping = ev.getResponse();
		if(ServerManager.hasRAMForNextServer() || ServerManager.isRunningServer(parts[0])) {
			ping.setDescription(ChatColor.translateAlternateColorCodes('&', Main.getConfig().getString("MOTD").replace("{serverName}", parts[0])));
			
			try {
				ping.setFavicon(Favicon.create(ImageIO.read(new File(Main.getInstance().getDataFolder() + "/success.png"))));
			} catch (IOException e) {
				System.out.println("No favicon found. To use dynamic favicons, create a 64x64 pixel image named success.png in the DevathlonRound2 folder.");
			}
		} else {
			ping.setDescription(ChatColor.translateAlternateColorCodes('&', Main.getConfig().getString("errorMOTD").replace("{serverName}", parts[0])));
			Protocol version = ping.getVersion();
			version.setName((Main.getConfig().getString("errorVersion").replace("{serverName}", parts[0])));
			version.setProtocol(-1);
			ping.setVersion(version);
			
			try {
				ping.setFavicon(Favicon.create(ImageIO.read(new File(Main.getInstance().getDataFolder() + "/fail.png"))));
			} catch (IOException e) {
				System.out.println("No favicon found. To use dynamic favicons, create a 64x64 pixel image named fail.png in the DevathlonRound2 folder.");
			}
		}
	}
}
