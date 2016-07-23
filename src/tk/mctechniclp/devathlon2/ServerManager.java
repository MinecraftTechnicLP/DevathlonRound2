package tk.mctechniclp.devathlon2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

import com.google.common.io.Files;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerManager {
	/** TODO: Make configurable */
	private static int port = 25568;
	private static int ramPerServer = 512;
	private static String host = "localhost";
	
	
	
	public static void startServer(String name, String motd) {
		/** Connect to BungeeCord */
		ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(name, new InetSocketAddress(host, port), motd, false);
		ProxyServer.getInstance().getServers().put(name, serverInfo);
		
		
		
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
		
		port++;
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
