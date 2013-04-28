package com.example.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import android.util.Log;

public class EventGenerator {
	private static Map<String, ArrayList<RandomEvent>> rEvents = new HashMap<String, ArrayList<RandomEvent>>();
	private static Map<String, ArrayList<TriggeredEvent>> tEvents = new HashMap<String, ArrayList<TriggeredEvent>>();
	
	public static final double CHANCE_EVENTHAPPENS = 0.5;
	
	/**
	 * Establishes a random category
	 * @param category
	 */
	private static void registerRandomCategory(String category){
		if (rEvents.get(category) == null){
			rEvents.put(category, new ArrayList<RandomEvent>());
		}
	}
	
	/**
	 * Establishes a triggered category
	 * @param category
	 */
	private static void registerTriggeredCategory(String category){
		if (tEvents.get(category) == null){
			tEvents.put(category, new ArrayList<TriggeredEvent>());
		}
	}
	
	/**
	 * Clears a random category of all random events
	 * @param category
	 */
	public static void clearRandomCategory(String category){
		if (rEvents.get(category) != null){
			rEvents.remove(category);
		}
	}
	
	/**
	 * Clears a triggered Category of all triggered events
	 * @param category
	 */
	public static void clearTriggeredCategory(String category){
		if (tEvents.get(category) != null){
			tEvents.remove(category);
		}
	}
	
	/**
	 * Adds an event to the list of executable events
	 * @param category
	 * @param e
	 */
	public static void registerEvent(String category, Event e){
		if (e instanceof RandomEvent){
			registerRandomCategory(category);
			rEvents.get(category).add( (RandomEvent)e );
		} else if (e instanceof TriggeredEvent){
			registerTriggeredCategory(category);
			tEvents.get(category).add(  (TriggeredEvent)e );
		}
	}
	
	/**
	 * Adds an event to the list of executable events and makes the event expire after a duration
	 * @param category
	 * @param e
	 * @param duration in milliseconds
	 */
	/*public static void registerEventTimed(String category, Event e, long duration){
		registerEvent(category, e);
		Timer timer = new Timer();
		
		TimedEvent task = new TimedEvent(){
			public void run() {
				EventGenerator.unregisterEvent(category, event);
			}
		};
		
		task.category = category;
		task.event = e;
		
		timer.schedule(task, duration);
	}*/
	
	/**
	 * 
	 * @param category
	 * @param e
	 * @param turns. number of turns this event is valid before it expires
	 */
	/*public static void registerEventTimed(String category, Event e, int turns){
		e.expireTurn = Game.turn + turns;
		registerEvent(category, e);
	}*/
	
	
	/**
	 * Removes an event from the list of executable events
	 * @param category
	 * @param e
	 */
	public static void unregisterEvent(String category, Event e){
		if (e instanceof RandomEvent){
			if (rEvents.get(category) != null){
				if (rEvents.get(category).contains(e)) {
					rEvents.get(category).remove(e);
				}
			}
		} else if (e instanceof TriggeredEvent) {
			if (tEvents.get(category) != null){
				if (tEvents.get(category).contains(e)) {
					tEvents.get(category).remove(e);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param String[] categories. A list of categories that are to be used for this session of retrieving an event
	 * @param double chanceToHaveEvent. The random chance for a random event to occur at all
	 * @return
	 */
	public static Event requestRandomEvent(String[] categories, double chanceToHaveEvent){
		Event result = null;
		
		//Check if event will occur
		if (Math.random() < chanceToHaveEvent){
			//if so, start the process for getting an event (giving all events equal chance)
			int[] eventCount = new int[categories.length];
			int   totalCount = 0;
			
			//get event counts
			for (int i = 0; i<categories.length; i++){
				if (rEvents.containsKey(categories)){
					eventCount[i] = rEvents.get(categories[i]).size();
					totalCount += eventCount[i];
				}
			}
			
			//randomize value
			int randValue = (int) (Math.random() * totalCount);
			
			//get the appropriate value from the appropriate category
			Log.e("Random Event", "Start Loop");
			for (int i = 0; i<categories.length; i++){
				Log.d("Random Event", "If rEvents contains category");
				if (rEvents.containsKey(categories[i])){
					ArrayList<RandomEvent> eventList = rEvents.get(categories[i]);
					int size = eventList.size();
					randValue -= size;
					if (randValue <= 0){
						int finalRandValue = (int) (Math.random() * size);
						return eventList.get(finalRandValue);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Produces a random event based on the categories available. Uses the default random chance that an event will occur
	 * @param String[] categories. A list of categories that are to be used for this session of retrieving an event
	 * @return
	 */
	public static Event requestRandomEvent(String[] categories){
		return requestRandomEvent(categories, CHANCE_EVENTHAPPENS);
	}
	
	/**
	 * Returns all triggered events owned by a specific player that have expiration timers
	 * @param player
	 * @return Event[]
	 */
	public static Event[] getPlayerTriggeredEvents(int player){
		ArrayList<Event> result = new ArrayList<Event>();
		for (ArrayList<TriggeredEvent> events : EventGenerator.tEvents.values()){
			for (int i=0; i<events.size(); i++){
				Event e = events.get(i);
				if (e.expireTurn > -1 && e.player == player){
					result.add(e);
				}
			}
		}
		
		return result.toArray(new Event[]{});
	}
	
	/**
	 * Produces a list of triggered events whos conditions have been satisfied and are all ready to be executed
	 * @param String[] categories. A list of categories that are to be used for this session of retrieving events
	 * @return TriggeredEvent[]. A list of triggered events
	 */
	public static TriggeredEvent[] requestTriggeredEvents(String[] categories){
		ArrayList<TriggeredEvent> result = null;
		
		for (String category : categories){
			if (tEvents.containsKey(category)){
				ArrayList<TriggeredEvent> eventList = tEvents.get(category);
				for (TriggeredEvent event : eventList){
					if (event.condition() == true){
						if (result == null){
							result = new ArrayList<TriggeredEvent>();
						}
						result.add(event);
					}
				}
			}
		}
		
		//return null if the arraylist is empty, otherwise return the arraylist in regular array form
		return (result == null ? null : result.toArray(new TriggeredEvent[result.size()]));
	}
	
	/**
	 * Informs the event generator that a turn has expired so that the generator can update the events that are
	 * sensitive to turn counts
	 */
	public static void informTurnExpired(){
		//handle random events
		for (ArrayList<RandomEvent> events : rEvents.values()){
			for (int i =0; i<events.size(); i++){
				if (Game.turn == events.get(i).expireTurn){
					events.remove(i);
					i--; //account for the shifting of the arraylist when item is removed
				}
			}
		}
		
		//handle triggered events
		for (ArrayList<TriggeredEvent> events : tEvents.values()){
			for (int i =0; i<events.size(); i++){
				TriggeredEvent event = events.get(i);
				if (event.condition()){
					event.action();
					events.remove(i);
					i--; //account for the shifting of the arraylist when item is removed
				}
			}
		}
	}
	
	/**
	 * Executes all events in the array
	 * This method checks the conditions of TriggeredEvents
	 * @param events
	 */
	public static void executeEvents(Event[] events){
		if (events == null){
			Log.e("executeEvents", "No Events Found");
			return;
		}
		for (Event event : events){
			executeEvent(event);
		}
	}
	
	/**
	 * Executes an event
	 * This method checks the conditions of TriggeredEvents
	 * @param event
	 */
	public static void executeEvent(Event event){
		if (event instanceof TriggeredEvent){
			TriggeredEvent tEvent = (TriggeredEvent)event;
			if (!tEvent.condition()){
				return; //exit the method immediately if the condition for this event is not met
			}
			
		}
		event.action();
	}
	
	/**
	 * Randomises and executes a random event
	 */
	public static void chooseAndExecuteRandomEvent(String category){
		chooseAndExecuteRandomEvent(new String[]{category});
	}
	
	/**
	 * Randomises and executes a random event
	 */
	public static void chooseAndExecuteRandomEvent(String[] categories){
		chooseAndExecuteRandomEvent(categories, EventGenerator.CHANCE_EVENTHAPPENS);
	}
	
	/**
	 * Randomises and executes a random event
	 */
	public static void chooseAndExecuteRandomEvent(String category, double chanceToHaveEvent){
		chooseAndExecuteRandomEvent(new String[]{category}, chanceToHaveEvent);
	}
	
	/**
	 * Randomises and executes a random event
	 */
	public static void chooseAndExecuteRandomEvent(String[] categories, double chanceToHaveEvent){
		Event event = requestRandomEvent(categories, chanceToHaveEvent);
		if (event != null){
			executeEvent(event);
		}
	}
	
	/**
	 * Executes all triggered events associated with the category
	 * All events have their conditions checked
	 * @param category
	 */
	public static void executeTriggeredEvents(String category){
		executeTriggeredEvents(new String[]{category});
	}
	
	/**
	 * Executes all triggered events associated with the categories
	 * All events have their conditions checked
	 * @param category
	 */
	public static void executeTriggeredEvents(String[] categories){
		Event[] events = requestTriggeredEvents(categories);
		executeEvents(events);
	}
}
