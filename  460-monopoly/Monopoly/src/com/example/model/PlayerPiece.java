package com.example.model;

public class PlayerPiece extends MobileUnit {
	Tile currentTile = null;
	
	public PlayerPiece (Tile startPosition){
		//initialize variables
		currentTile = startPosition;
		
		//spawn the payer piece at the center of the starting hexagon
		this.setPosition(startPosition.getPosition());
		
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
	
	/**
	 * Moves the player piece to a new tile
	 * @param Tile tile [this is the target tile you want the piece to move to]
	 */
	public void move(Tile tile){
		//tell the old tile that it lost a unit
		if (currentTile != null){
			currentTile.removeVisitor(this);
		}
		//tell the new tile that it has gained a unit
		tile.addVisitor(this);
		
		//set the new tile as the current one
		currentTile = tile;
	}
}
