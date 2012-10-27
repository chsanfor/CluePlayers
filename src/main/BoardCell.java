package main;
public abstract class BoardCell {
	protected int column, row;
	public boolean isWalkway() {
		return false;
	}
	public boolean isRoom() {
		return false;
	}
	public boolean isDoorway() {
		return false;
	}
	public int getIndex() {
		return Board.calcIndex(row, column);
	}
	// TODO "later we will add an abstract method named draw
}