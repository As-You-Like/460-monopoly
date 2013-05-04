package com.example.controllers;

import java.util.ArrayList;

import com.example.bluetooth.Message;
import com.example.content.EventSetup;
import com.example.model.PlayerPiece;
import com.example.model.Tile;
import com.example.model.Unit;

public class Player {
	public static Player[] entities;
	
	private double balance;
	private PlayerPiece piece;
	private PlayerDevice device;
	private int color;
	private boolean jailed = false;
	private boolean sick = false;
	private int playerIndex;
	private boolean lost = false;
	private int tradeCount = 0;
	
	public static String[] COLOR_NAMES = new String[Device.player.length];
	
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
		this.piece.move(Tile.getJailTile());
		
		EventGenerator.registerEvent("newTurn", new EventSetup.EventJailRelease(EventSetup.JAIL_RELEASE, this.getPlayerIndex()));
		
		//place jail tile where null is
		//this.piece.move(null);
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public boolean isLost() {
		return lost;
	}

	public void setLost(boolean lost) {
		this.lost = lost;
	}

	public int getTradeCount() {
		return tradeCount;
	}

	public void setTradeCount(int tradeCount) {
		this.tradeCount = tradeCount;
	}
	
	/**
	 * Provides events owned by this player that have countdown timers
	 * This method is intended for database storage purposes
	 * @return
	 */
	public Event[] getPlayerEvents(){
		return EventGenerator.getPlayerTriggeredEvents(this.playerIndex);
	}
	
	public Tile[] getPlayerTiles(){
		ArrayList<Tile> result = new ArrayList<Tile>();
		for (int i=0; i<Unit.entity.size(); i++){
			Unit u = Unit.entity.get(i);
			if (u instanceof Tile){
				Tile t = (Tile)u;
				if (t.getOwner() == this.playerIndex){
					result.add(t);
				}
			}
			
		}
		return result.toArray(new Tile[]{});
	}
	
	public double countAssets(){
		Tile[] tiles = this.getPlayerTiles();
		double result = 0;
		for (int i=0; i<tiles.length; i++){
			Tile t = tiles[i];
			result += t.getPrice();
		}
		
		return result;
	}
	
	public boolean isTileOwnedByPlayer(Tile tile){
		Tile[] tiles = this.getPlayerTiles();
		for (int b=0; b<tiles.length; b++){
			Tile t = tiles[b];
			if (t.id == tile.id){
				return true;
			}
		}
		return false;
	}
	
	public boolean isRegionComplete(int region){
		boolean result = false;
		for (int a=0; a<Unit.entity.size(); a++){
			Unit u = Unit.entity.get(a);
			if (u instanceof Tile){
				Tile t = (Tile)u;
				if (t.getRegion() == region && this.isTileOwnedByPlayer(t)){
					result = true;
				} else if(t.getRegion() == region){
					return false;
				}
			}
		}
		return result;
	}
	
	public int countCompletedRegions(){
		int result = 0;
		for (int i=0; i<Tile.REGION_NAMES.length; i++){
			if(this.isRegionComplete(i)){
				result++;
			}
		}
		
		return result;
	}
	
	public int countShuttleStops(){
		return 0;
	}
}
