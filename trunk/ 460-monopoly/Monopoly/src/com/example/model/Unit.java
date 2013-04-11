package com.example.model;

import java.util.ArrayList;

import com.example.controllers.Player;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

public abstract class Unit {
	
	//constants
	public static final double DEFAULT_MOVESPEED = 5;
	public static final double DEFAULT_RADIUS = 10;
	public static final int DEFAULT_OWNER = 5;
	public static final double DEFAULT_FACING = 0;
	
	
	//unit list
	public static ArrayList<Unit> entity = new ArrayList<Unit>();
	
	//instance variables
	private Point position;
	private double facing;
	protected Point drawAnchor;
	private int owner;
	private LightingColorFilter colorFilter;
	protected double radius;
	private Bitmap sprite;
	
	//Constructors
	public Unit(){
		this(new Point(), Unit.DEFAULT_OWNER);
	}
	
	public Unit(Point position, int owner){
		this(position, owner, Unit.DEFAULT_RADIUS);
	}
	
	public Unit(Point position, int owner, double radius){
		this.position = position;
		this.owner = owner;
		this.radius = radius;
		this.facing = Unit.DEFAULT_FACING;
		Unit.entity.add(this);
	}
	
	//Destructor
	public void destroy(){
		Unit.entity.remove(this);
	}
	
	//Mutators
	public void setPosition(double x, double y){
		this.setPosition(new Point(x, y));
	}
	
	protected void updateDrawAnchor(){
		//set draw anchor
		this.drawAnchor = new Point(position.x, position.y);
		this.drawAnchor.x -= radius;
		this.drawAnchor.y -= radius;
	}
	
	public void setPosition(Point p){
		//set position
		this.position = p;
		
		//set draw anchor
		this.updateDrawAnchor();
	}
	
	public void setFacing(double f){
		while (f < 0){
			f += 360;
		}
		while (f >= 360){
			f -= 360;
		}
		this.facing = f;
	}
	
	public void setRadius(double r){
		if (r > 0){
			this.radius = r;
		} else {
			this.radius = Unit.DEFAULT_RADIUS;
		}
		
		//set draw anchor
		this.updateDrawAnchor();
	}
	
	public void setOwner(int o){
		this.owner = 0;
		this.colorFilter = new LightingColorFilter(Player.entities[owner].getColor(), 1);
	}
	
	
	
	//Accessors
	public Point getPosition(){
		return this.position;
	}
	
	public Point getDrawAnchor(){
		return this.drawAnchor;
	}
	
	public int getOwner(){
		return this.owner;
	}
	
	public double getRadius(){
		return this.radius;
	}
	
	public double getFacing(){
		return this.facing;
	}
	
	//draw method
	public abstract void draw(Canvas c, Paint p);
	
	//timer update method
	public abstract void onTimer();

	public Bitmap getSprite() {
		return sprite;
	}

	public void setSprite(Bitmap sprite) {
		this.sprite = sprite;
	}

	public LightingColorFilter getOwnerColorFilter() {
		return colorFilter;
	}
	
}
