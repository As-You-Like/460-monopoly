package com.example.monopoly;

import static android.widget.Toast.makeText;

import java.util.ArrayList;

import com.example.monopoly.R;
import com.example.monopoly.R.id;
import com.example.monopoly.R.layout;
import com.example.monopoly.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SaveGameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_savegame);
		//final ListView listView = (ListView) findViewById(R.id.lstHosts);
		ArrayList<String> listItems = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			listItems.add("");
		}

		final ArrayAdapter<String> myadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listItems);

		// Assign adapter to ListView
		//listView.setAdapter(myadapter);

		/*listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				makeText(getApplicationContext(), "This feature is not available.", Toast.LENGTH_LONG)
						.show();
			}
		});*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
