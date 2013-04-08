package com.example.controllers;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.model.*;

public class Game extends TimerTask{
	public static Game instance;
	public static String name;
	public static Timer timer;
	public static long ms = 0;
	
	//constructor to be run during the game creation process in setup module
	public Game(String name){
		Game.name = name;
		Game.instance = this;
	}
	
	//method to be run during loading of game module
	public static void start(){
		Game.timer = new Timer();
		Game.timer.scheduleAtFixedRate(Game.instance, 20, 20);
	}

	@Override
	public void run() {
		ms += 20;
		//render all units on the map
		/*Unit[] tmp;
		synchronized (Unit.entity) {
			tmp = Unit.entity.toArray(new Unit[Unit.entity.size()]);
		}
		for (Unit u: tmp){
			u.draw();
		}*/
		
		
		
		
	}
	
	
}
