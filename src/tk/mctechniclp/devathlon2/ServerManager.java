package tk.mctechniclp.devathlon2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerManager {
	/** TODO: Make configurable */
	private static int port = 25568;
	private static String host = "localhost";
	
	public static void startServer(String name, String motd) {
		/** Start Bukkit Server */
		
		File f = new File("../" + name);
		if(!f.exists()) {
			updatePort();
			f.mkdir();
		}
		
		
		/** Connect to BungeeCord */
		ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(name, new InetSocketAddress(host, port), motd, false);
		ProxyServer.getInstance().getServers().put(name, serverInfo);
		port++;
	}
	
	private static void updatePort() {
		try {
		FileInputStream in = new FileInputStream("../template/server.properties");
		Properties props = new Properties();
		props.load(in);
		
		in.close();
		FileOutputStream out = new FileOutputStream("../template/server.properties");
		props.setProperty("server-port", String.valueOf(port));
		props.store(out, null);
		out.close();
		
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
