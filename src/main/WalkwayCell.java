package main;

public class WalkwayCell extends BoardCell {
	public WalkwayCell(int row, int col) {
		this.row = row;
		this.column = col;
	}
	@Override
	public boolean isWalkway() {
		return true;
	}
	// TODO Override draw method from BoardCell
}