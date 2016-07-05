package kmh.blackjack;

public class Deck {

	private Card[] deck;
	private int cardsUsed;

	public Deck() {
		deck = new Card[52];
		for (int i = 0; i < 13; i++) {
			deck[i] = new Card("♠", i+1);
			deck[i + 13] = new Card("◆", i+1);
			deck[i + 26] = new Card("♣", i+1);
			deck[i + 39] = new Card("♥", i+1);
		}
		shuffle();
	}

	public void shuffle() {
		for (int i = 51; i > 0; i--) {
			int random = (int) (Math.random() * (i + 1));
			Card temp = deck[i];
			deck[i] = deck[random];
			deck[random] = temp;
		}
		cardsUsed = 0;
	}

	public Card dealCard() {
		if (cardsUsed == 52)
			shuffle();
		cardsUsed++;
		return deck[cardsUsed - 1];
	}

	public int getLeftCards() {
		return 52 - cardsUsed;
	}
}