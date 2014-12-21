package com.gmail.br45entei.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scheduler.BukkitScheduler;

import com.gmail.br45entei.main.yml.YamlMgmtClass;

/** @author Brian_Entei */
public class UUIDMasterList implements Listener {
	
	public void DEBUG(String str) {
		if(this.showDebugMsgs) {
			Main.sendConsoleMessage(this.pluginName + Main.formatColorCodes("&eDebug: " + str));
		}
	}
	
	public Main						plugin;
	public PluginDescriptionFile	pdffile;
	public final String				rwhite				= ChatColor.RESET + "" + ChatColor.WHITE;
	public final ChatColor			aqua				= ChatColor.AQUA;
	public final ChatColor			black				= ChatColor.BLACK;
	public final ChatColor			blue				= ChatColor.BLUE;
	public final ChatColor			bold				= ChatColor.BOLD;
	public final ChatColor			daqua				= ChatColor.DARK_AQUA;
	public final ChatColor			dblue				= ChatColor.DARK_BLUE;
	public final ChatColor			dgray				= ChatColor.DARK_GRAY;
	public final ChatColor			dgreen				= ChatColor.DARK_GREEN;
	public final ChatColor			dpurple				= ChatColor.DARK_PURPLE;
	public final ChatColor			dred				= ChatColor.DARK_RED;
	public final ChatColor			gold				= ChatColor.GOLD;
	public final ChatColor			gray				= ChatColor.GRAY;
	public final ChatColor			green				= ChatColor.GREEN;
	public final ChatColor			italic				= ChatColor.ITALIC;
	public final ChatColor			lpurple				= ChatColor.LIGHT_PURPLE;
	public final ChatColor			magic				= ChatColor.MAGIC;
	public final ChatColor			red					= ChatColor.RED;
	public final ChatColor			reset				= ChatColor.RESET;
	public final ChatColor			striken				= ChatColor.STRIKETHROUGH;
	public final ChatColor			underline			= ChatColor.UNDERLINE;
	public final ChatColor			white				= ChatColor.WHITE;
	public final ChatColor			yellow				= ChatColor.YELLOW;
	public final String				pluginName			= this.white + "[" + this.gold + "UUID Master List" + this.white + "] ";
	public Server					server;
	public ConsoleCommandSender		console;
	public BukkitScheduler			scheduler;
	public String					dataFolderName		= "";
	public File						dataFolder			= null;
	public boolean					enabled				= false;
	
	public boolean					showDebugMsgs;
	public String					configVersion		= "";
	
	public boolean					YamlsAreLoaded		= false;
	public FileConfiguration		config;
	public File						configFile			= null;
	public String					configFileName		= "uuidConfig.yml";
	
	public FileConfiguration		uuidConfig;
	public File						uuidConfigFile		= null;
	public String					uuidConfigFileName	= "UUID_MASTER_LIST.yml";
	
	public Main getPlugin() {
		return this.plugin;
	}
	
	private final ArrayList<String[]>	uuidMasterList	= new ArrayList<>();
	
	public ArrayList<String[]> getPlayerUUIDList() {
		return this.uuidMasterList;
	}
	
	public ArrayList<UUID> getAllListedUUIDS() {
		ArrayList<UUID> rtrn = new ArrayList<>();
		for(String[] entry : this.uuidMasterList) {
			rtrn.add(UUID.fromString(entry[0]));
		}
		return rtrn;
	}
	
	private void printMasterListToScreen() {
		int index = 0;
		for(String[] entry : this.uuidMasterList) {
			this.sendConsoleMessage(this.pluginName + "&a[" + index + "/" + (this.uuidMasterList.size() - 1) + "]:&z&auuid: \"&f" + entry[0] + "&r&a\";&z&aname: \"&f" + entry[1] + "&r&a\"");
		}
		index++;
	}
	
	private boolean loadMasterListFromConfig() {
		boolean success = false;
		ConfigurationSection section = this.uuidConfig.getConfigurationSection("UUID_LIST");
		if(section == null) {
			section = this.uuidConfig.createSection("UUID_LIST");
		}
		Map<String, Object> configList = section.getValues(false);
		for(Map.Entry<String, Object> entry : configList.entrySet()) {
			String uuid = entry.getKey().replaceAll("_", "-");//UUID
			String name = (String) entry.getValue();//String
			this.uuidMasterList.add(new String[] {uuid, name});
		}
		return success;
	}
	
	private boolean saveMasterListToConfig() {
		this.DEBUG("&0_____&cpublic boolean &6saveMasterListToConfig&f() {");
		boolean success = true;
		ConfigurationSection section = this.uuidConfig.getConfigurationSection("UUID_LIST");
		if(section == null) {
			section = this.uuidConfig.createSection("UUID_LIST");
		}
		try {
			for(String[] curEntry : this.uuidMasterList) {
				String uuid = curEntry[0].replaceAll("-", "_");
				String name = curEntry[1];
				MemorySection.createPath(section, uuid);
				this.uuidConfig.set("UUID_LIST." + uuid, name);
			}
			this.uuidConfig.save(this.uuidConfigFile);
		} catch(Exception e) {
			e.printStackTrace();
			success = false;
		}
		this.DEBUG("&0_____&f}");
		return success;
	}
	
	public String getPlayerNameFromUUID(UUID uuid) {
		return this.getPlayerNameFromUUID(uuid.toString());
	}
	
	public String getPlayerNameFromUUID(String uuid) {
		int uuidIndexInList = this.getUUIDIndexInUUIDList(uuid);
		if(uuidIndexInList != -1) {
			String[] entry = this.uuidMasterList.get(uuidIndexInList);
			if(entry != null) {
				return entry[1];
			}
		}
		return "";
	}
	
	public int getUUIDIndexInUUIDList(UUID uuid) {
		return this.getUUIDIndexInUUIDList(uuid.toString());
	}
	
	public int getUUIDIndexInUUIDList(String uuid) {
		int index = 0;
		for(String[] curEntry : this.uuidMasterList) {
			if(curEntry[0].equals(uuid)) {
				return index;
			}
			index++;
		}
		return -1;
	}
	
	public int getPlayerIndexInUUIDList(Player player) {
		int index = 0;
		for(String[] curEntry : this.uuidMasterList) {
			if(curEntry[0].equals(player.getUniqueId().toString())) {
				return index;
			}
			index++;
		}
		//The player hasn't been added yet, let's add them now:
		String[] newEntry = new String[] {player.getUniqueId().toString(), player.getName()};
		this.uuidMasterList.add(newEntry);
		return this.uuidMasterList.size() - 1;//Returns the most recently added entry by index.
		//return -1;
	}
	
	private void updatePlayerNameInUUIDList(Player player) {
		String[] newEntry = new String[] {player.getUniqueId().toString(), player.getName()};
		int playerIndexInList = this.getPlayerIndexInUUIDList(player);
		if(playerIndexInList != -1) {
			this.uuidMasterList.set(playerIndexInList, newEntry);
		} else if(Bukkit.getServer().getOnlineMode()) {
			this.uuidMasterList.add(newEntry);
		}
	}
	
	public String getPlayerNameFromUUIDList(Player player) {
		int playerIndexInList = this.getPlayerIndexInUUIDList(player);
		String[] playerEntry = this.uuidMasterList.get(playerIndexInList);
		return playerEntry[1];
	}
	
	public UUID getUUIDFromPlayerName(String playerName) {
		for(String[] curEntry : this.uuidMasterList) {
			if(curEntry[1].equalsIgnoreCase(playerName)) {
				return UUID.fromString(curEntry[0]);
			}
		}
		try {
			List<String> names = new ArrayList<>();
			names.add(playerName);
			UUIDFetcher fetch = new UUIDFetcher(names);
			Map<String, UUID> uuidMap = fetch.call();
			UUID rtrn = null;
			for(Map.Entry<String, UUID> entry : uuidMap.entrySet()) {
				int index = this.getUUIDIndexInUUIDList(entry.getValue());
				String[] newEntry = new String[] {entry.getValue().toString(), entry.getKey()};
				if(index == -1) {
					this.uuidMasterList.add(newEntry);
				} else {
					this.uuidMasterList.set(index, newEntry);
				}
				Main.sendConsoleMessage(Main.pluginName + "&aDownloaded and saved the UUID and username of a player who has never logged in:");
				Main.sendConsoleMessage(Main.pluginName + "&aUUID: \"&f" + newEntry[0] + "&r&a\"; Last Known Username: \"&f" + newEntry[1] + "&r&a\";!");
				rtrn = entry.getValue();
			}
			this.saveMasterListToConfig();
			return rtrn;
		} catch(Throwable ignored) {
		}
		return null;//uuid;
	}
	
	/** @param playerName
	 * @return The String UUID from the playername stored in the arraylist if
	 *         any, or an empty string(""). */
	public String getUUIDStringFromPlayerName(String playerName) {
		//int index = 0;
		for(String[] curEntry : this.uuidMasterList) {
			if(curEntry[1].equalsIgnoreCase(playerName)) {
				return curEntry[0];
			}
			//index++;
		}
		return "";
	}
	
	/** @param player
	 * @return The UUID listed in the database under the given player's name if
	 *         any, otherwise the player's UUID. */
	public String getWhatUUIDToUseForPlayer(Player player) {
		String uuid = this.getUUIDStringFromPlayerName(player.getName());
		if(uuid.isEmpty() == false) {
			return uuid;
		}
		return player.getUniqueId().toString();
	}
	
	public ArrayList<String> getAllOfflinePlayerNamesFromList() {
		ArrayList<String> rtrn = new ArrayList<>();
		for(String[] curEntry : this.uuidMasterList) {
			String playerName = curEntry[1];
			if(Main.getPlayer(curEntry[1]) == null) {
				rtrn.add(playerName);
			}
		}
		return rtrn;
	}
	
	public ArrayList<String> getAllOnlinePlayerNamesFromList() {
		ArrayList<String> rtrn = new ArrayList<>();
		for(String[] curEntry : this.uuidMasterList) {
			String playerName = curEntry[1];
			if(Main.getPlayer(curEntry[1]) != null) {
				rtrn.add(playerName);
			}
		}
		return rtrn;
	}
	
	public UUIDMasterList(Main plugin) {
		this.plugin = plugin;
	}
	
	public void onEnable() {
		this.pdffile = Main.pdffile;
		this.server = Bukkit.getServer();
		this.server.getPluginManager().registerEvents(this, this.plugin);
		this.console = this.server.getConsoleSender();
		this.scheduler = this.server.getScheduler();
		this.dataFolder = new File(this.plugin.getDataFolder().getParentFile(), "UUIDMasterList");
		if(!(this.dataFolder.exists())) {
			this.dataFolder.mkdir();
		}
		try {
			this.dataFolderName = this.dataFolder.getAbsolutePath();
		} catch(SecurityException e) {
			e.printStackTrace();
		}
		//loadconfig:
		boolean successfulLoad = this.LoadConfig();
		if(!successfulLoad) {
			this.sendConsoleMessage(this.pluginName + "&eSomething went wrong when loading the configuration files! Please check through the server log for details.");
		}
		try {
			this.loadMasterListFromConfig();
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.sendConsoleMessage(this.pluginName + "&eVersion " + this.pdffile.getVersion() + " is now enabled.");
		this.enabled = true;
	}
	
	public void onDisable() {
		this.saveMasterListToConfig();
		this.sendConsoleMessage(this.pluginName + "&eVersion " + this.pdffile.getVersion() + " is now disabled.");
		this.enabled = false;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent evt) {
		this.updatePlayerNameInUUIDList(evt.getPlayer());
		this.saveMasterListToConfig();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent evt) {
		this.updatePlayerNameInUUIDList(evt.getPlayer());
		this.saveMasterListToConfig();
	}
	
	public boolean LoadConfig() {
		this.plugin.saveDefaultConfig();
		this.configFile = new File(this.dataFolder, this.configFileName);
		this.config = new YamlConfiguration();
		this.uuidConfigFile = new File(this.dataFolder, this.uuidConfigFileName);
		this.uuidConfig = new YamlConfiguration();
		try {
			this.loadResourceFiles();
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.YamlsAreLoaded = this.reloadFiles(true);
		if(this.YamlsAreLoaded == true) {
			this.DEBUG(this.pluginName + "&aAll YAML Configration Files loaded successfully!");
		} else {
			this.sendConsoleMessage(this.pluginName + "&cError: Some YAML Files failed to load successfully! Check the server log or \"" + this.dataFolderName + "\\crash-reports.txt\" to solve the problem.");
		}
		return this.YamlsAreLoaded;
	}
	
	private void loadResourceFiles() throws Exception {
		if(!this.configFile.exists()) {
			this.configFile.getParentFile().mkdirs();
			this.configFile = YamlMgmtClass.getResourceFromStreamAsFile(this.dataFolder, this.configFileName);
		}
		if(!this.uuidConfigFile.exists()) {
			this.uuidConfigFile.getParentFile().mkdirs();
			this.uuidConfigFile = YamlMgmtClass.getResourceFromStreamAsFile(this.dataFolder, this.uuidConfigFileName);
		}
	}
	
	private boolean reloadFiles(boolean ShowStatus) {
		this.YamlsAreLoaded = false;
		boolean loadedAllVars = false;
		String unloadedFiles = "\"";
		Exception e1 = null;
		try {
			this.config.load(this.configFile);
		} catch(Exception e) {
			e1 = e;
			unloadedFiles += this.configFileName + "\" ";
		}
		Exception e2 = null;
		try {
			this.uuidConfig.load(this.uuidConfigFile);
		} catch(Exception e) {
			e2 = e;
			unloadedFiles += this.uuidConfigFileName + "\" ";
		}
		try {
			if(unloadedFiles.equals("\"")) {
				this.YamlsAreLoaded = true;
				loadedAllVars = this.loadYamlVariables();
				if(loadedAllVars == true) {
					if(ShowStatus) {
						this.sendConsoleMessage(this.pluginName + "&aAll of the yaml configuration files loaded successfully!");
					}
				} else {
					if(ShowStatus) {
						this.sendConsoleMessage(this.pluginName + "&aSome of the settings did not load correctly from the configuration files! Check the server log to solve the problem.");
					}
				}
				return true;
			}
			String Causes = "";
			if(e1 != null) {
				Causes = Causes.concat(Causes + "\r" + e1.toString());
			}
			if(e2 != null) {
				Causes = Causes.concat(Causes + "\r" + e2.toString());
			}
			throw new Exception(Causes);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean loadYamlVariables() {
		boolean loadedAllVars = true;
		try {
			this.configVersion = this.formatColorCodes(this.config.getString("version"));
			if(this.configVersion.equals(this.pdffile.getVersion())) {
				this.DEBUG(this.pluginName + "&aThe " + this.configFileName + "'s version matches this plugin's version(&f" + this.pdffile.getVersion() + "&a)!");
			} else {
				this.sendConsoleMessage(this.pluginName + "&eThe " + this.configFileName + "'s version does NOT match this plugin's version(&f" + this.pdffile.getVersion() + "&e)! Make sure that you update the " + this.configFileName + " from this plugin's latest version! You can find this at &ahttp://dev.bukkit.org/plugins/enteis-plugin-library/&e.");
			}
		} catch(Exception e) {
			this.sendConsoleMessage(this.pluginName + "&eThe version in the config.yml was not set!");
			this.sendConsoleMessage(this.pluginName + "&cInvalid configuration settings detected! Disabling this plugin to prevent bad settings from corrupting saved player data...");
			this.server.getPluginManager().disablePlugin(this.plugin);
			this.enabled = false;
			return false;
		}
		try {
			this.showDebugMsgs = (Main.forceDebugMsgs || (Boolean.valueOf(Main.formatColorCodes(Main.config.getString("showDebugMsgs")))).booleanValue() == true);
		} catch(Exception e) {
			loadedAllVars = false;
			Main.unSpecifiedVarWarning("showDebugMsgs", "config.yml", Main.pluginName);
		}
		return loadedAllVars;
	}
	
	public String formatColorCodes(String str) {
		return str.replaceAll("(?i)&w", this.white + "").replaceAll("(?i)&_", this.rwhite).replaceAll("(?i)&b", this.aqua + "").replaceAll("(?i)&0", this.black + "").replaceAll("(?i)&9", this.blue + "").replaceAll("(?i)&l", this.bold + "").replaceAll("(?i)&3", this.daqua + "").replaceAll("(?i)&1", this.dblue + "").replaceAll("(?i)&8", this.dgray + "").replaceAll("(?i)&2", this.dgreen + "").replaceAll("(?i)&5", this.dpurple + "").replaceAll("(?i)&4", this.dred + "").replaceAll("(?i)&6", this.gold + "").replaceAll("(?i)&7", this.gray + "").replaceAll("(?i)&a", this.green + "").replaceAll("(?i)&o", this.italic + "").replaceAll("(?i)&d", this.lpurple + "").replaceAll("(?i)&k", this.magic + "").replaceAll("(?i)&c", this.red + "").replaceAll("(?i)&m", this.striken + "").replaceAll("(?i)&n", this.underline + "").replaceAll("(?i)&f", this.white + "").replaceAll("(?i)&e", this.yellow + "").replaceAll("(?i)&r", this.reset + "");
	}
	
	public static String getCommandFromMsg(String str) {
		if(!(str.isEmpty())) {
			if(str.length() == 1 || str.contains(" ") == false) {
				return str;
			} else if(str.indexOf(" ") >= 1) {
				return str.substring(0, str.indexOf(" "));
			} else {
				return str;
			}
		}
		return "null";
	}
	
	public String sendConsoleMessage(String message) {
		if(message == null || message.isEmpty()) return "";
		message = this.formatColorCodes(message);
		if(StringUtils.containsIgnoreCase(message, "&z")) {
			String[] msgs = message.split("(?i)&z");
			for(String msg : msgs) {
				this.console.sendMessage(msg.replaceAll("(?i)&z", "").trim());
			}
			return message.trim();
		}
		this.console.sendMessage(message.trim());
		return message.trim();
	}
	
	public String sendMessage(Player target, String message) {
		return this.sendMessage((CommandSender) target, message);
	}
	
	public String sendMessage(CommandSender target, String message) {
		if(message == null || message.isEmpty() || target == null) return "";
		message = this.formatColorCodes(message);
		if(StringUtils.containsIgnoreCase(message, "&z")) {
			String[] msgs = message.split("(?i)&z");
			for(String msg : msgs) {
				target.sendMessage(msg.replaceAll("(?i)&z", "").trim());
			}
			return message;
		}
		target.sendMessage(message.trim());
		return message.trim();
	}
	
	public Player getPlayer(String uuid) {
		Player player = null;
		if(uuid == null) {
			return player;
		}
		for(Player curPlayer : this.server.getOnlinePlayers()) {
			if(curPlayer.getUniqueId().equals(uuid)) {
				return curPlayer;
			}
		}
		return player;
	}
	
	public boolean onCommand(final CommandSender sender, final Command cmd, final String command, final String[] args) {
		String strArgs = "";
		if(!(args.length == 0)) {
			strArgs = "";
			int x = 0;
			do {
				strArgs = strArgs.concat(args[x] + " ");
				x++;
			} while(x < args.length);
		}
		strArgs = strArgs.trim();
		Player user = null;
		if(sender instanceof Player) {
			user = Bukkit.getPlayer(((Player) sender).getUniqueId());
		}
		String userName = sender.getName();
		if(user != null) {
			userName = user.getDisplayName();
		}
		if(userName.equals("") == true) {
			userName = sender.getName();
		}
		if(command.equalsIgnoreCase("uuid") || command.equalsIgnoreCase("uuids")) {
			if(Main.hasPerm(sender, "uuid.cmd.use.uuid")) {
				if(args.length >= 1) {
					if(args[0].equalsIgnoreCase("printmasterlist") || args[0].equalsIgnoreCase("pml")) {
						if(Main.hasPerm(user, "uuid.cmd.use.uuid.pml")) {
							if(user != null) {
								this.sendConsoleMessage(this.pluginName + "&ePlayer \"&f" + user.getName() + "&r&e\" initiated master list print to console. Here goes:");
								this.printMasterListToScreen();
								this.sendMessage(user, this.pluginName + "&2Printed to console screen successfully, go and check the console.");
							} else {
								this.sendConsoleMessage(this.pluginName + "&ePrinting master UUID list to screen:");
								this.printMasterListToScreen();
							}
						} else {
							this.sendMessage(user, this.pluginName + Main.noPerm);
						}
						return true;
					} else if(args[0].equalsIgnoreCase("saveall")) {
						if(Main.hasPerm(sender, "uuid.cmd.use.uuid.saveall")) {
							if(user != null) {
								this.sendConsoleMessage(this.pluginName + "&ePlayer \"&f" + user.getName() + "&r&e\" initiated master list save to file.");
								boolean successful = this.saveMasterListToConfig();
								this.sendMessage(user, this.pluginName + (successful ? "&2Save was successful." : "&eSomething went wrong while saving to file! Check the server log for details."));
							} else {
								this.sendConsoleMessage(this.pluginName + "&eSaving master UUID list to file...");
								boolean successful = this.saveMasterListToConfig();
								this.sendConsoleMessage(this.pluginName + "&2Save complete.");
								this.sendMessage(user, this.pluginName + (successful ? "&2Save was successful." : "&eSomething went wrong while saving to file! Check the server log for details."));
							}
						} else {
							this.sendMessage(sender, this.pluginName + Main.noPerm);
						}
						return true;
					} else if(args[0].equalsIgnoreCase("uuid")) {
						if(Main.hasPerm(sender, "uuid.cmd.use.uuid.uuid")) {
							if(args.length == 2) {
								Player target = Main.getPlayer(args[1]);
								if(target != null) {
									this.sendMessage(sender, this.pluginName + "&a\"&f" + target.getDisplayName() + "&r&a\"'s Universally Unique ID is: \"&f" + target.getUniqueId().toString() + "&r&a\"!");
								} else {
									UUID uuid = this.getUUIDFromPlayerName(args[1]);
									if(uuid != null) {
										this.sendMessage(sender, this.pluginName + "&2[&6OFFLINE_PLAYER&2]: &a\"&f" + this.getPlayerNameFromUUID(uuid) + "&r&a\"'s Universally Unique ID is: \"&f" + this.getUUIDFromPlayerName(args[1]).toString() + "&r&a\"!");
										return true;
									}
									this.sendMessage(sender, this.pluginName + "&cThe player \"&f" + args[0] + "&r&\" does not exist!");
									return true;
								}
							} else if(args.length == 1) {
								if(user != null) {
									this.sendMessage(sender, this.pluginName + "&aYour Universally Unique ID is: \"&f" + user.getUniqueId().toString() + "&r&a\"!");
								} else {
									this.sendMessage(sender, this.pluginName + "&eUsage: \"/" + command + " " + args[0] + "&r&e [playerName]\"; where \"&f[playerName]&r&e\" is required.");
								}
							} else {
								this.sendMessage(sender, this.pluginName + "&eUsage: \"/" + command + " " + args[0] + "&r&e [playerName]\".");
							}
						} else {
							this.sendMessage(sender, this.pluginName + Main.noPerm);
						}
					} else {
						this.sendMessage(sender, this.pluginName + "&eUsage: \"/" + command + " [uuid|printmasterlist|pml|saveall]");
					}
				} else {
					this.sendMessage(sender, this.pluginName + "&eUsage: \"/" + command + " [uuid|printmasterlist|pml|saveall]");
				}
			} else {
				this.sendMessage(sender, this.pluginName + Main.noPerm);
			}
			return true;
		}
		return false;
	}
}
