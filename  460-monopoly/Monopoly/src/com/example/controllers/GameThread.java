package com.example.controllers;

import android.util.Log;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.BluetoothEvent;
import com.example.bluetooth.Message;
import com.example.model.Die;
import com.example.model.PlayerPiece;
import com.example.model.Tile;
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
	//public boolean gameWon = false;
	
	// Determines how many full turns have past (increments every time subTurnNumber == numberOfPlayers)
	//public int turnNumber = 0;
	
	// Determines which player is having his/her turn
	//public int subTurnNumber = 0;
	
	// How many players are playing?
	//public int numberOfPlayers;
	
	// In what order do players take their turns?
	//public int[] playerTurnOrder;
	
	// What player is taking his/her turn?
	//public int currentTurnPlayer;
	
	
	///Methods///
	
	public GameThread(){
		gt = this;
		
		//Bluetooth Messages
		Log.w(null, "Test1");
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){
			
			@Override
			public boolean typeValid(int type) {
				Log.w(null, "Test2");
				return type == Message.MOVEMENT_DICE_ROLL;
			}
			
			@Override
			public void processMessage(int sender, int reciever, String message) {
				/*GameThread.gt.resume();
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
				
				
				}
				*/
				Log.w(null, "Test3");
				Die.roll();
				Log.w(null, "Test4");
			}
			
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				return type == Message.RECEIVE_FORK_PATH;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				int forkChoice = Integer.parseInt(message.substring(0,1));
				Tile[] forks = Player.entities[Game.currentPlayer].getPiece().getCurrentTile().getForkTiles();
				Player.entities[Game.currentPlayer].getPiece().move(forks[forkChoice]);
				GameThread.gt.resume();
			}

		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				return type == Message.TILE_ACTIVITY_END_TURN;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				GameThread.gt.resume();
			}

		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				return type == Message.TILE_ACTIVITY_PURCHASE_PROPERTY;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				
				// Assumes that currentPlayer has enough money (handled by TileActivity)
				Player.entities[Game.currentPlayer].subBalance(Player.entities[Game.currentPlayer].getPiece().getCurrentTile().getPrice());
				Player.entities[Game.currentPlayer].getPiece().getCurrentTile().setOwner(Game.currentPlayer);
			}

		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				return type == Message.TILE_ACTIVITY_PAY_RENT;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				Player theCurrentPlayer = Player.entities[Game.currentPlayer];
				Player ownerPlayer = Player.entities[theCurrentPlayer.getPiece().getCurrentTile().getOwner()];
				theCurrentPlayer.subBalance(theCurrentPlayer.getPiece().getCurrentTile().getRent());
				ownerPlayer.addBalance(theCurrentPlayer.getPiece().getCurrentTile().getRent());
			}

		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				return type == Message.TILE_ACTIVITY_UPGRADE_PROPERTY;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				//To do
			}

		});
		
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
		Game.instance.determinePlayerTurnOrder();
		
		while(Game.gameWon == false){
			/**
			 * Note: reset all global variables you use between turns (to avoid raw data of one turn interfering with another turn)
			 */
			// Movement Phase
			
				// Dice Roll Subphase
			
					/**
					 * Figure out whose turn it is
					 */
					Game.instance.determineCurrentTurnPlayer();
			
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
					this.startMovementPhase();
			
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
					this.startDecisionPhase();
			
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
	
	/*public void determinePlayerTurnOrder(){
		
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
		
	}*/
	
	/*public void determineCurrentTurnPlayer(){
		for(int i = 0; i < numberOfPlayers; i++){
			if(playerTurnOrder[i] == Game.subturn){
				currentTurnPlayer = playerTurnOrder[i];
			}
		}
	}*/
	
	public void startSubTurn(){
		Device.player[Game.instance.currentPlayer].sendMessage(Message.START_PLAYER_TURN, "");
	}
	
	public synchronized void sleepGameThread(){
		try {
			//Thread.sleep(999999999);
			this.wait(99999999);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	public void startMovementPhase(){
		do{
			if(Die.doubleCount < 3){
				PlayerPiece currentPlayerPiece = Player.entities[Game.currentPlayer].getPiece();
				int spaceMovementDistance = Die.getTotalValue();
				int spacesMoved = 0;
				Tile tileLocation = currentPlayerPiece.getCurrentTile();
				Tile newTileLocation;
//				int tileLocation = PlayerEntity.getPlayer(currentTurnPlayer).getCurrentLocation();
//				int newTileLocation;
				
				while(spacesMoved != spaceMovementDistance){
					spacesMoved++;
					tileLocation = currentPlayerPiece.getCurrentTile();
					if(tileLocation.hasFork()){
						Tile[] forks = tileLocation.getForkTiles();
						Device.player[Game.currentPlayer].sendMessage(Message.CHOOSE_FORK_PATH, forks[0] + ":" + forks[1]);
						this.sleepGameThread();
						// DecisionActivity awakens GameThread
	
					}
					else {
						newTileLocation = tileLocation.getNextStop();
						currentPlayerPiece.move(newTileLocation);
					}

				}
			}
			
		}
			while(Die.isDouble() && (Die.doubleCount < 3));
		
	}
	
	public void startDecisionPhase(){
		int tileOwner = Player.entities[Game.currentPlayer].getPiece().getCurrentTile().getOwner();
		Device.player[Game.currentPlayer].sendMessage(Message.START_TILE_ACTIVITY, "" + tileOwner);
		this.sleepGameThread();
		//TileActivity awakens GameThread
		
	}
	
	public void startConclusionPhase(){
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
		

		
		/**
		 * Update Entity classes
		 */
		
		//Check defeat condition
		if(Player.entities[Game.currentPlayer].getBalance() <= 0){
			Player.entities[Game.currentPlayer].setLost(true);
			Device.player[Game.instance.currentPlayer].sendMessage(Message.ALERT, "You Lose");
			// Ticker inject to include news headline that player lost (dark humor possibilities...)
			Game.playerTurnOrder[Game.playerTurnOrderCounter] = 666;
			Game.numberOfPlayersRemaining--;
			
			//Check victory condition
			if(Game.numberOfPlayersRemaining == 1){
				Game.gameWon = true;
			}
		}
		
		if(Game.gameWon != true){
			
			//Decide who is next
			do{
				Game.playerTurnOrderCounter++;
				if(Game.playerTurnOrderCounter == Game.numberOfPlayers){
					Game.playerTurnOrderCounter = 0;
				}
				Game.subturn++;
			}
			while(Game.playerTurnOrder[Game.playerTurnOrderCounter] == 666);
			
			if((Game.subturn % Game.numberOfPlayers) == 0){
				Game.turn++;
			}
			
		}
		
	}
	
}
