package com.example.controllers;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.BluetoothEvent;
import com.example.bluetooth.Message;
import com.example.monopoly.LoadingActivity;

/**
 * Main game thread of the game module. the run method of the thread contains all the logic of a single turn
 * being looped over and over again until the game concludes
 * 
 * This thread is initialized in LoadThread.run()
 * This thread is started in LoadingActivity.startGameModule()
 * @author Ryan Hebert April 5 2013
 *
 */
public class GameThread extends Thread{

	///Global Variables///
	
	public static GameThread gt;
	
	// Determines if victory has been achieved by a player
	public boolean gameWon = false;
	
	// Determines how many full turns have past (increments every time subTurnNumber == numberOfPlayers)
	public int turnNumber = 0;
	
	// Determines which player is having his/her turn
	public int subTurnNumber = 0;
	
	// How many players are playing?
	public int numberOfPlayers;
	
	// In what order do players take their turns?
	public int[] playerTurnOrder;
	
	// What player is taking his/her turn?
	public int currentTurnPlayer;
	
	// Values of movement dice
	public int valueDie1;
	public int valueDie2;
	
	//Turn Booleans
	
	public boolean doublesOnce = false;
	public boolean doublesTwice = false;
	public boolean doublesThrice = false;
	
	
	
	///Methods///
	
	public GameThread(){
		gt = this;
	}
	
	public void run(){
		
		/**
		 * Determine player order
		 * Walk player array
		 * 		each player rolls one die. highest to lowest determines order
		 * 		ties are rerolled for the players who tied
		 * 		order is set to playerOrder
		 * 
		 * <<<<<Temporary - determined by index order>>>>>
		 */
		this.determinePlayerTurnOrder();
		
		while(gameWon == false){
			/**
			 * Note: reset all global variables you use between turns (to avoid raw data of one turn interfering with another turn)
			 */
			// Movement Phase
			
				// Dice Roll Subphase
			
					/**
					 * Figure out whose turn it is
					 */
					this.determineCurrentTurnPlayer();
			
					/**
					 * Home tab overrided
					 */
					this.startSubTurn();
			
					/**
					 * Make dice screen show up
					 */
					//Done in player device (probably TurnActivity)
			
					/**
					 * Sleep
					 */
					this.sleepGameThread();
					
				
				// Player Movement Subphase
			
					/**
					 * Process dice roll
					 */
			
					/**
					 * Send out movement values
					 */
			
					/**
					 * If fork,
					 * 		tell TurnActivity to initiate fork popup
					 * 		sleep
					 */
			
					/**
					 * Movement stops on tile
					 */
			
			// Decision Phase
			
				/**
				 * Adjust player variables
				 */
			
				/**
				 * Adjust tile variables
				 */
			
				/*
				 * Initiate TileActivity
				 */
			
				/**
				 * Sleep
				 */
			
			// Conclusion Phase
			
				/**
				 * End turn
				 * If necessary, update turn and subturn counts
				 * Wipe turn booleans
				 * Update Entity classes
				 * Check victory conditions
				 * 		if elimination game, check if one player remains. if yes, gameWon = true
				 * 		if turn limit game, check if turnsExecuted = turnLimit.
				 * 			if yes, gameWon = true and flag player with highest Net Assets as winner
				 */
			
		}
		
		/**
		 * Victory
		 */
		
	}
	
	public void determinePlayerTurnOrder(){
		
		// Counts actual number of players
		for(int i = 0; i < Device.player.length; i++){
			if(Device.player[i] != null){
				numberOfPlayers++;
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
		for(int i = 0; i < numberOfPlayers; i++){
			if(playerTurnOrder[i] == subTurnNumber){
				currentTurnPlayer = playerTurnOrder[i];
			}
		}
	}
	
	public void startSubTurn(){
		Device.player[currentTurnPlayer].sendMessage(Message.START_PLAYER_TURN, "");
	}
	
	public void sleepGameThread(){
		try {
			Thread.sleep(999999999);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	public void processDiceRoll(){
		do{
			Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

				@Override
				public boolean typeValid(int type) {
					return type == Message.MOVEMENT_DICE_ROLL;
				}

				@Override
				public void processMessage(int sender, int reciever, String message) {
					GameThread.gt.resume();
					valueDie1 = Integer.parseInt(message.substring(0, 1));
					valueDie2 = Integer.parseInt(message.substring(1, 2));
					
					if(valueDie1 == valueDie2){
						Device.player[currentTurnPlayer].sendMessage(Message.ROLLED_DOUBLES, "");
					}
					
					if(doublesOnce == false && valueDie1 == valueDie2){
						doublesOnce = true;
					}
					else if(doublesTwice == false && valueDie1 == valueDie2){
						doublesTwice = true;
					} else if(doublesThrice == false && valueDie1 == valueDie2){
						doublesThrice = true;
						/**
						 * Send message to Event class to make player go to jail
						 */
					}
				}
				
			});
			
			if(doublesThrice == false){
				int spaceMovementDistance = valueDie1 + valueDie2;
				int spacesMoved = 0;
//				int tileLocation = PlayerEntity.getPlayer(currentTurnPlayer).getCurrentLocation();
//				int newTileLocation;
				
				while(spacesMoved != spaceMovementDistance){
					spacesMoved++;
/*					newTileLocation = Tile.getTile(tileLocation).nextTile()
 					boolean fork = Tile.getTile().hasFork();
					if(fork == true){
						int forkTile = tileLocation + spacesMoved;
						Device.player[currentTurnPlayer].sendMessage(Message.CHOOSE_FORK_PATH, forkTile + "");
						this.sleepGameThread();
						
						Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

							@Override
							public boolean typeValid(int type) {
								return type == Message.RECEIVE_FORK_PATH;
							}

							@Override
							public void processMessage(int sender, int reciever, String message) {
								GameThread.gt.resume();
								int forkChoice = Integer.parseInt(message.substring(0,1));
								
							}
				
						});
					}
*/
				}
			}
			
		}
			while(valueDie1 == valueDie2 && doublesThrice == false);
		
		
	}
	
}
