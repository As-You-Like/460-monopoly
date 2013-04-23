package com.example.monopoly;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.BluetoothEvent;
import com.example.bluetooth.Message;
import com.example.controllers.PlayerDevice;
import com.example.model.Tile;

@SuppressWarnings("deprecation")
public class CommandCardActivity extends TabActivity { 
	public TabHost tabBar;
	public static CommandCardActivity activity;

	final static int TAB_TURN = 0;
	final static int TAB_TILE = 1;
	final static int TAB_DECISION = 2;
	final static int TAB_HOME = 3;
	final static int TAB_PROPERTIES = 4;
	final static int TAB_INTERACT = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commandcard);
		
		activity = this;
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent() {
			
			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.ALERT;
			}
			
			@Override
			public void processMessage(int sender, int reciever, String message) {
				CommandCardActivity.activity.createAlert(message);
				
			}
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent() {
			
			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.PURCHASE_SUCCESS;
			}
			
			@Override
			public void processMessage(int sender, int reciever, String message) {
				CommandCardActivity.activity.createAlert("You have succesfully purchased the property");
				TileActivity.activity.setPurchaseButtonStatus(PlayerDevice.currentPlayer, true);
				TileActivity.activity.count++;
				TileActivity.activity.txtNotice.setText("You Own " + TileActivity.activity.count + "/" + TileActivity.activity.totalCount + " Properties in This Region");
				
			}
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent() {
			
			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.PAY_FINE_SUCCESS;
			}
			
			@Override
			public void processMessage(int sender, int reciever, String message) {
				CommandCardActivity.activity.createAlert("Your fee was received");
				TileActivity.activity.btnPurchase.setEnabled(false);
				
			}
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent() {
			
			@Override
			public boolean typeValid(int type) {
				return type == Message.START_PLAYER_TURN; 
			}
			
			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				Log.e("turn start", "TAB_TURN");
				tabBar.getTabWidget().getChildTabViewAt(TAB_TURN).setEnabled(true);
				tabBar.getTabWidget().getChildTabViewAt(TAB_TURN).setVisibility(View.VISIBLE);
				tabBar.getTabWidget().getChildTabViewAt(TAB_TILE).setVisibility(View.GONE);
				Log.e("turn start", "TAB_TURN_1");
				tabBar.setCurrentTab(TAB_TURN);
				Log.e("turn start", "TAB_TURN_2");
			}
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.MOVEMENT_ROLL;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				TabTurnActivity.activity.btnTurn.setVisibility(View.VISIBLE);
				TabTurnActivity.activity.txtYourTurn.setText(message);
			}
			
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.CHOOSE_FORK_PATH;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				tabBar.getTabWidget().getChildTabViewAt(TAB_TILE).setVisibility(View.GONE);
				tabBar.getTabWidget().getChildTabViewAt(TAB_DECISION).setVisibility(View.VISIBLE);
				tabBar.setCurrentTab(TAB_DECISION);
				
				String choice1 = message.substring(0, message.indexOf(":"));
				String choice2 = message.substring(message.indexOf(":"));
				
				DecisionActivity.activity.setButtonName(
						choice1.substring(choice1.indexOf(")")+1), 
						choice2.substring(choice2.indexOf(")")+1)
					);
			}
			
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				return type == Message.YOUR_TURN_IS_OVER;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				tabBar.getTabWidget().getChildTabViewAt(TAB_TURN).setEnabled(false);
				tabBar.getTabWidget().getChildTabViewAt(TAB_TURN).setVisibility(View.VISIBLE);
				tabBar.getTabWidget().getChildTabViewAt(TAB_TILE).setVisibility(View.GONE);
				tabBar.setCurrentTab(TAB_HOME);
			}
			
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				return type == Message.START_TILE_ACTIVITY;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				String tileName = "";
				int tileOwner = 0;
				String tileOwnerName = "";
				double tilePrice = 0D;
				String regionName = "";
				int regionTileCount = 0;
				int regionOwnedTileCount = 0;
				double currentBalance = 0;
				JSONObject obj = null;
        		try {
					obj = new JSONObject(message);
				} catch (JSONException e) {
					Log.e("Message.START_TILE_ACTIVITY", "JSON IS NULL");
					obj = null;
				}
        		if (obj != null){
        			tabBar.getTabWidget().getChildTabViewAt(TAB_TURN).setVisibility(View.GONE);
    				tabBar.getTabWidget().getChildTabViewAt(TAB_TILE).setVisibility(View.VISIBLE);
    				tabBar.setCurrentTab(TAB_TILE);
    				
    				try {
    					tileName = obj.getString("tileName");
						tileOwner = obj.getInt("tileOwner");
						tileOwnerName = obj.getString("tileOwnerName");
						tilePrice = obj.getDouble("tilePrice");
						regionName = obj.getString("regionName");
						regionTileCount = obj.getInt("regionTileCount");
						regionOwnedTileCount = obj.getInt("regionOwnedTileCount");
						currentBalance = obj.getDouble("currentBalance");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
				
				
        		TileActivity.activity.btnPurchase.setEnabled(true);
				if (tileOwner == Tile.OWNER_NEUTRAL){
					//if nobody owns it
					TileActivity.activity.setPurchaseButtonStatus(tileOwner, false);
					if (currentBalance < tilePrice){
						TileActivity.activity.btnPurchase.setEnabled(false);
					}
				} else if (tileOwner == Tile.OWNER_UNOWNABLE){
					//if the tile cannot be owned
					TileActivity.activity.setPurchaseButtonStatus(tileOwner, false);
					TileActivity.activity.btnPurchase.setEnabled(false);
				} else {
					//if somebody owns it
					if (tileOwner == PlayerDevice.currentPlayer){
						//if it is owned by the current player
						TileActivity.activity.setPurchaseButtonStatus(tileOwner, true);
					} else {
						//if somebody else owns it
						TileActivity.activity.setPurchaseButtonStatus(tileOwner, false);
					}
				}
				
				TileActivity.activity.setLandInfo(R.drawable.sample_house, tileName, ""+tilePrice, regionName, "Owned by " + tileOwnerName, regionOwnedTileCount, regionTileCount);
			}
			
		});
		
		/*if (isTablet(this)) { // isTablet : Move to MapActivty
			Intent i = new Intent(this, MapActivity.class);
			startActivity(i);
		}*/
		
		setTab();
		
		
		
		/*Bluetooth.registerBluetoothEvent(new BluetoothEvent() {
			
			@Override
			public boolean typeValid(int type) {
				//I don't know how it would fit to do so..
				return type == Message.MOVEMENT_DICE_ROLL; 
			}
			
			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				
			}
		});*/
		
	}
	
	private void setTab(){
		tabBar = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		
		// Turn Tab (Not Clickable) : 0
		intent = new Intent().setClass(this, TabTurnActivity.class);
		spec = tabBar.newTabSpec("Turn").setIndicator("Turn").setContent(intent);
		tabBar.addTab(spec);
		
		// Tile Tab (Not Visible until decision phase) : 0
		intent = new Intent().setClass(this, TileActivity.class);
		spec = tabBar.newTabSpec("Tile").setIndicator("Tile").setContent(intent);
		tabBar.addTab(spec);
		
		// Decision Tab (Not Visible until decision phase) : 0
		intent = new Intent().setClass(this, DecisionActivity.class);
		spec = tabBar.newTabSpec("Decision").setIndicator("Decision").setContent(intent);
		tabBar.addTab(spec);

		// Home Tab : 1
		intent = new Intent().setClass(this, TabHomeActivity.class);
		spec = tabBar.newTabSpec("Home").setIndicator("Home").setContent(intent);
		tabBar.addTab(spec);

		// Properties Tab : 2
		intent = new Intent().setClass(this, TabPropertiesActivity.class);
		spec = tabBar.newTabSpec("Properties").setIndicator("Properties").setContent(intent);
		tabBar.addTab(spec);

		// Interact Tab : 3
		intent = new Intent().setClass(this, TabInteractActivity.class);
		spec = tabBar.newTabSpec("Interact").setIndicator("Interact").setContent(intent);
		tabBar.addTab(spec);
		
		
				
		tabBar.setCurrentTab(TAB_HOME);
		tabBar.getTabWidget().getChildTabViewAt(TAB_TURN).setEnabled(false); // Turn Tab not clickable..
		tabBar.getTabWidget().getChildTabViewAt(TAB_DECISION).setVisibility(View.GONE); // Decision tab hidden away
		tabBar.getTabWidget().getChildTabViewAt(TAB_TILE).setVisibility(View.GONE); //Tile tab hidden away
		
	}
	
	@Override
	public void onBackPressed() {
		if(tabBar.getCurrentTab() == TAB_HOME) // If Home Tab, Finish Activity
			finish();
		else 							// else Another Tab, move to Home Tab
			tabBar.setCurrentTab(TAB_HOME); 
	}

	public void createAlert(String msg){
		new AlertDialog.Builder(this)
		.setMessage(msg)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which){
				// TODO
			}
		})
		.show();		
	}
	
	public boolean isTablet(Context context) {
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		return (xlarge || large);
	}


}