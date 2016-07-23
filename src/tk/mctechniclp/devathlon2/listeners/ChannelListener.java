package tk.mctechniclp.devathlon2.listeners;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import tk.mctechniclp.devathlon2.ServerManager;

public class ChannelListener implements Listener {

	@EventHandler
	public void onReceivePluginMessage(PluginMessageEvent ev) {
		System.out.println(ev.getTag() + ":");
		DataInputStream in2 = new DataInputStream(new ByteArrayInputStream(ev.getData()));
		
		if(ev.getTag().equalsIgnoreCase("BungeeCord")) {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(ev.getData()));
			
			try {
				if(in.readUTF().equals("shutdown")) {
					System.out.println(BungeeCord.getInstance().getPlayer(ev.getSender().toString()).getServer().getInfo().getName());
					ServerManager.unregisterServer(BungeeCord.getInstance().getPlayer(ev.getSender().toString()).getServer().getInfo().getName());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
