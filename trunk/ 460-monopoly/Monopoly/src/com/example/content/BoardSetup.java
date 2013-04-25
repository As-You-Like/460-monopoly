package com.example.content;

import com.example.model.Tile;

public class BoardSetup {
	public static void setupBoard(){
		//create central circle
		Tile start = new Tile(3, 3, Tile.OWNER_NEUTRAL);
		start.setName("Tile One");
		start.setPrice(10);
		start.setRegion(0);
		
		Tile tile1 = start.addNextStop(Tile.DIRECTION_EAST);
		tile1.setName("Tile Two");
		tile1.setPrice(20);
		tile1.setRegion(0);
		
		Tile tile2 = tile1.addNextStop(Tile.DIRECTION_SOUTHEAST);
		tile2.setName("Tile Three");
		tile2.setPrice(30);
		tile2.setRegion(0);
		
		Tile tile3 = tile2.addNextStop(Tile.DIRECTION_SOUTHWEST);
		tile3.setName("Tile Four");
		tile3.setPrice(40);
		tile3.setRegion(1);
		
		Tile tile4 = tile3.addNextStop(Tile.DIRECTION_WEST);
		tile4.setName("Tile Five");
		tile4.setPrice(50);
		tile4.setRegion(1);
		
		Tile tile5 = tile4.addNextStop(Tile.DIRECTION_NORTHWEST);
		tile5.setName("Tile Six");
		tile5.setPrice(60);
		tile5.setRegion(1);
		
		tile5.addNextStop(start);
		
		//create bridge
		Tile bridge1 = tile2.addNextStop(Tile.DIRECTION_EAST);
		bridge1.setName("Tile Seven");
		bridge1.setPrice(70);
		bridge1.setRegion(2);
		bridge1.addNextStop(tile2);
		
		Tile bridge2 = bridge1.addNextStop(Tile.DIRECTION_EAST);
		bridge2.setName("Tile Eight");
		bridge2.setPrice(80);
		bridge2.setRegion(3);
		bridge2.addNextStop(bridge1);
		
		//create secondary circle
		Tile tile6 = bridge2.addNextStop(Tile.DIRECTION_NORTHEAST);
		tile6.setName("Tile Nine");
		tile6.setPrice(90);
		tile6.setRegion(3);
		
		Tile tile7 = tile6.addNextStop(Tile.DIRECTION_EAST);
		tile7.setName("Tile Ten");
		tile7.setPrice(100);
		tile7.setRegion(3);
		
		Tile tile8 = tile7.addNextStop(Tile.DIRECTION_SOUTHEAST);
		tile8.setName("Tile Eleven");
		tile8.setPrice(110);
		tile8.setRegion(4);
		
		Tile tile9 = tile8.addNextStop(Tile.DIRECTION_SOUTHWEST);
		tile9.setName("Tile Twelve");
		tile9.setPrice(120);
		tile9.setRegion(4);
		
		Tile tile10 = tile9.addNextStop(Tile.DIRECTION_WEST);
		tile10.setName("Tile Thirteen");
		tile10.setPrice(130);
		tile10.setRegion(4);
		
		tile10.addNextStop(bridge2);
		
		Tile tile11 = tile10.addNextStop(Tile.DIRECTION_SOUTHWEST);
		tile11.setName("Tile Fourteen");
		tile11.setPrice(140);
		tile11.setRegion(2);
		
		Tile tile12 = tile11.addNextStop(Tile.DIRECTION_SOUTHEAST);
		tile12.setName("Tile Fifteen");
		tile12.setPrice(150);
		tile12.setRegion(2);
		
		Tile tile13 = tile12.addNextStop(Tile.DIRECTION_EAST);
		tile13.setName("Tile Sixteen");
		tile13.setPrice(160);
		tile13.setRegion(5);
		
		Tile tile14 = tile13.addNextStop(Tile.DIRECTION_NORTHEAST);
		tile14.setName("Tile Seventeen");
		tile14.setPrice(170);
		tile14.setRegion(5);
		tile14.addNextStop(tile9);
		
		Tile tile15 = tile14.addNextStop(Tile.DIRECTION_EAST);
		tile15.setName("Tile Eighteen");
		tile15.setPrice(180);
		tile15.setRegion(5);
		
		Tile tile16 = tile15.addNextStop(Tile.DIRECTION_NORTHEAST);
		tile16.setName("Tile Nineteen");
		tile16.setPrice(190);
		tile16.setRegion(6);
		
		Tile tile17 = tile16.addNextStop(Tile.DIRECTION_NORTHWEST);
		tile17.setName("Tile Twenty");
		tile17.setPrice(200);
		tile17.setRegion(6);
		tile17.addNextStop(tile8);
		
	}
}
