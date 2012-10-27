package main;

public class Solution {
	public String person;
	public String weapon;
	public String room;
	
	public Solution() {
		person = new String();
		weapon = new String();
		room = new String();
	}
	public Solution(String aPerson, String aWeapon, String aRoom) {
		person = aPerson;
		weapon = aWeapon;
		room = aRoom;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getWeapon() {
		return weapon;
	}
	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}

}
