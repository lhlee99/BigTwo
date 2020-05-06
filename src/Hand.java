/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards. It
 * has a private instance variable for storing the player who plays this hand. It also has methods
 * for getting the player of this hand, checking if it is a valid hand, getting the type of this hand,
 * getting the top card of this hand, and checking if it beats a specified hand.
 * 
 * @author leelonghin
 */
abstract class Hand extends CardList{
	private CardGamePlayer player;
	/**
	 * a constructor for building a hand with
	 * the specified player and list of cards.
	 * 
	 * @param player the specified player
	 * @param card the list of card
	 */
	public Hand(CardGamePlayer player, CardList cards){
		this.player = player;
		this.removeAllCards();
		for (int i = 0; i < cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
	}
	/**
	 * a method returning the player who plays this hand.
	 * 
	 * @return the player who plays this hand
	 */
	public CardGamePlayer getPlayer(){
		return player;
	}
	/**
	 * a method for retrieving the top card of this hand.
	 * 
	 * @return the top card of this hand
	 */
	public Card getTopCard(){
		Card topCard = new Card(0,2);
		for (int i = 0; i < size(); i++) {
			if(getCard(i).compareTo(topCard) == 1){
				topCard = getCard(i);
			}
		}
		return topCard;
	}
	/**
	 * a method for checking if this hand beats a specified hand.
	 * 
	 * @param hand hand to be compared
	 * @return whether this hand beats the specified hand
	 */
	public boolean beat(Hand hand){
		return getTopCard().compareTo(hand.getTopCard()) == 1;
	}
	/**
	 * a method for returning the strength of different 5-card hand 
	 * (to be overridden in 5-card hand subclasses)
	 * 
	 * @return the strength of a 5-card hand
	 */
	public int getStrength() {
		return -1;
	};
	/**
	 * a method for checking if this is a valid single
	 * 
	 * @return whether this hand is valid
	 */
	abstract boolean isValid();
	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return a string of the type of this hand
	 */
	abstract String getType();
}
