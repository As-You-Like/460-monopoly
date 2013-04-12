package com.example.monopoly;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class TabStatActivity extends Activity {
	public static TabStatActivity activity = null;

	public static final int PLAYER_NAME = 0;
	public static final int PLAYER_COLOR = 1;
	public static final int PLAYER_CASH = 2;
	public static final int PLAYER_ASSETS = 3;
	public static final int PLAYER_OWEND = 4;
	public static final int PLAYER_REGIONS_COMPLETED = 5;
	
	ImageView imageViewAvater;
	ImageView imageViewLand;
	TextView txtPlayer;
	TextView txtColor;
	TextView txtCash;
	TextView txtAssets;
	TextView txtOwend;
	TextView txtCompleted;
	TextView txtTime;
	TextView txtTrade;
	TextView txtShuffle;
	TextView txtBoard;
	TextView txtStop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab_stat);
		activity = this;

		imageViewAvater = (ImageView) findViewById(R.id.img_avater);
		imageViewAvater = (ImageView) findViewById(R.id.img_land);
		
		txtPlayer = (TextView) findViewById(R.id.txt_player);
		txtColor = (TextView) findViewById(R.id.txt_piece_color);
		txtCash = (TextView) findViewById(R.id.txt_cash);
		txtAssets = (TextView) findViewById(R.id.txt_assets);
		txtOwend = (TextView) findViewById(R.id.txt_owend);
		txtCompleted = (TextView) findViewById(R.id.txt_completed);
		
		txtTime = (TextView) findViewById(R.id.txt_time);
		txtTrade = (TextView) findViewById(R.id.txt_trade);
		txtShuffle = (TextView) findViewById(R.id.txt_shuffle);
		txtBoard = (TextView) findViewById(R.id.txt_board);
		txtStop = (TextView) findViewById(R.id.txt_stop);
	
		setStat(PLAYER_NAME, "CWS");
		setStat(PLAYER_COLOR, "Red");
		setStat(PLAYER_CASH, "10,000,000");
	}
	
	public void setStat(int idx, String value){
		switch(idx){
		case PLAYER_NAME:
			txtPlayer.setText("Player : " +  value);
			break;
		case PLAYER_COLOR:
			txtColor.setText("Piece Coler : " +  value);
			break;
		case PLAYER_CASH:
			txtCash.setText("Cash on Hand :  $" +  value);
			break;
		case PLAYER_ASSETS:
			txtAssets.setText("Cash in Assets : $" +  value);
			break;
		case PLAYER_OWEND:
			txtOwend.setText("Properties Owned : " +  value);
			break;
		case PLAYER_REGIONS_COMPLETED:
			txtCash.setText("Regions Completed : " +  value);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		this.getParent().onBackPressed(); // Call CommandCardActivity.class onBackPressed()
	}

}
