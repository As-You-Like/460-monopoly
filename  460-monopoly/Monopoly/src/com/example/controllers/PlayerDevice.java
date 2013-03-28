package com.example.controllers;

import org.json.JSONException;
import org.json.JSONObject;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.bluetooth.Bluetooth;

/**
 * Class that manages everything to do with the player
 * @author VEBER_DMIT
 *
 */
public class PlayerDevice extends Device {
	
	public boolean self = false; //Indicates if the current device is this player slot
	public int playerConnectionStatus = 0;
	public String name = "";
	
	// === If CurrentDevice is the host ===
	//Thread for the host to communicate with the connected player
	public Bluetooth.ActiveThread active;
	public BluetoothDevice device;
	
	public static final int CONNECTION_ACTIVE       = 3; 
		//instance - This player slot is taken by a connected player
		//static   - The current player is connected
	public static final int CONNECTION_CONNECTING   = 2; 
		//instance - This player slot is taken by a player who is trying to connect
		//static   - The current player is attempting to connect
	public static final int CONNECTION_DISCONNECTED = 1;
		//instance - This player slot is taken by a player not current connected to the game
		//static   - The current player is disconnected from the game (connection failure, left, etc)
	public static final int CONNECTION_IDLE         = 0; 
		//instance - Does not do anything
		//static   - Current player is not connected, no instance definition exists
	
	// === If CurrentDevice is not the host
	//Tracks the connection status of the current player
	public static int currentPlayerConnectionStatus = 0;
	
	//tracks the device object for the connection of the current player to the host
	public static BluetoothDevice selfDevice;
	
	public PlayerDevice(boolean self){
		//Device.player[index] = this;
		this.self = self;
		//if (self){
		//	Device.currentPlayer = index;
		//}
	}
	
	public PlayerDevice(boolean self, int index){
		this(self);
		Device.player[index] = this;
		if (self){
			Device.currentPlayer = index;
		}
	}
	
	/**
	 * Check if the indicated player name is available. if so, no error is returned (blank string), otherwise
	 * a string indicating the problem is returned
	 * @param String name
	 * @return String
	 */
	public static String isNameAvailable(String playerName){
		
		return null;
		/*boolean taken = false;
		
		for(int i = 0; i < player.length; i++){
			if (player[i] != null){
				if(playerName.equals(player[i].name)){
					taken = true;
				}
			}
		}
		
		if(taken == true){
			String errorMessage = playerName + " is taken. Please type another name.";
			return errorMessage;
		}
		
		else{
			return null;
		}*/
	}
	
	public int getPlayerNumber(){
		for (int i=0; i<Device.player.length; i++){
			if (Device.player[i] != null){
				if (Device.player[i].equals(this)){
					return i;
				}
			}
		}
		Log.e("PlayerDevice.getPlayerNumber()", "Unable to identify player");
		return -1;
	}
	
	public void sendMessage(int type, String message, int sender){
		//Convert message to JSON, and add the destination address as well as the type
		JSONObject map = new JSONObject();
		try {
			map.put(Device.MESSAGE_COMPONENT_RECIEVER, ""+this.getPlayerNumber()); //store address
			map.put(Device.MESSAGE_COMPONENT_SENDER, ""+sender); //store address
			map.put(Device.MESSAGE_COMPONENT_TYPE, ""+type); //store message type
			map.put(Device.MESSAGE_COMPONENT_MESSAGE, message); //store message itself
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		
		String json = map.toString();
		
		//get byte array of the json
		byte[] bytes = json.getBytes();
		
		//Send the message
		if (!HostDevice.self){ //if not a host, send to the host
			Bluetooth.ActiveThread tmpActive;
			synchronized (this){
				//if not connected, abort message send attempt
				if (HostDevice.connectionStatus != HostDevice.CONNECTION_ACTIVE) return;
				
				//set the active connection to the host as the temporary active variable
				tmpActive = HostDevice.active;
			}
			Log.e("sendMessage", "[from "+Device.getCurrentDevice()+" to "+this.getPlayerNumber()+" type: "+type+"] " + message);
			tmpActive.write(bytes);
		} else { //else send to target address
			Bluetooth.ActiveThread tmpActive;
			synchronized (this){
				//if not connected, abort message send attempt
				if (this.playerConnectionStatus != PlayerDevice.CONNECTION_ACTIVE) return;
				
				//set the active connection to the host as the temporary active variable
				tmpActive = this.active;
			}
			Log.e("sendMessage", "[from "+Device.getCurrentDevice()+" to "+this.getPlayerNumber()+" type: "+type+"] " + message);
			tmpActive.write(bytes);
		}
	}
	
	public void sendMessage(int type, String message){
		this.sendMessage(type, message, Device.getCurrentDevice());
	}
	
	/**
	 * Used for the attempt to connect the current player device to the host
	 * @param device
	 */
	public static synchronized void connect(BluetoothDevice device){
		//cancel any current connection attempts to the host
		if (PlayerDevice.currentPlayerConnectionStatus == PlayerDevice.CONNECTION_CONNECTING){
			if (HostDevice.connect != null){
				HostDevice.connect.cancel();
				HostDevice.connect = null;
			}
		}
		
		//cancel any existing connection to the host
		if (PlayerDevice.currentPlayerConnectionStatus == PlayerDevice.CONNECTION_ACTIVE){
			if (HostDevice.active != null){
				HostDevice.active.cancel();
				HostDevice.active = null;
			}
		}
		
		//start the connection attempt
		HostDevice.connect = Bluetooth.entity.new ConnectThread(device);
		HostDevice.connect.start();
		
		//create host object, but set self = false (current device is not a host)
		HostDevice.host = new HostDevice(false);
		
		//mark current player as trying to connect
		PlayerDevice.currentPlayerConnectionStatus = PlayerDevice.CONNECTION_CONNECTING;
		HostDevice.connectionStatus = HostDevice.CONNECTION_CONNECTING;
	}
	
	/**
	 * Used for connecting the player to the host
	 * @param socket
	 * @param device
	 */
	public static synchronized void activate(BluetoothSocket socket, BluetoothDevice device){
		//Connect the device, and start the active thread
		Device.tmpCurrentPlayer = new PlayerDevice(true);
		Device.tmpCurrentPlayer.device = device;
		if (HostDevice.active != null){
			HostDevice.active.cancel();
			HostDevice.active = null;
		}
		HostDevice.active = Bluetooth.entity.new ActiveThread(socket);
		HostDevice.active.start();
		
		
		//Mark as connected
		PlayerDevice.currentPlayerConnectionStatus = PlayerDevice.CONNECTION_ACTIVE;
		HostDevice.connectionStatus = HostDevice.CONNECTION_ACTIVE;
	}	

}
