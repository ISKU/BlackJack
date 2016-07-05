package kmh.blackjack;

public class Card {

	private String shape;
	private int value;

	// ---------------------------------------------------------- //
	// shape: Spade, Diamond, Clover, Heart 					  //
	// value: 2 ~ 10, Ace, Jack, Queen, King (value: 1, 11 ~ 13)  //
	// 		: specialValue: Ace, Jack, Queen, King 				  //
	// ---------------------------------------------------------- //

	public Card(String shape, int value) {
		this.shape = shape;
		this.value = value;
	}

	public Card(String shape, String specialValue) {
		this.shape = shape;
		this.value = toIntegerValue(specialValue);
	}

	public String getShape() {
		return shape;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("%s%s", shape, value > 1 && value < 11 ? value : toSpecialValue(value));
	}

	private String toSpecialValue(int value) {
		switch (value) {
			case 1:
				return "A";
			case 11:
				return "J";
			case 12:
				return "Q";
			case 13:
				return "K";
			default:
				return value + "";
		}
	}

	private int toIntegerValue(String specialValue) {
		switch (specialValue) {
			case "A":
				return 1;
			case "J":
				return 11;
			case "Q":
				return 12;
			case "K":
				return 13;
			default:
				throw new IllegalArgumentException("*** Invalid Parameter ***");
		}
	}
}