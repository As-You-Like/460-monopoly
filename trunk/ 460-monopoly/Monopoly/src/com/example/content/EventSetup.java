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
import com.example.model.Tile;

public class EventSetup {
	
	public static final int JAIL_RELEASE = 3;
	
	public static class EventJailRelease extends TriggeredEvent {
		final int evenNumber = 0;
		
		public EventJailRelease(int time, int player){
			this.expireTurn = Game.turn + time;
			this.player = player;
		}
		
		@Override
		public boolean condition() {
			return (Game.turn == this.expireTurn) && //release turn was reached
					(Game.currentPlayer == this.player); //the current player is the player this event is associated with
		}

		@Override
		public void action() {
			Player.entities[this.player].setJailed(false);
			String[] releaseLines = new String[]{
					"You have been released from jail",
					"You have been released from jail",
					"You have been released from jail",
					"You have been released from jail",
					"You have been released from jail",
					"You have bribed your way out of jail",
					"You have bribed your way out of jail",
					"You have bribed your way out of jail",
					"You have bribed your way out of jail",
					"You have bribed your way out of jail",
					"You have seduced your way out of jail",
					"You woke up near the front entrance of the police station",
					"You have broke your way out of jail",
					"You have broke your way out of jail",
					"You crawled through a river of shit, and came out clean on the other side"
			};
			Device.player[this.player].sendMessage(Message.ALERT, releaseLines[(int)(Math.random()*releaseLines.length)]);
		}
		
	}
	
	public static void setupEvents(){
		
		//EventGenerator.registerEvent("newTurn", new EventJailRelease(time, player));
		
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
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, "You have been robbed of $" + String.format("%.2f", amount) + 
						" by the College Cartel");
			}
		});
		
		//Triggered Event - Weekly stipend
		EventGenerator.registerEvent("newTurn", new TriggeredEvent(){
			@Override
			public boolean condition() {
				return Game.getWeekDay() == 0 && Game.subturn == 0; //if the day is sunday
			}
			
			public void action() {
				for (int i=0; i<Player.entities.length; i++){
					Player p = Player.entities[i];
					if (p != null){
					if (p.isJailed() == false){
						p.addBalance(Settings.WEEKLY_STIPEND);
						Device.player[i].sendMessage(Message.ALERT, "You have received a weekly stipend of " + Settings.WEEKLY_STIPEND);
					} else {
						Device.player[i].sendMessage(Message.ALERT, "You have missed your weekly stipend due to being in jail");
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
				EventGenerator.registerEvent("newTurn", 
						new EventSetup.EventJailRelease(Settings.EVENT_ROLL_DOUBLE_JAIL_TIME, Game.currentPlayer)
				);
				
				
				
			}
		});
		
		//Triggered Event - rolling a double while in jail causes you to be smuggled out
		EventGenerator.registerEvent("diceRoll", new TriggeredEvent(){
			public boolean condition() {
				return Player.entities[Game.currentPlayer].isJailed();
			}

			
			public void action() {
				Player.entities[Game.currentPlayer].setJailed(false);
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, "You have been smuggled out of jail");
			}
		});
		
		//Random Event - Getting busted
		EventGenerator.registerEvent("residential", new RandomEvent(){
			public void action() {
				//Send the player to jail
				Player.entities[Game.currentPlayer].goToJail();
				
				//Inform the player of this unfortunate occurrence
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, "You have been thrown in jail for not wearing a spring day bracelet");
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 50;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].subBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, "You got a parking ticket and have been fined $50");
				
			}
		});
		
		EventGenerator.registerEvent("newTurn", new RandomEvent(){
			@Override
			public void action() {
				// TODO Auto-generated method stub
				Player.entities[Game.currentPlayer].goToJail();
				
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT,
						"You drank too much and got PC'd. Go to jail");
			}
		});
		
		EventGenerator.registerEvent("newTurn", new RandomEvent(){
			@Override
			public void action() {
				// TODO Auto-generated method stub
				Player.entities[Game.currentPlayer].goToJail();
				
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT,
						"You got caught cheating on an exam. Go to jail");
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 50;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].addBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"You got an A on an exam.  Gain $50");
				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 200;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].addBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Joined a Bentley organization. Gain $200");
				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 100;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].addBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Help out on a service project.  Gain $100");
				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 1000;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].addBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Achieve President's list.  Gain $1000");
				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				Player.entities[Game.currentPlayer].goToJail();
				
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT,
						"You turned yourself in. Go to jail");
				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				Player.entities[Game.currentPlayer].goToJail();
				
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT,
						"Threw nuggets at the Seasons manager. Go to jail");
				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 50;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].subBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Overdue library book.  Pay $50");
				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 100;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].subBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Spill soda on your laptop.  Pay $100");
				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 200;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].subBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Spill beer on your roommate's laptop, pay $200");
				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 100;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].subBalance(amount);
				Player.entities[Game.currentPlayer].goToJail();
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Throw TV out the window because it goes out during your" +
						" favorite team's game.  Go to jail and pay $100");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].goToJail();
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Throw roommate out window.  Go to jail.");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].goToJail();
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Throw roommate out window.  Go to jail.");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 50;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].addBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Climb up smith stairs in 2 minutes.  Gain $50");
				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 20;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].addBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Attend academic advising.  Gain $20");
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 50;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].addBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Declared your major.  Gain $50");
				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 100;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].subBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Get caught insider trading. Pay $100");
				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 20;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].subBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Long line at Einstein's, you're late for class.  Pay $20");
				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].goToJail();
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Got caught trying to steal the Bentley Falcon.  Go to jail.");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].goToJail();
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Got caught skinny dipping in the pond.  Go to jail.");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].goToJail();
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Crash party at the Pres Villa.  Go to jail.");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].goToJail();
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Throw roommate out window.  Go to jail.");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 50;
				
				Player.entities[Game.currentPlayer].subBalance(amount);
				
				Player.entities[Game.currentPlayer].goToJail();
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Attack someone with a pool cue at" +
						" Bentley Pub.  Go to jail and pay $50.");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = 50;
				
				Player.entities[Game.currentPlayer].addBalance(amount);
								
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Pledge a frat, gain $50");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].goToJail();
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Caught taking 5 pieces of fruit from seasons.  Go to jail.");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				double amount = (Player.entities[Game.currentPlayer].getBalance())*.9;
						
				//subtract the generated value from the player who's turn it currently is
				Player.entities[Game.currentPlayer].subBalance(amount);
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Bank invests your cash in money market mutual funds " +
						"AAAAAAAAAAAAAAND it's gone.  Lose 90% of cash.");
				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].getPiece().move(Tile.getClinicTile());
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Got food poisoning at Seasons, go to Health and Wellness.");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].getPiece().move(Tile.getClinicTile());
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Got bit by a goose at the pond, go to Health and Wellness.");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].getPiece().move(Tile.getClinicTile());
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Fell off the treadmill at the gym, go to Health and Wellness.");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].goToJail();
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Stayed in Smith after it closed.  Go to jail.");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].getPiece().move(Tile.getClinicTile());
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Got beaten up by Flex at Mad Falcon.  Go to Health and Wellness");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].getPiece().move(Tile.getFacilitiesTile());
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Had a crazy party in your room.  Go to facilities.");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].getPiece().move(Tile.getFacilitiesTile());
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Graffitied Seasons.  Go to facilities management.");				
			}
		});
		EventGenerator.registerEvent("newTurn", new RandomEvent() {
			@Override
			public void action() {
				
				Player.entities[Game.currentPlayer].getPiece().move(Tile.getFacilitiesTile());
				
				//Inform the player of the subtraction
				Device.player[Game.currentPlayer].sendMessage(Message.ALERT, 
						"Vandalized every dorm on campus.  Go to facilities.");				
			}
		});
		
	}
}
