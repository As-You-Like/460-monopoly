package com.example.monopoly;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FindHostActivity extends Activity {
	
	public static FindHostActivity activity = null;
	
	private Button btnBack = null;
	private Button btnRefresh = null;
	private ListView lstHosts = null;
	private TextView txtTitle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findhost); 
		
		// Setup this activity as needed
		FindHostActivity.activity = this;
		
		//this.btnBack    = (Button) this.findViewById(R.id.btnBack);
		this.btnRefresh = (Button) this.findViewById(R.id.btnRefresh);
		this.lstHosts   = (ListView) this.findViewById(R.id.lstHosts);
		this.txtTitle   = (TextView) this.findViewById(R.id.txtTitle);
		
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
