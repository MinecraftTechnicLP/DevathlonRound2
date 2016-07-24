# DevathlonRound2

Project for the second round of [GommeHD.net's Devathlon 2016] (https://www.gommehd.net/forum/threads/devathlon-nummer-3-sei-dabei.501442/)

## TODO:

- [x] Create a system to start servers dynamically
- [x] Create a system to stop servers dynamically
- [x] Create servers dynamically from the used subdomain
- [ ] Servers have to stay online, even if the proxy is restarting
     - [x] Tried using reflection to modify BungeeCords stop() method
     - [x] Tried getting an outgoing kick packet from BungeeCord
     - [x] Tried getting incoming packets on the spigot server to cancel kick
     - [x] Tried using all kinds of events to prevent kicking
     - [x] Tried listening to the BungeeCord channel for kicking
     - [x] Tried decompiling disconnec0() method, failed to decompile unsafe
- [x] The MOTD has to work, even if the server is offline
- [x] Do not create servers if hardware is already used

## Set up

1. Put this plugin in your BungeeCord plugin folder.
2. Put the DevathlonRound2Bukkit plugin in the plugin folders of your existing servers.
3. Create a folder called "template" in the same folder your proxy server folder is in.
4. Put a spigot.jar file, a spigot.yml file with BungeeCord set true, an eula.txt file with accepted set to true and a plugins folder with the DevathlonRound2Bukkit plugin into the template folder.
5. You can add other plugins, worlds or configuration files in the template folder, they will automatically be installed on all new servers.
6. Put a server.properties file with online-mode set to false in the same folder as your proxy servers folder an the template folder are in.
7. When you launch the server for the first time with this plugin installed, it will generate a folder called DevathlonRound2 will be generated in your proxys plugin folder. Put a server icon of the size 64x64 pixel for joinable servers called success.png and one unjoinable servers called fail.png in here. Additionally you will find a file called config.yml you can set the range of ports used for the dynamic servers, banned ports, MOTDs, the maximum RAM each server can have, the maximum RAM all dynamically started servers should be able to use in total and other important things here.
8. Make WILDCARD entry on your DNS-Server to point all subdomains to the BungeeCord server
9. Enjoy your Server running out of memory :joy: