/**
 * This class is a subclass of the Hand class, and are used to model a hand of 
 * straightflush in a Big Two card game
 * 
 * @author leelonghin
 */
public class StraightFlush extends Hand{
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid straightflush
	 * 
	 * @return whether this hand is valid
	 */
	public boolean isValid(){
		this.sort();
		if(this.size() != 5)
			return false;
		else{
			for(int i = 0; i < 4; i++){
				if((getCard(i).getRank() - 2 + 13) % 13 + 1 != ((getCard(i + 1).getRank() - 2 + 13) % 13))
					break;

				if ( getCard(i).getSuit() == getCard(i + 1).getSuit() ){
					if(i == 3)
						return true;
				}
				else
					break;
			}
			return false;
		}
	}
	/**
	 * a method for checking if this hand beats a specified hand
	 * 
	 * @param hand hand to be compared
	 * @return whether this hand beats the specified hand
	 */
	public boolean beat(Hand hand){
		if(hand.getType() == "StraightFlush"){
			return (this.getTopCard()).compareTo(hand.getTopCard()) == 1;
		}
		else
			return this.getStrength() > hand.getStrength();
	}
	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return a string of the type of this hand
	 */
	public String getType(){
		return "StraightFlush";
	}
	/**
	 * a method for returning the strength of different 5-card hand
	 * 
	 * @return the strength of this 5-card hand
	 */
	public int getStrength() {
		return 4;
	}
}
