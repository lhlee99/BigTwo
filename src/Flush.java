/**
 * This class is a subclass of the Hand class, and are used to model a hand of 
 * flush in a Big Two card game
 * 
 * @author leelonghin
 */
public class Flush extends Hand{
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid flush
	 * 
	 * @return whether this hand is valid
	 */
	public boolean isValid(){
		this.sort();
		if(this.size() != 5)
			return false;
		else{
			int suit = getCard(0).getSuit();
			for(int i = 0; i < 5; i++){
				if((getCard(i).getSuit() == suit)){
					if(i == 4)
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
		if(hand.getType() == "Flush"){
			if(getTopCard().getSuit() == hand.getTopCard().getSuit())
				return this.getTopCard().compareTo(hand.getTopCard()) == 1;
			else
				return getTopCard().getSuit() > hand.getTopCard().getSuit();
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
		return "Flush";
	}
	/**
	 * a method for returning the strength of different 5-card hand
	 * 
	 * @return the strength of this 5-card hand
	 */
	public int getStrength() {
		return 1;
	}
}
