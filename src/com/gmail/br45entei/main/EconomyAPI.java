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
	
	public static double getBalance(String playerName) {
		return Main.econ.bankBalance(playerName).balance;
	}
	
	public static boolean deposit(String playerName, double amtToAdd) {
		boolean success = Main.econ.bankDeposit(playerName, amtToAdd).transactionSuccess();
		return success;
	}
	
	public static boolean withdraw(String playerName, double amtToSubtract) {
		boolean success = Main.econ.bankWithdraw(playerName, amtToSubtract).transactionSuccess();
		return success;
	}
	
}
