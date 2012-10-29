package main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
	
	private ArrayList<Card> seenList;
	
	public ComputerPlayer(String theName, String theColor, WalkwayCell theStart) {
		super(theName, theColor, theStart);
		seenList = new ArrayList<Card>();
	}
	
	public ArrayList<Card> createSuggestion() {
		ArrayList<Card> returnList = new ArrayList<Card>();
		//Adds Kitchen to the suggestion list, since we do not have code to determine
		//which room the player is in.
		for(Card i : allCards) {
			if(i.getName().equals("Kitchen")) {
				returnList.add(i);
				break;
			}
		}
		Random rand = new Random();
		boolean hasPerson = false;
		boolean hasWeapon = false;
		while(returnList.size() < 3) {
			int index = rand.nextInt(allCards.size());
			Card randCard = allCards.get(index);
			if((randCard.getCardType() == Card.CardType.PERSON) && !seenList.contains(randCard) && !hasPerson) {
				returnList.add(randCard);
				hasPerson = true;
			}
			else if((randCard.getCardType() == Card.CardType.WEAPON) && !seenList.contains(randCard) && !hasWeapon) {
				returnList.add(randCard);
				hasWeapon = true;
			}
		}
		return returnList;
	}
	
	public ArrayList<Card> getSeenList() {
		return seenList;
	}
	
	public BoardCell pickLocation(Set<BoardCell> targets){
		
		Random rand = new Random();
		int size = targets.size();
		ArrayList<BoardCell> targetArray = new ArrayList<BoardCell>();
		boolean isOtherRooms = false;
		for(BoardCell i : targets) {
			targetArray.add(i);
			if(i.isDoorway() && !i.equals(this.getLastVisited())) {
				return i;
			}
		}
		return targetArray.get(rand.nextInt(size));
	}
}
