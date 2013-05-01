package com.example.bluetooth;

import com.example.controllers.Device;
import com.example.controllers.HostDevice;
import com.example.controllers.PlayerDevice;

/**
 * Static message class for holding all the message constants that bluetooth uses
 * @author VEBER_DMIT
 */
public class Message { 
	//Chat messages
	public static final int CHAT = 0;
	
	//Host informing incoming player of their position in the lobby
	public static final int LOBBY_SLOT = 1;
	
	//Host informing all players of a new player in the lobby (also used to inform new players on the status of the lobby)
	public static final int LOBBY_NEW_PLAYER = 2;
	
	//User asking the host if a certain name is allowed
	public static final int NAME_REQUEST = 3;
	
	//Host responding to NAME_REQUEST saying it is allowed
	public static final int NAME_REQUEST_ACCEPTED = 4;
	
	//Host responding to NAME_REQUEST saying it is not allowed
	public static final int NAME_REQUEST_REJECTED = 5;
	
	//Host telling all players to enter loading screen for the game
	public static final int LOBBY_START = 6;
	
	//
	public static final int PLAYER_READY = 7;
	
	//
	public static final int ALL_READY = 8;
	
	//
	public static final int START_PLAYER_TURN = 9;
	
	//
	public static final int MOVEMENT_DICE_ROLL = 10;
	
	//
	public static final int ROLLED_DOUBLES = 11;
	
	//
	public static final int CHOOSE_FORK_PATH = 12;
	
	//
	public static final int RECEIVE_FORK_PATH = 13;
	
	//Host telling the player that one of his balance has changed
	public static final int PLAYER_STAT_UPDATE_BALANCE = 14;
	
	//Host sending a message to the player that is to be displayed in a popup
	public static final int ALERT = 15;
	
	//
	public static final int START_TILE_ACTIVITY = 16;

	//
	public static final int TILE_ACTIVITY_END_TURN = 17;
	
	//
	public static final int TILE_ACTIVITY_PURCHASE_PROPERTY = 18;

	//
	public static final int TILE_ACTIVITY_PAY_RENT = 19;

	//
	public static final int TILE_ACTIVITY_UPGRADE_PROPERTY = 20;
	
	//
	public static final int YOUR_TURN_IS_OVER = 21;

	//For rolling again
	public static final int MOVEMENT_ROLL = 22;

	//For indicating that a purchase has been successful
	public static final int PURCHASE_SUCCESS = 23;

	public static final int PAY_FINE_SUCCESS = 24;

	public static final int PLAYER_DATA = 25;
	
	//A request by the player for upgrade information about the tile the player is currently on
	public static final int TILE_GET_UPGRADE_DATA = 26;
	
	//A request by the player to get updated information for the home tab
	public static final int REQUEST_HOME_DATA = 27;

	//The host sends data to the player to input into the text fields of the home tab
	public static final int DATA_HOME_TAB = 28;
	
	//The host tells the player that the upgrade worked
	
	public static final int UPGRADE_SUCCESS = 29;
	
	//The player tells the host which player he's reconnecting as
	public static final int RECONNECT_CHOICE = 30;

	//The host tells the player that the player choice from RECONNECT_CHOICE was accepted
	public static final int RECONNECT_CHOICE_ACCEPT = 31;

	//The host tells the player that the player choice from RECONNECT_CHOICE was rejected
	public static final int RECONNECT_CHOICE_REJECT = 32;

	//The host confirming the connection attempt by the player and starting the reconnect process
	public static final int RECONNECT_START = 33;

	//The player requesting the host to provide properties owned by the current player
	public static final int REQUEST_PROPERTIES_DATA = 34; 
	
	//The host responding to REQUEST_PROPERTIES_DATA event with data
	public static final int REQUEST_PROPERTIES_DATA_ACCEPT = 35;

	//The player requesting tile data on a specific tile
	public static final int REQUEST_TILE_DATA = 36;

	//The host responding to REQUEST_TILE_DATA
	public static final int REQUEST_TILE_DATA_ACCEPT = 37; 
	
	//A player starting trade
	public static final int TRADE_START = 38; 
	
	//Hosting informing second player of trade
	public static final int TRADE_START_PLAYER = 39;
	
	//A player informing the host of changing trade details
	public static final int TRADE_CHANGE_DETAILS = 40;
	
	//The host updating both players with the new details of trade
	public static final int TRADE_UPDATE_DETAILS = 40;
	
	//A player informing the host of trade being accepted
	public static final int TRADE_ACCEPT = 41;
	
	//A player informing the host of trade being rejected
	public static final int TRADE_REJECT = 42;
	
	//The host informing the player of Trade success
	public static final int TRADE_SUCCESS = 43;
	
	//The host informing the player of Trade failure
	public static final int TRADE_FAIL = 44;


	
	
}
