package com.example.monopoly;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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
				myAdapter.notifyDataSetChanged();
				selectPlayerProperty(position);
			}
		});

		lvMySelect.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				myPropertyList.add(mySelectList.get(position));
				mySelectList.remove(position);
				mySelectAdapter.notifyDataSetChanged();
				returnPlayerProperty(position);
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
	}

	public void clickEventDeny(View v){
		Toast.makeText(this, "Deny..", Toast.LENGTH_SHORT).show();
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
	
}
