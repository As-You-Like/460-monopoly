package com.example.controllers;

import com.example.bluetooth.Message;
import com.example.model.PlayerPiece;

public class Player {
	public static Player[] entities;
	
	private double balance;
	private PlayerPiece piece;
	private PlayerDevice device;
	private int color;
	private boolean jailed = false;
	private boolean sick = false;
	private int playerIndex;
	
	public Player(PlayerDevice device, int playerIndex, int color){
		this.device = device;
		this.playerIndex = playerIndex;
		this.color = color;
	}

	public double getBalance() {
		return balance;
	}
	
	/**
	 * Adds amount to the current player's balance
	 * @param amount
	 */
	public void addBalance(double amount){
		this.setBalance(this.balance + amount);
	}
	
	/**
	 * Subtracts amount from the current player's balance
	 * @param amount
	 */
	public void subBalance(double amount){
		this.addBalance(amount * -1);
	}
	
	/**
	 * Sets the current player's balance to a specific value
	 * @param balance
	 */
	public void setBalance(double amount) {
		//change the balance
		double priorBalance = this.balance;
		this.balance = amount;
		
		//Inform the player that the balance was changed (updates the player screen if the player is watching his stats)
		Device.player[this.playerIndex].sendMessage(
				Message.PLAYER_STAT_UPDATE_BALANCE, 
				""+this.balance + ":" + (priorBalance - this.balance));
	}

	public PlayerPiece getPiece() {
		return piece;
	}

	public void setPiece(PlayerPiece piece) {
		this.piece = piece;
	}

	public PlayerDevice getDevice() {
		return device;
	}
	
	public String getName(){
		return device.name;
	}

	public int getColor() {
		return color;
	}

	public boolean isJailed() {
		return jailed;
	}

	public void setJailed(boolean jailed) {
		this.jailed = jailed;
	}

	public boolean isSick() {
		return sick;
	}

	public void setSick(boolean sick) {
		this.sick = sick;
	}
	
	public void goToJail(){
		this.jailed = true;
		
		//place jail tile where null is
		//this.piece.move(null);
	}

	public int getPlayerIndex() {
		return playerIndex;
	}
}
