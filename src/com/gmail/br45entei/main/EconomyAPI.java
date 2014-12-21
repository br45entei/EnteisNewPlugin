package com.gmail.br45entei.main;

import org.bukkit.entity.Player;

/** @author Brian_Entei */
public class EconomyAPI {
	public Main	plugin;
	
	public EconomyAPI(Main plugin) {
		this.plugin = plugin;
	}
	
	public static boolean hasAccount(Player playerName) {
		boolean accountExists = Main.econ.hasAccount(playerName);
		return accountExists;
	}
	
	public static boolean createAccount(Player playerName) {
		boolean successful = Main.econ.createPlayerAccount(playerName);
		return successful;
	}
	
	public static double getBalance(Player player) {
		return Main.econ.getBalance(player);
	}
	
	public static boolean deposit(Player player, double amtToAdd) {
		return Main.econ.depositPlayer(player, amtToAdd).transactionSuccess();
	}
	
	public static boolean withdraw(Player player, double amtToSubtract) {
		return Main.econ.withdrawPlayer(player, amtToSubtract).transactionSuccess();
	}
	
}
