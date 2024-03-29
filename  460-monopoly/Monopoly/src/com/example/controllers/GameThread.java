package com.example.controllers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.BluetoothEvent;
import com.example.bluetooth.Message;
import com.example.model.Die;
import com.example.model.PlayerPiece;
import com.example.model.Tile;
import com.example.model.Unit;
import com.example.monopoly.DataLoadingActivity;
import com.example.monopoly.LoadingActivity;
import com.example.monopoly.MapActivity;
import com.example.monopoly.SaveGameActivity;

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
	public DatabaseThread dbt;
	
	// isFromSavedGame: determines if game was loaded from New Game (false) or from Saved Game (true)
	public static boolean isFromSavedGame = false;
	public boolean isQuitting = false;
	
	// Determines if victory has been achieved by a player
	//public boolean gameWon = false;
	
	// Determines how many full turns have past (increments every time subTurnNumber == numberOfPlayers)
	public int turnNumber = 0;
	
	// Determines which player is having his/her turn
	public int subTurnNumber = 0;
	
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
				Die.roll();
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
				GameThread.gt.awaken();
			}

		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				return type == Message.TILE_ACTIVITY_END_TURN;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				GameThread.gt.awaken();
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
				Device.player[Game.currentPlayer].sendMessage(Message.PURCHASE_SUCCESS, "");
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
				return type == Message.REQUEST_HOME_DATA;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				//create a JSON string out of the data above
				JSONObject map = new JSONObject();
				Player p = Player.entities[sender];
				try {
					map.put("playerName", p.getName());
					map.put("playerColor", Player.COLOR_NAMES[sender]);
					map.put("playerCash", ""+p.getBalance());
					map.put("playerAssets", ""+p.countAssets());
					map.put("playerOwned", ""+p.getPlayerTiles().length);
					map.put("playerCompleted", ""+p.countCompletedRegions());
					map.put("playerTrade", ""+p.getTradeCount());
					map.put("playerShuttle", ""+p.countShuttleStops());
				} catch (JSONException e) {
					e.printStackTrace();
				} 
				
				String json = map.toString();
				
				Device.player[sender].sendMessage(Message.DATA_HOME_TAB, json);
			}

		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.REQUEST_PROPERTIES_DATA;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				Tile[] properties = Player.entities[sender].getPlayerTiles();
				JSONArray json = new JSONArray();
				for (Tile t : properties){
					JSONObject tile = new JSONObject();
					try {
						tile.put("id", t.id);
						tile.put("name", t.getName());
						tile.put("region", t.getRegion());
						json.put(tile.toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Device.player[sender].sendMessage(Message.REQUEST_PROPERTIES_DATA_ACCEPT, json.toString());
			}
			
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				return type == Message.TILE_ACTIVITY_UPGRADE_PROPERTY;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				int upgrade = Integer.parseInt(message);
				Player p = Player.entities[Game.currentPlayer];
				Tile tile = p.getPiece().getCurrentTile();
				
				//pay for the cost of the property
				p.subBalance(tile.getUpgradePrice(upgrade)); //temporarily deduct 100
				
				//upgrade the property
				tile.upgrade(upgrade);
				
				Device.player[sender].sendMessage(Message.UPGRADE_SUCCESS, ""+upgrade);
				
				/*switch(upgrade){
				case Tile.UPGRADE_ELECTRICAL:
					break;
				case Tile.UPGRADE_PLUMBING:
					break;
				case Tile.UPGRADE_VENDING:
					break;
				case Tile.UPGRADE_HVAC:
					break;
				}*/
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
		
		
		
		if(isFromSavedGame == false){
			this.setUpBoard();
		}
		isFromSavedGame = true;
		Game.instance.determinePlayerTurnOrder();
		
		//Wait 2 seconds to allow the phones to catch up
		synchronized (this){
			try {
				this.wait(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		while(Game.gameWon == false && this.isQuitting == false){
			try {
				
				/**
				 * Note: reset all global variables you use between turns (to avoid raw data of one turn interfering with another turn)
				 */
				// Movement Phase

				//Figure out who's turn it is
				Game.instance.determineCurrentTurnPlayer();
				
				
				//Weekly Stipends and other start of turn events
				EventGenerator.executeTriggeredEvents("newTurn");
				EventGenerator.chooseAndExecuteRandomEvent("newTurn", 0.1);
				
				
					//Override Home Tab
					this.startSubTurn();
					
					// Player Movement Subphase
					this.startMovementPhase();
				if (!Player.entities[Game.currentPlayer].isJailed()){
					// Decision Phase
					this.startDecisionPhase();
				}
				// Conclusion Phase
				this.startConclusionPhase();
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
			 * Victory
			 */
				
				
			} catch (Exception e) {			
				e.printStackTrace();
				this.isQuitting = true;
				Game.timer.cancel();
				
			}
		}	
		
	}
	
	public void setUpBoard(){
		for(int i = 0; i < Player.entities.length; i++){
			if (Player.entities[i] != null){
				Player.entities[i].setPiece(new PlayerPiece(Tile.entity[3][0], i));
			}
		}
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
			if(playerTurnOrder[i] == subTurnNumber){
				currentTurnPlayer = playerTurnOrder[i];
			}
		}
	}*/
	
	public void startSubTurn(){
		Device.player[Game.currentPlayer].sendMessage(Message.START_PLAYER_TURN, "");
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
	
	public synchronized void awaken(){
		GameThread.gt.notifyAll();
	}
	
	public void startMovementPhase(){
		boolean isDouble = false;
		do{
			Device.player[Game.currentPlayer].sendMessage(Message.MOVEMENT_ROLL, isDouble ? "You have rolled a double, roll again" : "It's your turn, press to roll dice");
			this.sleepGameThread();
			Log.e("MovementPhase", "Execution");
			if(Player.entities[Game.currentPlayer].isJailed() == false){
				if(Die.doubleCount < 3){
					//get the player piece
					PlayerPiece currentPlayerPiece = Player.entities[Game.currentPlayer].getPiece();
					
					//calculate how much to move
					int spaceMovementDistance = Die.getTotalValue();
					
					//create tracker for how much player has moved
					int spacesMoved = 0;
					
					//get the current location of the player
					Tile tileLocation = currentPlayerPiece.getCurrentTile();
					
					//create tracker for the new location of the player
					Tile newTileLocation = null;
//					int tileLocation = PlayerEntity.getPlayer(currentTurnPlayer).getCurrentLocation();
//					int newTileLocation;
					
					while(spacesMoved != spaceMovementDistance){
						
						tileLocation = currentPlayerPiece.getCurrentTile();
						if(tileLocation.hasFork()){
							Tile[] forks = tileLocation.getForkTiles();
							ArrayList<Tile> forksAL = new ArrayList<Tile>();
							for(int i = 0; i < forks.length; i++){
								forksAL.add(forks[i]);
							}
							
							//temporary random movement code until DecisionActivity is ready
							//newTileLocation = forks[(int) (Math.random()*forks.length)];
							//currentPlayerPiece.move(newTileLocation);
							
							for(int i = 0; i < forksAL.size(); i++){
								if(forksAL.get(i).id == (Player.entities[Game.currentPlayer].getPiece().getPreviousTile().id)){
									forksAL.remove(i);
								}
							}
							forks = new Tile[forksAL.size()];
							for(int i = 0; i < forks.length; i++){
								forks[i] = forksAL.get(i);
							}
							
							Log.i("", "Forks.length = " + forks.length);
							if(forks.length > 1){
								Log.i("", "Send message");
								Device.player[Game.currentPlayer].sendMessage(Message.CHOOSE_FORK_PATH, forks[0] + ":" + forks[1]);
								this.sleepGameThread();
								// DecisionActivity awakens GameThread
							}
							else{
								newTileLocation = forks[0];
								currentPlayerPiece.move(newTileLocation);
							}
		
						}
						else {
							Log.i("", "Going to next stop // no fork");
							newTileLocation = tileLocation.getNextStop();
							currentPlayerPiece.move(newTileLocation);
						}
						spacesMoved++;
						
						if (newTileLocation != null){
							if(newTileLocation.id == 0){
								Player p = Player.entities[Game.currentPlayer];
								p.setBalance(p.getBalance() + 200);
								Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
										"You passed the Pond. You collect $200.");
							} 
						}
					}
				}
				isDouble = Die.isDouble();
			}
			else{
				Device.player[Game.currentPlayer].sendMessage(Message.IN_JAIL, "You are in jail! ");
				Log.i("", Player.entities[Game.currentPlayer] + " IS IN JAIL!");
			}
			
		}while(isDouble && (Die.doubleCount < 3));
		
	}
	
	public void startDecisionPhase(){
		//Execute a random event
		/*EventGenerator.chooseAndExecuteRandomEvent(new String[]{
			"residential"
		}, 0.9);*/
		
		//End decision phase if an event caused the player to be jailed
		if (Player.entities[Game.currentPlayer].isJailed()){
			return;
		}
		
		//Gather tile data
		Tile currentTile = Player.entities[Game.currentPlayer].getPiece().getCurrentTile();
		String tileName = currentTile.getName();
		int tileOwner = currentTile.getOwner();
		String tileOwnerName = "";
		if (tileOwner != Tile.OWNER_NEUTRAL && tileOwner != Tile.OWNER_UNOWNABLE){
			tileOwnerName = Player.entities[currentTile.getOwner()].getName();
		} 
		double tilePrice = currentTile.getPrice();
		double tileRent = currentTile.getRent();
		int region = currentTile.getRegion();
		String regionName = Tile.REGION_NAMES[region];
		int regionTileCount = Tile.getTileCountInRegion(region);
		int regionOwnedTileCount = Tile.getTileCountOwnedByPlayerInRegion(region, Game.currentPlayer);
		
		//create a JSON string out of the data above
		JSONObject map = new JSONObject();
		try {
			map.put("tileName", tileName);
			map.put("tileOwner", tileOwner);
			map.put("tileId", currentTile.id);
			map.put("tileOwnerName", tileOwnerName);
			map.put("tilePrice", tilePrice);
			map.put("tileRent", tileRent);
			map.put("regionName", regionName);
			map.put("regionTileCount", regionTileCount);
			map.put("regionOwnedTileCount", regionOwnedTileCount);
			map.put("currentBalance", Player.entities[Game.currentPlayer].getBalance());
			
			map.put("upgrade1", currentTile.getUpgradePrice(0));
			map.put("upgrade2", currentTile.getUpgradePrice(1));
			map.put("upgrade3", currentTile.getUpgradePrice(2));
			map.put("upgrade4", currentTile.getUpgradePrice(3));
			
			map.put("upgrade1bool", currentTile.upgraded[0]);
			map.put("upgrade2bool", currentTile.upgraded[1]);
			map.put("upgrade3bool", currentTile.upgraded[2]);
			map.put("upgrade4bool", currentTile.upgraded[3]);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		
		String json = map.toString();
		
		//Send message
		Device.player[Game.currentPlayer].sendMessage(Message.START_TILE_ACTIVITY, json);
		
		//Sleep the thread
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
		Log.e("ConclusionPhase", "Check Defeat");
		
		//Force exception
		/*Player.entities = null; */
		
		//Check defeat condition
		if(Player.entities[Game.currentPlayer].getBalance() <= 0){
			Player.entities[Game.currentPlayer].setLost(true);
			Device.player[Game.instance.currentPlayer].sendMessage(Message.ALERT, "You Lose");
			// Ticker inject to include news headline that player lost (dark humor possibilities...)
			Game.playerTurnOrder[Game.playerTurnOrderCounter] = 666;
			Game.numberOfPlayersRemaining--;
			
			Tile[] tiles = Player.entities[Game.currentPlayer].getPlayerTiles();
			for(int i = 0; i < tiles.length; i++){
				tiles[i].setOwner(-1);
			}
			
			
			//Check victory condition
			if(Game.numberOfPlayersRemaining == 1){
				Log.e("ConclusionPhase", "GameWon = true");
				Game.gameWon = true;
			}
		}
		
		Log.e("ConclusionPhase", "Check gameWon != true");
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
				//DBHandle handle = new DBHandle();
				MapActivity.activity.openDatabase();
				Game.turn++;
			}
			
		}
		
	}
	
	/*
	//Handler for DatabaseThread
		public static class DBHandle extends Handler {
			public void handleMessage(android.os.Message msg){
				HostDevice.self = true;
				// TODO: check for HostDevice.self is redundant since the device that runs this code is always the host
				// TODO: also, we should set that variable to true :) ... this device needs to know that it's the host 
				//if(HostDevice.self){
				if(msg.what == 1){
					SaveGameActivity.populateListView();
				}
				
				else if(msg.what == 2){
					DataLoadingActivity.startGameModule();
				}
				else if(msg.what == 3){
					//dt.killLooper();
				}

			}
		}
		*/
}
