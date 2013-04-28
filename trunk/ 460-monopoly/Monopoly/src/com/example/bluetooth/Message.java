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
	
	
}
