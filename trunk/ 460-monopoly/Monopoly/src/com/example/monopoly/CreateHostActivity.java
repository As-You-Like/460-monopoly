package com.example.monopoly;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CreateHostActivity extends Activity {
	
	public static CreateHostActivity activity = null;
	
	private Button btnStartGame = null;
	public EditText edtGameName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createhost); 
		
		// Setup this activity as needed
		CreateHostActivity.activity = this;
		
		this.btnStartGame = (Button) this.findViewById(R.id.btnStartGame);
		this.edtGameName = (EditText) this.findViewById(R.id.edtGameName);
		
		this.btnStartGame.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String gameName = CreateHostActivity.activity.edtGameName.getText().toString();
				
				Intent intent = new Intent(CreateHostActivity.activity, LobbyActivity.class);
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

}
