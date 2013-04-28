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
	
	public static final int UPGRADE_ELECTRICAL = 0;
	public static final int UPGRADE_PLUMBING = 1;
	public static final int UPGRADE_VENDING = 2;
	public static final int UPGRADE_HVAC = 3;
	
	public static int[] REGION_COLORS = {
		Color.RED, //0
		Color.BLUE, //1
		Color.CYAN,
		Color.MAGENTA,
		Color.GREEN,
		Color.YELLOW,
		Color.rgb(255, 63, 0)
	};
	
	public static String[] REGION_NAMES = {
		"THE TREES",      //0
		"Region Two", //1
		"Region Three",
		"Region Four",
		"Region Five",
		"Region Six",
		"Region Seven"
	};
	
	public static final int DIRECTION_NORTHEAST = 0;
	public static final int DIRECTION_EAST      = 1;
	public static final int DIRECTION_SOUTHEAST = 2;
	public static final int DIRECTION_SOUTHWEST = 3;
	public static final int DIRECTION_WEST      = 4;
	public static final int DIRECTION_NORTHWEST = 5;
	
	public int id;
	public static int totalTileCount = 0;
	private static Tile jailTile = null;
	
	private double width;
	private double side;
	private double h;
	private double height;
	private double r2;
	private int hexX;
	private int hexY;
	
	private String name;
	private LightingColorFilter regionColorFilter;
	private int region;
	private double price = 0;
	private double baseRent = 0;
	
	
	public boolean[] upgraded = new boolean[4];
	private double[] upgradePrices = new double[4];
	private ArrayList<MobileUnit> visitors = new ArrayList<MobileUnit>();
	private ArrayList<Tile> nextStops = new ArrayList<Tile>();
	private double baseRegionRent;
	
	public Tile(int hexX, int hexY, int owner){
		super(Tile.getCartesianPositionFromHexPoint(hexX, hexY), owner, Tile.TILE_RADIUS);
		this.setRadius(Tile.TILE_RADIUS);
		Tile.entity[hexX][hexY] = this;
		this.updateDrawAnchor();
		this.name = "(" + hexX + ", " + hexY + ")";
		
		//get id and increment total
		this.id = totalTileCount;
		totalTileCount++;
		
		this.hexX = hexX;
		this.hexY = hexY;
		
		this.setSprite(Image.HEXAGON_BOTTOM);
		
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
	
	public int getHexX(){
		return hexX;
	}
	
	public int getHexY(){
		return hexY;
	}
	
	public Tile[] getForkTiles(){
		return this.nextStops.toArray(new Tile[]{});
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
	
	public void setBaseRent(double rent){
		this.baseRent = rent;
	}
	
	public double getRent(){
		double result = 0;
		for (int i=0; i<this.upgraded.length; i++){
			if (this.upgraded[i]){
				result += this.upgradePrices[i];
			}
		}
		
		//return added up upgrades, or baserent if 0 was returned
		return result == 0 ? this.baseRent : result;
	}
	
	public void setPrice(double price){
		this.price = price;
	}
	
	public double getPrice(){
		return this.price;
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
			unit.move(this.getPosition().getPolarOffset(Tile.DEFAULT_POLAROFFSET, angle*i));
		}
	}
	
	public void upgrade(int upgradeID){
		this.upgraded[upgradeID] = true;
		//this.price += this.upgradePrices[upgradeID];
	}
	
	public void priceUpgrades(double elec, double plum, double vend, double hvac){
		this.upgradePrices[UPGRADE_ELECTRICAL] = elec;
		this.upgradePrices[UPGRADE_PLUMBING] = plum;
		this.upgradePrices[UPGRADE_VENDING] = vend;
		this.upgradePrices[UPGRADE_HVAC] = hvac;
	}
	
	public double getUpgradePrice(int upgradeID){
		return this.upgradePrices[upgradeID];
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
		int left = (int)anchor.x;
		int top = (int)anchor.y;
		int right = (int)(anchor.x + this.width);
		int bottom = (int)(anchor.y + this.height);
		
		//draw texture
		c.drawBitmap(this.getSprite(), null, new Rect(left, top, right, bottom), p);
		
		//draw the region color
		p.setColorFilter(this.getRegionColorFilter()); //colors the paint object to the color of the region of this tile
		c.drawBitmap(Image.HEXAGON_REGION, null, new Rect(left, top, right, bottom), p);
		//c.drawCircle((int)this.getPosition().x, (int)this.getPosition().y, (int)(this.radius/3), p);
		
		//draw the player color
		p.setColorFilter(this.getOwnerColorFilter()); //colors the paint object to the color of the owner of this tile
		c.drawBitmap(Image.HEXAGON_PLAYER, null, new Rect(left, top, right, bottom), p);
		
		
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

	public String getName() {
		return name;
	}
	
	public String toString(){
		return ("(" + this.hexX + ", " + this.hexY + ")") + this.name;
		
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static int getTileCountInRegion(int region){
		int count = 0;
		for (int i=0; i<Unit.entity.size(); i++){
			Unit u = Unit.entity.get(i);
			if (u instanceof Tile){
				Tile t = (Tile)u;
				if (t.getRegion() == region){
					count++;
				}
			}
		}
		return count;
	}
	
	public static int getTileCountOwnedByPlayerInRegion(int region, int player){
		int count = 0;
		for (int i=0; i<Unit.entity.size(); i++){
			Unit u = Unit.entity.get(i);
			if (u instanceof Tile){
				Tile t = (Tile)u;
				if (t.getRegion() == region && t.getOwner() == player){
					count++;
				}
			}
		}
		return count;
	}
	
	public static void setJailTile(Tile tile){
		Tile.jailTile  = tile;
	}
	
	public static Tile getJailTile(){
		return Tile.jailTile;
	}

	public void setCompletedRegionRent(double rent) {
		// TODO Auto-generated method stub
		this.setBaseRegionRent(rent);
	}

	public double getBaseRegionRent() {
		return baseRegionRent;
	}

	public void setBaseRegionRent(double baseRegionRent) {
		this.baseRegionRent = baseRegionRent;
	}
}
