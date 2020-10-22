package model;

import model.interfaces.PlayingCard;

public class PlayingCardImpl implements PlayingCard {
	private Suit suit;
	private Value value;

	public PlayingCardImpl(Suit suit, Value value) {
		this.suit = suit;
		this.value = value;
	}

	@Override
	public Suit getSuit() {
		return suit;
	}

	@Override
	public Value getValue() {
		return value;
	}

	@Override
	public int getScore() {
		int score = 0;
		if (this.value == Value.ACE) {
			score = 11;
		} else if (this.value == Value.TEN || this.value == Value.JACK || this.value == Value.QUEEN
				|| this.value == Value.KING) {
			score = 10;
		} else if (this.value == Value.NINE) {
			score = 9;
		} else if (this.value == Value.EIGHT) {
			score = 8;
		}
		return score;
	}

	@Override
	public String toString() {
		String suitStr = "";
		if (this.suit == Suit.CLUBS) {
			suitStr = "Club";
		} else if (this.suit == Suit.DIAMONDS) {
			suitStr = "Diamonds";
		} else if (this.suit == Suit.HEARTS) {
			suitStr = "Hearts";
		} else if (this.suit == Suit.SPADES) {
			suitStr = "Spades";
		}
		
		String valueStr = "";
		if (this.value == Value.ACE) {
			valueStr = "Ace";
		} else if (this.value == Value.KING) {
			valueStr = "King";
		} else if (this.value == Value.QUEEN) {
			valueStr = "Queen";
		} else if (this.value == Value.JACK) {
			valueStr = "Jack";
		} else if (this.value == Value.TEN) {
			valueStr = "Ten";
		} else if (this.value == Value.NINE) {
			valueStr = "Nine";
		} else if (this.value == Value.EIGHT) {
			valueStr = "Eight";
		}

		return String.format("Suit: %s, Value: %s, Score: %s", suitStr, valueStr, this.getScore());
	}

	public boolean equals(PlayingCard card) {
		if (this.suit == card.getSuit() && this.value == card.getValue()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object card) {
		return card instanceof PlayingCard && ((PlayingCard) card).equals(this); 
	}

	@Override
	public int hashCode() {
		int prime = 37;
		int result = 1;
		
		result = result * prime + ((suit == null) ? 0 : suit.hashCode());
		result = result * prime + ((value == null) ? 0 : value.hashCode());
		
		return result;
	}

}
