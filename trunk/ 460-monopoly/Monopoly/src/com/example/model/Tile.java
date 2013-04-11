package com.example.model;

import java.util.ArrayList;

import com.example.content.Image;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Tile extends StaticUnit {
	
	public static final double DEFAULT_POLAROFFSET = 5;
	public static final double TILE_RADIUS = 20;
	public static Tile[][] entity = new Tile[400][400];
	
	public static int[] REGION_COLORS = {
		Color.RED,
		Color.BLUE,
		Color.CYAN,
		Color.MAGENTA
	};
	
	public static final int DIRECTION_NORTHEAST = 0;
	public static final int DIRECTION_EAST      = 1;
	public static final int DIRECTION_SOUTHEAST = 2;
	public static final int DIRECTION_SOUTHWEST = 3;
	public static final int DIRECTION_WEST      = 4;
	public static final int DIRECTION_NORTHWEST = 5;
	
	private double width;
	private double side;
	private double h;
	private double height;
	private double r2;
	private int hexX;
	private int hexY;
	
	private LightingColorFilter regionColorFilter;
	private int region;
	
	public boolean[] upgradeActive;
	private ArrayList<MobileUnit> visitors = new ArrayList<MobileUnit>();
	private ArrayList<Tile> nextStops = new ArrayList<Tile>();
	
	public Tile(int hexX, int hexY, int owner){
		super(Tile.getCartesianPositionFromHexPoint(hexX, hexY), owner, Tile.TILE_RADIUS);
		this.setRadius(Tile.TILE_RADIUS);
		Tile.entity[hexX][hexY] = this;
		this.updateDrawAnchor();
		
		this.hexX = hexX;
		this.hexY = hexY;
		
		this.setSprite(Image.HEXAGON_TEXTURE);
		
		Log.e("newTile", "(" + hexX + ", " + hexY + ")");
	}
	
	protected void updateDrawAnchor(){
		Point pos = this.getPosition();
		Point p = new Point(pos.x, pos.y);
		
		//offset the x and y by half the width and height respectively
		p.x -= this.width/2;
		p.y -= this.height/2;
		
		this.drawAnchor = p;
	}
	
	public void addNextStop(Tile t){
		this.nextStops.add(t);
	}
	
	/**
	 * Attaches another tile as a valid destination from the current tile in the progression
	 * @param direction
	 */
	public Tile addNextStop(int direction){
		int hexXOffset = 0;
		int hexYOffset = 0;
		
		switch (direction){
		case Tile.DIRECTION_NORTHEAST:
			hexYOffset = -1;
			hexXOffset = 1;
			break;
		
		case Tile.DIRECTION_EAST:
			hexYOffset = 0;
			hexXOffset = 1;
			break;
		
		case Tile.DIRECTION_SOUTHEAST:
			hexYOffset = 1;
			hexXOffset = 0;
			break;
			
		case Tile.DIRECTION_SOUTHWEST:
			hexYOffset = 1;
			hexXOffset = -1;
			break;
			
		case Tile.DIRECTION_WEST:
			hexYOffset = 0;
			hexXOffset = -1;
			break;
			
		case Tile.DIRECTION_NORTHWEST:
			hexYOffset = -1;
			hexXOffset = 0;
			break;
		}
		
		int hexX = this.hexX + hexXOffset;
		int hexY = this.hexY + hexYOffset;
		Tile t = null;
		if (Tile.entity.length > hexX){
			if (Tile.entity[hexX].length > hexY){
				t = Tile.entity[hexX][hexY];
				if (t == null) {
					t = Tile.entity[hexX][hexY] = new Tile(hexX, hexY, Unit.DEFAULT_OWNER);
				}
				this.addNextStop(t);
			}
		} 
		return t;
	}
	
	/**
	 * Returns true if the tile has multiple options to travel to
	 * @return
	 */
	public boolean hasFork(){
		return this.nextStops.size() > 1;
	}
	
	/**
	 * Returns the next tile in the progression.
	 * Returns null if there is a fork or the tile has no registered progression
	 * @return
	 */
	public Tile getNextStop(){
		if (!this.hasFork() && this.nextStops.size() > 0){
			return this.nextStops.get(0);
		}
		return null;
	}
	
	public static Point getCartesianPositionFromHexPoint(double hexX, double hexY){
		Point p = new Point();
		// === For X ===
		// Do the x offset that results from y going up
		p.x = 2 * Tile.TILE_RADIUS * Math.sin(Math.PI/6D);
		p.x *= hexY;
		
		// Do the x offset that is part of the X position
		p.x += Tile.TILE_RADIUS * 2 * hexX;
		
		// === For Y ===
		// Do the y offset from y going up
		p.y = 2 * Tile.TILE_RADIUS * Math.cos(Math.PI/6D);
		p.y *= hexY;
		
		return p;
	}
	
	public static Tile getTileFromCartesianPoint(int x, int y){
		double noRoundX = (x-y*Math.tan(Math.PI/6D))/(2D*Tile.TILE_RADIUS);
		double noRoundY = y/(2D*Tile.TILE_RADIUS*Math.cos(Math.PI/6D));
		
		int hexX = (int)Math.round(noRoundX);
		int hexY = (int)Math.round(noRoundY);
		
		if (Tile.entity.length > hexX){
			if (Tile.entity[hexX].length > hexY){
				return Tile.entity[hexX][hexY];
			}
		} 
		return null;
	}
	
	public void setRadius(double radius){
		this.radius = radius;
		
		this.width = this.radius * 2;
		this.side = (this.radius * Math.tan(Math.PI/6D))*2;
		this.r2 = (this.side/2) / Math.sin(Math.PI/6D);
		this.h = this.radius * Math.tan(Math.PI/6D);
		this.height = this.side + 2 * this.h;
	}
	
	private void updateVisitorPositions(){
		double angle = 360D / this.visitors.size();
		for(int i=0; i<this.visitors.size(); i++){
			MobileUnit unit = this.visitors.get(i);
			unit.move(unit.getPosition().getPolarOffset(Tile.DEFAULT_POLAROFFSET, angle*i));
		}
	}
	
	public void addVisitor(MobileUnit unit){
		visitors.add(unit);
		
		this.updateVisitorPositions();
	}
	
	public void removeVisitor(MobileUnit unit){
		visitors.remove(unit);
		
		this.updateVisitorPositions();
	}
	
	public void draw(Canvas c, Paint p){
		//draw the actual tile
		//super.draw(c, p);
		p.setColor(Color.WHITE);
		
		//get draw position
		Point anchor = this.getDrawAnchor();
		
		//draw texture
		c.drawBitmap(this.getSprite(), null, new Rect((int)anchor.x, (int)anchor.y, (int)(anchor.x + this.width), (int)(anchor.y + this.height)), p);
		
		//draw the region color
		p.setColorFilter(this.getRegionColorFilter()); //colors the paint object to the color of the region of this tile
		c.drawCircle((int)this.getPosition().x, (int)this.getPosition().y, (int)(this.radius/3), p);
		
		//draw the player color
		p.setColorFilter(this.getOwnerColorFilter()); //colors the paint object to the color of the owner of this tile
		
		
		p.setColorFilter(null); //clears the color filter
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
		this.regionColorFilter = new LightingColorFilter(Tile.REGION_COLORS[this.getRegion()], 1);
	}

	public LightingColorFilter getRegionColorFilter() {
		return regionColorFilter == null ? new LightingColorFilter(Color.WHITE, 1) : regionColorFilter;
	}
}
