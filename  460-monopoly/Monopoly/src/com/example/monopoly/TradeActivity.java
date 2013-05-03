package com.example.monopoly;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.bluetooth.Bluetooth;
import com.example.bluetooth.BluetoothEvent;
import com.example.bluetooth.Message;
import com.example.controllers.Device;
import com.example.controllers.HostDevice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TradeActivity extends Activity {

	public static TradeActivity activity;
	
	TradaAdapter myAdapter;
	TradaAdapter playerAdapter;
	TradaAdapter mySelectAdapter;
	TradaAdapter playerSelectAdapter;
	
	ArrayList<String> myPropertyList;
	ArrayList<String> playerPropertyList;
	ArrayList<String> mySelectList;
	ArrayList<String> playerSelectList;
	
	String stackMyPropertyName = "";
	String stackPlayerPropertyName = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade);
		setView();
		
		setPlayerInfo("Annie",4000, "Katarina", 2500);
		
		// Create Annie Property list
		myPropertyList = new ArrayList<String>();
		/*myPropertyList.add("Slade Hill");
		myPropertyList.add("Boyston");
		myPropertyList.add("Currito");
		myPropertyList.add("Pres Villa");
		myPropertyList.add("Westone");
		myPropertyList.add("APT");*/
		
		// Create Katarina Property list
		playerPropertyList = new ArrayList<String>();
		/*playerPropertyList.add("Annie's Slade Hill");
		playerPropertyList.add("Annie's Boyston");
		playerPropertyList.add("Annie's Currito");
		playerPropertyList.add("Annie's Pres Villa");
		playerPropertyList.add("Annie's Westone");
		playerPropertyList.add("Annie's APT");*/
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    String tiles = extras.getString("Tiles");
		    String tileNames = extras.getString("TileNames");
		    String otherTiles = extras.getString("OtherTiles");
		    String otherTileNames = extras.getString("OtherTileNames");
		    this.populateList(tiles, tileNames, otherTiles, otherTileNames);
		}
		
		mySelectList = new ArrayList<String>(); 	// Annie Select list
		playerSelectList = new ArrayList<String>(); // Katarina Select list
		
		myAdapter = new TradaAdapter(this, myPropertyList, true);
		lvMy.setAdapter(myAdapter);
		
		mySelectAdapter = new TradaAdapter(getBaseContext(), mySelectList, true);
		lvMySelect.setAdapter(mySelectAdapter);
		
		playerAdapter = new TradaAdapter(this, playerPropertyList, false);
		lvPlayer.setAdapter(playerAdapter);
		
		playerSelectAdapter = new TradaAdapter(this, playerSelectList, false);
		lvPlayerSelect.setAdapter(playerSelectAdapter);
		
		
		lvMy.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				mySelectList.add(myPropertyList.get(position));
				myPropertyList.remove(position);
				
				GhostTile t = GhostTile.myList.get(position);
				//GhostTile.myOfferList.add(t);
				//GhostTile.myList.remove(t);
				
				myAdapter.notifyDataSetChanged();
				
				HostDevice.host.sendMessage(Message.TRADE_CHANGE_DETAILS, t.id + ":" + 1);
				
			}
		});

		lvMySelect.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				myPropertyList.add(mySelectList.get(position));
				mySelectList.remove(position);
				
				
				GhostTile t = GhostTile.myOfferList.get(position);
				//GhostTile.myOfferList.remove(t);
				//GhostTile.myList.add(t);
				
				mySelectAdapter.notifyDataSetChanged();
				//myAdapter.notifyDataSetChanged();
				
				HostDevice.host.sendMessage(Message.TRADE_CHANGE_DETAILS, t.id + ":" + 0);
			}
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.TRADE_UPDATE_DETAILS;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				//message = "$amount/tileId:directions:changePlayer";
				int delimiter = message.indexOf(":");
				int delimiter2 = message.indexOf(":", delimiter+1);
				String data = message.substring(0, delimiter);
				String direction = message.substring(delimiter+1, delimiter2);
				int player = Integer.parseInt(message.substring(delimiter2+1));
				double cash = -1;
				int tile = -1;
				
				if (data.charAt(0) == '$'){
					cash = Double.parseDouble(data.substring(1));
					if (Device.currentPlayer == player){
						
						
					} else {
						//If the data is about the other player
						
					}
				} else {
					tile = Integer.parseInt(data);
					int id = -1;
					
					if (Device.currentPlayer == player){
						//If the data is about the current player
						if (direction.equals("0")){ //remove from offer list
							for (int i=0; i<GhostTile.myOfferList.size(); i++){
								if (GhostTile.myOfferList.get(i).id == tile){
									id = i;
								}
							}
							
							if (id >= 0){
								GhostTile t = GhostTile.myOfferList.get(id);
								GhostTile.myOfferList.remove(t);
								GhostTile.myList.add(t);
								
								myPropertyList.add(t.name);
								mySelectList.remove(t.name);
								
								mySelectAdapter.notifyDataSetChanged();
								myAdapter.notifyDataSetChanged();
								playerSelectAdapter.notifyDataSetChanged();
								playerAdapter.notifyDataSetChanged();
							}
						} else { //add to offer list
							for (int i=0; i<GhostTile.myList.size(); i++){
								if (GhostTile.myList.get(i).id == tile){
									id = i;
								}
							}
							
							if (id >= 0){
								GhostTile t = GhostTile.myList.get(id);
								GhostTile.myOfferList.add(t);
								GhostTile.myList.remove(t);
								
								myPropertyList.remove(t.name);
								mySelectList.add(t.name);
								
								mySelectAdapter.notifyDataSetChanged();
								myAdapter.notifyDataSetChanged();
								playerSelectAdapter.notifyDataSetChanged();
								playerAdapter.notifyDataSetChanged();
							}
						}
					} else {
						//If the data is about the other player
						if (direction.equals("0")){ //remove from offer list
							for (int i=0; i<GhostTile.otherOfferList.size(); i++){
								if (GhostTile.otherOfferList.get(i).id == tile){
									id = i;
								}
							}
							
							if (id >= 0){
								GhostTile t = GhostTile.otherOfferList.get(id);
								GhostTile.otherOfferList.remove(t);
								GhostTile.otherList.add(t);
								
								playerPropertyList.add(t.name);
								playerSelectList.remove(t.name);
								
								mySelectAdapter.notifyDataSetChanged();
								myAdapter.notifyDataSetChanged();
								playerSelectAdapter.notifyDataSetChanged();
								playerAdapter.notifyDataSetChanged();
							}
						} else { //add to offer list
							Log.e("Tile", "Tile: "+tile);
							for (int i=0; i<GhostTile.otherList.size(); i++){
								Log.e("Tile", ""+GhostTile.otherList.get(i).id);
								if (GhostTile.otherList.get(i).id == tile){
									id = i;
								}
							}
							if (id >= 0){
								GhostTile t = GhostTile.otherList.get(id);
								GhostTile.otherOfferList.add(t);
								GhostTile.otherList.remove(t);
								
								playerPropertyList.remove(t.name);
								playerSelectList.add(t.name);
								
								mySelectAdapter.notifyDataSetChanged();
								myAdapter.notifyDataSetChanged();
								playerSelectAdapter.notifyDataSetChanged();
								playerAdapter.notifyDataSetChanged();
							}
						}
					}
				}
				
				
				
			}
			
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.TRADE_ACCEPT_INFORM;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				// Message: <name of other party> has accepted the trade in it's current state.
				createAlert("The other person has accepted the trade in it's current state");
			}
			
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.TRADE_REJECT_INFORM;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				// Message: Your terms were rejected
				createAlert("The other person has rejected your trade");
			}
			
		});
		
		Bluetooth.registerBluetoothEvent(new BluetoothEvent(){

			@Override
			public boolean typeValid(int type) {
				// TODO Auto-generated method stub
				return type == Message.TRADE_SUCCESS;
			}

			@Override
			public void processMessage(int sender, int reciever, String message) {
				// TODO Auto-generated method stub
				createAlert("The Trade was successful");
				TradeActivity.activity.finish();
			}
			
		});
	}
	
	
    public void selectPlayerProperty(int idx){
    	playerSelectList.add(playerPropertyList.get(idx));
    	playerPropertyList.remove(idx);
    	playerAdapter.notifyDataSetChanged();
    }
    
    public void returnPlayerProperty(int idx){
    	playerPropertyList.add(playerSelectList.get(idx));
    	playerSelectList.remove(idx);
    	playerSelectAdapter.notifyDataSetChanged();
    }
    
    
	/**
	 *  If experiencing margin..
	 *  @param who 0: my , 1: player
	 *  @param val cash
	 * */  
	public void setMargin(int who, int val) {
		if (who == 0) // set my margin
			txtMyMagrin.setText(String.valueOf(val));
		else if (who == 1) // set player margin
			txtPlayerMagrin.setText(String.valueOf(val));
	}
	
	/**
	 * @param My name
	 * @param My cash
	 * @param Player name
	 * @param Player cash
	 * **/
	private void setPlayerInfo(String myName, int myCash, String playerName, int pCash) {
		txtMyName.setText(myName);
		txtMyCash.setText("$" + myCash);
		txtPlayer.setText(playerName);
		txtPlayerCash.setText("$" + pCash);
	}

	public void clickEventAccept(View v){
		Toast.makeText(this, "Accpet..", Toast.LENGTH_SHORT).show();
		HostDevice.host.sendMessage(Message.TRADE_ACCEPT, "");
	}

	public void clickEventDeny(View v){
		Toast.makeText(this, "Deny..", Toast.LENGTH_SHORT).show();
		HostDevice.host.sendMessage(Message.TRADE_REJECT, "");
	}			
	

	public class TradaAdapter extends BaseAdapter {
		Context context;
		LayoutInflater inflater;
		ArrayList<String> itemlists;
		boolean MY = false;

		public TradaAdapter(Context c, ArrayList<String> arrayList, boolean my) {
			this.context = c;
			this.itemlists = arrayList;
			this.MY = my;
			
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return itemlists.size();
		}

		public String getItem(int position) {
			return itemlists.get(position).toString();
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.listrow_trade_property, parent, false);
			}

			TextView txt = (TextView) convertView.findViewById(R.id.txt);
			txt.setText(itemlists.get(position).toString());
			
			if(MY)
				txt.setTextColor(0xff9ee02b);
			else 
				txt.setTextColor(0xffffb53d);	
			
			return convertView;
		}

	}

	public void setView() {
		txtMyName = (TextView) findViewById(R.id.txt_my);
		txtPlayer = (TextView) findViewById(R.id.txt_player);
		txtMyCash = (TextView) findViewById(R.id.txt_mycash);
		txtPlayerCash = (TextView) findViewById(R.id.txt_playercash);

		lvMySelect = (ListView) findViewById(R.id.selectlistview);
		lvPlayerSelect = (ListView) findViewById(R.id.playerSelectlistview);
		txtMyMagrin = (TextView) findViewById(R.id.txt_my_margin);
		txtPlayerMagrin = (TextView) findViewById(R.id.txt_player_margin);
		lvMy = (ListView) findViewById(R.id.mylistview);
		lvPlayer = (ListView) findViewById(R.id.playerlistview);

	}
	
	TextView txtMyName;
	TextView txtPlayer;
	TextView txtMyCash;
	TextView txtPlayerCash;
	TextView txtMyMagrin;
	TextView txtPlayerMagrin;
	
	
	ListView lvMySelect;
	ListView lvPlayerSelect;
	ListView lvMy;
	ListView lvPlayer;

	public void populateList(String tiles, String tileNames, String otherTiles, String otherTileNames) {
		// TODO Auto-generated method stub
		try {
			JSONArray tileArr = new JSONArray(tiles);
			JSONArray tileNameArr = new JSONArray(tileNames);
			
			JSONArray otherTileArr = new JSONArray(otherTiles);
			JSONArray otherTileNameArr = new JSONArray(otherTileNames);
			
			for (int i=0; i<tileArr.length(); i++){
				new TradeActivity.GhostTile(tileNameArr.getString(i), tileArr.getInt(i), 0);
				myPropertyList.add(tileNameArr.getString(i));
			}
			
			for (int i=0; i<otherTileArr.length(); i++){
				new TradeActivity.GhostTile(otherTileNameArr.getString(i), otherTileArr.getInt(i), 2);
				playerPropertyList.add(otherTileNameArr.getString(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static class GhostTile {
		public String name;
		public int id;
		
		public static ArrayList<TradeActivity.GhostTile> myList = new ArrayList<TradeActivity.GhostTile>();
		public static ArrayList<TradeActivity.GhostTile> myOfferList = new ArrayList<TradeActivity.GhostTile>();
		public static ArrayList<TradeActivity.GhostTile> otherList = new ArrayList<TradeActivity.GhostTile>();
		public static ArrayList<TradeActivity.GhostTile> otherOfferList = new ArrayList<TradeActivity.GhostTile>();
		
		public GhostTile(String name, int id, int type){
			this.name = name;
			this.id = id;
			
			switch (type){
			case 0:
				myList.add(this);
				break;
			case 1:
				myOfferList.add(this);
				break;
			case 2:
				otherList.add(this);
				break;
			case 3:
				otherOfferList.add(this);
				break;
			}
		}
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
	
}
