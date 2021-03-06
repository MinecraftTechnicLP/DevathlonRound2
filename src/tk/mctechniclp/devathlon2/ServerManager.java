package tk.mctechniclp.devathlon2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.io.Files;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerManager {
	private static int minPort = Main.getConfig().getInt("minPort");
	private static int maxPort = Main.getConfig().getInt("maxPort");
	private static List<Integer> blockedPorts = Main.getConfig().getIntList("blockedPorts");
	private static int ramPerServer = Main.getConfig().getInt("maxRAMPerServer");
	private static int totalRam = Main.getConfig().getInt("maxTotalRAM");
	private static int usedRam = 0;
	private static String host = Main.getConfig().getString("host");
	
	private static ArrayList<String> startingServers = new ArrayList<String>();
	private static HashMap<UUID, String> waitingPlayers = new HashMap<UUID, String>();
	private static HashMap<String, Integer> ports = new HashMap<String, Integer>();
	
	
	public static void reconnectPlayer(ProxiedPlayer p, String serverName) {
		if(!ProxyServer.getInstance().getServers().containsKey(serverName)) {
			waitingPlayers.put(p.getUniqueId(), serverName);
			if(!startingServers.contains(serverName)) {
				startServer(serverName);
			}
		} else {
			p.connect(ProxyServer.getInstance().getServers().get(serverName));
		}
	}
	
	private static void startServer(final String name) {
		startingServers.add(name);
		usedRam += ramPerServer;
		
		int port = getPortFor(name);
		if(port == -1) return;
		
		/** Start Bukkit Server */
		File serverDir = new File("../" + name);
		File templateDir = new File("../template");
		
		if(!serverDir.exists()) {
			serverDir.mkdir();
			addProperties(serverDir, port);
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
		final ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(name, new InetSocketAddress(host, port), ChatColor.translateAlternateColorCodes('&', Main.getConfig().getString("MOTD").replace("{serverName}", name)), false);
		ProxyServer.getInstance().getServers().put(name, serverInfo);
		
		startingServers.remove(name);
		
		
		/** Connect waiting Players*/
		BungeeCord.getInstance().getScheduler().schedule(Main.getInstance(), new Runnable() {

			@Override
			public void run() {
				Iterator<Entry<UUID, String>> iter = waitingPlayers.entrySet().iterator();
				
				while (iter.hasNext()) {
				    Entry<UUID, String> e = iter.next();
				    
				    if(e.getValue().equals(name)) {
						BungeeCord.getInstance().getPlayer(e.getKey()).connect(serverInfo);
						waitingPlayers.remove(e);
					}
				}
			}
			
		}, 10, TimeUnit.SECONDS);

	}
	
	public static void unregisterServer(String name) {
		ProxyServer.getInstance().getServers().remove(name);
		usedRam -= ramPerServer;
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
	
	private static void addProperties(File toDirectory, int port) {
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
	
	private static int getPortFor(String serverName) {
		if(ports.containsKey(serverName)) return ports.get(serverName);
		
		for(int i = minPort; i <= maxPort; i++) {
			if(ports.containsValue(i) || blockedPorts.contains(i)) continue;
			ports.put(serverName, i);
			return i;
		}
		System.err.println("No port availabel! Maybe the configured range is to small?");
		return -1;
	}
	
	public static boolean hasRAMForNextServer() {
		return usedRam + ramPerServer <= totalRam;
	}
	
	public static boolean isRunningServer(String name) {
		return BungeeCord.getInstance().getServers().containsKey(name);
	}
	
}
