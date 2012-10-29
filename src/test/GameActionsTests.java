package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import main.*;
import junit.framework.Assert;

import main.Board;
import main.Card.CardType;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GameActionsTests {
	private static Board board;
	
	@BeforeClass
	public static void setUp() {
		board = new Board();
		board.loadConfigFiles("resources/clueKey.txt", "resources/cluelayout.csv", "resources/CluePlayers.txt", "resources/ClueCards.txt");
		board.calcAdjacencies();
	}

	@Test
	public void testAccusation() {
		board.deal();
		Assert.assertTrue(board.checkAccusation(board.getSolution().getPerson(), board.getSolution().getWeapon(), 
				board.getSolution().getRoom()));
		Assert.assertFalse(board.checkAccusation(board.getSolution().getPerson(), " ", " "));
		Assert.assertFalse(board.checkAccusation(" ", board.getSolution().getWeapon(), board.getSolution().getRoom()));
		Assert.assertFalse(board.checkAccusation(" ", " ", " "));
	}
	
	@Test
	public void testTargetLocation() {
		// First checking computer chooses door, or if door was last visited, that it chooses other locations
		ComputerPlayer comp = board.getComputerPlayer(1);
		board.calcTargets(Board.calcIndex(9, 2), 1);
		boolean room = true;
		for(int i=0; i < 100; i++) {
			BoardCell selected = comp.pickLocation(board.getTargets());
			if((board.getCellAt(Board.calcIndex(10, 2)) != selected) || 
					(comp.getLastVisited() == board.getCellAt(Board.calcIndex(10, 2))))
				room = false;
		}
		Assert.assertTrue(room);
		
		// Checking movement if no door available
		board.calcTargets(Board.calcIndex(4, 11), 1);
		int cell4_10 = 0;
		int cell4_12 = 0;
		for(int i=0; i < 100; i++) {
			BoardCell selected = comp.pickLocation(board.getTargets());
			if(board.getCellAt(Board.calcIndex(4, 10)) == selected)
				cell4_10++;
			else if(board.getCellAt(Board.calcIndex(4, 12)) == selected)
				cell4_12++;
		}
		Assert.assertTrue(cell4_10 > 10);
		Assert.assertTrue(cell4_12 > 10);
	}
	
	@Test
	public void testDisproveSuggestion() {
		// Test Disproving a suggestion where the only valid card is a room card.
		boolean isDisproved = false;
		board.deal();
		
		ComputerPlayer first = board.getComputerPlayer(0);
		Card testRoom = new Card("testRoom", Card.CardType.ROOM);
		Card testWeapon = new Card("testWeapon", Card.CardType.WEAPON);
		Card testPerson = new Card("testPerson", Card.CardType.PERSON);
		first.addCard(testRoom);
		if(first.disproveSuggestion(null, null, testRoom) == testRoom) {
			isDisproved = true;
		}
		Assert.assertTrue(isDisproved);
		first.removeCard(testRoom);
		
		//Test Disproving a suggestion where the same player has two cards.
		first.addCard(testRoom);
		first.addCard(testWeapon);
		int weaponCount = 0;
		int roomCount = 0;
		for(int count = 0; count < 10; ++count) {
			if(first.disproveSuggestion(null, testWeapon, testRoom) == testRoom)
				++roomCount;
			if(first.disproveSuggestion(null, testWeapon, testRoom) == testWeapon)
				++weaponCount;
		}
		Assert.assertTrue(roomCount > 0 && weaponCount > 0);
		first.removeCard(testRoom);
		first.removeCard(testWeapon);
		
		//Test Disproving a suggestion where two different players each have one suggested card.
		ComputerPlayer second = board.getComputerPlayer(1);
		first.addCard(testRoom);
		second.addCard(testWeapon);
		int firstCount = 0;
		int secondCount = 0;
		for(int count = 0; count < 10; ++count) {
			if(board.handleSuggestion(null, testWeapon, testRoom, board.getHumanPlayer()) == first)
				++firstCount;
			if(board.handleSuggestion(null, testWeapon, testRoom, board.getHumanPlayer()) == second)
				++secondCount;
		}
		Assert.assertTrue(firstCount > 0 && secondCount > 0);
		first.removeCard(testRoom);
		second.removeCard(testWeapon);
		
		//Test that the human player correctly disproves a suggestion.
		HumanPlayer you = board.getHumanPlayer();
		you.addCard(testRoom);
		Assert.assertEquals(testRoom, you.disproveSuggestion(null, null, testRoom));
		you.removeCard(testRoom);
		
		//Test that the player who's turn it is does not return a card.
		first.addCard(testRoom);
		Assert.assertNotSame(first, board.handleSuggestion(null, null, testRoom, first));
	}
	
	@Test
	public void testMakingSuggestion() {
		ComputerPlayer first = board.getComputerPlayer(0);
		ArrayList<Card> suggested = first.createSuggestion();
		boolean suggestionIsSeen = false;
		for(Card i : suggested) {
			if(i.getCardType() != Card.CardType.ROOM) {
				if(first.getSeenList().contains(i))
					suggestionIsSeen = true;
			}
		}
		Assert.assertFalse(suggestionIsSeen);
	}
	
	
}
