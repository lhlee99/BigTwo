/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a card used in a
 * Big Two card game. It should override the compareTo() method it inherited from the Card
 * class to reflect the ordering of cards used in a Big Two card game. Below is a detailed
 * description for the BigTwoCard class.
 * 
 * @author leelonghin
 *
 */
public class BigTwoCard extends Card {
	/**
	 * a constructor for building a card with the specified
	 * suit and rank. suit is an integer between 0 and 3, and rank is an integer between 0 and 12
	 *
	 * @param suit suit of the card
	 * @param rank rank of the card
	 */
	public BigTwoCard(int suit, int rank){
		super(suit,rank);
	}
	/**
	 * a method for comparing the order of this card with the 
	 * specified card. Returns a negative integer, zero, or a positive integer as this card is less 
	 * than, equal to, or greater than the specified card.
	 * 
	 * @return Returns a negative integer, zero, or a positive integer as this card is less 
	 * than, equal to, or greater than the specified card.
	 */
	public int compareTo(Card card){
		int tmpRank1 = (this.rank + 11) % 13;
		int tmpRank2 = (card.rank + 11) % 13;
		if (tmpRank1 > tmpRank2) {
			return 1;
		} else if (tmpRank1 < tmpRank2) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
}
