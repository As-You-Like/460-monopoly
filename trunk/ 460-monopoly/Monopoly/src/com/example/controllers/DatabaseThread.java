package com.example.controllers;

import java.util.ArrayList;

import com.example.content.BoardSetup;
import com.example.content.EventSetup;
import com.example.content.Image;
import android.content.BroadcastReceiver;
import android.content.Intent;

import com.example.model.PlayerPiece;
import com.example.model.Tile;
import com.example.model.Unit;
import com.example.monopoly.DataLoadingActivity;
import com.example.monopoly.LoadingActivity;
import com.example.monopoly.R;
import com.example.monopoly.SaveGameActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.CalendarContract.Events;
import android.database.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;
import android.content.*;

public class DatabaseThread extends Thread {
	
// ALL TO TEST
	// (1) Create tables, if they don't exist (SQL checks that)
	// (2) Enforce referential integrity
	// (3) IF SAVING:
	//			(a) Get info from Game and insert into database via SQL
	//			(b) Loop through the Players: get info from Player and insert into database via SQL
	//			(c) Loop through the Players: loop through each Player's owned Tiles: get info from Tile and insert into database via SQL
	//			(d) Loop through the Players: loop through each Player's turn-based events: get info from Event and insert into database via SQL
	//			(e) Log confirmation of successful save
	// (4) IF LOADING: (TO DO)
	//			(a) Execute BoardSetup.setupBoard();
	//			(b) Load everything
	//          	(1) Follow order from LoadThread
	//						Exception being setupPlayers()
	//							Create PlayerDevice objects - then do setupPlayers() (do not do the colors piece)
	//							Update most things
	//							Then instantiate and start GameThread
	//							consider changes to GameThread to account for differences
	//							Update some other things?
	//			(c) Get info for everything from SQL Select statements; store them to Cursors
	//			(d) Convert Cursors into usable arrays of ints and Strings
	//			(e) Get info from singular GameQuery array; insert game info into Game class
	//			(g) Loop through PlayerQuery array; get info from each Player; insert player info into Player and Device classes (create objects)
	//			(f) Loop through Player array; loop through TileQuery array; get info of each tile for each player; insert tile info into Tile class
	//			(g) Loop through Player array; loop through TurnEventInstanceQuery array; get info of each turn-based event for each player; insert event info into Event class
	
	
	
	
	
	//     //     //     //     //     //
	
	//        General Variables        //
	
	//     //     //     //     //     //
	
	public static DatabaseThread dt;
	public static boolean isLoad;
	public static boolean isGettingGameNames;
	public static final String DATABASE_NAME = "smartstartupsdatabase4.db";
	public volatile Looper mMyLooper;
	public String selectedGameName = "";
	public String[] listViewContents;
	public SQLiteDatabase db;
	public static String[][][] relationship = {
		//  {{parent_table  | parent_key | child_table              | child_key    }, {cascade_delete}}
			{{"GameTable"   , "GameName" , "PlayerTable"            , "GameName"   }, {"true"        }},
			{{"PlayerTable" , "PlayerID" , "TurnEventInstanceTable" , "PlayerID"   }, {"true"        }}
	};
	
	//{{"PlayerTable" , "PlayerID" , "TileTable"              , "OwnerID"    }, {"true"        }},
	
	public String gameTableName = "GameTable";
	public String playerTableName = "PlayerTable";
	public String tileTableName = "TileTable";
	public String turnEventInstanceTableName = "TurnEventInstanceTable";
	public static boolean gameMade = false;
	
	//private DBHandle handler;
	
	
	
	//     //     //     //     //     //
	
	//     Saving Method Variables     //
	
	//     //     //     //     //     //
	
	public String gameName = "GameName";
	public String gameType = "GameType";
	public int turnCount = 0;
	public int playerID = 0;
	ArrayList<Integer> playerNumber;
	//int playerNumber = 0;
	ArrayList<String> playerName;
	//String playerName = "PlayerName";
	ArrayList<String> playerColor;
	//String playerColor = "PlayerColor";
	ArrayList<String> bluetoothAddress;
	//String bluetoothAddress = "BluetoothAddress";
	ArrayList<String> currentLocation;
	//String currentLocation = "CurrentLocation";
	ArrayList<String> previousLocation;
	//String previousLocation = "PreviousLocation";
	ArrayList<Integer> netCash;
	//int netCash = 0;
	ArrayList<Integer> tradeCount;
	//int tradeCount = 0;
	ArrayList<ArrayList<Integer>> tileID;
	//int tileID = 0;
	public int turnEventInstanceID = 0;
	ArrayList<ArrayList<Integer>> eventNumber;
	//int eventNumber = 0;
	ArrayList<ArrayList<Integer>> eventTurnsLeft;
	//int eventTurnsLeft = 0;
	
	
	
	
	//     //     //     //     //     //
	
	//     Loading Method Variables     //
	
	//     //     //     //     //     //
	
	public Cursor gameQuery;
	public Cursor playerQuery;
	public Cursor tileQuery;
	public Cursor upgradeQuery;
	public Cursor turnEventInstanceQuery;
	
	public String[] st = new String[0];
	
	public String[] tableGame_fieldAll;
	public String[] tableGame_fieldGameName;
	
	public int[] tablePlayer_fieldPlayerNumber;
	public String[] tablePlayer_fieldPlayerName;
	public String[] tablePlayer_fieldPlayerColor;
	public String[] tablePlayer_fieldBluetoothAddress;
	public String[] tablePlayer_fieldCurrentLocation;
	public String[] tablePlayer_fieldPreviousLocation;
	public int[] tablePlayer_fieldNetCash;
	public int[] tablePlayer_fieldTradeCount;
	
	int[] tableTile_fieldOwnerID;
	int[] tableTile_fieldElectricalBought;
	int[] tableTile_fieldPlumbingBought;
	int[] tableTile_fieldVendingBought;
	int[] tableTile_fieldHVACBought;
	
	int[] tableTurnEventInstance_fieldPlayerID;
	int[] tableTurnEventInstance_fieldEventNumber;
	int[] tableTurnEventInstance_fieldEventTurnsLeft;
	
	
	
	public void run(){
		dt = this;
		
		Log.i("", "Thread start");
		//isLoad = false;
		
		if(isGettingGameNames == true){
			dt.setUpDatabase();
			dt.populateGameNameListView();
			//Message msg = handler.obtainMessage();
			//msg.what = 1;
			//msg.sendToTarget();
		}
		
		else if(isLoad == true){
			dt.setUpDatabase();
			dt.loadGame();
			//Message msg = handler.obtainMessage();
			/*msg.what = 2;
			msg.sendToTarget();*/
			dt.callDatabaseReceiverBroadcast();
		}
		
		else if(isLoad == false){
			dt.setUpDatabase();
			dt.saveGame();
			Log.i("", "Save Complete");
		}
		
		/*dt.setUpDatabase();
		dt.saveGame();
		isLoad = true;
		dt.loadGame();*/

		
		isLoad = false;
		isGettingGameNames = false;
		db.close();
		//Looper.loop();
	}
	
	
	
	
	
	
	
	
	public void setUpDatabase(){
		
		 //create database and tables
		
		 

		String sql;
		
		//SQLiteDatabase db = openOrCreateDatabase(DATABASE_NAME, 
        //        Context.MODE_PRIVATE, null);
        //db = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null);
        
        sql = "CREATE TABLE IF NOT EXISTS " + gameTableName + "(GameName VARCHAR(50) PRIMARY KEY,GameType VARCHAR(50),TurnCount INTEGER)";
        db.execSQL(sql);
        //CREATE = create table query
        
        sql = "CREATE TABLE IF NOT EXISTS " + playerTableName + " (PlayerID INTEGER PRIMARY KEY AUTOINCREMENT,GameName VARCHAR(50),PlayerNumber INTEGER, PlayerName VARCHAR(50),PlayerColor VARCHAR(50)," +
        		"BluetoothAddress VARCHAR(50),CurrentLocation VARCHAR(50),PreviousLocation VARCHAR(50),NetCash INTEGER,TradeCount INTEGER)";
        db.execSQL(sql);
        //CREATE = create table query
        
        /*
        sql = "CREATE TABLE IF NOT EXISTS " + tileTableName + "(TileID INTEGER PRIMARY KEY AUTOINCREMENT,OwnerID INTEGER,ElectricalBought INTEGER,PlumbingBought INTEGER, VendingBought INTEGER, HVACBought INTEGER)";
        db.execSQL(sql);
        //CREATE = create table query
        */
        
        sql = "CREATE TABLE IF NOT EXISTS " + turnEventInstanceTableName + "(TurnEventInstanceID INTEGER PRIMARY KEY AUTOINCREMENT,PlayerID INTEGER,EventNumber INTEGER,EventTurnsLeft INTEGER)";
        db.execSQL(sql);
        //CREATE = create table query
        
        // Model: CREATE TABLE inventory(StockNumber INTEGER PRIMARY KEY,Descrip VARCHAR(50),OnHandQuan INTEGER,PackQty INTEGER,PackCost FLOAT); 
   
       dt.enforceReferentialIntegrity();
       Log.i("", "Enforcing referential integrity");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void saveGame(){
		
		String sqlGame;
		String sqlPlayer;
		String sqlTile;
		String sqlTurnEventInstance;
		
		//Replace Game entity
		sqlGame = "REPLACE INTO " + gameTableName + "(GameName,GameType,TurnCount) VALUES ('" +
					 Game.name + "','" + Game.type + "'," + Game.turn + ");";
		Log.i("", sqlGame);
		db.execSQL(sqlGame);
		
		//Replace player entities
		for(int i = 0; i < Player.entities.length; i++){
			if(Player.entities[i] != null){
				sqlPlayer = "REPLACE INTO " + playerTableName + "(GameName,PlayerNumber,PlayerName,PlayerColor,BluetoothAddress,CurrentLocation,PreviousLocation,NetCash,TradeCount) VALUES ('" +
				 		 Game.name + "'," + Player.entities[i].getPlayerIndex() + ",'" + Player.entities[i].getName() + "','" + Player.COLOR_NAMES[i] + "','" + Device.player[i].device.getAddress() + "','" + Player.entities[i].getPiece().getCurrentTile().id +
				 		 "','" + Player.entities[i].getPiece().getPreviousTile().id + "'," + (int)Player.entities[i].getBalance() + "," + Player.entities[i].getTradeCount() + ");";
				Log.i("", sqlPlayer);
				db.execSQL(sqlPlayer);
				
				//Replace player tile ownership data
				/*
				for(int j = 0; j < Player.entities[i].getPlayerTiles().length; j++){
					if(Player.entities[i] != null){
						Tile[] tiles = Player.entities[i].getPlayerTiles();
						int electrical = 0;
						int plumbing = 0;
						int vending = 0;
						int hvac = 0;
						boolean[] upgrades = tiles[j].upgraded;
						if(upgrades[0] = true){
							electrical = 1;
						}
						if(upgrades[1] == true){
							plumbing = 1;
						}
						if(upgrades[2] == true){
							vending = 1;
						}
						if(upgrades[3] == true){
							hvac = 1;
						}
						
						sqlTile = "REPLACE INTO " + tileTableName + "(OwnerID,ElectricalBought,PlumbingBought,VendingBought,HVACBought) VALUES (" +
						        Player.entities[i].getPlayerIndex() + "," + electrical + "," + plumbing + "," + vending + "," + hvac + ");";
						Log.i("", sqlTile);
						db.execSQL(sqlTile);
					}
					
				}
				*/
				
				//Replace player events
				// TODO: Shifted the turnEvents around to reduce confusion.
				Event[] turnEvents = Player.entities[i].getPlayerEvents();
				for(int k = 0; k < turnEvents.length; k++){
					if(Player.entities[i] != null){
						sqlTurnEventInstance = "REPLACE INTO " + turnEventInstanceTableName + "(PlayerID,EventNumber,EventTurnsLeft) VALUES (" +
								 Player.entities[i].getPlayerIndex() + "," + turnEvents[k].eventNumber + "," + (turnEvents[k].expireTurn - Game.turn) + ");";
						Log.i("", sqlTurnEventInstance);
						db.execSQL(sqlTurnEventInstance);
					}
					
				}
				
			}
						
		}
		/*String sqlPlayer = "REPLACE INTO " + playerTableName + "(PlayerID,GameName,PlayerNumber,PlayerName,PlayerColor,BluetoothAddress,CurrentLocation,PreviousLocation,NetCash,TradeCount) VALUES (" +
				 		 playerID + ",'" + gameName + "'," + playerNumber + ",'" + playerName + "','" + playerColor + "','" + bluetoothAddress + "','" + currentLocation +
				 		 "','" + previousLocation + "'," + netCash + "," + tradeCount + ");";*/
		
		/*String sqlTurnEventInstance = "REPLACE INTO " + turnEventInstanceTableName + "(TurnEventInstanceID,PlayerID,EventNumber,EventTurnsLeft) VALUES (" +
				 turnEventInstanceID + "," + playerID + "," + eventNumber + "," + eventTurnsLeft + ");";*/
		
		
		/*db.execSQL(sqlGame);
		Log.i("", "Game SQL executed");
		db.execSQL(sqlPlayer);
		Log.i("", "Player SQL executed");
		db.execSQL(sqlTile);
		Log.i("", "Tile SQL executed");
		db.execSQL(sqlUpgrade);
		Log.i("", "Upgrade SQL executed");
		db.execSQL(sqlTurnEventInstance);
		Log.i("", "TurnEventInstance SQL executed");*/
		
		//Model: INSERT INTO inventory(StockNumber,Descrip,OnHandQuan,PackQty,PackCost)VALUES (51002,'AA Dry Cells 4 Pack',173,12,9.00); 		
		
		Log.i("", "Saving complete");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("deprecation")
	public void loadGame(){
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//  Update Element Values from Database //
		
		// SELECT * FROM GameTable WHERE GameName = [get from GameName];
					// SELECT * FROM PlayerTable WHERE GameName IN (SELECT GameName FROM GameTable WHERE GameName = [get from GameName]);
				//For every player:
					// SELECT * FROM TileTable WHERE OwnerID IN (SELECT PlayerID FROM PlayerTable WHERE GameName IN (SELECT GameName FROM GameTable WHERE GameName = [get from GameName]));
				//For every player:
					// SELECT * FROM TurnEventInstanceTable WHERE PlayerID IN (SELECT PlayerID FROM PlayerTable WHERE GameName IN (SELECT GameName FROM GameTable WHERE GameName = [get from GameName]));
		
		//Define all sql statements
		String sqlGame = "SELECT * FROM " + gameTableName + " WHERE GameName = " + gameName + ";";
		Log.i("", sqlGame);
		String sqlPlayer = "SELECT * FROM " + playerTableName + " WHERE GameName IN (SELECT GameName FROM GameTable WHERE GameName = " + gameName + ");";
		Log.i("", sqlPlayer);
		/*String sqlTile = "SELECT * FROM " + tileTableName + " WHERE OwnerID IN (SELECT PlayerID FROM PlayerTable WHERE GameName IN (SELECT GameName FROM GameTable WHERE GameName = " + gameName + "));";
		Log.i("", sqlTile);*/
		String sqlTurnEventInstance = "SELECT * FROM " + turnEventInstanceTableName + " WHERE PlayerID IN (SELECT PlayerID FROM PlayerTable WHERE GameName IN (SELECT GameName FROM GameTable WHERE GameName = '" + gameName + "'));";
		Log.i("", sqlTurnEventInstance);
		
		//Retrieve all database data
		gameQuery = db.rawQuery(sqlGame, st);
		playerQuery = db.rawQuery(sqlPlayer, st);
		//tileQuery = db.rawQuery(sqlTile, st);
		turnEventInstanceQuery = db.rawQuery(sqlTurnEventInstance, st);
		
		// String[] -----> GameTable values for one row
		// String[][] -----> PlayerTable values for 2-4 rows
		// String[][][] -----> TileTable values for 2-4 players
		// String[][][] -----> TurnEventInstanceTable values for 2-4 players
		
		// === Game Entity ===
		gameQuery.moveToNext();		
		tableGame_fieldAll = new String[gameQuery.getColumnCount()];
		//Loop through all columns and insert the data into tableGame_fieldAll array
		for(int i = 0; i < gameQuery.getColumnCount(); i++){
			tableGame_fieldAll[i] = gameQuery.getString(i);
			Log.i("", gameQuery.getString(i));
		}
		
		// === Player Entity ===
		/////////////////////
		
		//Instantiate player data fields where the index represents the player.
		tablePlayer_fieldPlayerNumber = new int[playerQuery.getCount()];
		tablePlayer_fieldPlayerName = new String[playerQuery.getCount()];
		tablePlayer_fieldPlayerColor = new String[playerQuery.getCount()];
		tablePlayer_fieldBluetoothAddress = new String[playerQuery.getCount()];
		tablePlayer_fieldCurrentLocation = new String[playerQuery.getCount()];
		tablePlayer_fieldPreviousLocation = new String[playerQuery.getCount()];
		tablePlayer_fieldNetCash = new int[playerQuery.getCount()];
		tablePlayer_fieldTradeCount = new int[playerQuery.getCount()];
		
		//fill in the above player data fields
		playerQuery.moveToFirst();
		for(int i = 0; i < playerQuery.getCount(); i++){
			tablePlayer_fieldPlayerNumber[i] = playerQuery.getInt(2);
			tablePlayer_fieldPlayerName[i] = playerQuery.getString(3);
			tablePlayer_fieldPlayerColor[i] = playerQuery.getString(4);
			tablePlayer_fieldBluetoothAddress[i] = playerQuery.getString(5);
			tablePlayer_fieldCurrentLocation[i] = playerQuery.getString(6);
			tablePlayer_fieldPreviousLocation[i] = playerQuery.getString(7);
			tablePlayer_fieldNetCash[i] = playerQuery.getInt(8);
			tablePlayer_fieldTradeCount[i] = playerQuery.getInt(9);
			playerQuery.moveToNext();
		}
		
		// === Tile Entity ===
		/////////////////////////////
		
		/*
		//Instantiate tile data fields where the index represents a different tile row
		tableTile_fieldOwnerID = new int[tileQuery.getCount()];
		tableTile_fieldElectricalBought = new int[tileQuery.getCount()];
		tableTile_fieldPlumbingBought = new int[tileQuery.getCount()];
		tableTile_fieldVendingBought = new int[tileQuery.getCount()];
		tableTile_fieldHVACBought = new int[tileQuery.getCount()];

		//fill in the above tile data fields
		tileQuery.moveToFirst();
		for(int i = 0; i < tileQuery.getCount(); i++){
			tableTile_fieldOwnerID[i] = tileQuery.getInt(1);
			tableTile_fieldElectricalBought[i] = tileQuery.getInt(2);
			tableTile_fieldPlumbingBought[i] = tileQuery.getInt(3);
			tableTile_fieldVendingBought[i] = tileQuery.getInt(4);
			tableTile_fieldHVACBought[i] = tileQuery.getInt(5);
			tileQuery.moveToNext();
		}
		*/
		
		// === Turn Event Entity ===
		////////////////////////
		
		//Instantiate event data fields where the index represents individual event instances
		tableTurnEventInstance_fieldPlayerID = new int[turnEventInstanceQuery.getCount()];
		tableTurnEventInstance_fieldEventNumber = new int[turnEventInstanceQuery.getCount()];
		tableTurnEventInstance_fieldEventTurnsLeft = new int[turnEventInstanceQuery.getCount()];
		
		turnEventInstanceQuery.moveToFirst();
		for(int i = 0; i < turnEventInstanceQuery.getCount(); i++){
			tableTurnEventInstance_fieldPlayerID[i] = turnEventInstanceQuery.getInt(1);
			tableTurnEventInstance_fieldEventNumber[i] = turnEventInstanceQuery.getInt(2);
			tableTurnEventInstance_fieldEventTurnsLeft[i] = turnEventInstanceQuery.getInt(3);
			turnEventInstanceQuery.moveToNext();
		}
		
		// === Testing Code ===
		/////////////////////////
		String gamee = "Game: ";
		for(int i = 0; i < tableGame_fieldAll.length; i++){
			gamee = gamee.concat(tableGame_fieldAll[i] + " : ");
		}
		
		String playere = "Player: ";
		for(int i = 0; i < tablePlayer_fieldPlayerNumber.length; i++){
			playere = playere.concat(tablePlayer_fieldPlayerNumber[i] + " : ");
		}
		playere.concat(" // ");
				
		
		for(int i = 0; i < tablePlayer_fieldPlayerName.length; i++){
			playere = playere.concat(tablePlayer_fieldPlayerName[i] + " : ");
		}
		
		for(int i = 0; i < tablePlayer_fieldPlayerColor.length; i++){
			playere = playere.concat(tablePlayer_fieldPlayerColor[i] + " : ");
		}
		
		for(int i = 0; i < tablePlayer_fieldBluetoothAddress.length; i++){
			playere = playere.concat(tablePlayer_fieldBluetoothAddress[i] + " : ");
		}
		
		for(int i = 0; i < tablePlayer_fieldCurrentLocation.length; i++){
			playere = playere.concat(tablePlayer_fieldCurrentLocation[i] + " : ");
		}
		
		for(int i = 0; i < tablePlayer_fieldPreviousLocation.length; i++){
			playere = playere.concat(tablePlayer_fieldPreviousLocation[i] + " : ");
		}
		
		for(int i = 0; i < tablePlayer_fieldNetCash.length; i++){
			playere = playere.concat(tablePlayer_fieldNetCash[i] + " : ");
		}
		
		for(int i = 0; i < tablePlayer_fieldTradeCount.length; i++){
			playere = playere.concat(tablePlayer_fieldTradeCount[i] + " : ");
		}
		
		Log.i("", gamee);
		Log.i("", playere);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//  Load New Game Elements //
		
		
		//			(b) Load everything
		//          	(1) Follow order from LoadThread
		//						Exception being setupPlayers()
		//							Create PlayerDevice objects - then do setupPlayers() (do not do the colors piece)
		//							Update most things
		//							Then instantiate and start GameThread
		//							consider changes to GameThread to account for differences
		//							Update some other things?
		
		
		// constants for initial settings
		ArrayList<Integer> LIST_COLORS = new ArrayList<Integer>();
		LIST_COLORS.add(Color.rgb(255, 0, 0)); // Red
		LIST_COLORS.add(Color.rgb(0, 255, 0)); // Green
		LIST_COLORS.add(Color.rgb(0, 0, 255)); // Blue
		LIST_COLORS.add(Color.rgb(0, 255, 255)); // Cyan
		
		// Set up images
		Image.HEXAGON_TEXTURE = BitmapFactory.decodeResource(DataLoadingActivity.activity.getResources(), R.drawable.hexagon_blue);
		Image.HEXAGON_BOTTOM = BitmapFactory.decodeResource(DataLoadingActivity.activity.getResources(), R.drawable.hexagon_layer_bot);
		Image.HEXAGON_REGION = BitmapFactory.decodeResource(DataLoadingActivity.activity.getResources(), R.drawable.hexagon_layer_rgn);
		Image.HEXAGON_PLAYER = BitmapFactory.decodeResource(DataLoadingActivity.activity.getResources(), R.drawable.hexagon_layer_plr);
		
		Image.DIE[0] = BitmapFactory.decodeResource(DataLoadingActivity.activity.getResources(), R.drawable.die_1);
		Image.DIE[1] = BitmapFactory.decodeResource(DataLoadingActivity.activity.getResources(), R.drawable.die_2);
		Image.DIE[2] = BitmapFactory.decodeResource(DataLoadingActivity.activity.getResources(), R.drawable.die_3);
		Image.DIE[3] = BitmapFactory.decodeResource(DataLoadingActivity.activity.getResources(), R.drawable.die_4);
		Image.DIE[4] = BitmapFactory.decodeResource(DataLoadingActivity.activity.getResources(), R.drawable.die_5);
		Image.DIE[5] = BitmapFactory.decodeResource(DataLoadingActivity.activity.getResources(), R.drawable.die_6);
		
		//Set up Game and GameThread
		new GameThread();
		GameThread.gt.isFromSavedGame = true;
		//new Game("");
		dt.makeGame();
		Game.name = tableGame_fieldAll[0];
		Game.numberOfPlayers = tablePlayer_fieldPlayerNumber.length;
		Game.type = tableGame_fieldAll[1];
		Game.turn = Integer.parseInt(tableGame_fieldAll[2]);
		
		// Set up Player Devices
		for(int i = 0; i < tablePlayer_fieldPlayerNumber.length; i++){
			Device.player[tablePlayer_fieldPlayerNumber[i]] = new PlayerDevice(false, tablePlayer_fieldPlayerNumber[i]);
			
			String btAdd = tablePlayer_fieldBluetoothAddress[i];
			Device.player[tablePlayer_fieldPlayerNumber[i]].setBluetoothAddress(btAdd);
		}
		
		// Set up Players
		Player.entities = new Player[Device.player.length]; //I changed it
		for(int i = 0; i < tablePlayer_fieldPlayerName.length; i++){
			int color = Color.rgb(0, 0, 0);
			for(int c = 0; c < tablePlayer_fieldPlayerColor.length; c++){
				
				if(tablePlayer_fieldPlayerColor[c].equals("Red")){
					color = LIST_COLORS.get(c);
				}
				else if(tablePlayer_fieldPlayerColor[c].equals("Green")){
					color = LIST_COLORS.get(c);
				}
				else if(tablePlayer_fieldPlayerColor[c].equals("Blue")){
					color = LIST_COLORS.get(c);
				}
				else if(tablePlayer_fieldPlayerColor[c].equals("Cyan")){
					color = LIST_COLORS.get(c);
				}
			}

			Player p = new Player(Device.player[i], tablePlayer_fieldPlayerNumber[i], color);
			// Set player name
			// TODO: The name of the player exists in PlayerDevice, I added the line below
			Device.player[tablePlayer_fieldPlayerNumber[i]].name = tablePlayer_fieldPlayerName[i];
			
			p.setBalance(tablePlayer_fieldNetCash[i]);
			p.setTradeCount(tablePlayer_fieldTradeCount[i]);

			Player.entities[i] = p;
		}
		
		//Set up board and images
		BoardSetup.setupBoard();
		Image.setupImages();
		
		// Set up PlayerPieces, Current Location, and Previous Location
		for(int i = 0; i < tablePlayer_fieldCurrentLocation.length; i++){
			int currentLocID = Integer.parseInt(tablePlayer_fieldCurrentLocation[i]);
			
			Tile currentLocTile;
			
			/*
			for(int j = 0; j < Tile.entity.length; j++){
				
				for(int k = 0; k < Tile.entity[j].length; k++){
					// TODO: added null check
					if (Tile.entity[j][k] != null){
						if(Tile.entity[j][k].getID() == currentLocID){
							currentLocTile = Tile.entity[j][k];
							Player.entities[i].setPiece(new PlayerPiece(currentLocTile, tablePlayer_fieldPlayerNumber[i]));
						}
					}
				}
				
			}
			*/
			
			for(int j = 0; j < Unit.entity.size(); j++){
				if(Unit.entity.get(j) instanceof Tile){
					currentLocTile = (Tile)Unit.entity.get(j);
					Log.i("", "CURRENTLOCTILE.ID = " + currentLocTile.id + "    :    currentLocID = " + currentLocID);
					if(currentLocTile.id == currentLocID){
						Player.entities[i].setPiece(new PlayerPiece(currentLocTile, tablePlayer_fieldPlayerNumber[i]));
						Player.entities[i].getPiece().move(currentLocTile);
						Log.i("","Player " + i + "has new piece at Tile " + currentLocTile.id);
					}
			
				}
			}
			
		}		
		
		
		for(int i = 0; i < tablePlayer_fieldPreviousLocation.length; i++){
			int previousLocID = Integer.parseInt(tablePlayer_fieldPreviousLocation[i]);
			
			Tile previousLocTile;
			
			for(int j = 0; j < Tile.entity.length; j++){
				
				for(int k = 0; k < Tile.entity[j].length; k++){
					// TODO: added null check
					if (Tile.entity[j][k] != null){
						if(Tile.entity[j][k].getID() == previousLocID){
							previousLocTile = Tile.entity[j][k];
							Player.entities[i].getPiece().setPreviousTile(previousLocTile);
							
						}
					}
				}
				
			}
			
		}
		
		// Set upgrades and turn-based events in players
		for(int i = 0; i < Player.entities.length; i++){
			if(Player.entities[i] != null){
				
				/*
				for(int j = 0; j < Player.entities[i].getPlayerTiles().length; j++){
					
					boolean elecUpgradeBool;
					boolean plumUpgradeBool;
					boolean vendUpgradeBool;
					boolean hvacUpgBool;
					
					int elecUpgrade = tableTile_fieldElectricalBought[i];
					int plumUpgrade = tableTile_fieldPlumbingBought[i];
					int vendUpgrade = tableTile_fieldVendingBought[i];
					int hvacUpg = tableTile_fieldHVACBought[i];
					
					if (elecUpgrade == 1){
						elecUpgradeBool = true;
					} else {
						elecUpgradeBool = false;
					}
					// TODO: Here is a 1-liner if statement that is the exact same as the above 5 liner
					elecUpgradeBool = elecUpgrade == 1 ? true : false;
					
					if (plumUpgrade == 1){
						plumUpgradeBool = true;
					} else {
						plumUpgradeBool = false;
					}
					
					if (vendUpgrade == 1){
						vendUpgradeBool = true;
					} else {
						vendUpgradeBool = false;
					}
					
					if (hvacUpg == 1){
						hvacUpgBool = true;
					} else {
						hvacUpgBool = false;
					}
					
					
					Player.entities[i].getPlayerTiles()[j].upgraded[0] = elecUpgradeBool;
					Player.entities[i].getPlayerTiles()[j].upgraded[1] = plumUpgradeBool;
					Player.entities[i].getPlayerTiles()[j].upgraded[2] = vendUpgradeBool;
					Player.entities[i].getPlayerTiles()[j].upgraded[3] = hvacUpgBool;
					
				}
				*/
				
				for(int k = 0; k < tableTurnEventInstance_fieldPlayerID.length; k++){
					
					//If: Event Jail Release
					if(tableTurnEventInstance_fieldEventNumber[k] == 0){
						EventGenerator.registerEvent("newTurn", new EventSetup.EventJailRelease(tableTurnEventInstance_fieldEventTurnsLeft[k], tableTurnEventInstance_fieldPlayerID[k]));
					}
					
					//else if()
					
				}
				
			}
			
		}
		
	}
	
	
	
	
	
	
	public void populateGameNameListView(){
		
		// TODO: Which is there a WHERE filter on this sql statement? don't you want them all?
		String sqlGame = "SELECT GameName FROM " + gameTableName + ";";
		Log.i("", sqlGame);
		
		gameQuery = db.rawQuery(sqlGame, st);
		
		gameQuery.moveToNext();		
		tableGame_fieldGameName = new String[gameQuery.getCount()];
		for(int i = 0; i < gameQuery.getCount(); i++){
			tableGame_fieldGameName[i] = gameQuery.getString(0);
			Log.i("", gameQuery.getString(0));
			gameQuery.moveToNext();
		}
		
		SaveGameActivity.listViewContents = tableGame_fieldGameName;
		Intent myObserverSender = new Intent(
				"Bentley.action.POPULATELISTVIEW");
		//broadcastIntent.setAction(DatabaseReceiver.ACTION_RESP);
		//broadcastIntent.putExtra("Controller", "connect");
		Log.i("", "Sending broadcast");
		SaveGameActivity.context.sendBroadcast(myObserverSender);
		
	}
	
	
	
	
	
	
	
	
	public void enforceReferentialIntegrity(){
		
		for (int r=0; r<relationship.length; r++){ //for each relationship
			String sql = "";
			String parent_table = relationship[r][0][0];
			String parent_key   = relationship[r][0][1];
			String child_table  = relationship[r][0][2];
			String child_key    = relationship[r][0][3];
			
			// Enforcing Referential Integrity
			//adding
			sql =   "CREATE TRIGGER IF NOT EXISTS relation" + r + "EnforceAdd BEFORE INSERT ON " + child_table + " BEGIN " +
					"SELECT CASE " +
					"WHEN ((SELECT " + parent_table + " . " + parent_key + " FROM " + parent_table + " WHERE " + parent_table + " . " + parent_key + " = NEW. " + child_key + " ) ISNULL) " +
					"THEN RAISE(ABORT, 'Error Message')" +
					"END; " +
					"END;";
			db.execSQL(sql);
			
			//updating
			sql =   "CREATE TRIGGER IF NOT EXISTS relation" + r + "EnforceUpdate BEFORE UPDATE ON " + child_table + " FOR EACH ROW BEGIN " +
					"SELECT CASE " +
					"WHEN ((SELECT " + parent_table + " . " + parent_key + " FROM " + parent_table + " WHERE " + parent_table + " . " + parent_key + " = NEW." + child_key + " ) ISNULL) " +
					"THEN RAISE(ABORT, 'Error Message') " +
					"END; " +
					"END;";
			db.execSQL(sql);
			
			//cascade delete
			if (relationship[r][1][0].equals("true")) {
				sql =   "CREATE TRIGGER IF NOT EXISTS relation" + r + "Delete " +
						"BEFORE DELETE ON " + parent_table + " " +
						"FOR EACH ROW BEGIN " +
						"DELETE FROM " + child_table + " WHERE " + child_table + "." + child_key + " = OLD. " + parent_key + " ;" +
						"END;";
				db.execSQL(sql);
			}
		}
	}
	
	public void callDatabaseReceiverBroadcast(){
		Intent myObserverSender = new Intent(
				"Bentley.action.GOSERVICE");
		//broadcastIntent.setAction(DatabaseReceiver.ACTION_RESP);
		//broadcastIntent.putExtra("Controller", "connect");
		Log.i("", "Sending broadcast");
		DataLoadingActivity.context.sendBroadcast(myObserverSender);
	}
	
	public void makeGame(){
		Intent myObserverSender = new Intent(
				"Bentley.action.MAKEGAME");
		//broadcastIntent.setAction(DatabaseReceiver.ACTION_RESP);
		//broadcastIntent.putExtra("Controller", "connect");
		Log.i("", "Sending broadcast");
		DataLoadingActivity.context.sendBroadcast(myObserverSender);
		int count = 0;
		while(gameMade != true){
			count++;
		}
		Game.start();
		Log.i("","" + count);
		gameMade = false; //reset for next time
	}
	
}
