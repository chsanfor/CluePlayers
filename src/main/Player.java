package main;

import java.util.ArrayList;


public class Player {
	private String name;
	private String color;
	private WalkwayCell start;
	private ArrayList<Card> cards;
	private RoomCell lastVisited;
	
	public Player(String theName, String theColor, WalkwayCell theStart) {
		name = theName;
		color = theColor;
		start = theStart;
		cards = new ArrayList<Card>();
	}
	
	public Card disproveSuggestion(String person, String weapon, String room) {
		return null;
	}
	
	public RoomCell getLastVisited() {
		return lastVisited;
	}
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public void addCard(Card toAdd) {
		cards.add(toAdd);
	}
	public void removeCard(Card toRemove) {
		cards.remove(toRemove);
	}
	
	public String getName() {
		return name;
	}
	public String getColor() {
		return color;
	}
	public WalkwayCell getStart() {
		return start;
	}
}

