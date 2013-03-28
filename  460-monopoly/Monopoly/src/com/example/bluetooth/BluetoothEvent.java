package com.example.bluetooth;

/**
 * @description defines an interface that will be used for bluetooth event registration
 * @firstWritten Thursday March, 28 2013 by Dmitry Veber
 */
public interface BluetoothEvent {
	
	/**
	 * @description returns true if the message type is appropriate to this message event
	 * @param int type (the type that is being checked)
	 * @return boolean
	 */
	public boolean typeValid(int type);
	
	/**
	 * @description does something with the message recieved
	 * @param int sender (Player number of the sending player, -1 if host)
	 * @param int reciever (Player number of the target player, -1 if host)
	 * @param String message (The message that is being sent)
	 */
	public void processMessage(int sender, int reciever, String message);
}
