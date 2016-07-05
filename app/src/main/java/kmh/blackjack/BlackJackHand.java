package kmh.blackjack;

public class BlackJackHand extends Hand {

	public int getBlackJackValue() {
		int sumOfValue = 0;
		boolean hasAce = false;
		int cards = getCardCount();

		for (int i = 0; i < cards; i++) {
			Card card = getCard(i);
			int cardValue = card.getValue();

			if (cardValue > 10) {
				cardValue = 10;
			}
			if (cardValue == 1) {
				hasAce = true;
			}
			sumOfValue = sumOfValue + cardValue;
		}

		if (hasAce == true && sumOfValue + 10 <= 21)
			sumOfValue = sumOfValue + 10;

		return sumOfValue;
	}
}
