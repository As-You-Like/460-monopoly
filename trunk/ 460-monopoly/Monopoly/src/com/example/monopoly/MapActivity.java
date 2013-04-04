package com.example.monopoly;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MapActivity extends Activity {
	
	public static MapActivity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		Toast.makeText(this, "This is Map!!", Toast.LENGTH_SHORT).show();

	}
	
	
	

}