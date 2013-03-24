package com.example.monopoly;

import static android.widget.Toast.makeText;

import com.example.monopoly.R;
import com.example.monopoly.R.id;
import com.example.monopoly.R.layout;
import com.example.monopoly.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NewLoadActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newload);
		Button buttonNewGame = (Button) this.findViewById(R.id.btnNewGame);
		buttonNewGame.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				makeText(getApplicationContext(), "Go to CreateHostActivity", Toast.LENGTH_LONG).show();

			}
		});

		Button buttonLoadGame = (Button) this.findViewById(R.id.btnLoadGame);
		buttonLoadGame.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String actionName = "android.intent.action.SaveGameActivity";
				Intent intent = new Intent(actionName);
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
