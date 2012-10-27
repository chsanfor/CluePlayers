package test;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import main.Board;
import main.BoardCell;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

public class AdjacencyTest {
	private static Board board;
	private static LinkedList<Integer> testList;
	private static Set<BoardCell> testSet;
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	@Before
	public void setUp() {
		board = new Board();
		board.loadConfigFiles("resources/clueKey.txt", "resources/cluelayout.csv", "resources/CluePlayers.txt", "resources/ClueCards.txt");
		board.calcAdjacencies();
		testList = new LinkedList<Integer>();
		testSet = new HashSet<BoardCell>();
	}
	@Test
	public void adjTestOnlyWalkway() {
		setTestList(15, 4);
		checkListContains(15, 3);
		checkListContains(14, 4);
		checkListContains(15, 5);
		checkListContains(16, 4);
	}
	@Test
	public void adjTestEdge() {
		setTestList(0, 0);
		checkListContains(0, 1);
		checkListContains(1, 0);
		
		setTestList(18, 0);
		checkListContains(17, 0);
		checkListContains(19, 0);
		checkListContains(18, 1);
		
		setTestList(24, 7);
		checkListContains(24, 6);
		checkListContains(24, 8);
		checkListContains(23, 7);
		
		setTestList(14, 12);
		checkListContains(14, 11);
		checkListContains(13, 12);
		checkListContains(15, 12);
		
		setTestList(0, 5);
		checkListContains(0, 4);
		checkListContains(0, 6);
		checkListContains(1, 5);
		
		setTestList(24, 2);
		checkListContains(24, 3);
		checkListContains(24, 1);
		checkListContains(23, 2);
	}
	@Test
	public void adjTestNearRoom() {
		setTestList(16, 6);
		checkListContains(16, 5);
		checkListContains(16, 7);
		checkListContains(15, 6);
		
		setTestList(18, 11);
		checkListContains(18, 10);
		checkListContains(18, 12);
		checkListContains(19, 11);
	}
	@Test
	public void adjDoor() {
		setTestList(20, 9);
		checkListContains(20, 10);
		checkListContains(19, 9);
		checkListContains(21, 9);
		
		setTestList(10, 9);
		checkListContains(9, 9);
		checkListContains(11, 9);
		checkListContains(10, 10);
	}
	@Test
	public void targetOneStep() {
		setTestSet(21, 3, 1);
		checkSetContains(21, 4, 3);
		checkSetContains(20, 3, 3);
		checkSetContains(22, 3, 3);
		
		setTestSet(14, 8, 1);
		checkSetContains(14, 7, 4);
		checkSetContains(14, 9, 4);
		checkSetContains(15, 8, 4);
		checkSetContains(13, 8, 4);
	}
	@Test
	public void targetTwoStep() {
		setTestSet(6, 9, 2);
		checkSetContains(8, 9, 2);
		checkSetContains(4, 9, 2);
	}
	@Test
	public void targetThreeStep() {
		setTestSet(7, 4, 3);
		checkSetContains(4, 4, 4);
		checkSetContains(8, 2, 4);
		checkSetContains(9, 3, 4);
		checkSetContains(10, 4, 4);
		
	}
	@Test
	public void targetEnterRoom() {
		setTestSet(8, 0, 1);
		checkSetContains(9, 0, 3);
		checkSetContains(7, 0, 3);
		checkSetContains(8, 1, 3);
		
		setTestSet(19, 10, 1);
		checkSetContains(20, 10, 4);
		checkSetContains(19, 11, 4);
		checkSetContains(18, 10, 4);
		checkSetContains(19, 9, 4);
	}
	@Test
	public void targetLeaveRoom() {
		setTestSet(24, 2, 1);
		checkSetContains(24, 3, 1);
		
		setTestSet(12, 10, 1);
		checkSetContains(12, 9, 2);
		checkSetContains(11, 10, 2);
	}
	public void setTestList(int row, int col) {
		System.out.println("Testing row: " + row + " col: " + col);
		testList = board.getAdjList(board.calcIndex(row, col));
	}
	public void setTestSet(int row, int col, int moves) {
		System.out.println("Testing: " + board.calcIndex(row, col));
		board.calcTargets(board.calcIndex(row, col), moves);
		testSet = board.getTargets();
	}
	public void checkListContains(int row, int col) {
		System.out.println("Row: " + row + " Col: " + col);
		System.out.println("Looking for this index: " + board.calcIndex(row, col));
		System.out.println(testList);
		collector.checkThat(testList.contains(board.calcIndex(row, col)), equalTo(true));
	}
	public void checkSetContains(int row, int col, int size) {
		for (BoardCell b : testSet) {
			System.out.print(b.getIndex() + " ");
		}
		System.out.println();
		collector.checkThat(testSet.size(), equalTo(size));
		collector.checkThat(testSet.contains(board.getCellAt(board.calcIndex(row, col))), equalTo(true));
	}
}
