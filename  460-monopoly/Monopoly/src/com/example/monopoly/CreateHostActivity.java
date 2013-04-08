package com.example.monopoly;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.controllers.Device;
import com.example.controllers.Game;
import com.example.controllers.HostDevice;

public class CreateHostActivity extends Activity {

	public static CreateHostActivity activity = null;

	private Button btnStartGame = null;
	public EditText edtGameName = null;

	private ArrayList<String> gamelist;
	private ArrayAdapter<String> spinnerAdapter; 
	private Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createhost);

		// Setup this activity as needed
		CreateHostActivity.activity = this;

		this.btnStartGame = (Button) this.findViewById(R.id.btnStartGame);
		this.edtGameName = (EditText) this.findViewById(R.id.edtGameName);

		this.btnStartGame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String gameName = CreateHostActivity.activity.edtGameName.getText().toString();

				HostDevice host = new HostDevice(true); // create host device bject and indicate the current device is the host.
				host.listenStart(false, gameName);
				
				//creates the game class and global timer loop
				new Game(gameName);

				Intent intent = new Intent(CreateHostActivity.activity, LobbyActivity.class);
				intent.putExtra("gn", gameName);
				startActivity(intent);
			}

		});
		
		int currentPlayerNumber = Device.currentPlayer;
		spinner = (Spinner) findViewById(R.id.spinner);
		// Spinner Drop down elements
		gamelist = new ArrayList<String>();
		gamelist.add("League of Legend");
		gamelist.add("Diablo");
		gamelist.add("Simcity");
	}
}