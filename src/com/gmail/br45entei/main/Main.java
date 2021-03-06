package com.gmail.br45entei.main;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.br45entei.main.player.PlayerInformation;
import com.gmail.br45entei.main.util.CodeUtils;
import com.gmail.br45entei.main.yml.YamlMgmtClass;

/** @author Brian_Entei */
public class Main extends JavaPlugin implements Listener {
	private static Main			p;
	public Main					plugin			= this;
	public static final boolean	forceDebugMsgs	= false;
	
	public static final Main getPlugin() {
		return Main.p;
	}
	
	public static void DEBUG(String str) {
		if(Main.showDebugMsgs) {
			Main.sendConsoleMessage(Main.pluginName + Main.formatColorCodes("&eDebug: " + str));
		}
	}
	
	public static File getPluginFile() {
		return Main.pluginFile;
	}
	
	public static final String		rwhite		= ChatColor.RESET + "" + ChatColor.WHITE;
	public static final ChatColor	aqua		= ChatColor.AQUA;
	public static final ChatColor	black		= ChatColor.BLACK;
	public static final ChatColor	blue		= ChatColor.BLUE;
	public static final ChatColor	bold		= ChatColor.BOLD;
	public static final ChatColor	daqua		= ChatColor.DARK_AQUA;
	public static final ChatColor	dblue		= ChatColor.DARK_BLUE;
	public static final ChatColor	dgray		= ChatColor.DARK_GRAY;
	public static final ChatColor	dgreen		= ChatColor.DARK_GREEN;
	public static final ChatColor	dpurple		= ChatColor.DARK_PURPLE;
	public static final ChatColor	dred		= ChatColor.DARK_RED;
	public static final ChatColor	gold		= ChatColor.GOLD;
	public static final ChatColor	gray		= ChatColor.GRAY;
	public static final ChatColor	green		= ChatColor.GREEN;
	public static final ChatColor	italic		= ChatColor.ITALIC;
	public static final ChatColor	lpurple		= ChatColor.LIGHT_PURPLE;
	public static final ChatColor	magic		= ChatColor.MAGIC;
	public static final ChatColor	red			= ChatColor.RED;
	public static final ChatColor	reset		= ChatColor.RESET;
	public static final ChatColor	striken		= ChatColor.STRIKETHROUGH;
	public static final ChatColor	underline	= ChatColor.UNDERLINE;
	public static final ChatColor	white		= ChatColor.WHITE;
	public static final ChatColor	yellow		= ChatColor.YELLOW;
	public static String			pluginName	= Main.white + "[" + Main.white + "Entei's New Plugin" + Main.white + "] ";
	
	public static String broadcast(String str) {
		str = Main.formatColorCodes(str);
		Bukkit.getServer().broadcastMessage(str);
		return str;
	}
	
	/** @param str
	 * @return The given string with ChatColor. */
	public static String formatColorCodes(String str) {
		return str.replaceAll("(?i)&w", Main.white + "").replaceAll("(?i)&_", Main.rwhite).replaceAll("(?i)&b", Main.aqua + "").replaceAll("(?i)&0", Main.black + "").replaceAll("(?i)&9", Main.blue + "").replaceAll("(?i)&l", Main.bold + "").replaceAll("(?i)&3", Main.daqua + "").replaceAll("(?i)&1", Main.dblue + "").replaceAll("(?i)&8", Main.dgray + "").replaceAll("(?i)&2", Main.dgreen + "").replaceAll("(?i)&5", Main.dpurple + "").replaceAll("(?i)&4", Main.dred + "").replaceAll("(?i)&6", Main.gold + "").replaceAll("(?i)&7", Main.gray + "").replaceAll("(?i)&a", Main.green + "").replaceAll("(?i)&o", Main.italic + "").replaceAll("(?i)&d", Main.lpurple + "").replaceAll("(?i)&k", Main.magic + "").replaceAll("(?i)&c", Main.red + "").replaceAll("(?i)&m", Main.striken + "").replaceAll("(?i)&n", Main.underline + "").replaceAll("(?i)&f", Main.white + "").replaceAll("(?i)&e", Main.yellow + "").replaceAll("(?i)&r", Main.reset + "")
		
		.replaceAll("(?i)�w", Main.white + "").replaceAll("(?i)�_", Main.rwhite).replaceAll("(?i)�b", Main.aqua + "").replaceAll("(?i)�0", Main.black + "").replaceAll("(?i)�9", Main.blue + "").replaceAll("(?i)�l", Main.bold + "").replaceAll("(?i)�3", Main.daqua + "").replaceAll("(?i)�1", Main.dblue + "").replaceAll("(?i)�8", Main.dgray + "").replaceAll("(?i)�2", Main.dgreen + "").replaceAll("(?i)�5", Main.dpurple + "").replaceAll("(?i)�4", Main.dred + "").replaceAll("(?i)�6", Main.gold + "").replaceAll("(?i)�7", Main.gray + "").replaceAll("(?i)�a", Main.green + "").replaceAll("(?i)�o", Main.italic + "").replaceAll("(?i)�d", Main.lpurple + "").replaceAll("(?i)�k", Main.magic + "").replaceAll("(?i)�c", Main.red + "").replaceAll("(?i)�m", Main.striken + "").replaceAll("(?i)�n", Main.underline + "").replaceAll("(?i)�f", Main.white + "").replaceAll("(?i)�e", Main.yellow + "").replaceAll("(?i)�r", Main.reset + "");
	}
	
	/** @param str String
	 * @return The given string without ChatColor. Does not remove the ChatColor
	 *         codes(i.e. "&f" or "�4"). */
	public static String stripColorCodes(String str) {
		return str.replaceAll("(?i)\u00A7w", "").replaceAll("(?i)\u00A7b", "").replaceAll("\u00A70", "").replaceAll("\u00A79", "").replaceAll("(?i)\u00A7l", "").replaceAll("\u00A73", "").replaceAll("\u00A71", "").replaceAll("\u00A78", "").replaceAll("\u00A72", "").replaceAll("\u00A75", "").replaceAll("\u00A74", "").replaceAll("\u00A76", "").replaceAll("\u00A77", "").replaceAll("(?i)\u00A7a", "").replaceAll("(?i)\u00A7o", "").replaceAll("(?i)\u00A7d", "").replaceAll("(?i)\u00A7k", "").replaceAll("(?i)\u00A7c", "").replaceAll("(?i)\u00A7m", "").replaceAll("(?i)\u00A7n", "").replaceAll("(?i)\u00A7f", "").replaceAll("(?i)\u00A7e", "").replaceAll("(?i)\u00A7r", "");/*return Pattern.compile("(?i)"+String.valueOf("\u00A7")+"[0-9A-FK-OR]+").matcher(str).replaceAll("");*/
	}
	
	public static String getFirstLetterOfGameMode(GameMode gm) {
		if(gm == null) {
			return "";
		}
		return gm.name().substring(0, 1).toUpperCase();
	}
	
	public static String limitStringToNumOfChars(String str, int limit) {
		return(str != null ? (str.length() >= 1 ? (str.substring(0, (str.length() >= limit ? limit : str.length()))) : "") : "");
	}
	
	/** @param stackTraceElements The elements to convert
	 * @return The resulting string */
	public static final String stackTraceElementsToStr(StackTraceElement[] stackTraceElements) {
		String str = "";
		for(StackTraceElement stackTrace : stackTraceElements) {
			str += (!stackTrace.toString().startsWith("Caused By") ? "     at " : "") + stackTrace.toString() + "\n";
		}
		return str;
	}
	
	/** @param t The Throwable to convert
	 * @return The resulting string */
	public static final String throwableToStr(Throwable t) {
		String str = t.getClass().getName() + ": ";
		if((t.getMessage() != null) && !t.getMessage().isEmpty()) {
			str += t.getMessage() + "\n";
		} else {
			str += "\n";
		}
		str += Main.stackTraceElementsToStr(t.getStackTrace());
		if(t.getCause() != null) {
			str += "Caused by:\n" + Main.throwableToStr(t.getCause());
		}
		return str;
	}
	
	public static File					pluginFile		= null;
	public static PluginDescriptionFile	pdffile;
	public static ConsoleCommandSender	console;
	public static Server				server			= null;
	public static File					dataFolder		= null;
	public static String				dataFolderName	= "";
	public static boolean				YamlsAreLoaded	= false;
	public static FileConfiguration		config;
	public static File					configFile		= null;
	public static String				configFileName	= "config.yml";
	public static String				consoleSayFormat;
	public static CommandSender			rcon;
	
	public static Chat					chat			= null;
	public static Permission			perm			= null;
	public static Economy				econ			= null;
	public static EconomyAPI			eco				= null;
	
	public static UUIDMasterList		uuidMasterList	= null;
	
	public static boolean isStringUUID(String str) {
		boolean success = true;
		try {
			UUID.fromString(str);
		} catch(Exception e) {
			success = false;
		}
		return success;
	}
	
	public static Player getPlayer(String name) {
		Player rtrn = null;
		for(Player curPlayer : Main.server.getOnlinePlayers()) {
			if(curPlayer.getName().equals(name)) {
				return curPlayer;
			}
		}
		for(Player curPlayer : Main.server.getOnlinePlayers()) {
			if(curPlayer.getName().startsWith(name)) {
				return curPlayer;
			}
		}
		return rtrn;
	}
	
	public static boolean				enabled					= true;
	public static final String			getPunctuationChars		= "\\p{Punct}+";
	public static final String			getWhiteSpaceChars		= "\\s+"/*"\\p{Space}+"*/;
	public static final String			getAlphaNumericChars	= "\\p{Alnum}+";
	public static final String			getAlphabetChars		= "\\p{Alpha}+";
	public static final String			getNumberChars			= "\\p{Digit}+";
	public static final String			getUpperCaseChars		= "\\p{Lower}+";
	public static final String			getLowerCaseChars		= "\\p{Upper}+";
	
	/** The variable used to store messages that are meant to be displayed only
	 * once.
	 * 
	 * @see <a
	 *      href="http://enteisislandsurvival.no-ip.org/javadoc/index.html">Main.sendOneTimeMessage()</a>
	 * @see <a
	 *      href="http://enteisislandsurvival.no-ip.org/javadoc/index.html">Java
	 *      Documentation for EnteisCommands</a> */
	private static ArrayList<String>	oneTimeMessageList		= new ArrayList<>();
	
	// TODO To be loaded from config.yml
	public static boolean				showDebugMsgs			= false;
	public static String				noPerm					= "";
	
	public static String				configVersion			= "";
	
	@Override
	public void onDisable() {
		Main.uuidMasterList.onDisable();
		Main.sendConsoleMessage(Main.pluginName + "&eVersion " + Main.pdffile.getVersion() + " is now disabled!");
	}
	
	@Override
	public void onLoad() {
		Main.p = this;
		Main.pdffile = this.getDescription();
		Main.server = Bukkit.getServer();
		//server.getPluginManager().registerEvents(this, this);
		Main.console = Main.server.getConsoleSender();
		File dataFolder = this.getDataFolder();
		if(!(dataFolder.exists())) {
			dataFolder.mkdir();
		}
		Main.pluginFile = this.getFile();
		try {
			Main.dataFolderName = this.getDataFolder().getAbsolutePath();
		} catch(SecurityException e) {
			FileMgmt.LogCrash(e, "onEnable()", "Failed to get the full directory of this library's folder(\"" + Main.dataFolderName + "\")!", true, Main.dataFolderName);
		}
		Main.uuidMasterList = new UUIDMasterList(this.plugin);
	}
	
	private static Main	instance;
	
	public static Main getInstance() {
		return Main.instance;
	}
	
	@Override
	public void onEnable() {
		Main.instance = this;
		Main.console = Main.server.getConsoleSender();
		Main.consoleSayFormat = "&e" + Main.console.getName() + "&r&f ";
		Main.showDebugMsg(Main.pluginName + "The dataFolderName variable is: \"" + Main.dataFolderName + "\"!", Main.showDebugMsgs);
		// TODO Loading Files, plugins, etc.
		YamlMgmtClass.LoadConfig();
		Main.uuidMasterList.onEnable();
		if(Main.setupEconomy()) {
			Main.sendConsoleMessage(Main.pluginName + "&2Vault Economy API Successful!");
		} else {
			Main.sendConsoleMessage(Main.pluginName + "&cUnable to utilize Vault Economy API. Shop features may not work as intended.");
		}
		if(Main.setupPermissions()) {
			Main.sendConsoleMessage(Main.pluginName + "&2Vault Permission API Successful!");
		} else {
			Main.sendConsoleMessage(Main.pluginName + "&eUnable to utilize Vault Permissions API. Commands and/or in game gui systems may not function.");
		}
		if(Main.isEconApiAvailable()) {
			Main.eco = new EconomyAPI(this);
		}
		// TODO End of Loading Files, plugins, etc.
		if(Main.enabled) {
			Main.server.getPluginManager().registerEvents(this, this);
			Main.sendConsoleMessage(Main.pluginName + "&aVersion " + Main.pdffile.getVersion() + " is now enabled!");
			//Back up the config files:
			File playerFolder = PlayerInformation.getSaveFolder();
			try {
				String date = CodeUtils.getSystemTime(false, true, true);//Date only, file system safe
				File backupsFolder = new File(Main.dataFolder, FilenameUtils.getName(playerFolder.getAbsolutePath()) + "_Backups");
				backupsFolder.mkdirs();
				File playerZip = new File(backupsFolder, FilenameUtils.getName(playerFolder.getAbsolutePath()) + "_" + date + ".zip");
				try {
					playerZip.delete();
				} catch(Throwable ignored) {
				}
				FileMgmt.zipDir(playerFolder, playerZip);
				YamlMgmtClass.getResourceFromStreamAsFile(Main.dataFolder, "permissions.txt");
			} catch(Throwable e) {
				Main.sendConsoleMessage(Main.pluginName + "&cUnable to automatically back up configuration files:");
				e.printStackTrace();
			}
		} else {
			Main.uuidMasterList.onDisable();
			this.onDisable();
		}
	}
	
	public static boolean setupPermissions() {
		if(Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
		if(rsp.getProvider() != null) {
			Main.perm = rsp.getProvider();
		}
		return Main.perm != null;
	}
	
	public static boolean setupEconomy() {
		if(Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp == null) {
			return false;
		}
		Main.econ = rsp.getProvider();
		return Main.econ != null;
	}
	
	public static final boolean isPermApiAvailable() {
		return Main.perm != null;
	}
	
	public static final boolean isEconApiAvailable() {
		return Main.econ != null;
	}
	
	public static final boolean hasPerm(CommandSender sender, String perm) {
		if(sender == null) {
			return false;
		}
		if(sender.isOp()) {
			return true;
		}
		if(Main.isPermApiAvailable()) {
			return Main.perm.has(sender, perm);
		}
		return sender.hasPermission(perm);
	}
	
	public static String getSystemTime(boolean getTimeOnly) {
		String timeAndDate = "";
		if(getTimeOnly == false) {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date date = new Date();
			timeAndDate = dateFormat.format(date);
		} else {
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			timeAndDate = dateFormat.format(date);
		}
		return timeAndDate;
	}
	
	/** Checks str1 against str2(and vice versa) to see if they either equal(case
	 * ignored), if str1 starts with str2 and str1 is less than 6 characters(and
	 * vice versa), and removes all non-alpha-numeric characters and performs
	 * the checks again.
	 * 
	 * @param str1 String
	 * @param str2 String
	 * @return True if str1 is equal to, starts with, or ends with str1, and
	 *         vice versa; false otherwise */
	public static boolean isSimilarTo(String str1, String str2) {
		if(str1 == null || str2 == null || str1.equals("") || str2.equals("")) return false;
		boolean rtrn = (str1.equals(str2)) || (str1.equalsIgnoreCase(str2)) || (str1.startsWith(str2) && str1.length() + 1 >= (str2.length())) || (str1.endsWith(str2) && str1.length() + 1 >= str2.length() && str2.length() <= 6) || (str2.startsWith(str1) && str2.length() + 1 >= (str1.length())) || (str2.endsWith(str1) && str2.length() + 1 >= str1.length() && str1.length() <= 6);
		if(rtrn == true) {
			return true;
		}
		str1 = str1.replaceAll("[^\\p{Alnum}\\p{Space}]+", "");
		str2 = str2.replaceAll("[^\\p{Alnum}\\p{Space}]+", "");//Removes everything except AaBbCc, 123, and whitespace)
		if(str1.equals("") || str2.equals("")) return false;
		rtrn = (str1.equals(str2)) || (str1.equalsIgnoreCase(str2)) || (str1.startsWith(str2) && str1.length() + 1 >= (str2.length())) || (str1.endsWith(str2) && str1.length() + 1 >= str2.length() && str2.length() <= 6) || (str2.startsWith(str1) && str2.length() + 1 >= (str1.length())) || (str2.endsWith(str1) && str2.length() + 1 >= str1.length() && str1.length() <= 6);
		return false;
	}
	
	public static String ignoreCase(String str) {
		return "(?i)" + Pattern.quote(str);
	}
	
	@SuppressWarnings("boxing")
	public static double toNumber(String str) {
		if(Main.checkIsNumber(str)) {
			return Double.valueOf(str);
		}
		return Double.NaN;
	}
	
	public static boolean checkIsNumber(String str) {
		final String Digits = "(\\p{Digit}+)";
		final String HexDigits = "(\\p{XDigit}+)";
		final String Exp = "[eE][+-]?" + Digits;
		final String fpRegex = ("[\\x00-\\x20]*[+-]?(NaN|Infinity|(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|(\\.(" + Digits + ")(" + Exp + ")?)|" + "(((0[xX]" + HexDigits + "(\\.)?)|(0[xX]" + HexDigits + "?(\\.)" + HexDigits + "))[pP][+-]?" + Digits + "))[fFdD]?))[\\x00-\\x20]*");
		if(Pattern.matches(fpRegex, str)) {
			return true;
		}
		return false;
	}
	
	public static boolean checkIsInteger(String str) {
		boolean rtrn = true;
		try {
			Integer.valueOf(str);
		} catch(NumberFormatException e) {
			rtrn = false;
		}
		return rtrn;
	}
	
	public static boolean checkIsFloat(String str) {
		boolean rtrn = true;
		try {
			Float.valueOf(str);
		} catch(NumberFormatException e) {
			rtrn = false;
		}
		return rtrn;
	}
	
	public static boolean checkIsDouble(String str) {
		boolean rtrn = true;
		try {
			Double.valueOf(str);
		} catch(NumberFormatException e) {
			rtrn = false;
		}
		return rtrn;
	}
	
	public static boolean checkIsLong(String str) {
		boolean rtrn = true;
		try {
			Long.valueOf(str);
		} catch(NumberFormatException e) {
			rtrn = false;
		}
		return rtrn;
	}
	
	public static void onRconEvent(RemoteServerCommandEvent evt) {
		Main.rcon = evt.getSender();
	}
	
	public static boolean hasRconConnectedYet() {
		return(Main.rcon != null);
	}
	
	public static CommandSender getRcon() {
		if(Main.hasRconConnectedYet()) {
			return Main.rcon;
		}
		return null;
	}
	
	public static final List<String> getStringsBetweenStrings(String input, String pattern1, String pattern2) {
		List<String> strings = Arrays.asList(input.replaceAll("^.*?(?i)" + pattern1 + "", "").split("(?i)" + pattern2 + ".*?(" + pattern1 + "|$)"));
		return strings;
	}
	
	/** @param str The entire command string
	 * @return The command used */
	public static String getCommandFromMsg(String str) {
		if(!(str.isEmpty())) {
			if(str.length() == 1 || str.contains(" ") == false) {
				return str;
			} else if(str.indexOf(" ") >= 1) {
				String command = str.substring(0, str.indexOf(" "));
				return command;
			} else {
				return str;
			}
		}
		return "null";
	}
	
	/** Takes the given command string(cmd, which is everything you type) and
	 * cuts the actual commandLabel(the command you used) out, resulting in only
	 * the arguments.
	 * 
	 * @param cmd
	 * @return The arguments of the given command, in String form.
	 * @since 4.9
	 * @see Main#getCommandFromMsg(String) */
	public static String[] getArgumentsFromCommand(String cmd) {// TODO Move this to the Main
		String[] args = {""};
		String getArgs = "";
		//String command = Main.getCommandFromMsg(cmd);
		if(cmd.indexOf(" ") >= 1) {
			getArgs = cmd.trim().substring(cmd.indexOf(" ")).trim();
			args = getArgs.split(Main.getWhiteSpaceChars);
			//showDebugMsg("&aDebug: The command(\"" + command + "\") has the following arguments: " + args, showDebugMsgs);
		} else {
			//showDebugMsg("&aDebug: The command(\"" + command + "\") had no arguments.", showDebugMsgs);
		}
		return args;
	}
	
	public static String sendConsoleMessage(String message) {
		if(message == null || message.isEmpty()) {
			return "";
		}
		message = Main.formatColorCodes(message);
		if(message.contains("&z") || message.contains("&Z")) {
			String[] msgs = message.split("(?i)&z");
			for(String msg : msgs) {
				Main.console.sendMessage(msg.replaceAll("(?i)&z", "").trim());
			}
			return message.trim();
		}
		Main.console.sendMessage(message.trim());
		return message.trim();
	}
	
	public static String sendMessage(Player target, String message) {
		if(message == null || message.isEmpty() || target == null) return "";
		message = Main.formatColorCodes(message);
		if(message.contains("&z")) {
			String[] msgs = message.split("&z");
			for(String msg : msgs) {
				target.sendMessage(msg.replaceAll("&z", "").trim());
			}
			return message.trim();
		}
		target.sendMessage(message.trim());
		return message.trim();
	}
	
	public static String sendMessage(CommandSender target, String message) {
		if(message == null || message.isEmpty() || target == null) return "";
		message = Main.formatColorCodes(message);
		if(message.contains("&z")) {
			String[] msgs = message.split("&z");
			for(String msg : msgs) {
				target.sendMessage(msg.replaceAll("&z", "").trim());
			}
			return message;
		}
		target.sendMessage(message.trim());
		return message.trim();
	}
	
	public static String fixPluralWord(int number, String word) {
		// TODO Auto-generated method stub
		String newWord = "";
		if(number != 1) {
			if(word.equalsIgnoreCase("those") || word.equalsIgnoreCase("that")) {
				return "those";
			}
			if(word.length() <= 3 && word.equalsIgnoreCase("is")) {
				word = "are";
				return word;
			}
			if(word.length() >= 4) {
				String beginningOfWord = word.substring(0, word.length() - 3);
				String endOfWord = word.substring(word.length() - 3, word.length());
				List<String> suffixes = new ArrayList<>();
				suffixes.add("x");
				suffixes.add("sh");
				suffixes.add("ch");
				suffixes.add("z");
				suffixes.add("ss");
				Iterator<String> it = suffixes.iterator();
				boolean replacedSuffix = false;
				while(it.hasNext()) {
					if(endOfWord.contains(it.next()) && replacedSuffix == false) {
						endOfWord = endOfWord + "es";
						replacedSuffix = true;
					}
				}
				if(replacedSuffix == true) {
					newWord = beginningOfWord + endOfWord;
					return newWord;
				}
				return word + "s";
			}
			return word;
		} else if(word.equalsIgnoreCase("are")) {
			word = "is";
			return word;
		} else if(word.equalsIgnoreCase("those") || word.equalsIgnoreCase("that")) {
			return "that";
		} else {
			if(word.endsWith("s") && word.length() >= 2) {
				return word.substring(0, word.length() - 1);
			}
			return word;
		}
	}
	
	public static boolean showDebugMsg(String str, boolean Override) {
		if(Override == true) {
			Main.sendConsoleMessage(str);
			return true;
		}
		return false;
	}
	
	public static String replaceWord(String str, String regex, String replacement, boolean case_sensitive, String dataFolderName) {
		try {
			if(case_sensitive == true) {
				return str.replaceAll("\\b" + regex + "\\b", replacement);
			}
			return str.replaceAll("(?i)\\b" + regex + "\\b", replacement);
		} catch(PatternSyntaxException e) {
			FileMgmt.LogCrash(e, "replaceWord(\"" + str + "\", \"" + regex + "\", \"" + replacement + "\", " + case_sensitive + ")", "A bad regex was used.", false, dataFolderName);
			Main.showDebugMsg("A bad regex was used when running function \"replaceWord()\". Check the server log or \"" + dataFolderName + "\\crash-reports.txt\" to solve the problem.", false);
		}
		return str;
	}
	
	public static String GrammarEnforcement(String msg, Player chatter, String dataFolderName) {
		try {
			msg = Main.capitalizeFirstLetter(msg);
			msg = Main.replaceWord(msg, "(\\w+)(\\s+\\1)+", "$1", false, dataFolderName);
			msg = Main.replaceWord(msg, "(\\w+)(\\.*\\4)+", "$1", false, dataFolderName);
			/**/msg = Main.replaceWord(msg, "\\p{Alnum}\\1{4,}", "$1", false, dataFolderName);
			//msg = msg.replaceAll("\\w+(\\.\\w+)+", "");//This SHOULD get rid of spammy letters like helloooooooooooooooo, but I'm not sure...
			msg = Main.replaceWord(msg, "lol", "*Laughs out loud*", false, dataFolderName);
			msg = Main.replaceWord(msg, "Jk", "Just joking", true, dataFolderName);
			msg = Main.replaceWord(msg, "jk", "just joking", false, dataFolderName);
			msg = Main.replaceWord(msg, "Np", "No problem", true, dataFolderName);
			msg = Main.replaceWord(msg, "np", "no problem", false, dataFolderName);
			msg = Main.replaceWord(msg, "Wtf", "What the-", true, dataFolderName);
			msg = Main.replaceWord(msg, "wtf", "what the-", false, dataFolderName);
			msg = Main.replaceWord(msg, "Wtc", "What the heck", true, dataFolderName);
			msg = Main.replaceWord(msg, "wtc", "what the heck", false, dataFolderName);
			msg = Main.replaceWord(msg, "Wth", "What the heck", true, dataFolderName);
			msg = Main.replaceWord(msg, "wth", "what the heck", false, dataFolderName);
			msg = Main.replaceWord(msg, "lmao", "*Laughing my butt off*", false, dataFolderName);
			msg = Main.replaceWord(msg, "lmfao", "*Laughing my frekin' butt off*", false, dataFolderName);
			msg = Main.replaceWord(msg, "ikr", "I know, right", false, dataFolderName);
			msg = Main.replaceWord(msg, "idk", "I don't know", false, dataFolderName);
			msg = Main.replaceWord(msg, "U", "You", true, dataFolderName);
			msg = Main.replaceWord(msg, "u", "you", false, dataFolderName);
			msg = Main.replaceWord(msg, "i", "I", true, dataFolderName);
			msg = Main.replaceWord(msg, "Budder", "Gold", true, dataFolderName);//XD
			msg = Main.replaceWord(msg, "budder", "gold", false, dataFolderName);//XD
			msg = Main.replaceWord(msg, "Buder", "Gold", true, dataFolderName);//XD
			msg = Main.replaceWord(msg, "buder", "gold", false, dataFolderName);//XD
			msg = Main.replaceWord(msg, "Brb", "Be right back", true, dataFolderName);
			msg = Main.replaceWord(msg, "brb", "be right back", false, dataFolderName);
			msg = Main.replaceWord(msg, "Gtg", "Got to go", true, dataFolderName);
			msg = Main.replaceWord(msg, "gtg", "got to go", false, dataFolderName);
			msg = Main.replaceWord(msg, "Liek", "Like", true, dataFolderName);
			msg = Main.replaceWord(msg, "liek", "like", false, dataFolderName);
			msg = Main.replaceWord(msg, "Y", "Why", true, dataFolderName);
			msg = Main.replaceWord(msg, "y", "why", false, dataFolderName);
			msg = Main.replaceWord(msg, "Plz", "Please", true, dataFolderName);
			msg = Main.replaceWord(msg, "plz", "please", false, dataFolderName);
			msg = Main.replaceWord(msg, "Pzl", "Please", true, dataFolderName);
			msg = Main.replaceWord(msg, "pzl", "please", false, dataFolderName);
			msg = Main.replaceWord(msg, "Yo", "Hey", true, dataFolderName);
			msg = Main.replaceWord(msg, "yo", "hey", false, dataFolderName);
			msg = Main.replaceWord(msg, "http://", "", false, dataFolderName);
			msg = Main.replaceWord(msg, "https://", "", false, dataFolderName);
			//msg = replaceWord(msg, "(?i)(\\w+)(\\s+\\4)+", "$1", true, dataFolderName);
			msg = Main.replaceWord(msg, "Gangsta", "Gang member", true, dataFolderName);
			msg = Main.replaceWord(msg, "gangsta", "gang member", false, dataFolderName);
			msg = Main.replaceWord(msg, "Gangster", "Gang member", true, dataFolderName);
			msg = Main.replaceWord(msg, "gangster", "gang member", false, dataFolderName);
			msg = Main.replaceWord(msg, "Wut", "What", true, dataFolderName);
			msg = Main.replaceWord(msg, "wut", "what", false, dataFolderName);
			msg = Main.replaceWord(msg, "Wat", "What", true, dataFolderName);
			msg = Main.replaceWord(msg, "wat", "what", false, dataFolderName);
			msg = Main.replaceWord(msg, "ik", "I know", false, dataFolderName);
			msg = Main.replaceWord(msg, "gotta", "have to", false, dataFolderName);
			msg = Main.replaceWord(msg, "K", " Okay", true, dataFolderName);
			msg = Main.replaceWord(msg, "k", " okay", false, dataFolderName);
			msg = Main.replaceWord(msg, "ill", "I'll", false, dataFolderName);
			msg = Main.replaceWord(msg, "Shure", "Sure", true, dataFolderName);
			msg = Main.replaceWord(msg, "shure", "sure", false, dataFolderName);
			msg = Main.replaceWord(msg, "KK", "Okay, I understand", true, dataFolderName);
			msg = Main.replaceWord(msg, "Kk", "Okay, I understand", true, dataFolderName);
			msg = Main.replaceWord(msg, "kk", "okay, I understand", false, dataFolderName);
			msg = Main.replaceWord(msg, "Nvm", "Nevermind", true, dataFolderName);
			msg = Main.replaceWord(msg, "nvm", "nevermind", false, dataFolderName);
			msg = Main.replaceWord(msg, "Wanna", "Want to", true, dataFolderName);
			msg = Main.replaceWord(msg, "wanna", "want to", false, dataFolderName);
			msg = Main.replaceWord(msg, "Gimme", "Give me", true, dataFolderName);
			msg = Main.replaceWord(msg, "gimme", "give me", false, dataFolderName);
			msg = Main.replaceWord(msg, "Sec", "Second", true, dataFolderName);
			msg = Main.replaceWord(msg, "sec", "second", false, dataFolderName);
			msg = Main.replaceWord(msg, "Promo", "Promotion", true, dataFolderName);
			msg = Main.replaceWord(msg, "promo", "promotion", false, dataFolderName);
			msg = Main.replaceWord(msg, "Yah", "Yes", true, dataFolderName);
			msg = Main.replaceWord(msg, "yah", "yes", false, dataFolderName);
			msg = Main.replaceWord(msg, "Ya", "You", true, dataFolderName);
			msg = Main.replaceWord(msg, "ya", "you", false, dataFolderName);
			msg = Main.replaceWord(msg, "im", "I'm", true, dataFolderName);
			msg = Main.replaceWord(msg, "imma", "I'll", true, dataFolderName);
			msg = Main.replaceWord(msg, "Yur", "Your", true, dataFolderName);
			msg = Main.replaceWord(msg, "yur", "your", false, dataFolderName);
			msg = Main.replaceWord(msg, "bc", "because", false, dataFolderName);
			msg = Main.replaceWord(msg, "Dont", "Don't", true, dataFolderName);
			msg = Main.replaceWord(msg, "dont", "don't", false, dataFolderName);
			msg = Main.replaceWord(msg, "L8ter", "Later", true, dataFolderName);
			msg = Main.replaceWord(msg, "l8ter", "later", false, dataFolderName);
			msg = Main.replaceWord(msg, "Gg", "Good game", true, dataFolderName);
			msg = Main.replaceWord(msg, "GG", "Good game", true, dataFolderName);
			msg = Main.replaceWord(msg, "gg", "good game", false, dataFolderName);
			msg = Main.replaceWord(msg, "Teh", "The", true, dataFolderName);
			msg = Main.replaceWord(msg, "teh", "the", false, dataFolderName);
			msg = Main.replaceWord(msg, "pussy cat", "^%GH*D@", true, dataFolderName);//Workaround
			msg = Main.replaceWord(msg, "Pussy cat", "^%GH*D#", true, dataFolderName);//Workaround
			msg = Main.replaceWord(msg, "Pussy", "Wimp", true, dataFolderName);
			msg = Main.replaceWord(msg, "pussy", "wimp", false, dataFolderName);
			msg = Main.replaceWord(msg, "^%GH*D@", "pussy cat", true, dataFolderName);//Workaround
			msg = Main.replaceWord(msg, "^%GH*D#", "Pussy cat", true, dataFolderName);//Workaround
			msg = Main.replaceWord(msg, "Rofl", "*Rolling on the floor*", true, dataFolderName);
			msg = Main.replaceWord(msg, "rofl", "*rolling on the floor*", false, dataFolderName);
			msg = Main.replaceWord(msg, "Roflol", "*Rolling on the floor laughing out loud*", true, dataFolderName);
			msg = Main.replaceWord(msg, "roflol", "*rolling on the floor laughing out loud*", false, dataFolderName);
			msg = Main.replaceWord(msg, "Omg", "Oh my gosh", true, dataFolderName);
			msg = Main.replaceWord(msg, "omg", "oh my gosh", false, dataFolderName);
			msg = Main.replaceWord(msg, "Wierd", "Weird", true, dataFolderName);
			msg = Main.replaceWord(msg, "wierd", "weird", false, dataFolderName);
			msg = Main.replaceWord(msg, "Nm", "Not much", true, dataFolderName);
			msg = Main.replaceWord(msg, "nm", "not much", false, dataFolderName);
			msg = Main.replaceWord(msg, "Min", "Minute", true, dataFolderName);
			msg = Main.replaceWord(msg, "min", "minute", false, dataFolderName);
			msg = Main.replaceWord(msg, "mc", "Minecraft", false, dataFolderName);
			msg = Main.replaceWord(msg, "Tbh", "To be honest", true, dataFolderName);
			msg = Main.replaceWord(msg, "tbh", "to be honest", false, dataFolderName);
			msg = Main.replaceWord(msg, "Btw", "By the way", true, dataFolderName);
			msg = Main.replaceWord(msg, "btw", "by the way", false, dataFolderName);
			msg = Main.replaceWord(msg, "Cya", "See you", true, dataFolderName);
			msg = Main.replaceWord(msg, "cya", "see you", false, dataFolderName);
			msg = Main.replaceWord(msg, "Atm", "At the moment", true, dataFolderName);
			msg = Main.replaceWord(msg, "atm", "at the moment", true, dataFolderName);
			msg = Main.replaceWord(msg, "Imo", "In my opinion", true, dataFolderName);
			msg = Main.replaceWord(msg, "imo", "in my opinion", false, dataFolderName);
			msg = Main.replaceWord(msg, "Dood", "Dude", true, dataFolderName);
			msg = Main.replaceWord(msg, "dood", "dude", false, dataFolderName);
			msg = Main.replaceWord(msg, "Dude", "Man", true, dataFolderName);
			msg = Main.replaceWord(msg, "dude", "man", false, dataFolderName);
			msg = Main.replaceWord(msg, "Woot", "Hurrah", true, dataFolderName);
			msg = Main.replaceWord(msg, "woot", "hurrah", false, dataFolderName);
			/*msg = replaceWord(msg, "", "", true, dataFolderName);
			msg = replaceWord(msg, "", "", false, dataFolderName);*/
			msg = msg.replaceAll("(?i)\\bbud der\\b", "gold");
		} catch(ArrayIndexOutOfBoundsException e) {
			FileMgmt.LogCrash(e, "GrammarEnforcement()", "A bad regex was used in the function \"replaceWord(String msg(\"" + msg + "\"), Player chatter(\"" + chatter.getName() + "\"), String dataFolderName(\"" + dataFolderName + "\"))\"!", true, dataFolderName);
		}
		return msg.trim();
	}
	
	/** @param msg String
	 * @return The given String, with the first letter capitalized. If the first
	 *         character is not a letter, then this function only returns the
	 *         same String. */
	public static String capitalizeFirstLetter(String msg) {
		if(msg.length() >= 1) {
			msg = msg.substring(0, 1).toUpperCase() + msg.substring(1, msg.length());
		}
		return msg;
	}
	
	public static int getRandomIntValBetween(int Low, int High) {
		Random r = new Random();
		int R = r.nextInt(High - Low) + Low;
		return R;
	}
	
	/** <pre>
	 * public static String unSpecifiedVarWarning(final String warningVar, final String fileName, final String pluginName) {
	 * 	sendConsoleMessage(pluginName + &quot;&amp;eWarning! \&quot;&quot; + warningVar + &quot;\&quot; was not specified(or was set incorrectly) in the \&quot;&quot; + fileName + &quot;\&quot; file!&amp;z&amp;eHas the existing \&quot;&quot; + fileName + &quot;\&quot; file not been updated from a past version?&quot;);
	 * 	return warningVar;
	 * }
	 * </pre>
	 * 
	 * @param warningVar The variable in question
	 * @param fileName The name of the config.yml file used
	 * @param pluginName The name of the plugin in question
	 * @return The variable in question */
	public static String unSpecifiedVarWarning(final String warningVar, final String fileName, final String pluginName) {
		Main.sendConsoleMessage(pluginName + "&eWarning! \"" + warningVar + "\" was not specified(or was set incorrectly) in the \"" + fileName + "\" file!&z&eHas the existing \"" + fileName + "\" file not been updated from a past version?");
		return warningVar;
	}
	
	/** Sends the specified target the message if the message hasn't been sent
	 * before during this server session. If no target is specified, or the
	 * target that is specified does not exist, then this function broadcasts
	 * the parameter str.
	 * 
	 * @param str String
	 * @param target String
	 * @return True if a message was sent, false otherwise. */
	public static boolean sendOneTimeMessage(String str, String target) {
		if(!StringUtils.isNotEmpty(str)) {
			return false;
		}
		boolean hasMessageBeenSentBefore = false;
		for(String curMsg : Main.oneTimeMessageList) {
			if(str.equalsIgnoreCase(curMsg)) {
				hasMessageBeenSentBefore = true;
				break;
			}
		}
		if(hasMessageBeenSentBefore == false) {
			Player plyr = Main.getPlayer(target);
			if(plyr != null) {
				Main.sendMessage(plyr, str);
			} else if(target.equalsIgnoreCase("console") || target.equals("!")) {
				Main.sendConsoleMessage(str);
			} else {
				Main.server.broadcastMessage(Main.formatColorCodes(str));
			}
			return true;
		}
		return false;
	}
	
	/** Takes the given command string(cmd, which is everything you type) and
	 * cuts the actual commandLabel(the command you used) out, resulting in only
	 * the arguments.
	 * 
	 * @param cmd
	 * @return The arguments of the given command, in String form.
	 * @since 4.9
	 * @see CommandMgmt#getCommandFromMsg(String) */
	public static String getStringArgumentsFromCommand(String cmd) {// TODO Move this to the Main
		String args = "";
		//String command = Main.getCommandFromMsg(cmd);
		if(cmd.indexOf(" ") >= 1) {
			args = cmd.trim().substring(cmd.indexOf(" ")).trim();
			//showDebugMsg("&aDebug: The command(\"" + command + "\") has the following arguments: " + args, showDebugMsgs);
		} else {
			//showDebugMsg("&aDebug: The command(\"" + command + "\") had no arguments.", showDebugMsgs);
		}
		return args;
	}
	
	/** Takes the given command string(cmd, which is everything you type) and
	 * cuts the actual commandLabel(the command you used) out, resulting in only
	 * the arguments.
	 * 
	 * @param cmd
	 * @return The arguments of the given command, in String form.
	 * @since 4.9
	 * @see CommandMgmt#getCommandFromMsg(String) */
	public static String getStringArgumentsFromCommand(String[] args) {// TODO Move this to the Main
		String strArgs = "";
		for(String curArg : args) {
			strArgs += curArg + " ";
		}
		strArgs = strArgs.trim();
		return strArgs;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		ArrayList<String> rtrn = new ArrayList<>();
		String command = Main.getCommandFromMsg(cmd.getName());
		if(command.equalsIgnoreCase("uuid")) {
			for(String[] curEntry : Main.uuidMasterList.getPlayerUUIDList()) {
				rtrn.add(curEntry[1]);
			}
		}
		return((rtrn.size() != 0) ? rtrn : null);
	}
	
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static final boolean startsWithIgnoreCase(String str1, String str2) {
		str1 = str1.toLowerCase();
		str2 = str2.toLowerCase();
		return str1.startsWith(str2);
	}
	
	public static final boolean endsWithIgnoreCase(String str1, String str2) {
		str1 = str1.toLowerCase();
		str2 = str2.toLowerCase();
		return str1.endsWith(str2);
	}
	
	protected static boolean	shutdownInProgress		= false;
	protected static boolean	shutdownTaskIsCancelled	= false;
	
	public void stopServer(final String strargs) {
		final String strArgs = strargs.trim();
		Bukkit.getServer().getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
			@Override
			public void run() {
				Main.shutdownInProgress = true;
				if(!strArgs.equalsIgnoreCase("nowait")) {
					for(int i = 10; i > 0; i--) {
						if(Main.shutdownTaskIsCancelled) {
							break;
						}
						String message = "&f[&5Server&f]&5" + (strArgs.isEmpty() ? " Server will restart in TIME seconds!" : " " + strArgs).replace("TIME", i + "").replace("seconds", (i == 1 ? "second" : "seconds"));
						Main.broadcast(message);
						Main.sleep(1000);
					}
					if(Main.shutdownTaskIsCancelled) {
						Main.shutdownTaskIsCancelled = false;
						Main.shutdownInProgress = false;
						return;
					}
					String message = "&f[&5Server&f]&5 Restarting server...";
					Main.broadcast(message);
				}
				Bukkit.dispatchCommand(Main.console, "save-all");
				Main.sleep(250);
				try {
					Bukkit.dispatchCommand(Main.console, "stop" + (strArgs.isEmpty() ? "" : " " + strArgs));
				} catch(Throwable ignored) {
				}
				Main.shutdownInProgress = false;
				//onDisable();
			}
		});
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String command, final String[] args) {
		if(Main.uuidMasterList.onCommand(sender, cmd, command, args)) {
			return true;
		}
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
		Player user = Main.getPlayer(sender.getName());
		String userName = sender.getName();
		if(user != null) {
			userName = user.getDisplayName();
		}
		if(userName.equals("") == true) {
			userName = sender.getName();
		}
		boolean uuidMasterListCmd = Main.uuidMasterList.onCommand(sender, cmd, command, args);
		if(uuidMasterListCmd) {
			return true;
		}
		if(command.equalsIgnoreCase("exit")) {
			if(sender.hasPermission("newplugin.exit")) {
				if(args.length == 1 && (args[0].equalsIgnoreCase("-a") || args[0].equals("abort"))) {
					if(Main.shutdownInProgress) {
						Main.shutdownTaskIsCancelled = true;
						Main.broadcast("&5[Server] The restart was aborted by \"&f" + userName + "&r&5\".");
					}
					return true;
				}
				this.stopServer(strArgs);
			} else {
				Main.sendMessage(sender, Main.pluginName + Main.noPerm + "\n&6[CONSOLE --> me]: &eWhy is you tryin' tah stahp teh servers???/");
			}
			return true;
		}
		if(command.equalsIgnoreCase("enteisnewplugin") || command.equalsIgnoreCase("newplugin")) {
			if(args.length >= 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					boolean userHasPerm = false;
					if(user != null) {
						userHasPerm = (user.hasPermission("newplugin.reload") || user.hasPermission("newplugin.*") || user.isOp());
					} else {
						userHasPerm = true;
					}
					if(userHasPerm == true || user == null) {
						boolean reloaded = YamlMgmtClass.LoadConfig();
						if(reloaded == true) {
							if(sender.equals(Main.console) == false) {
								Main.sendMessage(sender, Main.pluginName + "&2Configuration files successfully reloaded!");
							} else {
								Main.showDebugMsg(Main.pluginName + "&aYaml configuration files reloaded successfully!", Main.showDebugMsgs);
							}
						} else {
							if(sender.equals(Main.console) == false) {
								Main.sendMessage(sender, Main.pluginName + "&cThere was an error when reloading the configuration files.");
							} else {
								Main.showDebugMsg(Main.pluginName + "&eSome of the yaml configuration files failed to load successfully, check the server log for more information.", Main.showDebugMsgs);
							}
						}
					} else {
						Main.sendMessage(sender, Main.noPerm);
					}
					return true;
				} else if(args[0].equalsIgnoreCase("save")) {
					boolean userHasPerm = false;
					if(user != null) {
						userHasPerm = (user.hasPermission("newplugin.save") || user.hasPermission("newplugin.*") || user.isOp());
					} else {
						userHasPerm = true;
					}
					if(userHasPerm == true || user == null) {
						boolean saved = YamlMgmtClass.saveYamls();
						if(saved == true) {
							if(sender.equals(Main.console) == false) {
								Main.sendMessage(sender, Main.pluginName + "&2The configuration files saved successfully!");
							} else {
								Main.showDebugMsg(Main.pluginName + "&aThe yaml configuration files were saved successfully!", Main.showDebugMsgs);
							}
						} else {
							if(sender.equals(Main.console) == false) {
								Main.sendMessage(sender, Main.pluginName + "&cThere was an error when saving the configuration files.");
							} else {
								Main.showDebugMsg(Main.pluginName + "&eSome of the yaml configuration files failed to save successfully, check the crash-reports.txt file for more information.", Main.showDebugMsgs);
							}
						}
					} else {
						Main.sendMessage(sender, Main.noPerm);
					}
					return true;
				} else if(args[0].equalsIgnoreCase("info")) {
					boolean userHasPerm = false;
					if(user != null) {
						userHasPerm = (user.hasPermission("newplugin.info") || user.hasPermission("newplugin.*") || user.isOp());
					} else {
						userHasPerm = true;
					}
					if(userHasPerm || user == null) {
						if(args.length == 1) {
							String authors = "\"";
							for(String curAuthor : Main.pdffile.getAuthors()) {
								authors += curAuthor + "\", \"";
							}
							if(authors.equals("\"") == false) {
								authors += ".";
								authors = authors.replace("\", \".", "\"");
							} else {
								authors = "&oNone specified in plugin.yml!&r";
							}
							Main.sendMessage(sender, Main.green + Main.pdffile.getPrefix() + " " + Main.pdffile.getVersion() + "; Main class: " + Main.pdffile.getMain() + "; Author(s): (" + authors + "&a).");
						} else {
							Main.sendMessage(sender, Main.pluginName + "&eUsage: /" + command + " info");
						}
						//return true;
					} else {
						Main.sendMessage(sender, Main.pluginName + Main.noPerm);
					}
					return true;
				} else {
					Main.sendMessage(sender, Main.pluginName + "&eUsage: \"/" + command + " info\" or use an admin command.");
				}
				return true;
			}
			Main.sendMessage(sender, Main.pluginName + "&eUsage: \"/" + command + " info\" or use an admin command.");
			return true;
		} else if(command.equalsIgnoreCase("newcommandhere")) {
			
			return true;
		} else {
			return false;
		}
	}
	
}
