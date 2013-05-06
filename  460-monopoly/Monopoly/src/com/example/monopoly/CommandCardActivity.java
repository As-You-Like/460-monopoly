package com.example.monopoly;

import java.util.ArrayList;

import org.json.JSONArray;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.BluetoothEvent;
import com.example.bluetooth.Message;
import com.example.controllers.Device;
import com.example.controllers.HostDevice;
import com.example.controllers.PlayerDevice;
import com.example.model.Tile;
import com.example.model.Unit;

@SuppressWarnings("deprecation")
public class CommandCardActivity extends TabActivity { 
	public TabHost tabBar;
	public static CommandCardActivity activity;

	final static int TAB_TURN = 0;
	final static int TAB_TILE = 1;
	final static int TAB_DECISION = 2;
	final static int TAB_UPGRADE = 3;
	final static int TAB_HOME = 4;
	final static int TAB_PROPERTIES = 5;
	final static int TAB_INTERACT = 6;
	final static int TAB_TRADE = 7;
	
	public static double[] upgrade = new double[4];
	public static boolean[] upgradeStat = new boolean[4];
	public static double cash;
	public static double rent;
	public static boolean turn = false;
	
	public MenuItem menuQuit;
	
	ArrayList<Integer> propertyRegions = new ArrayList<Integer>();
	ArrayList<ArrayList<String>> properties = new ArrayList<ArrayList<String>>();
	
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
				TileActivity.activity.regionTilesOwned++;
			}
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent() {
			
			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.UPGRADE_SUCCESS;
			}
			
			@Override
			public void processMessage(int sender, int reciever, String message) {
				CommandCardActivity.activity.createAlert("You have succesfully upgraded your property");
				rent += upgrade[Integer.parseInt(message)];
				upgradeStat[Integer.parseInt(message)] = true;
				TileActivity.activity.setCashInfo(""+rent);
				
				//if (tabHost.)
				onBackPressed();
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
				CommandCardActivity.turn = true;
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
				Log.e("", "Event 22 Executed");
				if (TabTurnActivity.activity.btnTurn.getVisibility() != View.VISIBLE){
					TabTurnActivity.activity.btnTurn.setVisibility(View.VISIBLE);
					TabTurnActivity.activity.txtYourTurn.setText(message);
				}
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
				CommandCardActivity.activity.goToTab(TAB_DECISION);
				
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
				return type == Message.DATA_HOME_TAB;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				JSONObject obj = null;
        		try {
					obj = new JSONObject(message);
				} catch (JSONException e) {
					Log.e("Message.START_TILE_ACTIVITY", "JSON IS NULL");
					obj = null;
				}
        		if (obj != null){
        			try {
						TabHomeActivity.activity.setInfo(
								0, 
								0, 
								obj.getString("playerName"), 
								obj.getString("playerColor"), 
								obj.getString("playerCash"), 
								obj.getString("playerAssets"),
								obj.getString("playerOwned"), 
								obj.getString("playerCompleted"), 
								obj.getString("playerTrade"), 
								obj.getString("playerShuttle")
							);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		} else {
        			createAlert("There was an error, go to another tab and come back");
        		}
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
				double tileRent = 0D;
				String regionName = "";
				int regionTileCount = 0;
				int tileId = 0;
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
    					tileId = obj.getInt("tileId");
    					tileName = obj.getString("tileName");
						tileOwner = obj.getInt("tileOwner");
						tileOwnerName = obj.getString("tileOwnerName");
						tilePrice = obj.getDouble("tilePrice");
						tileRent = obj.getDouble("tileRent");
						regionName = obj.getString("regionName");
						regionTileCount = obj.getInt("regionTileCount");
						regionOwnedTileCount = obj.getInt("regionOwnedTileCount");
						currentBalance = obj.getDouble("currentBalance");
						
						rent = tilePrice;
						cash = currentBalance;
						upgrade[0] = obj.getDouble("upgrade1");
						upgrade[1] = obj.getDouble("upgrade2");
						upgrade[2] = obj.getDouble("upgrade3");
						upgrade[3] = obj.getDouble("upgrade4");
						
						upgradeStat[0] = obj.getBoolean("upgrade1bool");
						upgradeStat[1] = obj.getBoolean("upgrade2bool");
						upgradeStat[2] = obj.getBoolean("upgrade3bool");
						upgradeStat[3] = obj.getBoolean("upgrade4bool");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
				Tile t = null;
        		for (int i=0; i<Unit.entity.size() && t == null; i++){
        			Unit u = Unit.entity.get(i);
        			if (u instanceof Tile){
        				Tile t1 = (Tile)u;
        				if (tileId == t1.id){
        					t = t1;
        				}
        			}
        		}
        		
        		TileActivity.activity.setLandInfo(t.image, tileName, ""+tilePrice, regionName, "Owned by " + tileOwnerName, regionOwnedTileCount, regionTileCount);
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
						TileActivity.activity.setRent(""+tileRent);
					}
				}
				
				
			}
			
		});
		
		setTab();
		
		tabBar.setOnTabChangedListener(new OnTabChangeListener(){

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				if (tabId.equals("Home")){
					TabHomeActivity.activity.clearInfo();
					HostDevice.host.sendMessage(Message.REQUEST_HOME_DATA, "");
				}
				if (tabId.equals("Upgrade")){
					UpgradeActivity.activity.setButtonValue(
							upgrade[0], 
							upgrade[1], 
							upgrade[2], 
							upgrade[3]
						);
					
					UpgradeActivity.activity.setRentValue(
							upgrade[0]+rent, 
							upgrade[1]+rent, 
							upgrade[2]+rent, 
							upgrade[3]+rent
						);
					
					UpgradeActivity.activity.enableUpgradeButtons(upgradeStat[0], upgradeStat[1], upgradeStat[2], upgradeStat[3]);
					
					UpgradeActivity.activity.setCashValues(cash, rent);
				}
				if (tabId.equals("Properties")){
					HostDevice.host.sendMessage(Message.REQUEST_PROPERTIES_DATA, "");
				}
			}
			
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.REQUEST_PROPERTIES_DATA_ACCEPT;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				//Tile.entity = new Tile[400][400];
				properties.clear();
				propertyRegions.clear();
				try {
					JSONArray json = new JSONArray(message);
					for (int i=0; i<json.length(); i++){
						JSONObject tile = new JSONObject(json.getString(i));
						String name = tile.getString("name");
						int region = tile.getInt("region");
						//t.id = tile.getInt("id");
						if (!propertyRegions.contains(region)){
							propertyRegions.add(region);
							properties.add(new ArrayList<String>());
						}
						int indexOfRegion = propertyRegions.indexOf(region);
						properties.get(indexOfRegion).add(name);
						
						Log.e("Tile Data: ", region+": "+name);
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				TabPropertiesActivity.populate();
				
			}
			
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.TRADE_INFORM;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				try {
					JSONObject msg = new JSONObject(message);
					int tradeReceiver = msg.getInt("Player");
					double cash = msg.getDouble("Cash");
					String tiles = msg.getString("Tiles");
					String tileNames = msg.getString("TileNames");
					
					String otherTiles = msg.getString("OtherTiles");
					String otherTileNames = msg.getString("OtherTileNames");
					
					//do a check
					if (Device.currentPlayer == tradeReceiver){
						//if the current player is the trade recipient
						Intent 
						intent = 
								new Intent(
								CommandCardActivity
								.activity,
								TradeActivity
								.class);
						intent.putExtra("Tiles", tiles);
						intent.putExtra("TileNames", tileNames);
						intent.putExtra("OtherTiles", otherTiles);
						intent.putExtra("OtherTileNames", otherTileNames);
						startActivity(intent);
						//ADD TO LISTVIEW
						//return
					} else {
						//If the current player is the trade starter (will already be in TradeActivity)
						TradeActivity.activity.populateList(tiles, tileNames, otherTiles, otherTileNames);
					} 
					//ADD TO LISTVIEW
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace(); 
				}
			}
			
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				return type == Message.IN_JAIL;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				CommandCardActivity.turn = false;
				CommandCardActivity.activity.tabBar.setCurrentTab(CommandCardActivity.TAB_HOME);
				CommandCardActivity.activity.tabBar.getTabWidget().getChildTabViewAt(CommandCardActivity.TAB_TURN).setVisibility(View.VISIBLE);
				CommandCardActivity.activity.tabBar.getTabWidget().getChildTabViewAt(CommandCardActivity.TAB_TILE).setVisibility(View.GONE);
				CommandCardActivity.activity.tabBar.getTabWidget().getChildTabViewAt(CommandCardActivity.TAB_TURN).setEnabled(false);
			}
		});
		
		
		TabHomeActivity.activity.clearInfo();
		HostDevice.host.sendMessage(Message.REQUEST_HOME_DATA, "");
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
		
		// Upgrade Tab (Not Visible until decision phase) : 0
		intent = new Intent().setClass(this, UpgradeActivity.class);
		spec = tabBar.newTabSpec("Upgrade").setIndicator("Upgrade").setContent(intent);
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
		
		// Trade Tab : 3 (Not Visible until trade is initiated) : 0
		intent = new Intent().setClass(this, TradeActivity.class);
		spec = tabBar.newTabSpec("Trade").setIndicator("Trade").setContent(intent);
		tabBar.addTab(spec);
		
		
				
		tabBar.setCurrentTab(TAB_HOME);
		tabBar.getTabWidget().getChildTabViewAt(TAB_TURN).setEnabled(false); // Turn Tab not clickable..
		
		tabBar.getTabWidget().getChildTabViewAt(TAB_TILE).setVisibility(View.GONE); // Tile tab hidden away
		tabBar.getTabWidget().getChildTabViewAt(TAB_DECISION).setVisibility(View.GONE); // Decision tab hidden away
		tabBar.getTabWidget().getChildTabViewAt(TAB_UPGRADE).setVisibility(View.GONE); // Upgrade tab hidden away
		tabBar.getTabWidget().getChildTabViewAt(TAB_TRADE).setVisibility(View.GONE); // Upgrade tab hidden away
		
		tabBar.getTabWidget().getChildTabViewAt(TAB_INTERACT).setVisibility(View.GONE); // Upgrade tab hidden away
		//tabBar.getTabWidget().getChildTabViewAt(TAB_PROPERTIES).setEnabled(false); // Properties tab hidden away
		
	}
	
	@Override
	public void onBackPressed() {
		if(tabBar.getCurrentTab() == TAB_HOME){ // If Home Tab, Finish Activity
			//do nothing, back button is disabled on the home tab
		} else if (tabBar.getCurrentTab() == TAB_DECISION){
			//do nothing, decision tab is mandatory, can't leave it
			createAlert("You must choose a path");
		} else if (tabBar.getCurrentTab() == TAB_UPGRADE){
			this.goToTab(TAB_TILE);
		}else
			// else Another Tab, move to Home Tab
			tabBar.setCurrentTab(TAB_HOME); 
	}
	
	public void goToTab(int tabCode){
		tabBar.setCurrentTab(tabCode);
		tabBar.getTabWidget().getChildTabViewAt(TAB_UPGRADE).setVisibility(View.GONE); // Upgrade tab hidden away
		tabBar.getTabWidget().getChildTabViewAt(TAB_TILE).setVisibility(View.GONE); // Tile tab made visible
		tabBar.getTabWidget().getChildTabViewAt(TAB_TURN).setVisibility(View.GONE); // Tile tab made visible
		tabBar.getTabWidget().getChildTabViewAt(TAB_DECISION).setVisibility(View.GONE); // Tile tab made visible
		tabBar.getTabWidget().getChildTabViewAt(tabCode).setVisibility(View.VISIBLE); // Tile tab made visible
	}
	
	public void goToInteractTab(int tabCode){
		tabBar.setCurrentTab(tabCode);
		tabBar.getTabWidget().getChildTabViewAt(TAB_INTERACT).setVisibility(View.GONE); // Upgrade tab hidden away
		tabBar.getTabWidget().getChildTabViewAt(TAB_TRADE).setVisibility(View.GONE); // Tile tab made visible
		tabBar.getTabWidget().getChildTabViewAt(tabCode).setVisibility(View.VISIBLE); // Tile tab made visible
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menuQuit = menu.add(0, 1, Menu.NONE, "Quit Game");
		
		menuQuit.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
					// TODO Auto-generated method stub
					SplashActivity.activity.cascadeQuitBool = true;
					CommandCardActivity.activity.terminate();				
					return true;
			}
		});
		return true;
	}
	
	public void terminate() {
	      Log.i("","terminated!!");
	      super.onDestroy();
	      this.finish();
	}

}