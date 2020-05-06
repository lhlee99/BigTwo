/**
 * This class is a subclass of the Hand class, and are used to model a hand of 
 * triple in a Big Two card game
 * 
 * @author leelonghin
 */
public class Triple extends Hand{
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid triple
	 * 
	 * @return whether this hand is valid
	 */
	public boolean isValid(){
		if(this.size() != 3)
			return false;
		else
			return getCard(0).rank == getCard(1).rank && getCard(1).rank == getCard(2).rank;
	}
	/**
	 * a method for checking if this hand beats a specified hand
	 * 
	 * @param hand hand to be compared
	 * @return whether this hand beats the specified hand
	 */
	public boolean beat(Hand hand){
		if(hand.getType() == "Triple"){
			return this.getTopCard().compareTo(hand.getTopCard()) == 1;
		}
		return false;
	}
	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return a string of the type of this hand
	 */
	public String getType(){
		return "Triple";
	}
}
