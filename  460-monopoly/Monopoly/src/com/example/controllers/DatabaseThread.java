package com.example.controllers;

import java.util.ArrayList;

import com.example.model.Tile;

import android.app.Activity;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.database.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
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
	//			(a) Get info for everything from SQL Select statements; store them to Cursors
	//			(b) Convert Cursors into usable arrays of ints and Strings
	//			(c) Get info from singular GameQuery array; insert game info into Game class
	//			(d) Loop through PlayerQuery array; get info from each Player; insert player info into Player and Device classes
	//			(e) Loop through Player array; loop through TileQuery array; get info of each tile for each player; insert tile info into Tile class
	//			(f) Loop through Player array; loop through TurnEventInstanceQuery array; get info of each turn-based event for each player; insert event info into Event class
	
	public static DatabaseThread dt;
	public static boolean isLoad;
	public static final String DATABASE_NAME = "smartstartupsdatabasedmd.db";
	
	
	String selectedGameName = "";
	
	
	String gameName = "GameName";
	String gameType = "GameType";
	int turnCount = 0;
	int playerID = 0;
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
	int turnEventInstanceID = 0;
	ArrayList<ArrayList<Integer>> eventNumber;
	//int eventNumber = 0;
	ArrayList<ArrayList<Integer>> eventTurnsLeft;
	//int eventTurnsLeft = 0;
			
	String[] listViewContents;
	

	
	public SQLiteDatabase db;
	static String[][][] relationship = {
		//  {{parent_table  | parent_key | child_table              | child_key    }, {cascade_delete}}
			{{"GameTable"   , "GameName" , "PlayerTable"            , "GameName"   }, {"true"        }},
			{{"PlayerTable" , "PlayerID" , "TileTable"              , "OwnerID"    }, {"true"        }},
			{{"PlayerTable" , "PlayerID" , "TurnEventInstanceTable" , "PlayerID"   }, {"true"        }}
	};
	
	public String gameTableName = "GameTable";
	public String playerTableName = "PlayerTable";
	public String tileTableName = "TileTable";
	public String turnEventInstanceTableName = "TurnEventInstanceTable";
	
	private ContentValues values;
	private Cursor cursor;
	
	public DatabaseThread(){
		dt = this;
		
	}
	
	public void run(){
		
		Log.i("", "Thread start");
		isLoad = false;
		
		/*if(isLoad == true){
			dt.setUpDatabase();
			dt.loadGame();
		}
		
		else if(isLoad == false){
			dt.setUpDatabase();
			dt.saveGame();
		}*/
		
		dt.setUpDatabase();
		dt.saveGame();
		isLoad = true;
		dt.loadGame();

		db.close();
		
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
        
        sql = "CREATE TABLE IF NOT EXISTS " + tileTableName + "(TileID INTEGER PRIMARY KEY,OwnerID INTEGER,ElectricalBought INTEGER,PlumbingBought INTEGER, VendingBought INTEGER, HVACBought INTEGER)";
        db.execSQL(sql);
        //CREATE = create table query
        
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
		
		sqlGame = "REPLACE INTO " + gameTableName + "(GameName,GameType,TurnCount) VALUES ('" +
						 Game.name + "','" + gameType + "'," + Game.turn + ");";
		Log.i("", sqlGame);
		
		for(int i = 0; i < Player.entities.length; i++){
			sqlPlayer = "REPLACE INTO " + playerTableName + "(PlayerID,GameName,PlayerNumber,PlayerName,PlayerColor,BluetoothAddress,CurrentLocation,PreviousLocation,NetCash,TradeCount) VALUES (" +
			 		 playerID + ",'" + Game.name + "'," + Player.entities[i].getPlayerIndex() + ",'" + Player.entities[i].getName() + "','" + Player.COLOR_NAMES[i] + "','" + Device.player[i].device.getAddress() + "','" + Player.entities[i].getPiece().getCurrentTile().id +
			 		 "','" + Player.entities[i].getPiece().getPreviousTile().id + "'," + (int)Player.entities[i].getBalance() + "," + Player.entities[i].getTradeCount() + ");";
			db.execSQL(sqlPlayer);
			Log.i("", sqlPlayer);
			

			for(int j = 0; j < Player.entities[i].getPlayerTiles().length; j++){
				Tile[] tiles = Player.entities[i].getPlayerTiles();
				int electrical = 0;
				int plumbing = 0;
				int vending = 0;
				int hvac = 0;
				boolean[] upgrades = tiles[j].upgraded;
				if(upgrades[0] = true){
					electrical = 1;
				}
				if(upgrades[1] = true){
					plumbing = 1;
				}
				if(upgrades[2] = true){
					vending = 1;
				}
				if(upgrades[3] = true){
					hvac = 1;
				}
				
				
				sqlTile = "REPLACE INTO " + tileTableName + "(TileID,OwnerID) VALUES (" +
				        tiles[j].id  + "," + Player.entities[i].getPlayerIndex() + "," + electrical + "," + plumbing + "," + vending + "," + hvac + ");";
				db.execSQL(sqlTile);
				Log.i("", sqlTile);
			}
			
			for(int k = 0; k < Player.entities[i].getPlayerEvents().length; k++){
				Event[] turnEvents = Player.entities[i].getPlayerEvents();
				sqlTurnEventInstance = "REPLACE INTO " + turnEventInstanceTableName + "(TurnEventInstanceID,PlayerID,EventNumber,EventTurnsLeft) VALUES (" +
						 turnEventInstanceID + "," + Player.entities[i].getPlayerIndex() + "," + turnEvents[k].eventNumber + "," + (turnEvents[k].expireTurn - Game.turn) + ");";
				Log.i("", sqlTurnEventInstance);
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
	
	public void loadGame(){
		
		String[] st = new String[0];
		Cursor gameQuery;
		Cursor playerQuery;
		Cursor tileQuery;
		Cursor upgradeQuery;
		Cursor turnEventInstanceQuery;
		
		String sqlGame = "SELECT * FROM " + gameTableName + ";";
		Log.i("", sqlGame);
		String sqlPlayer = "SELECT * FROM " + playerTableName + ";";
		Log.i("", sqlPlayer);
		String sqlTile = "SELECT * FROM " + tileTableName + ";";
		Log.i("", sqlTile);
		String sqlTurnEventInstance = "SELECT * FROM " + turnEventInstanceTableName + ";";
		Log.i("", sqlTurnEventInstance);
		
		gameQuery = db.rawQuery(sqlGame, st);
		playerQuery = db.rawQuery(sqlPlayer, st);
		tileQuery = db.rawQuery(sqlTile, st);
		turnEventInstanceQuery = db.rawQuery(sqlTurnEventInstance, st);
		
		/**
		 * get all the info from the above Cursor objects
		 * 
		 * and set them to the proper variables (reverse of what goes on in save)
		 * 
		 */
		
		
		String[] gameColumns = gameQuery.getColumnNames();
		for(int i = 0; i < gameColumns.length; i++){
			Log.e("", gameColumns[i]);
		}
		
		String[] playerColumns = playerQuery.getColumnNames();
		for(int i = 0; i < playerColumns.length; i++){
			Log.e("", playerColumns[i]);
		}
		
		String[] tileColumns = tileQuery.getColumnNames();
		for(int i = 0; i < tileColumns.length; i++){
			Log.e("", tileColumns[i]);
		}
		
		String[] turnEventInstanceColumns = turnEventInstanceQuery.getColumnNames();
		for(int i = 0; i < turnEventInstanceColumns.length; i++){
			Log.e("", turnEventInstanceColumns[i]);
		}
		
		listViewContents = gameColumns;
		
		int co = playerQuery.getCount();
		Log.i("", co + " PlayerID records in PlayerTable");
		
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
	
	//close database
	public void closeDatabase(){
		if(db != null){
			db.close();
		}
	}
	
}
