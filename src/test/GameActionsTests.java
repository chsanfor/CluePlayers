package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import main.*;
import junit.framework.Assert;

import main.Board;
import main.Card.CardType;

import org.junit.Test;

public class GameActionsTests {
	private static Board board;
	
	public GameActionsTests() {
		board = new Board();
		board.loadConfigFiles("resources/clueKey.txt", "resources/cluelayout.csv", "resources/CluePlayers.txt");
	}

	@Test
	public void testAccusation() {
		board.setSolution("Mr. T", "Knife", "Entry");
		Assert.assertTrue(board.checkAccusation("Mr. T", "Knife", "Entry"));
		Assert.assertFalse(board.checkAccusation("Mrs. F", "Kinfe", "Entry"));
		Assert.assertFalse(board.checkAccusation("Mr. T", "Gun", "Entry"));
		Assert.assertFalse(board.checkAccusation("Mr. T", "Knife", "Kitchen"));
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
		board.Deal();
		
		ComputerPlayer first = board.getComputerPlayer(0);
		Card testRoom = new Card("testRoom", Card.CardType.ROOM);
		Card testWeapon = new Card("testWeapon", Card.CardType.WEAPON);
		Card testPerson = new Card("testPerson", Card.CardType.PERSON);
		first.addCard(testRoom);
		if(first.disproveSuggestion(" ", " ", "testRoom") == testRoom) {
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
			if(first.disproveSuggestion(" ", "testWeapon", "testRoom") == testRoom)
				++roomCount;
			if(first.disproveSuggestion(" ", "testWeapon", "testRoom") == testWeapon)
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
			if(board.handleSuggestion(" ", "testWeapon", "testRoom") == first)
				++firstCount;
			if(board.handleSuggestion(" ", "testWeapon", "testRoom") == second)
				++secondCount;
		}
		Assert.assertTrue(firstCount > 0 && secondCount > 0);
		first.removeCard(testRoom);
		second.removeCard(testWeapon);
		
		//Test that the human player correctly disproves a suggestion.
		HumanPlayer you = board.getHumanPlayer();
		you.addCard(testRoom);
		Assert.assertEquals(testRoom, you.disproveSuggestion(" ", " ", "testRoom"));
		you.removeCard(testRoom);
		
		//Test that the player who's turn it is does not return a card.
		first.addCard(testRoom);
		Assert.assertNotSame(first, board.handleSuggestion(" ", " ", "testRoom"));
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