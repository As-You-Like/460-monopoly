package com.example.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.controllers.Device;
import com.example.controllers.HostDevice;
import com.example.controllers.PlayerDevice;
import com.example.monopoly.FindHostActivity;
import com.example.monopoly.LobbyActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class Bluetooth {
	// Variables
	public static String mGameName = "Monopoly";
	public static String originalDeviceName = "";
	
	public static Handle mHandler;
	public static BluetoothAdapter mAdapter;
	public static Bluetooth entity;
	public static String NAME = "Monopoly";
	public static Context context;
	
	// Constants
	public static final int MESSAGE_RECIEVE = 0;
	public static final int MESSAGE_ERROR = -1;
	
	public static final int USERTYPE_SERVER = 0;
	public static final int USERTYPE_CLIENT = 1;
	
	//private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	private static final UUID MY_UUID = UUID.fromString("0f1e9a70-9030-11e2-9e96-0800200c9a66");
	
	
	// Constructor
	public Bluetooth(Context context){
		Bluetooth.mAdapter = BluetoothAdapter.getDefaultAdapter();
		Bluetooth.originalDeviceName = Bluetooth.mAdapter.getName();
		
		if (mAdapter == null){
			Toast.makeText(context, "Your device does not support Bluetooth, you cannot play this game", Toast.LENGTH_LONG)
			.show();
		}
		
		Bluetooth.mHandler = new Bluetooth.Handle();
		Bluetooth.entity = this;
		Bluetooth.context = context;
	} // End Constructor
	
	/**
	 * Changes the name of the device on the discovery stage
	 * Includes the name of the game, so that this app on other devices can distinguish
	 * devices that are hosting an instance of this game
	 * @param name
	 */
	public static void changeDeviceName(String name){
		Bluetooth.mAdapter.setName("[" + Bluetooth.mGameName + "]" + name);
    }
	
	// Methods
	/**
     * Start AcceptThread to begin a session in listening (server) mode.
	 */
	public synchronized void start(){
		
		
	} //End start()
	
	/**
	 * Stops the AcceptThread and ends the listening session of the server
	 */
	public synchronized void stop(){
		
	}
	
	
	/**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
	public void write(byte[] out){
		
	} //End write()
	
	// Event Callbacks
	/**
     * Indicate course of action if a connection attempt fails
     */
	private void connectionFailed(){
		
	} //End connectionFailed()
	
	/**
     * Indicate course of action if an existing connection is lost
     */
	private void connectionLost(){
		
	} //End connectionLost()
	
	/**
     * Start device discover with the BluetoothAdapter
     */
    public static void startDiscovery() {
        // If we're already discovering, stop it
        if (Bluetooth.mAdapter.isDiscovering()) {
        	Bluetooth.mAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        Bluetooth.mAdapter.startDiscovery();
    }
    
    /**
     * End device discover with the BluetoothAdapter
     */
    public static void endDiscovery(){
    	// Make sure we're not doing discovery anymore
        if (Bluetooth.mAdapter != null) {
            Bluetooth.mAdapter.cancelDiscovery();
        }
    }
    
    /**
     * Makes sure the device is discoverable
     */
    
	
	// Inner Classes
	public class AcceptThread extends Thread {
		private final BluetoothServerSocket mmServerSocket;
		public boolean listen = true;
		
		public AcceptThread(){
			BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try {
            	
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
                Log.d("Log1", "Succesfully created listener");
            } catch (Exception e) { 
                //Toast.makeText(context, "AcceptThread failed to begin listening", Toast.LENGTH_SHORT).show();
                Log.e("Log1", "AcceptThread failed to begin listening"); 
                
                e.printStackTrace();
            }
            mmServerSocket = tmp;
		} //End constructor
		
		public void run(){
			setName("AcceptThread");
			BluetoothSocket socket = null;
			
			// Listen to the server socket if still listening
			Log.e("5", "before while");
			while(listen){
				try {
					socket = mmServerSocket.accept();
				} catch (IOException e){
					Log.e("1", "AcceptThread server socket error");
					break; 
				}
				
				if (socket != null){
					Log.e("5", socket.getRemoteDevice().getName());
					synchronized (Bluetooth.this){
						HostDevice.activate(socket, socket.getRemoteDevice()); 
						
						//if the number of connected players is already at its limit, stop listening
						
					}
				}
			}
			Log.e("5", "after while");
			
		} //End run()
		
		public void cancel(){
			try {
				Bluetooth.changeDeviceName(Bluetooth.originalDeviceName);
				listen = false;
				mmServerSocket.close();
			} catch (IOException e) {
				Toast.makeText(context, "AcceptThread failed to close socket", Toast.LENGTH_SHORT).show();
			}
		} //End cancel()
	} //End class AcceptThread
	
	public class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
		
		public ConnectThread(BluetoothDevice device){
			this.mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                
            	//Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
                //tmp = (BluetoothSocket) m.invoke(device, 1);
            } catch (IOException e) {
            	Log.e("2", e.getMessage());
            } 
            mmSocket = tmp;
		} //End constructor
		
		public void run(){
			setName("ConnectThread");
			
			mAdapter.cancelDiscovery();
			
			// Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
            	Log.e("1", "1");
                mmSocket.connect();
                Log.e("1", "2");
            } catch (IOException e) {
                connectionFailed();
                Log.e("1", e.getMessage());
                
                // Close the socket
                try {
                    mmSocket.close();
                    Log.e("1", "4");
                } catch (IOException e2) {
                	Log.e("1", "5");
                }
                // Start the service over to restart listening mode
                Log.e("1", "6");
                Bluetooth.this.start();
                Log.e("1", "7");
                return;
            }
            
            //Reset the ConnectThread because we're done
            synchronized (Bluetooth.this) {
            	HostDevice.connect = null;
            }

            // Start the active connection thread
            PlayerDevice.activate(mmSocket, mmDevice);
		} //End run()
		
		public void cancel(){
			try {
                mmSocket.close();
            } catch (IOException e) {
            	Toast.makeText(context, "ConnectThread unable to close socket on cancel", Toast.LENGTH_SHORT).show();
            }
		} //End cancel()
	} //End class ConnectThread
	
	public class ActiveThread extends Thread {
		public final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		
		public ActiveThread(BluetoothSocket socket){
			mmSocket = socket;
			
			InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            	Log.e("ActiveThread", "Failed to make temp sockets, exception: " + e.getMessage());
                connectionFailed();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
		} //End constructor
		
		public void run(){
			byte[] buffer = new byte[1024*100];
            int bytes;

            // Keep listening to the InputStream while connected
          
			int totalBytes = 0;
            
			StringBuilder builder = new StringBuilder();
			while(true){
				try {
					// Read from the InputStream
	            	do {
						bytes = mmInStream.read(buffer,0,1008);
						String readMessage = new String(buffer, 0, bytes);
						builder.append(readMessage);
						totalBytes+=bytes;
					} while (bytes==1008);
	            	
	                // Send the obtained bytes to the UI Activity
	            	if (builder.length() > 0){ 
	            		String json = builder.toString();
	            		JSONObject obj = null;
	            		try {
							obj = new JSONObject(json);
						} catch (JSONException e) {
							Log.e("AcceptThread", "JSON IS NULL");
							obj = null;
						}
	            		if (obj != null){
	            			
	            			Message msg = Bluetooth.mHandler.obtainMessage(Bluetooth.MESSAGE_RECIEVE, obj);
	            			if (HostDevice.self == true){
	            				msg.arg1 = 0; //identify which playerdevice has the current activethread
	            				for (int i=0; i<Device.player.length; i++){
	            					if (Device.player[i] != null){
	            						if (Device.player[i].active.getId() == this.getId()){
	            							Log.d("getId()", "i = " + i);
	            							msg.arg1 = i;
	            						}
	            					}
	            				}
	            			} else {
	            				msg.arg1 = -1; //All messages are recieved from the host only
	            			}
	            			
	            			msg.sendToTarget();
	            		}
	            		
	            	}
	            	
	                builder.delete(0, builder.length());
	                
				} catch (IOException e){
					//Toast.makeText(context, "ConnectedThread player " + this.playerNum + " has disconnected", Toast.LENGTH_SHORT).show();
					Log.e("ActiveThread", "A player has disconnected, exception: " + e.getMessage());
					
					Message msg = Bluetooth.mHandler.obtainMessage(Bluetooth.MESSAGE_ERROR, "playerDisconnect");
					if (HostDevice.self == true){
        				msg.arg1 = 0; //identify which playerdevice has the current activethread
        				for (int i=0; i<Device.player.length; i++){
        					if (Device.player[i] != null){
        						if (Device.player[i].active.getId() == this.getId()){
        							Log.d("getId()", "i = " + i);
        							msg.arg1 = i;
        						}
        					}
        				}
        			} else {
        				msg.arg1 = -1; //All messages are recieved from the host only
        			}
					
					msg.sendToTarget();
					connectionLost();
					break;
				}
			}
			if (totalBytes == 0){
				// The stream was never used
			}
		} //End run()
		
		public void write(byte[] buffer){
			try {
                mmOutStream.write(buffer);
                mmOutStream.flush();
            } catch (IOException e) {
            	Log.e("ActiveThread", "failed to write: " + e.getMessage());
            }
		} //End write()
		
		public void cancel(){
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e("ActiveThread", "failed to close socket: " + e.getMessage());
			}
		} //End cancel()
	} //End class ConnectedThread
	
	private static class Handle extends Handler {
		public void handleMessage(Message msg){
			switch (msg.what){
			case Bluetooth.MESSAGE_RECIEVE:
				JSONObject obj = (JSONObject)msg.obj;
				try {
					Log.d("Handler", "Message Recieved!");
					int sender = obj.getInt(Device.MESSAGE_COMPONENT_SENDER);
					int reciever = obj.getInt(Device.MESSAGE_COMPONENT_RECIEVER);
					int type = obj.getInt(Device.MESSAGE_COMPONENT_TYPE);
					String message = obj.getString(Device.MESSAGE_COMPONENT_MESSAGE);
					String debugMsg = "[from " + sender 
							+ " to " + reciever
							+ " type: " + type + "] " 
							+ message;
					
					
					Log.d("message", debugMsg);
					/*Toast.makeText(context, 
							
							debugMsg
							
							, Toast.LENGTH_LONG).show();
					*/
					//SYSTEM - Handle system messages
					switch (type){
					//Handle system messages
					case Device.MESSAGE_TYPE_CHAT:
						if (HostDevice.self == true){
							if (reciever != -1){
								//device is a host, must relay the message to the appropriate player
								Device.player[reciever].sendMessage(Device.MESSAGE_TYPE_CHAT, message, sender);
							} else {
								//message was directed to host
								String player = Device.player[sender].name + " (player " + sender + ")";
								Toast.makeText(LobbyActivity.activity, "Message from " + player + ": " + message, Toast.LENGTH_LONG).show();
							}
						} else {
							//device is a player, the player has received a message addressed to the current player
							String player = sender == -1 ? "host" : Device.player[sender].name + " (player " + sender + ")";
							if (sender != -1 && sender == Device.currentPlayer){
								player = "yourself";
							}
							Toast.makeText(LobbyActivity.activity, "Message from " + player + ": " + message, Toast.LENGTH_LONG).show();
						}
					case Device.MESSAGE_TYPE_SYSTEM:
						//handle player entering lobby
						if (sender == -1 && message.substring(0, 9).equals("newPlayer")){
							//extract player data
							int playerNum = Integer.parseInt(message.substring(9, 10));
							String deviceMac = message.substring(10);
							
							if (deviceMac.equals(Bluetooth.mAdapter.getAddress())){
								//the new player is the current device
								
								PlayerDevice.currentPlayerConnectionStatus = PlayerDevice.CONNECTION_ACTIVE;
								Device.player[playerNum] = Device.tmpCurrentPlayer;
								Device.currentPlayer = playerNum;
								FindHostActivity.goToLobby();
							} else {
								//the new player is not the current device
								
								//create player object
								PlayerDevice p = new PlayerDevice(false, playerNum);
								p.name = "Player " + playerNum + " is connecting ...";
								
								//update lobby UI
								if (LobbyActivity.activity != null){
									LobbyActivity.activity.updatePlayerList();
								}
							}
						}
						
						//handle lobbyPlayer update (the message that updates new players on the current state of the lobby)
						if (sender == -1 && message.substring(0, 11).equals("lobbyPlayer")){
							int playerNum = Integer.parseInt(message.substring(11,12));
							String name = message.substring(12);
							
							PlayerDevice p = new PlayerDevice(false, playerNum);
							p.name = name;
							
							if (LobbyActivity.activity != null){
								LobbyActivity.activity.updatePlayerList();
							}
						}
						
						//handle name request (-1 reciever is the host)
						if (reciever == -1 && message.substring(0, 11).equals("nameRequest")){
							int playerNum = sender;
							String playerName = message.substring(11);
							
							//check if name is available
							String error = PlayerDevice.isNameAvailable(playerName);
							
							if (error == null){
								//update data
								if (playerNum < 0){
									playerNum = msg.arg1;
								}
								PlayerDevice.player[playerNum].name = playerName;
								
								//update UI
								LobbyActivity.activity.updatePlayerList();
								
								//send message to player indicating name is accepted
								Log.d("System Message", "Name Accepted: " + playerName + " for Player " + playerNum);
								//PlayerDevice.player[playerNum].sendMessage(Device.MESSAGE_TYPE_SYSTEM, "nameRequestAccepted" + playerName);
								Device.sendMessageToAllPlayers(Device.MESSAGE_TYPE_SYSTEM, "nameRequestAccepted" + playerNum + playerName);
							} else {
								//send message to player indicating name is rejected
								Log.e("System Message", "Name Rejected: " + playerName + " for Player " + playerNum);
								PlayerDevice.player[playerNum].sendMessage(Device.MESSAGE_TYPE_SYSTEM, "nameRequestRejected" + error);
							}
						}
						
						//handle nameRequestAccepted and nameRequestRejected (on player side)
						if (message.length() >= 11){
							if (sender == -1 &&  message.substring(0, 11).equals("nameRequest")){
								boolean accepted = (message.substring(11, 19).equals("Accepted") ? true : false);
								if (accepted){
									int playerNum = Integer.parseInt(message.substring(19, 20));
									String name = message.substring(20);
									//Display accepted message
									Log.d("Name Request", "Name Accepted: " + name);
									
									if (PlayerDevice.player[playerNum] != null){
										PlayerDevice.player[playerNum].name = name;
									} else {
										Log.e("nameRequestAccepted", "Player " + playerNum + " does not exist");
									}
									//change name
									//if (reciever == Device.currentPlayer){
									//	PlayerDevice.player[reciever].name = name;
									//} else {
									//	Log.e("PlayerRequest", reciever + " != " + Device.currentPlayer);
									//}
									
									//update lobby UI
									LobbyActivity.activity.updatePlayerList();
								} else {
									//Display error message
									String error = message.substring(19);
									Log.d("Name Request", "Name Rejected: " + error);
									
									//ask for name again
									LobbyActivity.activity.requestName();
								}
							}
						}
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case Bluetooth.MESSAGE_ERROR:
				String message = (String)msg.obj;
				if (message.equals("playerDisconnect")){
					if (HostDevice.self){
						Device.player[msg.arg1] = null;
						Toast.makeText(context, "Player " + msg.arg1 + " has disconnected", Toast.LENGTH_SHORT).show();
						LobbyActivity.activity.updatePlayerList();
					} else {
						Toast.makeText(context, "You have disconnected from the game", Toast.LENGTH_SHORT).show();
						
						//go back
						LobbyActivity.activity.finish();
						
						//flush out existing knowledge of lobby
						for (int i=0; i<Device.player.length; i++){
							if (Device.player[i] != null){
								Device.player[i] = null;
							}
						}
					}
					
				}
				//Toast.makeText(context, msg.getData().getString("toast"), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	} //End class Handle
} //End class Bluetooth