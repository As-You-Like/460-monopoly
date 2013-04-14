package com.example.model;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.content.Image;
import com.example.controllers.EventGenerator;
import com.example.controllers.Game;
import com.example.controllers.GameThread;
import com.example.controllers.TriggeredEvent;

public class Die extends ScreenUnit { 
	public static int diceCount = 2;
	private static Die[] dice;
	private static int currentPlayer = -1;
	public static int doubleCount = 0;
	
	private static boolean timerReset;
	
	public static int executionCount = 10;
	private static int executionCountTimesDice;
	
	private Timer timer;
	public int value;
	
	private Die(int count){
		timer = new Timer();
		this.setPosition(new Point((this.radius + 5) + 2*this.radius * count, this.radius + 5));
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				//cycle through random values
				for (int i=0; i<diceCount; i++){
					dice[i].value = (int)(Math.random() * 6);
				}
				
				Die.executionCountTimesDice--;
				Die.stopDiceTimer();
			}
		}, 0, 100);
	}
	
	@SuppressWarnings("deprecation")
	private static void stopDiceTimer(){
		if (timerReset == false) return;
		
		timerReset = true;
		for (int i=0; i<diceCount; i++){
			dice[i].timer.cancel();
		}
		
		//if there is a double, say so, and trigger an event
		if (dice[0].value == dice[1].value){
			doubleCount++;
			EventGenerator.executeTriggeredEvents("diceRoll");
		}
		
		//GAME THREAD CODE GOES HERE FOR RESUMING GAME AFTER DICE ROLL
		GameThread.gt.resume();
	}
	
	/**
	 * Method causes a dice roll to occur
	 * Notice: The dice roll occurs asynchronously! Wait for stopDiceTimer to execute!
	 */
	public static void roll(){
		
		//reset double count if the player has changed
		if (currentPlayer != Game.currentPlayer) {
			currentPlayer = Game.currentPlayer;
			doubleCount = 0;
		}
		
		//roll the dice
		dice = new Die[diceCount];
		timerReset = false;
		executionCountTimesDice = executionCount * diceCount;
		for (int i=0; i<diceCount; i++){
			if (dice[i] != null){
				dice[i].destroy();
			}
			dice[i] = new Die(i);
		}
	}
	
	/**
	 * Gives you the added up value of the dice rolls
	 * @return
	 */
	public static int getTotalValue(){
		int value = 0;
		for (int i = 0; i<diceCount; i++){
			value += dice[i].value;
		}
		return value;
	}
	
	/**
	 * Tells you if the roll is a double, works only if the dice count is 2 (this is the default)
	 * @return
	 */
	public static boolean isDouble(){
		if (diceCount != 2) return false;
		return dice[0].value == dice[1].value;
	}
	
	public void draw(Canvas c, Paint p){
		Point atp = this.getDrawAnchor();
		Point abr = new Point((atp.x + this.radius * 2), (atp.y + this.radius * 2));
		c.drawBitmap(Image.DIE[this.value-1], null, new Rect((int)atp.x, (int)atp.y, (int)abr.x, (int)abr.y), p);
	}
	
}
