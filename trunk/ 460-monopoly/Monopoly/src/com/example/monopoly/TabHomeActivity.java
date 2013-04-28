package com.example.monopoly;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class TabHomeActivity extends Activity { 

	public static TabHomeActivity activity;
	
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
	TextView txtShuttle;
	TextView txtBoard;
	TextView txtStop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab_home);
		
		activity = this;
		
		imageViewAvater = (ImageView) findViewById(R.id.img_avater);
		imageViewLand = (ImageView) findViewById(R.id.img_land);
		
		txtPlayer = (TextView) findViewById(R.id.txt_player);
		txtColor = (TextView) findViewById(R.id.txt_piece_color);
		txtCash = (TextView) findViewById(R.id.txt_cash);
		txtAssets = (TextView) findViewById(R.id.txt_assets);
		txtOwend = (TextView) findViewById(R.id.txt_owend);
		txtCompleted = (TextView) findViewById(R.id.txt_completed);
		
		txtTime = (TextView) findViewById(R.id.txt_time);
		txtTrade = (TextView) findViewById(R.id.txt_trade);
		txtShuttle = (TextView) findViewById(R.id.txt_shuffle);
		txtBoard = (TextView) findViewById(R.id.txt_board);
		txtStop = (TextView) findViewById(R.id.txt_stop);
		
		/** Set view' contents */
		final int avatar_resId = R.drawable.ic_launcher; // R.drawable.ic_launcher;
		final int land_resId = R.drawable.sample_house; // R.drawable.sample_hotel;
		
		setInfo(avatar_resId, land_resId, "Mike", "Red", "100000", "20000", "5", "1", "01:03:24", "5", "2", "2", "");
		
	}
	
	public void clearInfo(){
		//set first text to retrieving info and clear the rest
		txtPlayer.setText("Retrieving Info...");
		txtColor.setText("");
		txtCash.setText("");
		txtAssets.setText("");
		txtOwend.setText("");
		txtCompleted.setText("");
		txtTime.setText("");

		txtTrade.setText("");
		txtShuttle.setText("");
		txtBoard.setText("");
		txtStop.setText("");
	}

	public void setInfo(int res, int res2, String player, String color,
			String cash, String assets, String owned, String completed,
			String time, String trade, String shf, String board, String stop) {
		
		imageViewAvater.setImageResource(res);
		imageViewLand.setImageResource(res2);

		txtPlayer.setText("Player : " + player);
		txtColor.setText("Piece Color : " + color);
		txtCash.setText("Cash on Hand : " + cash);
		txtAssets.setText("Cash in Assets : " + assets);
		txtOwend.setText("Properties Owned  : " + owned);
		txtCompleted.setText("Regions Completed : " + completed);
		txtTime.setText("Time played : " + time);

		txtTrade.setText("# of Trades : " + trade);
		txtShuttle.setText("# of Shuttle Stops owned  : " + shf);
		txtBoard.setText("# of times traveled around board : " + board);
		txtStop.setText("Most Frequent stop " + stop);
	}

	
	@Override
	public void onBackPressed() { 
		this.getParent().onBackPressed(); // Call CommandCardActivity.class onBackPressed()
	}

}
