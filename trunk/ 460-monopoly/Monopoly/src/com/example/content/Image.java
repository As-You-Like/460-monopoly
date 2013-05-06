package com.example.content;

import java.util.HashMap;
import java.util.Map;

import com.example.monopoly.LoadingActivity;
import com.example.monopoly.R;
import com.example.monopoly.SplashActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Image {
	
	/**
	 * Bitmap for a hexagon object
	 * @deprecated This value has been deprecated, use HEXAGON_BOTTOM instead
	 */
	public static Bitmap HEXAGON_TEXTURE;
	
	/**
	 * Bitmap for the bottom-most layer of the hexagon tile
	 */
	public static Bitmap HEXAGON_BOTTOM;
	
	/**
	 * Bitmap for the region alpha layer of the hexagon tile
	 */
	public static Bitmap HEXAGON_REGION;
	
	/**
	 * Bitmap for the player alpha layer of the hexagon tile
	 */
	public static Bitmap HEXAGON_PLAYER;
	
	/**
	 * Bitmap array for the 6 faces of a die
	 */
	public static Bitmap[] DIE = new Bitmap[6];
	
	public static void setupImages() {
		//b=BitmapFactory.decodeResource(getResources(), R.drawable.bruintest);
		Image.HEXAGON_TEXTURE = BitmapFactory.decodeResource(SplashActivity.activity.getResources(), R.drawable.hexagon_blue);
		Image.HEXAGON_BOTTOM = BitmapFactory.decodeResource(SplashActivity.activity.getResources(), R.drawable.hexagon_layer_bot);
		Image.HEXAGON_REGION = BitmapFactory.decodeResource(SplashActivity.activity.getResources(), R.drawable.hexagon_layer_rgn);
		Image.HEXAGON_PLAYER = BitmapFactory.decodeResource(SplashActivity.activity.getResources(), R.drawable.hexagon_layer_plr);
		
		Image.DIE[0] = BitmapFactory.decodeResource(SplashActivity.activity.getResources(), R.drawable.die_1);
		Image.DIE[1] = BitmapFactory.decodeResource(SplashActivity.activity.getResources(), R.drawable.die_2);
		Image.DIE[2] = BitmapFactory.decodeResource(SplashActivity.activity.getResources(), R.drawable.die_3);
		Image.DIE[3] = BitmapFactory.decodeResource(SplashActivity.activity.getResources(), R.drawable.die_4);
		Image.DIE[4] = BitmapFactory.decodeResource(SplashActivity.activity.getResources(), R.drawable.die_5);
		Image.DIE[5] = BitmapFactory.decodeResource(SplashActivity.activity.getResources(), R.drawable.die_6);
	}
}
