package com.example.content;

import com.example.model.Tile;

public class BoardSetup {
	public static void setupBoard(){
		//create central circle
		Tile start = new Tile(10, 10, Tile.OWNER_NEUTRAL);
		start.setName("Pond");
		start.setOwner(Tile.OWNER_UNOWNABLE);
		
		Tile spruce = start.addNextStop(Tile.DIRECTION_EAST);
		spruce.setName("Spruce");
		spruce.setPrice(50); //initial cost
		spruce.setCompletedRegionRent(10); //Rent w/ entire region
		spruce.setBaseRent(5); //Rent w/o upgrade
		spruce.setRegion(0); //region
		spruce.priceUpgrades(37.5, 75, 112.5, 150); //upgrades
		
		Tile oakhall = spruce.addNextStop(Tile.DIRECTION_SOUTHWEST);
		oakhall.setName("Oak Hall");
		oakhall.setPrice(50);
		oakhall.setCompletedRegionRent(10);
		oakhall.setBaseRent(0);
		oakhall.setRegion(0);
		oakhall.priceUpgrades(20, 30, 40, 50);
		
		oakhall.addNextStop(start);
		
		
		
		Tile.setJailTile(start);
	}
}
