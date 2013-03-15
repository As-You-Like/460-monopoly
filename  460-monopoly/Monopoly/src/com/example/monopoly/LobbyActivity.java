package com.example.monopoly;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class LobbyActivity extends Activity {
	
	public static LobbyActivity activity = null;
	
	private Button btnBack = null;
	private Button btnRefresh = null;
	private ListView lstPlayers = null;
	private TextView txtGameName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby); 
		
		//this.btnBack    = (Button) this.findViewById(R.id.btnBack);
		this.btnRefresh = (Button) this.findViewById(R.id.btnRefresh);
		this.lstPlayers   = (ListView) this.findViewById(R.id.lstPlayers);
		this.txtGameName   = (TextView) this.findViewById(R.id.txtGameName);
		
		this.btnRefresh.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	} 

}
