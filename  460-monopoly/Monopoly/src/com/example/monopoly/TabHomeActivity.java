package com.example.monopoly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.model.TileProperty;

public class TabHomeActivity extends Activity {
	TabHomeActivity activity = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab_home);
		activity = this;
		
	}
	
	public void callTurnActivity(){
		Intent intent =new Intent(this, TurnActivity.class);
		startActivity(intent);
	}

	public void clickEventAdd(View v) {
		TileProperty item = new TileProperty();
		item.setName("Trees Region");
		item.setOwned("owned : false");
		item.setValue("value : $5000");
		TabPropertiesActivity.activity.addItem(item);
	}

	public void clickEventRemove(View v) {
		TabPropertiesActivity.activity.removeItem("Trees Region");
	}

	public void clickEventClear(View v) {
		TabPropertiesActivity.activity.clear();
	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed(); // Call CommandCardActivity.class  onBackPressed()
	}

}
