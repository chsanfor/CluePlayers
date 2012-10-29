package main;

import java.util.ArrayList;
import java.util.Random;


public class Player {
	private String name;
	private String color;
	private WalkwayCell start;
	private ArrayList<Card> cards;
	private RoomCell lastVisited;
	public static ArrayList<Card> allCards = new ArrayList<Card>();
	
	public Player(String theName, String theColor, WalkwayCell theStart) {
		name = theName;
		color = theColor;
		start = theStart;
		cards = new ArrayList<Card>();
	}
	
	public Card disproveSuggestion(Card person, Card weapon, Card room) {
		ArrayList<Card> suggestionHand = new ArrayList<Card>();
		if(cards.contains(person)) 
			suggestionHand.add(person);
		if(cards.contains(weapon))
			suggestionHand.add(weapon);
		if(cards.contains(room))
			suggestionHand.add(room);
		Random rand = new Random();
		if(suggestionHand.isEmpty())
			return null;
		int size = suggestionHand.size();
		return suggestionHand.get(rand.nextInt(size));
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

