package com.example.controllers;

import org.json.JSONException;
import org.json.JSONObject;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.Bluetooth.ActiveThread;

public class HostDevice extends Device {
	
	public static boolean self = false; //indiciates if the current device is the host
	public static int connectionStatus = 0;
	
	// === Attributes if self is FALSE ===
	public static final int CONNECTION_IDLE         = 0; //The host slot is empty
	public static final int CONNECTION_DISCONNECTED = 1; //The host is not connected
	public static final int CONNECTION_CONNECTING   = 2; //The host is trying to connect
	public static final int CONNECTION_ACTIVE       = 3; //The host is connected
	
	//Thread for the current player device to communicate with a connected host
	public static Bluetooth.ActiveThread active;
	
	//Thread for the current player device to connect to the host
	public static Bluetooth.ConnectThread connect;
	
	// === Attributes if self is TRUE ===
	//              INCLUDE CONNECTION_IDLE      = 0; //The host is not listening for players
	public static final int CONNECTION_LISTENING = 1; //The host is listening for players
	
	
	//Thread for the host to listen for connections
	public static Bluetooth.AcceptThread accept;
	
	public HostDevice(boolean self) {
		Device.host = this;
		HostDevice.self = self;
	}
	
	public void sendMessage(int type, String message){
		//Convert message to JSON, and add the destination address as well as the type
		JSONObject map = new JSONObject();
		try {
			map.put(Device.MESSAGE_COMPONENT_RECIEVER, ""+(-1)); //store address
			map.put(Device.MESSAGE_COMPONENT_SENDER, ""+Device.getCurrentDevice()); //store address
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
			tmpActive.write(bytes);
		} else { //else do nothing (host can't self messages to himself)
			
		}
	}
	
	public synchronized void listenStart(boolean refresh){
		if (HostDevice.self){ //if the current device is the host
			if (HostDevice.accept == null){
				HostDevice.accept = Bluetooth.entity.new AcceptThread();
			} else if (refresh == true){
				this.listenStop();
				HostDevice.accept = Bluetooth.entity.new AcceptThread();
			}
		}
		HostDevice.connectionStatus = HostDevice.CONNECTION_LISTENING;
	}
	
	public void listenStop(){
		HostDevice.accept.listen = false;
		HostDevice.accept = null;
		HostDevice.connectionStatus = HostDevice.CONNECTION_IDLE;
	}
	
	/**
	 * Used for connecting the host to the player
	 * @param socket
	 * @param device
	 */
	public static synchronized void activate(BluetoothSocket socket, BluetoothDevice device){
		//Assign a player number
		int p = 0;
		boolean stop = false;
		for (p=0; p<Device.player.length && !stop; p++){
			if (Device.player[p] != null){
				stop = true;
			}
		}
		
		//Connect the device, and start the active thread
		Device.player[p] = new PlayerDevice(true);
		Device.player[p].device = device;
		Device.player[p].active = Bluetooth.entity.new ActiveThread(socket, Device.player[p]);
		
		//Mark as connected
		PlayerDevice.player[p].playerConnectionStatus = PlayerDevice.CONNECTION_ACTIVE;
		
		//Send message to the newly connected player giving him his player slot number
		//to be created
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
