package com.gmail.br45entei.main.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.gmail.br45entei.main.Main;

/** @author Brian_Entei */
public final class PlayerInformation {
	public static final ArrayList<PlayerInformation>	instances		= new ArrayList<>();
	public static final String							fileExt			= ".yml";
	
	public YamlConfiguration							config;
	
	/** This player's UUID */
	public final UUID									uuid;
	/** This player's name */
	public final String									name;
	/** An un-set basic example option */
	public String										option;
	
	/** This player's island permissions */
	public final PlayerPerms							perms			= new PlayerPerms();
	
	public Inventory									invBeingViewed	= null;
	
	public static final class PlayerPerms {
		
		protected PlayerPerms() {
			this.resetAllValues();
		}
		
		protected boolean	option;
		
		public void resetAllValues() {
			this.option = false;
		}
		
		protected final void saveToConfig(ConfigurationSection memSection) {
			memSection.set("option", new Boolean(this.option));
		}
		
		protected final void loadFromConfig(ConfigurationSection memSection) {
			this.option = memSection.getBoolean("option");
		}
		
		public final boolean canOption() {
			return this.option;
		}
		
	}
	
	private PlayerInformation(Player player) {
		this.uuid = player.getUniqueId();
		this.name = player.getName();
		this.resetValuesToDefault();
		PlayerInformation.instances.add(this);
	}
	
	private PlayerInformation(UUID uuid) {
		this.uuid = uuid;
		this.name = Main.uuidMasterList.getPlayerNameFromUUID(uuid);
		this.resetValuesToDefault();
		PlayerInformation.instances.add(this);
	}
	
	public final void resetValuesToDefault() {
		this.option = "";
		this.perms.resetAllValues();
	}
	
	public static ArrayList<PlayerInformation> loadAllAvailableUUIDsIntoPlayerInformations() {
		for(UUID uuid : Main.uuidMasterList.getAllListedUUIDS()) {
			PlayerInformation.getPlayerInfo(uuid);
		}
		return PlayerInformation.instances;
	}
	
	public static PlayerInformation getPlayerInfo(UUID uuid) {
		if(uuid == null) {
			return null;
		}
		for(PlayerInformation info : PlayerInformation.instances) {
			if(info.uuid.toString().equals(uuid.toString())) {
				return info;
			}
		}
		PlayerInformation info = new PlayerInformation(uuid);
		info.loadFromFile();
		return info;
	}
	
	public static PlayerInformation getPlayerInfo(Player player) {
		if(player == null) {
			return null;
		}
		return PlayerInformation.getPlayerInfo(player.getUniqueId());
	}
	
	private final void saveToConfig() {
		YamlConfiguration config = this.getConfig();
		ConfigurationSection memSection = config.getConfigurationSection("player");
		if(memSection == null) {
			memSection = config.createSection("player");
		}
		memSection.set("uuid", this.uuid.toString());
		memSection.set("name", this.name);
		memSection.set("option", this.option);
		
		//this.perms.saveToConfig(memSection);
	}
	
	private final void loadFromConfig() {
		this.resetValuesToDefault();
		YamlConfiguration config = this.getConfig();
		ConfigurationSection memSection = config.getConfigurationSection("player");
		if(memSection == null) {
			memSection = config.createSection("player");
			memSection.set("uuid", this.uuid.toString());
			memSection.set("name", this.name);
			return;
		}
		this.option = memSection.getString("option");
		//this.perms.loadFromConfig(memSection);
		//...And we're done here!
		memSection = null;
		//System.gc();
	}
	
	//=======================================================
	
	public static final File getSaveFolder() {
		File folder = new File(Main.dataFolder, "PlayerData");
		if(!folder.exists()) {
			folder.mkdirs();
		}
		return folder;
	}
	
	public final File getSaveFile() {
		File file = new File(PlayerInformation.getSaveFolder(), this.uuid.toString() + PlayerInformation.fileExt);
		if(!file.exists()) {
			try {
				file.createNewFile();
				Main.sendConsoleMessage(Main.pluginName + "&aCreated a new data file for player \"&r&f" + this.getPlayerDisplayName() + "&r&a\"!");
			} catch(Throwable e) {
				Main.sendConsoleMessage(Main.pluginName + "&cAn error occurred while creating player data file \"" + file.getAbsolutePath() + "\":");
				e.printStackTrace();//throw e;
			}
		}
		return file;
	}
	
	public final YamlConfiguration getConfig() {
		if(this.config == null) {
			this.config = new YamlConfiguration();
		}
		return this.config;
	}
	
	public static final void saveAllPlayers() {
		for(PlayerInformation info : PlayerInformation.instances) {
			info.saveToFile();
		}
	}
	
	public static final void saveAllPlayersAndDispose() {
		PlayerInformation.saveAllPlayers();
		PlayerInformation.instances.clear();
	}
	
	public final void saveToFile() {
		this.saveToConfig();
		try {
			this.getConfig().save(this.getSaveFile());
			Main.DEBUG("&2Saved player \"&r&f" + this.getPlayerDisplayName() + "&r&2\"'s data file successfully!");
		} catch(IOException e) {
			Main.sendConsoleMessage(Main.pluginName + "&cAn error occurred while saving to player data file \"" + this.getSaveFile().getAbsolutePath() + "\":");
			e.printStackTrace();//throw e;
		}
	}
	
	public final void saveToFileAndDispose() {
		this.saveToFile();
		PlayerInformation.instances.remove(this);
	}
	
	public final void loadFromFile() {
		try {
			this.getConfig().load(this.getSaveFile());
			this.loadFromConfig();
			Main.DEBUG("&2Loaded player \"&r&f" + this.getPlayerDisplayName() + "&r&2\"'s data file successfully!");
		} catch(Throwable e) {
			Main.sendConsoleMessage(Main.pluginName + "&cAn error occurred while loading from player data file \"" + this.getSaveFile().getAbsolutePath() + "\":");
			e.printStackTrace();//throw e;
		}
	}
	
	//=======================================================
	
	public final boolean isPlayerOnline() {
		return this.getPlayer() != null;
	}
	
	public final Player getPlayer() {
		return Bukkit.getPlayer(this.uuid);
	}
	
	public final String getPlayerDisplayName() {
		return(this.getPlayer() != null ? this.getPlayer().getDisplayName() : this.name);
	}
	
}
