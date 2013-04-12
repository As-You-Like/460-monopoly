package com.example.content;

public class Settings {
	//the amount of weekly stiped provided to all players every sunday
	public static final double WEEKLY_STIPEND = 200;
	
	//the lowest amount a robber will steal
	public static final double EVENT_ROBBERY_PENALTY_MIN = 10;
	
	//the highest amount a robber will steal
	public static final double EVENT_ROBBERY_PENALTY_MAX = 100;
	
	//number of times doubles must be rolled in a row for the player to go to jail
	public static final int EVENT_ROLL_DOUBLE_COUNT_FOR_JAIL = 3;
	
	//the number of turns a player stays in jail for rolling doubles 3 times in a row
	public static final int EVENT_ROLL_DOUBLE_JAIL_TIME = 4;
}
