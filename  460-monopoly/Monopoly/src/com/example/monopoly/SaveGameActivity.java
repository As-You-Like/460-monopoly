package com.example.monopoly;

import static android.widget.Toast.makeText;

import java.util.ArrayList;

import com.example.controllers.DatabaseThread;
import com.example.controllers.SQLHelper;
import com.example.monopoly.R;
import com.example.monopoly.R.id;
import com.example.monopoly.R.layout;
import com.example.monopoly.R.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SaveGameActivity extends Activity {
	
	public static SaveGameActivity activity;
	
	public static ArrayList<String> listItems = new ArrayList<String>();
	public static String[] listViewContents;
	public static String gameNamee;
	
	public ListView lstGames;
	public Button btnStart;
	public SQLHelper helper;
	public static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_savegame);
		activity = this;
		context = this;
		
		DatabaseThread dbt = new DatabaseThread();
		helper = new SQLHelper(context);
		dbt.db = helper.getWritableDatabase();
		
		
		dbt.isGettingGameNames = true;
		dbt.start();
		
		lstGames = (ListView)this.findViewById(R.id.lstGames);


		final ArrayAdapter<String> myadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listItems);
		
		lstGames.setAdapter(myadapter);
		
		lstGames.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				// === Respond to clicks on a specific game here ===
				gameNamee = listViewContents[position];
				Intent intent = new Intent(SaveGameActivity.activity, DataLoadingActivity.class);
				startActivity(intent);	
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public static void populateListView(){
		// === populate list from database here ===	
		for (int i = 0; i < listViewContents.length; i++) {
			listItems.add(listViewContents[i]);
		}
	}

}
