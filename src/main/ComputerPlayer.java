package main;

import java.util.ArrayList;
import java.util.Set;

public class ComputerPlayer extends Player {
	
	private ArrayList<Card> seenList;
	
	public ComputerPlayer(String theName, String theColor, WalkwayCell theStart) {
		super(theName, theColor, theStart);
	}
	
	public ArrayList<Card> createSuggestion() {
		return null;
	}
	
	public ArrayList<Card> getSeenList() {
		return seenList;
	}
	
	public BoardCell pickLocation(Set<BoardCell> targets){
		return null;
	}
}
