package com.example.controllers;

import com.example.model.Tile;

public class BoardSetup {
	public static void setupBoard(){
		//create central circle
		Tile start = new Tile(3, 3, 0);
		start.setRegion(0);
		Tile tile1 = start.addNextStop(Tile.DIRECTION_EAST);
		tile1.setRegion(0);
		Tile tile2 = tile1.addNextStop(Tile.DIRECTION_SOUTHEAST);
		tile2.setRegion(0);
		Tile tile3 = tile2.addNextStop(Tile.DIRECTION_SOUTHWEST);
		tile3.setRegion(1);
		Tile tile4 = tile3.addNextStop(Tile.DIRECTION_WEST);
		tile4.setRegion(1);
		Tile tile5 = tile4.addNextStop(Tile.DIRECTION_NORTHWEST);
		tile5.setRegion(1);
		tile5.addNextStop(start);
		
		//create bridge
		Tile bridge1 = tile2.addNextStop(Tile.DIRECTION_EAST);
		bridge1.addNextStop(tile2);
		Tile bridge2 = bridge1.addNextStop(Tile.DIRECTION_EAST);
		bridge2.addNextStop(bridge1);
		
		//create secondary circle
		Tile tile6 = bridge2.addNextStop(Tile.DIRECTION_NORTHEAST);
		Tile tile7 = tile6.addNextStop(Tile.DIRECTION_EAST);
		Tile tile8 = tile7.addNextStop(Tile.DIRECTION_SOUTHEAST);
		Tile tile9 = tile8.addNextStop(Tile.DIRECTION_SOUTHWEST);
		Tile tile10 = tile9.addNextStop(Tile.DIRECTION_WEST);
		tile10.addNextStop(bridge2);
		
		
	}
}
