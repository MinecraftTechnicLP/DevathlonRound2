package tk.mctechniclp.devathlon2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import com.google.common.io.Files;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerManager {
	/** TODO: Make configurable */
	private static int port = Main.getConfig().getInt("minPort");
	private static int maxPort = Main.getConfig().getInt("minPort");
	private static int ramPerServer = Main.getConfig().getInt("maxRAMPerServer");
	private static String host = Main.getConfig().getString("host");
	
	private static ArrayList<String> startingServers = new ArrayList<String>();
	private static HashMap<UUID, String> waitingPlayers = new HashMap<UUID, String>();
	
	public static void reconnectPlayer(ProxiedPlayer p, String serverName) {
		if(!ProxyServer.getInstance().getServers().containsKey(serverName)) {
			if(!startingServers.contains(serverName)) startServer(serverName);
			waitingPlayers.put(p.getUniqueId(), serverName);
		}
		
	}
	
	private static void startServer(String name) {
		startingServers.add(name);
		
		/** Start Bukkit Server */
		File serverDir = new File("../" + name);
		File templateDir = new File("../template");
		
		if(!serverDir.exists()) {
			serverDir.mkdir();
			addProperties(serverDir);
			copyDirectory(templateDir, serverDir);
		}
		
		
		ProcessBuilder pb = new ProcessBuilder("java", "-Xms" + ramPerServer + "m", "-jar", "spigot.jar");
		pb.directory(serverDir);
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/** Connect to BungeeCord */
		ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(name, new InetSocketAddress(host, port), "Servername: " + name, false);
		ProxyServer.getInstance().getServers().put(name, serverInfo);
		
		
		port++;
		startingServers.remove(name);
		
		Iterator<Entry<UUID, String>> iter = waitingPlayers.entrySet().iterator();
		
		while (iter.hasNext()) {
		    Entry<UUID, String> e = iter.next();
		    
		    if(e.getValue().equals(name)) {
				BungeeCord.getInstance().getPlayer(e.getKey()).connect(serverInfo);
				waitingPlayers.remove(e);
			}
		}
	}
	
	public static void unregisterServer(String name) {
		ProxyServer.getInstance().getServers().remove(name);
	}
	
	private static void copyDirectory(File from, File to) {
		for(File child : from.listFiles()) {
			File childTo = new File(to.toPath() + "/" + child.getName());
			
			if(child.isDirectory()) {
				childTo.mkdir();
				copyDirectory(child, childTo);
				continue;
			}
			
			try {
				Files.copy(child, childTo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void addProperties(File toDirectory) {
		try {
			FileInputStream in = new FileInputStream("../server.properties");
			Properties props = new Properties();
			props.load(in);
			in.close();
			
			FileOutputStream out = new FileOutputStream(toDirectory.toPath() + "/server.properties");
			props.setProperty("server-port", String.valueOf(port));
			props.store(out, null);
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
