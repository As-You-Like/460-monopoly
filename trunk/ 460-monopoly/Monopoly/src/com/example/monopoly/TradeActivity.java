package com.example.monopoly;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

	TradeAdapter TradeAdapter;
	ArrayList<String> myPropertyList;
	ArrayList<String> playerPropertyList;
	
	String stackMyPropertyName = "";
	String stackPlayerPropertyName = "";
	boolean FIRST_SELECT_MY = true;
	boolean FIRST_SELECT_PLAYER = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade);
		setView();
		
		setPlayerInfo("Annie",4000, "Katarina", 2500);
		
		// Create Annie Property list
		myPropertyList = new ArrayList<String>();
		myPropertyList.add("Slade Hill");
		myPropertyList.add("Boyston");
		myPropertyList.add("Currito");
		myPropertyList.add("Pres Villa");
		myPropertyList.add("Westone");
		myPropertyList.add("APT");
		
		// Create Katarina Property list
		playerPropertyList = new ArrayList<String>();
		playerPropertyList.add("Annie's Slade Hill");
		playerPropertyList.add("Annie's Boyston");
		playerPropertyList.add("Annie's Currito");
		playerPropertyList.add("Annie's Pres Villa");
		playerPropertyList.add("Annie's Westone");
		playerPropertyList.add("Annie's APT");
		

		
		TradeAdapter = new TradeAdapter(this, myPropertyList, true);
		listViewMy.setAdapter(TradeAdapter);
		listViewMy.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (FIRST_SELECT_MY) {
					FIRST_SELECT_MY = false;
					stackMyPropertyName = myPropertyList.get(position);
					selectMyProperty(myPropertyList.get(position));
					myPropertyList.remove(position);
					TradeAdapter = new TradeAdapter(getBaseContext(), myPropertyList, true);
					listViewMy.setAdapter(TradeAdapter);
				} else {
					stackMyPropertyName = txtMySelect.getText().toString();
					selectMyProperty(myPropertyList.get(position));
					myPropertyList.remove(position);
					myPropertyList.add(myPropertyList.size(), stackMyPropertyName);
					TradeAdapter = new TradeAdapter(getBaseContext(), myPropertyList, true);
					listViewMy.setAdapter(TradeAdapter);
				}
				
				// send my list index to Katarina
				selectPlayerProperty(position);
			}
		});
		
		TradeAdapter = new TradeAdapter(this, playerPropertyList, false);
		listViewPlayer.setAdapter(TradeAdapter);
	}
	
	/**
	 * Annie's list index to here 
	 **/
	public void selectPlayerProperty(int idx){
		if (FIRST_SELECT_PLAYER) {
			FIRST_SELECT_PLAYER = false;
			stackPlayerPropertyName = playerPropertyList.get(idx);
			selectPlayerProperty(playerPropertyList.get(idx));
			playerPropertyList.remove(idx);
			TradeAdapter = new TradeAdapter(getBaseContext(), playerPropertyList, false);
			listViewPlayer.setAdapter(TradeAdapter);
		} else {
			stackPlayerPropertyName = txtPlayerSelect.getText().toString();
			selectPlayerProperty(playerPropertyList.get(idx));
			playerPropertyList.remove(idx);
			playerPropertyList.add(playerPropertyList.size(), stackPlayerPropertyName);
			TradeAdapter = new TradeAdapter(getBaseContext(), playerPropertyList, false);
			listViewPlayer.setAdapter(TradeAdapter);
		}
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

	private void selectMyProperty(String property){
		txtMySelect.setText(property);
	}
	
	private void selectPlayerProperty(String property){
		txtPlayerSelect.setText(property);
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
	}

	public void clickEventDeny(View v){
		Toast.makeText(this, "Deny..", Toast.LENGTH_SHORT).show();
	}			
	

	public class TradeAdapter extends BaseAdapter {
		Context context;
		LayoutInflater inflater;
		ArrayList<String> itemlists;
		boolean MY = false;

		public TradeAdapter(Context c, ArrayList<String> arrayList, boolean my) {
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

		txtMySelect = (TextView) findViewById(R.id.txt_mychoice);
		txtPlayerSelect = (TextView) findViewById(R.id.txt_playerchoice);
		txtMyMagrin = (TextView) findViewById(R.id.txt_my_margin);
		txtPlayerMagrin = (TextView) findViewById(R.id.txt_player_margin);
		listViewMy = (ListView) findViewById(R.id.mylistview);
		listViewPlayer = (ListView) findViewById(R.id.playerlistview);

	}
	
	TextView txtMyName;
	TextView txtPlayer;
	TextView txtMyCash;
	TextView txtPlayerCash;
	TextView txtMySelect;
	TextView txtPlayerSelect;
	TextView txtMyMagrin;
	TextView txtPlayerMagrin;
	ListView listViewMy;
	ListView listViewPlayer;
	
}
