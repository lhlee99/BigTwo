/**
 * This class is a subclass of the Hand class, and are used to model a hand of 
 * quad in a Big Two card game
 * 
 * @author leelonghin
 */
public class Quad extends Hand{
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid quad
	 * 
	 * @return whether this hand is valid
	 */
	public boolean isValid(){
		this.sort();
		if(this.size() != 5)
			return false;
		else{
			if( getCard(0).rank == getCard(1).rank && getCard(1).rank == getCard(2).rank && getCard(2).rank == getCard(3).rank )
				return true;
			else if ( getCard(1).rank == getCard(2).rank && getCard(2).rank == getCard(3).rank && getCard(3).rank == getCard(4).rank )
				return true;
		}
			return false;
		}
	/**
	 * a method for checking if this hand beats a specified hand
	 * 
	 * @param hand hand to be compared
	 * @return whether this hand beats the specified hand
	 */
	public boolean beat(Hand hand){
		this.sort();
		hand.sort();
		if(hand.getType() == "Quad"){
			return (this.getCard(1)).compareTo(hand.getCard(1)) == 1;
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
		return "Quad";
	}
	/**
	 * a method for returning the strength of different 5-card hand
	 * 
	 * @return the strength of this 5-card hand
	 */
	public int getStrength() {
		return 3;
	}
}

