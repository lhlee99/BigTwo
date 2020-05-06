/**
 * This class is a subclass of the Hand class, and are used to model a hand of 
 * straight in a Big Two card game
 * 
 * @author leelonghin
 */
public class Straight extends Hand{
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	/**
	 * a method for checking if this is a valid straight
	 * 
	 * @return whether this hand is valid
	 */
	public boolean isValid(){
		this.sort();
		if(this.size() != 5)
			return false;
		else{
			for(int i = 0; i < 4; i++){
				if((getCard(i).getRank() - 2 + 13) % 13 + 1 == ((getCard(i + 1).getRank() - 2 + 13) % 13)){
					//System.out.printf("%d %d %d\n",i, getCard(i).getRank(),(getCard(i + 1).getRank()));
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
		if(hand.getType() == "Straight"){
			return this.getTopCard().compareTo(hand.getTopCard()) == 1;
		}
		else
			return this.getStrength() > hand.getStrength();
	}
	/**
	 * a method for returning the strength of different 5-card hand
	 * 
	 * @return the strength of this 5-card hand
	 */
	public int getStrength() {
		return 0;
	}
	/**
	 * a method for returning a string specifying the type of this hand
	 * 
	 * @return a string of the type of this hand
	 */
	public String getType(){
		return "Straight";
	}
}
