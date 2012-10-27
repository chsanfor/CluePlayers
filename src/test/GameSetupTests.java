package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;
import main.*;


public class GameSetupTests {
	
	private static Board board;
	
	public GameSetupTests() {
		board = new Board();
		board.loadConfigFiles("resources/clueKey.txt", "resources/cluelayout.csv", "resources/CluePlayers.txt", "resources/ClueCards.txt");
	}
	
	@Test
	public void testSetupPlayers() {
		
		HumanPlayer mrYou = board.getHumanPlayer();
		assertEquals("You", mrYou.getName());
		assertEquals("White", mrYou.getColor());
		WalkwayCell space1 = new WalkwayCell(0, 3);
		assertEquals(space1.getIndex(), mrYou.getStart().getIndex());
		ComputerPlayer mrsF = board.getComputerPlayer(1);
		assertEquals("Mrs. F", mrsF.getName());
		assertEquals("Pink", mrsF.getColor());
		WalkwayCell space2 = new WalkwayCell(11, 12);
	}
	
	@Test
	public void testCards() {
		
		assertEquals(21, board.getCards().size());
		int people = 0;
		int weapons = 0;
		int rooms = 0;
		boolean hasT = false;
		boolean hasKnife = false;
		boolean hasEntry = false; 
		for(Card i: board.getCards()) {
			if(i.getCardType() == Card.CardType.PERSON) {
				++people;
				if(i.getName().equals("Mr. T"))
					hasT = true;
			}
			else if(i.getCardType() == Card.CardType.WEAPON) {
				++weapons;
				if(i.getName().equals("Knife"))
					hasKnife = true;
			}
			else if(i.getCardType() == Card.CardType.ROOM) {
				++rooms;
				if(i.getName().equals("Entry"))
					hasEntry = true;
			}
		}
		assertEquals(people, 6);
		assertEquals(weapons, 6);
		assertEquals(rooms, 9);
		Assert.assertTrue(hasT && hasKnife && hasEntry);
	}
	
	@Test
	public void TestDeal() {
		board.deal();
		HumanPlayer mrYou = board.getHumanPlayer();
		assertEquals(3, mrYou.getCards().size());
		ComputerPlayer godzilla = board.getComputerPlayer(4);
		assertEquals(3, godzilla.getCards().size());
		
		for(Card i: board.getCards()) {
			Assert.assertTrue(i.getDealt());
		}
		ArrayList<Card> combined = new ArrayList<Card>();
		combined.addAll(mrYou.getCards());
		for(ComputerPlayer i : board.getComputerPlayers()) {
			combined.addAll(i.getCards());
		}
		boolean duplicates = false;

		for ( int i = 0; i < combined.size(); i++ ) {
			for ( int j = 0; j < combined.size(); j++ ) {
				if ( i == j ){

				}
				else if (combined.get(j).equals(combined.get(i))){
					duplicates = true;
				}
			}
		}
		Assert.assertFalse(duplicates);
	}
}
