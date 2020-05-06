/**
 * This class is a subclass of the Hand class, and are used to model a hand of 
 * pair in a Big Two card game
 * 
 * @author leelonghin
 */
public class Pair extends Hand{
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}
	/**
	 * a method for checking if this is a valid pair
	 * 
	 * @return whether this hand is valid
	 */
	public boolean isValid(){
		if(this.size() != 2)
			return false;

		else if(getCard(0).rank == getCard(1).rank)
			return true;
		
		else
			return false;
	}
	/**
	 * a method for checking if this hand beats a specified hand
	 * 
	 * @param hand hand to be compared
	 * @return whether this hand beats the specified hand
	 */
	public boolean beat(Hand hand){
		if (hand.getType() == "Pair")
			return this.getTopCard().compareTo(hand.getTopCard()) == 1;
		return false;
	}
	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return a string of the type of this hand
	 */
	public String getType(){
		return "Pair";
	}
}
