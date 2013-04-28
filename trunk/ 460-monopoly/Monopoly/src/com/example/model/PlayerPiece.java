package com.example.model;

import com.example.controllers.Player;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class PlayerPiece extends MobileUnit {
	Tile currentTile = null;
	Tile previousTile = null;
	
	public PlayerPiece (Tile startPosition, int owner){
		//initialize variables
		super(startPosition.getPosition(), owner, 10);
		currentTile = startPosition;
		
		//tell the hexagon it has a new unit
		currentTile.addVisitor(this);
	}
	
	/**
	 * Gives the current position of the player as a tile object
	 * @return Tile
	 */
	public Tile getCurrentTile(){
		return currentTile;
	}
	
	public Tile getPreviousTile(){
		return previousTile;
	}
	
	/**
	 * Moves the player piece to a new tile
	 * @param Tile tile [this is the target tile you want the piece to move to]
	 */
	public void move(Tile tile){
		//tell the old tile that it lost a unit
		if (currentTile != null){
			currentTile.removeVisitor(this);
			previousTile = currentTile;
		}
		//tell the new tile that it has gained a unit
		tile.addVisitor(this);
		Log.e("moving", "Tile: (" + tile.getHexX() + ", " + tile.getHexY() + ")");
		
		//set the new tile as the current one
		currentTile = tile;
	}
	
	public void draw(Canvas c, Paint p){
		p.setColor(Player.entities[this.getOwner()].getColor());
		c.drawCircle((float)this.getPosition().x, (float)this.getPosition().y, (float)this.radius, p); 
		p.setColor(Color.BLACK);
	}
}