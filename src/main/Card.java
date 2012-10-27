package main;

public class Card {
	private String name;
	public static enum CardType{PERSON, WEAPON, ROOM};
	private CardType cardType;
	private boolean dealt;
	
	public Card() {
		
	}
	
	public Card(String name, CardType type) {
		this.name = name;
		cardType = type;
	}
	
	public boolean getDealt() {
		return dealt;
	}
	
	public String getName() {
		return name;
	}

	public CardType getCardType() {
		return cardType;
	}
	
	public void setCardType(CardType type) {
		cardType = type;
	}
	
	public void setName(String DrName) {
		name = DrName;
	}
	@Override
	public boolean equals(Object c)
	{
		return false;
	}
}
