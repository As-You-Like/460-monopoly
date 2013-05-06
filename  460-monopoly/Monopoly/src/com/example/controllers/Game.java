package com.example.controllers;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.model.*;
import com.example.monopoly.MapActivity; 
import com.example.monopoly.SplashActivity;

public class Game extends TimerTask{
	public static Game instance;
	public static Game.Handle mHandler = new Game.Handle();
	public static String name;
	public static String type;
	public static Timer timer;
	public static long ms = 0;
	
	public static int gameType = 0;
	
	// Determines if victory has been achieved by a player
	public static boolean gameWon = false;
	
	/**
	 * has the count of the current turn
	 */
	public static int turn = 0;
	
	/**
	 * Keeps track of which player's turn it is
	 */
	public static int currentPlayer = 0;
	
	/**
	 * Has the count of the current subturn
	 */
	public static int subturn = 0;
	
	// How many players are playing?
	public static int numberOfPlayers;
	
	// How many players are remaining?
	public static int numberOfPlayersRemaining;
	
	// In what order do players take their turns?
	public static int[] playerTurnOrder;
	
	// Determines current index of PlayerTurnOrder
	public static int playerTurnOrderCounter;
	
	//constructor to be run during the game creation process in setup module
	public Game(String name){
		Game.name = name;
		Game.instance = this;
		
	}
	
	//method to be run during loading of game module
	public static void start(){
		Game.timer = new Timer();
		Game.timer.schedule(Game.instance, 20);
		//Game.timer.scheduleAtFixedRate(Game.instance, 20, 20);
	}

	@Override
	public void run() {
		
		// Surrounded in try catch and tries to go to GameThread to force an exception if the app has crashed
		try{
			if (MapActivity.activity == null) return;
			if (SplashActivity.activity == null) {this.cancel(); return;}
			
			ms += 20;
			Unit[] tmp;
			synchronized (Unit.entity){
				tmp = Unit.entity.toArray(new Unit[]{});
			}
			for (Unit u : tmp){
				if (u instanceof MobileUnit){
					MobileUnit mu = (MobileUnit)u;
					mu.update();
				}
			}
			Game.mHandler.obtainMessage().sendToTarget();
			
			int cancelChecker = GameThread.gt.turnNumber;
			
		}
		catch(Exception e){
			Log.e("", "Application crash: timer cancelled");
			this.cancel();
		}
		
		if (Game.timer != null){
			Game.timer.schedule(Game.instance, 20);
		}
		
	}
	
	public static class Handle extends Handler {
		public void handleMessage(Message msg){
			
		}
	}
	
	public static int getWeekDay(){
		return Game.turn % 7;
	}
	
	/**
	 * Method that returns a list of events that have a turn expiration counter (for database purposes)
	 * @return
	 */
	public static Event[] getTurnSensitiveEvents(){
		return null;
	}
	
	public void determinePlayerTurnOrder(){
		
		// Counts actual number of players
		for(int i = 0; i < Device.player.length; i++){
			if(Device.player[i] != null){
				numberOfPlayers++;
				numberOfPlayersRemaining++;
			}
		}
		
		// Create array the size of the number of players to hold the players' index numbers from Device.player
		// Until dice roll mechanism is considered, index order = turn order
		playerTurnOrder = new int[numberOfPlayers];
		for(int i = 0; i < numberOfPlayers; i++){
			playerTurnOrder[i] = i;
		}
		
	}
	
	public void determineCurrentTurnPlayer(){
		currentPlayer = playerTurnOrder[playerTurnOrderCounter];
	}
	
	
}
