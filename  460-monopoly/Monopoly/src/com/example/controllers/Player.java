package com.example.controllers;

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

	public void setBalance(double balance) {
		this.balance = balance;
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
