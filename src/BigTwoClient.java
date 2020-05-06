import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

/**
 * The BigTwoClient class implements the CardGame interface and NetworkGame interface. It
 * is used to model a Big Two card game that supports 4 players playing over the Internet. 
 * 
 * @author leelonghin
 *
 */
public class BigTwoClient implements CardGame, NetworkGame{
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable = new ArrayList<Hand>();
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket socket;
	private ObjectOutputStream oos;
	private int currentIdx = -1;
	private BigTwoTable gui;
	private ObjectInputStream ois;
	
	private int passCount = 0;
	private boolean firstRound = true;
	private boolean isPlay = true;
	private boolean isUpdate = true;
	
	/**
	 * a constructor for creating a Big Two client. It (i) creates 4 
	 * players and add them to the list of players; (ii) creates a Big Two table which builds the
	 * GUI for the game and handles user actions; and (iii) makes a connection to the game
	 * server by calling the makeConnection() method from the NetworkGame interface.
	 */
	public BigTwoClient() {
		playerList = new ArrayList<CardGamePlayer>();
		for (int i = 0; i < 4; i++) {
			playerList.add(new CardGamePlayer(""));
		}
		numOfPlayers = 4;
		
		gui = new BigTwoTable(this);
		String name = JOptionPane.showInputDialog("Please enter your name: ");
		gui.setTitle("Big Two( " + name + ")");
		playerName = name; 
		makeConnection();
	}
	
	@Override
	public void start(Deck deck) {
		passCount = 0;
		firstRound = true;
		isPlay = true;
		isUpdate = true;
		for (int i = 0; i < 4; i++) {
			playerList.get(i).removeAllCards();
		}
		handsOnTable.clear();
		this.deck = deck;
		// distribute cards
		for(int i = 0; i < deck.size(); i++){
			this.playerList.get(i%4).addCard(deck.getCard(i));
			//find diamond 3
			if ( deck.getCard(i).equals(new Card(0,2)) ) {
				currentIdx = i % 4;
			}
		}
		gui.setActivePlayer(playerID);
		playerList.get(playerID).sortCardsInHand();
		gui.printMsg(playerList.get(currentIdx).getName() + "'s turn: ");
		gui.repaint();
	}
	
	@Override
	public int getPlayerID() {
		return this.playerID;
	}

	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	@Override
	public String getPlayerName() {
		return this.playerName;
	}

	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public String getServerIP() {
		return this.serverIP;
	}

	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	@Override
	public int getServerPort() {
		return this.serverPort;
	}

	@Override
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	public void makeConnection() {
		String input = JOptionPane.showInputDialog("Please input the IP", "127.0.0.1");
		if (input != null)
			setServerIP(input);
		input = JOptionPane.showInputDialog("Please Input the Port", "2396");
		if (input != null)
			setServerPort(Integer.parseInt(input));

		try {
			socket = new Socket(this.serverIP, this.serverPort);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Runnable threadJob = new ServerHandler();
		Thread thread = new Thread(threadJob);
		thread.start();
		sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, playerName));
		sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
	}

	@Override
	public synchronized void parseMessage(GameMessage message) {
		if (message.getType() == CardGameMessage.PLAYER_LIST){
			playerID = message.getPlayerID();
			gui.setActivePlayer(playerID);
			
			String[] name = (String[]) message.getData();
			for (int i = 0; i < 4; i++) {
				if (name[i] != null) {
					this.playerList.get(i).setName(name[i]);
				}
			}
			gui.disable();
			gui.repaint();
		}
		else if (message.getType() == CardGameMessage.JOIN){
			playerList.get(message.getPlayerID()).setName((String) message.getData());
			gui.printMsg((String) message.getData() + " joined the game!");
			gui.repaint();
		}
		else if (message.getType() == CardGameMessage.FULL){
			gui.printMsg("The server is full.");
			gui.repaint();
		}
		else if (message.getType() == CardGameMessage.QUIT){
			gui.printMsg(playerList.get(message.getPlayerID()).getName() + " has left the game.");
			playerList.get(message.getPlayerID()).setName("");
			if (!endOfGame()) {
				gui.disable();
				gui.repaint();
				sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			}
		}
		else if (message.getType() == CardGameMessage.READY){
			gui.printMsg(playerList.get(message.getPlayerID()).getName() + " is ready!");
		}
		else if (message.getType() == CardGameMessage.START){
			start((Deck) message.getData());
			gui.enable();
		}
		else if (message.getType() == CardGameMessage.MOVE){
			checkMove(message.getPlayerID(), (int[]) message.getData());
		}
		else if (message.getType() == CardGameMessage.MSG){
			gui.printChat((String)message.getData());
		}
	}

	@Override
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getNumOfPlayers() {
		return this.numOfPlayers;
	}

	@Override
	public Deck getDeck() {
		return this.deck;
	}

	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		return this.playerList;
	}

	@Override
	public ArrayList<Hand> getHandsOnTable() {
		return this.handsOnTable;
	}

	@Override
	public int getCurrentIdx() {
		return this.currentIdx;
	}

	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		sendMessage(new CardGameMessage(CardGameMessage.MOVE, playerID, cardIdx));
	}

	@Override
	public void checkMove(int playerID, int[] cardIdx) {
		playerList.get(playerID).sortCardsInHand();
		int[] input = cardIdx;
		CardList tmp = this.playerList.get(playerID).play(input);
		boolean isPass = (tmp == null);
		//gui.printMsg("hand: " + playerList.get(playerID).getCardsInHand().toString());
		Hand play = composeHand(this.playerList.get(playerID), tmp);
		//gui.printMsg("input: " + input[0]);
		// valid move?
		if ( isPass || play != null) {
			//	1st round
			if (firstRound) {
				//gui.printMsg("first round");
				//gui.printMsg(play.toString());
				if ( !isPass && play.contains(new BigTwoCard(0, 2))) {
					isPlay = true;
					isUpdate = true;
					firstRound = false;
				}
				else {
					isPlay = false;
					isUpdate = false;
				}
			}
			
			// Not 1st round
			else {
				// Pass
				if (isPass) {
					if(passCount == 3) {
						isUpdate = false;
						isPlay = false;
					}
					else {
						passCount++;
						isUpdate = true;
						isPlay = false;
						gui.printMsg(playerList.get(playerID).getName()+ " Pass !");
					}
				}
				// Not Pass
				else {
					Hand lastHandOnTable = handsOnTable.get(handsOnTable.size() - 1);
					if (passCount >= 3) {
						isPlay = true;
						isUpdate = true;
						passCount = 0;
					}
					// Same size? && Beat last hand?
					else if (lastHandOnTable.size() == play.size() && play.beat(lastHandOnTable)) {
						isPlay = true;
						isUpdate = true;
						passCount = 0;
					}
					else {
						isPlay = false;
						isUpdate = false;
					}
				}	
			}
		}
		// Not valid combination
		else {
			isUpdate = false;
			isPlay = false;
		}
		// isPlay: play the hand
		if (isPlay) {
			handsOnTable.add(play);
			this.playerList.get(playerID).removeCards(play);
			passCount = 0;
			gui.printMsg(" <" + play.getPlayer().getName()
					+ "> {" + play.getType() + "} "
					+ play.toString());
			gui.setShow(getHandsOnTable().size() - 1);
			if (endOfGame()) {
				gui.repaint();
				gui.end();
			}
		}
		 
		// isUpdate: next player
		if(isUpdate) {
			currentIdx = (currentIdx + 1) % 4;
			this.playerList.get(currentIdx).sortCardsInHand();
			gui.printMsg(playerList.get(currentIdx).getName() + "'s Turn:");
			gui.resetSelected();
			gui.repaint();
		}
		else {
			gui.printMsg(playerList.get(currentIdx).getName() + " is trying to make a illegal move :'(");
		}
	}

	@Override
	public boolean endOfGame() {
		for (CardGamePlayer p: playerList )
			if(p.getNumOfCards() == 0)
				return true;
		return false;
	}
	/**
	 * an inner class that implements the Runnable interface. It
	 * implements the run() method from the Runnable interface and create a thread
	 * with an instance of this class as its job in the makeConnection() method from the
	 * NetworkGame interface for receiving messages from the game server. Upon receiving a
	 * message, the parseMessage() method from the NetworkGame interface should be
	 * called to parse the messages accordingly.
	 * 
	 * @author leelonghin
	 *
	 */
	public class ServerHandler implements Runnable {
		@Override
		public void run() {
			CardGameMessage message;
			try {
				while ((message = (CardGameMessage) ois.readObject()) != null) {
					parseMessage(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * a method for creating an instance of BigTwoClient
	 * 
	 * @param args arguments, unused
	 */
	public static void main(String[] args) {
		new BigTwoClient();
	}
	/**
	 * a method for returning a valid hand from the specified list of cards 
	 * of the player. Returns null is no valid hand can be composed from the
	 * specified list of cards.
	 *
	 * @param player the owner of that hand
	 * @param cards specified list of cards
	 * @return null if no valid hand can be composed from the specified list of cards,
	 *	else return a valid hand from the specified list of cards of the player
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards){
		if (cards == null) {
			return null;
		}
		Hand tmp = null;
		if (new Single(player, cards).isValid()) {
			tmp = new Single(player, cards);
		}
		if (new Pair(player, cards).isValid()) {
			tmp = new Pair(player, cards);
		}
		if (new Triple(player, cards).isValid()) {
			tmp = new Triple(player, cards);
		}
		if (new Straight(player, cards).isValid()) {
			tmp = new Straight(player, cards);
		}
		if (new Flush(player, cards).isValid()) {
			tmp = new Flush(player, cards);
		}
		if (new FullHouse(player, cards).isValid()) {
			tmp = new FullHouse(player, cards);
		}
		if (new Quad(player, cards).isValid()) {
			tmp = new Quad(player, cards);
		}
		if (new StraightFlush(player, cards).isValid()) {
			tmp = new StraightFlush(player, cards);
		}
		return tmp;
	}
}
