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
	@Override
	public boolean equals(Object o) {
		if(o instanceof BoardCell) {
			BoardCell cellO = (BoardCell) o;
			return Board.calcIndex(row, column) == cellO.getIndex();
		}
		return false;
	}
	// TODO "later we will add an abstract method named draw
}