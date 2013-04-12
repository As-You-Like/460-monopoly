package com.example.content;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.example.bluetooth.Message;
import com.example.controllers.Device;
import com.example.controllers.Event;
import com.example.controllers.EventGenerator;
import com.example.controllers.Game;
import com.example.controllers.Player;
import com.example.controllers.RandomEvent;
import com.example.controllers.TriggeredEvent;
import com.example.model.Die;

public class EventSetup {
	
	public static final int JAIL_RELEASE = 0;
	
	public static class EventJailRelease extends TriggeredEvent {
		
		public EventJailRelease(int time, int player){
			this.expireTurn = Game.turn + time;
			this.eventNumber = JAIL_RELEASE;
		}
		
		@Override
		public boolean condition() {
			return (Game.turn == this.expireTurn) && //release turn was reached
					(Game.currentPlayer == this.player); //the current player is the player this event is associated with
		}

		@Override
		public void action() {
			Player.entities[this.player].setJailed(false);
			Device.player[this.player].sendMessage(Message.ALERT, "You have been released from jail");
		}
		
	}
	
	public static void setupEvents(){
		
		//Random Event - Being robbed
		EventGenerator.registerEvent("residential", new RandomEvent(){
			public void action() {
				//randomly generate value
				double amount = Math.random();
				amount *= (Settings.EVENT_ROBBERY_PENALTY_MAX - Settings.EVENT_ROBBERY_PENALTY_MIN);
				amount += (Settings.EVENT_ROBBERY_PENALTY_MIN);
				
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].subBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, "You have been robbed of $" + String.format("%.2f", amount) + "");
			}
		});
		
		//Triggered Event - Weekly stipend
		EventGenerator.registerEvent("newTurn", new TriggeredEvent(){
			@Override
			public boolean condition() {
				return Game.getWeekDay() == 0; //if the day is sunday
			}
			
			public void action() {
				for (int i=0; i<Player.entities.length; i++){
					Player p = Player.entities[i];
					if (p != null){
					if (p.isJailed() == false){
						p.addBalance(Settings.WEEKLY_STIPEND);
					}
					}
				}
			}
		});
		
		//Triggered Event - rolling doubles 3 times sends you to jail
		EventGenerator.registerEvent("diceRoll", new TriggeredEvent(){
			public boolean condition() {
				return Die.doubleCount >= Settings.EVENT_ROLL_DOUBLE_COUNT_FOR_JAIL;
			}

			
			public void action() {
				Player.entities[Game.currentPlayer].goToJail();
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, "You have been caught speeding and promptly sent to jail");
				
				//create the releasing event
				EventGenerator.registerEvent("turnStart", 
						new EventSetup.EventJailRelease(Settings.EVENT_ROLL_DOUBLE_JAIL_TIME, Game.currentPlayer)
				);
				
				
				
			}
		});
	}
}
