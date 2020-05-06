/**
 * This class is a subclass of the Hand class, and are used to model a hand of 
 * fullhouse in a Big Two card game
 * 
 * @author leelonghin
 */
public class FullHouse extends Hand{
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid fullhouse
	 * 
	 * @return whether this hand is valid
	 */
	public boolean isValid(){
		this.sort();
		if(this.size() != 5)
			return false;
		else{
			if( getCard(0).rank == getCard(1).rank && getCard(2).rank == getCard(3).rank && getCard(3).rank == getCard(4).rank )
				return true;
			else if ( getCard(0).rank == getCard(1).rank && getCard(1).rank == getCard(2).rank && getCard(3).rank == getCard(4).rank )
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
		if(hand.getType() == "FullHouse"){
			return this.getTopCard().compareTo(hand.getTopCard()) == 1;////// Question
		}
		else
			return this.getStrength() > hand.getStrength();
	}
	/**
	 * override the getTopCard() in Hand. Returning the card that
	 * determine whether a hand of fullhouse beat another hand of fullhouse
	 * 
	 * @return the card that determine whether a hand of fullhouse
	 * 		   beat another hand of fullhouse
	 */
	public Card getTopCard(){
		this.sort();
		if (this.isValid()){
			if ( getCard(2).rank == getCard(3).rank && getCard(3).rank == getCard(4).rank) ////Question
				return getCard(4);
			else
				return getCard(0);
		}
		System.out.println("Error: getFHRep() - not a FullHouse wtf?");
		return(getCard(0));

	}
	/**
	 * a method for returning the strength of different 5-card hand
	 * 
	 * @return the strength of this 5-card hand
	 */
	public int getStrength() {
		return 2;
	}
	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return a string of the type of this hand
	 */
	public String getType(){
		return "FullHouse";
	}
}

