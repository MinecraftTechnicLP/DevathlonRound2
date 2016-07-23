package tk.mctechniclp.devathlon2;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.logging.Handler;

import com.google.common.collect.Lists;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

public class CustomBungeeCord extends BungeeCord {

	public CustomBungeeCord() throws IOException {
		super();
	}

	@Override
	public void stop(final String reason) {
		new Thread("Shutdown Thread") {
			// @SuppressFBWarnings({ "DM_EXIT" })
			public void run() {
				CustomBungeeCord.this.isRunning = false;

				CustomBungeeCord.this.stopListeners();
				CustomBungeeCord.this.getLogger().info("Closing pending connections");

				CustomBungeeCord.this.getConnectionLock().readLock().lock();
				/*try {
					CustomBungeeCord.this.getLogger().log(Level.INFO, "Disconnecting {0} connections",
							Integer.valueOf(CustomBungeeCord.this.getConnections().size()));
					for (UserConnection user : CustomBungeeCord.this.getConnections().values()) {
						user.disconnect(this.val$reason);
					}
				} finally {
					CustomBungeeCord.this.connectionLock.readLock().unlock();
				}*/

				CustomBungeeCord.this.getLogger().info("Closing IO threads");
				CustomBungeeCord.this.eventLoops.shutdownGracefully();
				try {
					CustomBungeeCord.this.eventLoops.awaitTermination(9223372036854775807L, TimeUnit.NANOSECONDS);
				} catch (InterruptedException localInterruptedException) {
				}
				if (CustomBungeeCord.this.getReconnectHandler() != null) {
					CustomBungeeCord.this.getLogger().info("Saving reconnect locations");
					CustomBungeeCord.this.getReconnectHandler().save();
					CustomBungeeCord.this.getReconnectHandler().close();
				}
				CustomBungeeCord.this.getSaveThread().cancel();
				CustomBungeeCord.this.getMetricsThread().cancel();

				CustomBungeeCord.this.getLogger().info("Disabling plugins");
				for (Plugin plugin : Lists.reverse(new ArrayList<Plugin> (CustomBungeeCord.this.getPluginManager().getPlugins()))) {
					try {
						plugin.onDisable();
						for (Handler handler : plugin.getLogger().getHandlers()) {
							handler.close();
						}
					} catch (Throwable t) {
						CustomBungeeCord.this.getLogger().log(java.util.logging.Level.SEVERE, "Exception disabling plugin " + plugin.getDescription().getName(), t);
					}
					CustomBungeeCord.this.getScheduler().cancel(plugin);
					plugin.getExecutorService().shutdownNow();
				}

				CustomBungeeCord.this.getLogger().info("Thank you and goodbye");
				int i$;
				for (Handler handler : CustomBungeeCord.this.getLogger().getHandlers()) {
					handler.close();
				}
				System.exit(0);
			}
		}.start();
	}
	
	
	private ReadWriteLock getConnectionLock() {
		try {
			Field f = BungeeCord.class.getDeclaredField("connectionLock");
			f.setAccessible(true);
			return (ReadWriteLock) f.get(this);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Timer getSaveThread() {
		try {
			Field f = BungeeCord.class.getDeclaredField("saveThread");
			f.setAccessible(true);
			return (Timer) f.get(this);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Timer getMetricsThread() {
		try {
			Field f = BungeeCord.class.getDeclaredField("metricsThread");
			f.setAccessible(true);
			return (Timer) f.get(this);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
