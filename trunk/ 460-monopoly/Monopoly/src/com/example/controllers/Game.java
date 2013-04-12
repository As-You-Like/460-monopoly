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
	
	/**
	 * has the count of the current turn
	 */
	public static int turn = 0;
	
	/**
	 * Keeps track of which player's turn it is
	 */
	public static int currentPlayer = 0;
	
	/**
	 * Has the count of the current subturn
	 */
	public static int subturn = 0;
	
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
	
	public static int getWeekDay(){
		return Game.turn % 7;
	}
	
	/**
	 * Method that returns a list of events that have a turn expiration counter (for database purposes)
	 * @return
	 */
	public static Event[] getTurnSensitiveEvents(){
		return null;
	}
	
	
}
