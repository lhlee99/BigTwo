/**
 * This class is a subclass of the Hand class, and are used to model a hand of 
 * single in a Big Two card game
 * 
 * @author leelonghin
 */
public class Single extends Hand {
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}
	/**
	 * a method for checking if this is a valid single
	 * 
	 * @return whether this hand is valid
	 */
	public boolean isValid(){
		if(this.size() == 1)
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
		if(hand.getType() == "Single")
			return this.getTopCard().compareTo(hand.getTopCard()) == 1;
		else
			return false;
	}
	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return a string of the type of this hand
	 */
	public String getType(){
		return "Single";
	}
}
