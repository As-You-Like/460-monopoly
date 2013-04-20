package com.example.content;

import android.graphics.Bitmap;

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
}
