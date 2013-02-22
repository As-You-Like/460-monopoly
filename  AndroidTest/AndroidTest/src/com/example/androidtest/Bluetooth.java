package com.example.androidtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class Bluetooth {
	// Variables
	public static Handle mHandler;
	public static BluetoothAdapter mAdapter;
	public static Bluetooth mBluetooth;
	public static String NAME = "Monopoly";
	public static Context context;
	
	// Constants
	public static final int MESSAGE_RECIEVE = 0;
	public static final int MESSAGE_ERROR = -1;
	
	// Constructor
	public Bluetooth(Context context){
		Bluetooth.mAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if (mAdapter == null){
			Toast.makeText(context, "Your device does not support Bluetooth, you cannot play this game", Toast.LENGTH_LONG)
			.show();
		}
		
		Bluetooth.mHandler = new Bluetooth.Handle();
		Bluetooth.mBluetooth = this;
		Bluetooth.context = context;
	} // End Constructor
	
	// Methods
	/**
     * Start AcceptThread to begin a session in listening (server) mode.
	 */
	public synchronized void start(){
		
		// Start the thread to listen on a BlueboothServerSocket
		Bluetooth.AcceptThread mAccept = Player.entities[Player.currentPlayer].mAccept;
		if (mAccept == null){
			mAccept = new Bluetooth.AcceptThread();
			mAccept.start();
		}
		
		Player.playerConnectionStatus[Player.currentPlayer] = Player.CONNECTION_LISTEN;
	} //End start()
	
	/**
	 * Stops the AcceptThread and ends the listening session of the server
	 */
	public synchronized void stop(){
		this.stop(Player.CONNECTION_NONE);
	}
	
	public synchronized void stop(int status){
		Bluetooth.AcceptThread mAccept = Player.entities[Player.currentPlayer].mAccept;
		if (mAccept != null){
			mAccept.cancel();
			mAccept = null;
		}
		
		Player.playerConnectionStatus[Player.currentPlayer] = status;
	}
	
	/**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
	public synchronized void connect(BluetoothDevice device){
		int clientConnectionStatus = Player.playerConnectionStatus[Player.currentPlayer];
		Bluetooth.ConnectThread mConnect = Player.entities[Player.currentPlayer].mConnect;
		Bluetooth.ConnectedThread mConnected = Player.entities[Player.currentPlayer].mConnected;
		
		//cancel any current connection attempts to the server
		if (clientConnectionStatus == Player.CONNECTION_CONNECTING){
			if (mConnect != null) {mConnect.cancel(); mConnect = null;}
		}
		
		//cancel any existing connection
		if (mConnected != null){mConnected.cancel(); mConnected = null;}
		
		mConnect = new Bluetooth.ConnectThread(device);
		mConnect.start();
		
		Player.playerConnectionStatus[Player.currentPlayer] = Player.CONNECTION_CONNECTED;
	} //End connect()
	
	/**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
	public synchronized int connected(int p, BluetoothSocket socket, BluetoothDevice device){
		// Find untaken player slot
		/*int p = -1;
		for (int i=0; i<Player.playerMax || p >= 0; i++){
			if (Player.playerConnectionStatus[i] == Player.CONNECTION_NONE){
				p = i;
				Player.playerCount++;
			}
		}*/
		
		//if there are not available slots return maxPlayer (to stop listening) and display an error
		if (p == -1){
			Toast.makeText(context, "connected() no player slots are available", Toast.LENGTH_SHORT).show();
			return Player.playerMax;
		}
		
		Player.entities[p].mConnected = new Bluetooth.ConnectedThread(socket, p);
		Player.entities[p].mConnected.start();
		Player.playerConnectionStatus[p] = Player.CONNECTION_CONNECTED;
		
		//TEMPORARY CODE
		Player.entities[p].sendMessage("Message from player " + p);
		
		return Player.playerCount;
	} //End connected()
	
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
		start(); //make sure listening channel is open
		
		Message msg = mHandler.obtainMessage(Bluetooth.MESSAGE_ERROR);
        Bundle bundle = new Bundle();
        bundle.putString("toast", "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
	} //End connectionFailed()
	
	/**
     * Indicate course of action if an existing connection is lost
     */
	private void connectionLost(){
		start(); //make sure listening channel is open
		
		Message msg = mHandler.obtainMessage(Bluetooth.MESSAGE_ERROR);
        Bundle bundle = new Bundle();
        bundle.putString("toast", "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
	} //End connectionLost()
	
	// Inner Classes
	public class AcceptThread extends Thread {
		private final BluetoothServerSocket mmServerSocket;
		
		public AcceptThread(){
			BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try {
            	UUID uuid = Player.entities[Player.currentPlayer].uuid;
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, uuid);
            } catch (IOException e) {
                Toast.makeText(context, "AcceptThread failed to begin listening", Toast.LENGTH_SHORT).show();
            }
            mmServerSocket = tmp;
		} //End constructor
		
		public void run(){
			setName("AcceptThread");
			BluetoothSocket socket = null;
			
			// Listen to the server socket if still listening
			while(Player.playerConnectionStatus[Player.currentPlayer] == Player.CONNECTION_LISTEN){
				try {
					socket = mmServerSocket.accept();
				} catch (IOException e){
					Toast.makeText(context, "AcceptThread server socket error", Toast.LENGTH_SHORT).show();
					break;
				}
				
				if (socket != null){
					synchronized (Bluetooth.this){
						int presentPlayers = connected(1, socket, socket.getRemoteDevice());
						
						//if the number of connected players is already at its limit, stop listening
						if (presentPlayers == Player.playerMax){
							Bluetooth.mBluetooth.stop(Player.CONNECTION_CONNECTED);
						}
					}
				}
			}
			
		} //End run()
		
		public void cancel(){
			try {
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
			mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
            	UUID uuid = Player.entities[Player.currentPlayer].uuid;
                tmp = device.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
            	Toast.makeText(context, "ConnectedThread create failed", Toast.LENGTH_SHORT).show();
            }
            mmSocket = tmp;
		} //End constructor
		
		public void run(){
			setName("ConnectThread");
			
			// Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                	Toast.makeText(context, "ConnectThread unable to close socket during connection failure", Toast.LENGTH_SHORT).show();
                }
                // Start the service over to restart listening mode
                Bluetooth.this.start();
                return;
            }
            
            //Reset the ConnectThread because we're done
            synchronized (Bluetooth.this) {
            	Player.entities[Player.currentPlayer].mConnect = null;
            }

            // Start the connected thread
            connected(0, mmSocket, mmDevice);
		} //End run()
		
		public void cancel(){
			try {
                mmSocket.close();
            } catch (IOException e) {
            	Toast.makeText(context, "ConnectThread unable to close socket on cancel", Toast.LENGTH_SHORT).show();
            }
		} //End cancel()
	} //End class ConnectThread
	
	public class ConnectedThread extends Thread {
		private int playerNum;
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		
		public ConnectedThread(BluetoothSocket socket, int playerNum){
			this.playerNum = playerNum;
			mmSocket = socket;
			
			InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(context, "ConnectedThread failed to make temp sockets", Toast.LENGTH_SHORT).show();
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
	            	//Player.entities[this.playerNum].recieveMessage(builder.toString());
	            	if (builder.length() > 0){ 
	            		Bluetooth.mHandler.obtainMessage(Bluetooth.MESSAGE_RECIEVE, builder.toString()).sendToTarget();
	            	}
	            	
	                builder.delete(0, builder.length());
	                
				} catch (IOException e){
					Toast.makeText(context, "ConnectedThread player " + this.playerNum + " has disconnected", Toast.LENGTH_SHORT).show();
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
            	Toast.makeText(context, "ConnectedThread failed to write", Toast.LENGTH_SHORT).show();
            }
		} //End write()
		
		public void cancel(){
			try {
				mmSocket.close();
			} catch (IOException e) {
				Toast.makeText(context, "ConnectedThread failed to close socket", Toast.LENGTH_SHORT).show();
			}
		} //End cancel()
	} //End class ConnectedThread
	
	private static class Handle extends Handler {
		public void handleMessage(Message msg){
			switch (msg.what){
			case Bluetooth.MESSAGE_RECIEVE:
				//Player.entities[Player.currentPlayer].recieveMessage((String) msg.obj);
				Toast.makeText(context, (String) msg.obj, Toast.LENGTH_SHORT).show();
				break;
			case Bluetooth.MESSAGE_ERROR:
				Toast.makeText(context, msg.getData().getString("toast"), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	} //End class Handle
} //End class Bluetooth