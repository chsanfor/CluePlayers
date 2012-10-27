package main;

public class RoomCell extends BoardCell {
	// public enum DoorDirection {UP, DOWN, LEFT, RIGHT};
	public enum DoorDirection {UP, DOWN, LEFT, RIGHT, NONE};
	private DoorDirection doorDirection;
	private char roomInitial;
	public RoomCell(int row, int col) {
		this.row = row;
		this.column = col;
		doorDirection = DoorDirection.NONE;
	}
	public void setDirection(DoorDirection doorDirection) {
		this.doorDirection = doorDirection;
	}
	public void setRoomInitial(char room) {
		this.roomInitial = room;
	}
	@Override
	public boolean isRoom() {
		return true;
	}
	@Override
	public boolean isDoorway() {
		return doorDirection != DoorDirection.NONE;
	}
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	// TODO Override the draw method from BoardCell
	public char getInitial() {
		return roomInitial;
	}
}