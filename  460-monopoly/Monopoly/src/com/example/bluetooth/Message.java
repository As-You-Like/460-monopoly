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
}
