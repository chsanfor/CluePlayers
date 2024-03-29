package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import main.RoomCell.DoorDirection;

public class Board {
	private static int numRows;
	private static int numColumns;

	private ArrayList<BoardCell> cells;
	private Map<Character, String> rooms;
	private Map<Integer, LinkedList<Integer>> adjacencyMatrix;
	private Set<BoardCell> targets;
	private Vector<Integer> path;
	private boolean[] seenArray;
	private ArrayList<ComputerPlayer> computerPlayers;
	private ArrayList<Card> cards;
	private HumanPlayer humanPlayer;
	private Solution solution;
	
	public Board() {
		super();
		rooms = new HashMap<Character, String>();
		cells = new ArrayList<BoardCell>();
		adjacencyMatrix = new HashMap<Integer, LinkedList<Integer>>();
		targets = new HashSet<BoardCell>();
		path = new Vector<Integer>();
		computerPlayers = new ArrayList<ComputerPlayer>();
		cards = new ArrayList<Card>();
		solution = new Solution("Empty", "Empty", "Empty");
	}
	
	public boolean checkAccusation(String person, String weapon, String room) {
		return person.equals(solution.getPerson()) && weapon.equals(solution.getWeapon()) && room.equals(solution.getRoom());
	}
	
	public void deal() {
		Random rand = new Random();
		ArrayList<Card> cardsCopy = cards;
		int numCards = cards.size();
		int numDealt = 0;
		while(numCards > 0) {
			Card randomCard = cardsCopy.get(rand.nextInt(numCards));
			Card.CardType type = randomCard.getCardType();
			if(type == Card.CardType.PERSON && solution.getPerson().equals("Empty"))
				solution.setPerson(randomCard.getName());
			else if(type == Card.CardType.WEAPON && solution.getWeapon().equals("Empty"))
				solution.setWeapon(randomCard.getName());
			else if(type == Card.CardType.ROOM && solution.getRoom().equals("Empty"))
				solution.setRoom(randomCard.getName());
			else {
				int index = numDealt % (computerPlayers.size() + 1);
				if(index == computerPlayers.size())
					humanPlayer.addCard(randomCard);
				else {
					ComputerPlayer him = computerPlayers.get(index);
					him.addCard(randomCard);
					computerPlayers.set(index, him);
				}
				++numDealt;	
			}
			cardsCopy.remove(randomCard);
			--numCards;
			randomCard.setDealt(true);
			for(Card i : cards) {
				if(i.equals(randomCard))
					i = randomCard;
			}
		}
	}
	public Solution getSolution() {
		return solution;
	}
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public HumanPlayer getHumanPlayer() {
		return humanPlayer;
	}
	
	public ComputerPlayer getComputerPlayer(int i) {
		return computerPlayers.get(i);
	}
	
	public ArrayList<ComputerPlayer> getComputerPlayers() {
		return computerPlayers;
	}

	public void loadConfigFiles(String txtFile, String csvFile, String playerFile, String cardsFile) {
		loadTXTFile(txtFile);
		loadCSVFile(csvFile);
		loadPlayers(playerFile);
		loadCards(cardsFile);
		
		seenArray = new boolean[numRows*numColumns];
		/*
		for (int i = 0; i < numRows*numColumns; i++) {
			seenArray[i] = false;
		}
		 */
	}
	public void loadCSVFile(String csvFile){
		try {
			Scanner scan = new Scanner(new FileReader(csvFile));
			int row = 0;
			String[] stringArray = scan.nextLine().split(",");
			numColumns = stringArray.length;
			scan = new Scanner(new FileReader(csvFile));
			while (scan.hasNextLine()) {
				int countColumns = 0;
				String[] cellStrings = scan.nextLine().split(",");
				for (int i = 0; i < cellStrings.length; i++) {
					countColumns++;
					if (cellStrings[i].equalsIgnoreCase("W")) {
						cells.add(new WalkwayCell(row, i));
					} else {
						if (!rooms.containsKey(cellStrings[i].charAt(0))) {
							throw new BadConfigFormatException("Map doesn't contain room: " + cellStrings[i].charAt(1));
						}
						// CHANGE: Added 3 lines below
						RoomCell r = new RoomCell(row, i);
						r.setRoomInitial(cellStrings[i].charAt(0));
						cells.add(r);
						if(cellStrings[i].length() == 2){
							Character c = cellStrings[i].charAt(1);
							// RoomCell r = new RoomCell(row, i);
							// r.setRoomInitial(cellStrings[i].charAt(0));
							if(c.equals('U')){
								r.setDirection(DoorDirection.UP);
							}
							if(c.equals('D')){
								r.setDirection(DoorDirection.DOWN);
							}
							if(c.equals('L')){
								r.setDirection(DoorDirection.LEFT);
							}
							if(c.equals('R')){
								r.setDirection(DoorDirection.RIGHT);
							}

							// cells.add(r);
						} // else {
						// RoomCell r = new RoomCell(row, i);
						// r.setRoomInitial(cellStrings[i].charAt(0));
						// cells.add(r);
						//}
					}
				}
				if (countColumns != numColumns) {
					throw new BadConfigFormatException("Expected number of columns: " + numColumns + "receieved: " + countColumns);
				}
				row++;
			}
			numRows = row;

		} catch (FileNotFoundException e) {
			System.out.println("File CR-ClueLayout.csv not found.");
		}
	}
	public void loadTXTFile(String txtFile){
		try {
			Scanner scan = new Scanner(new FileReader(txtFile));
			while(scan.hasNextLine()){
				String[] temp = scan.nextLine().split(", ");
				if (temp.length != 2) {
					throw new BadConfigFormatException("Legend file does not contain two items on row: " + temp);
				}
				rooms.put(temp[0].charAt(0), temp[1]);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File CR-ClueLegend.txt not found.");
		}

	}
	
	public void loadPlayers(String playersFile) throws BadConfigFormatException {
		try {
			Scanner in = new Scanner(new FileReader(playersFile));
			String line1 = new String();
			String line2 = new String();
			String line3 = new String();
			line1 = in.nextLine();
			line2 = in.nextLine();
			line3 = in.nextLine();
			String[] coordinates = line3.split(", ");
			humanPlayer = new HumanPlayer(line1, line2, new WalkwayCell(Integer.parseInt(coordinates[0]),
					Integer.parseInt(coordinates[1])));
			
			while(in.hasNext()) {
				line1 = in.nextLine();
				line2 = in.nextLine();
				line3 = in.nextLine();
				coordinates = line3.split(", ");
				computerPlayers.add(new ComputerPlayer(line1, line2, new WalkwayCell(Integer.parseInt(coordinates[0]),
						Integer.parseInt(coordinates[1]))));
				
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
	}
	
	public void loadCards(String cardsFile) {
		try {
			Scanner in = new Scanner(new FileReader(cardsFile));
			String line1 = new String();
			String line2 = new String();
			while(in.hasNext()) {
				line1 = in.nextLine();
				line2 = in.nextLine();
				Card.CardType type = null;
				if(line2.equals("Person"))
					type = Card.CardType.PERSON;
				else if(line2.equals("Weapon"))
					type = Card.CardType.WEAPON;
				else if(line2.equals("Room"))
					type = Card.CardType.ROOM;
				else {
					System.out.println(line2);
					throw new BadConfigFormatException("Invalid config file.");
				}
				cards.add(new Card(line1, type));
				Player.allCards.add(new Card(line1, type));
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
	}
	
	public BoardCell getCellAt(int index) {
		return cells.get(index);
	}
	/**
	 * Method generates index of row & column, row and column need to be 0 based (i.e. 0..10)
	 * @param row
	 * @param column
	 * @return
	 */
	public static int calcIndex(int row, int column){
		return (row)*numColumns + column;
	}
	public RoomCell getRoomCellAt(int row, int column) {
		if (cells.get(calcIndex(row, column)) instanceof RoomCell)
			return (RoomCell) cells.get(calcIndex(row, column));
		else
			return null;
	}

	public ArrayList<BoardCell> getCells() {
		return cells;
	}

	public Map<Character, String> getRooms() {
		return rooms;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}
	public LinkedList<Integer> getAdjList(int index) {
		return adjacencyMatrix.get(index);
	}
	public Set<BoardCell> getTargets() {
		return targets;
	}
	public void calcAdjacencies(){
		LinkedList<Integer>adjacentIndex;
		int index = 0;
		for(int i =0; i < numRows ; i++){
			for(int j = 0; j<numColumns; j++){
				adjacentIndex = new LinkedList<Integer>();
				if( j +1 < numColumns){
					adjacentIndex.add(index+1);
				}
				if( j-1 >=0){
					adjacentIndex.add(index-1);	
				}
				if( i + 1 < numRows){			
					adjacentIndex.add(index + numColumns);
				}
				if( i-1>=0 ){
					adjacentIndex.add(index - numColumns);
				}
				adjacencyMatrix.put(index, adjacentIndex);
				index++;
			}
		}	
	}
	
	public Player handleSuggestion(Card person, Card weapon, Card room, Player suggestor) {
		Random rand = new Random();
		int size = computerPlayers.size();
		int randomInt = rand.nextInt(size);
		ArrayList<Player> disprovers = new ArrayList<Player>();
		if(!humanPlayer.getName().equals(suggestor.getName()))
			disprovers.add(humanPlayer);
		for(ComputerPlayer i : computerPlayers) {
			if(!i.getName().equals(suggestor.getName()))
				disprovers.add(i);
		}
		for(int i = 0; i < size; ++i) {
			int index = (randomInt + i) % size;
			Card disproved = disprovers.get(index).disproveSuggestion(person, weapon, room);
			if(disproved != null)
				return disprovers.get(index);
		}
		
		return null;
		
	}

	public void calcTargets(int startIndex, int steps) {
		targets.clear();
		actualCalcTargets(startIndex, steps);
	}

	public void actualCalcTargets(int startIndex, int steps) {
		boolean added = false;
		for (int i =0; i < adjacencyMatrix.get(startIndex).size(); i++) {
			seenArray[startIndex] = true;
			int adjCell = adjacencyMatrix.get(startIndex).get(i);
			if(!seenArray[adjCell] && (cells.get(adjCell).isDoorway()
					|| cells.get(adjCell).isWalkway())) {
				added=true;
				path.add(adjCell);
			}else{
				continue;
			}
			if (cells.get(adjCell).isDoorway()) {
				targets.add(cells.get(adjCell));
			}
			if(path.size() == steps){
				if (cells.get(path.lastElement()).isDoorway() || cells.get(path.lastElement()).isWalkway())
					targets.add(cells.get(path.lastElement()));
				path.remove(path.size()-1);
			}
			else{
				actualCalcTargets(adjCell, steps);
				seenArray[adjCell] = false;
				if(added){
					path.remove(path.size()-1);
				}

			}
		}

	}
}