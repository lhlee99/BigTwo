import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The BigTwoTable class implements the CardGameTable interface. It is used to build a GUI
 * for the Big Two card game and handle all user actions.
 * 
 * @author leelonghin
 *
 */
public class BigTwoTable extends JFrame implements CardGameTable{
	private BigTwoClient game;
	private boolean[] selected = new boolean[13];
	private boolean[] rised = new boolean[13];
	private boolean stop = true;
	private int activePlayer = -1;
	
	private JPanel panelLeft,panelRight,panelImage,panelButton,panelText,panelChat;
	private JTextArea textArea,chatArea;
	private JTextField textField;
	private JButton buttonLast, buttonReset, buttonNext,buttonPlay, buttonPass, buttonDeselect;
	private JMenu menu, menuCustomize;
	private JMenuBar menuBar;
	private JMenuItem itemQuit, itemConnect;
	private int yBorder = 50;
	private int show = 0;
	private Color fontColor = Color.WHITE;
	
	private Image[][] cardImages;
	private Image cardBackImage;
	private Image[] avatars, altAvatars;
	
	class Selector implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			int mouseX = e.getX();
			int mouseY = e.getY();
			for(int i = game.getPlayerList().get(activePlayer).getNumOfCards() - 1; i >= 0; i--) {
				if (selected[i]) {
					if (125 + 39*(i) < mouseX && mouseX < 125 + 39*(i) + 79 && yBorder + (activePlayer*150) - 20 < mouseY && mouseY < yBorder + (activePlayer*150) + 120 - 20) {
						selected[i] = false;
						repaint();
						break;
					}
				}
				else if (125 + 39*(i) < mouseX && mouseX < 125 + 39*(i) + 79) {
					if (yBorder + (activePlayer*150) < mouseY && mouseY < yBorder + (activePlayer*150) + 120 ) {//100 + 39*(j+1), 30 + (i*150) - 20 
						selected[i] = true;
						repaint();
						break;
					}
				}	
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	};
	Selector mL = new Selector();
	
	class PlayButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(game.getCurrentIdx() == activePlayer) {
				game.makeMove(activePlayer, getSelected());
				resetSelected();
			}
			repaint();
		}	
	}
	
	class PassButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if((game.getCurrentIdx() == activePlayer)) {
				game.makeMove(activePlayer, null);
				resetSelected();
			}
			repaint();
		}	
	}
	
	/**
	 * a constructor for creating a BigTwoTable.
	 * 
	 * @param game a reference to a card game associates with this table
	 */
	public BigTwoTable(BigTwoClient game) {
		this.game = game;
		run(game);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// width height
		//setSize(1295,800);
		setResizable(false);
		setVisible(true);
		pack();
	}

	private void run(BigTwoClient game) {
		avatars = new Image[game.getNumOfPlayers()];
		altAvatars = new Image[game.getNumOfPlayers()];
		for(int i = 0; i < game.getNumOfPlayers(); i++) {
			avatars[i] = new ImageIcon("image/avatars/player" + i + ".png").getImage();
			altAvatars[i] = new ImageIcon("image/avatars/player" + i + "Alt.png").getImage();
		}
		cardBackImage = new ImageIcon("image/cardBacks/back.png").getImage();
		cardImages = new Image[4][13];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				cardImages[i][j] = new ImageIcon("image/cards/"+ (i+1) + "-" + (j+1) +".png").getImage();
			}
		}
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menu = new JMenu("Game");
		itemQuit = new JMenuItem("Quit");
		itemConnect = new JMenuItem("Connect");
		itemConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 
				if(activePlayer == -1)
					game.makeConnection();
				else
					printMsg("You are already connected to the server.");
			}
		});
		itemQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(itemConnect);
		menu.add(itemQuit);
		menuBar.add(menu);
		
		menuCustomize = new JMenu("Customize");
		menuBar.add(menuCustomize);
		
		JMenuItem itemCardBack = new JMenuItem("Card Back");
		itemCardBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser("image/cardBacks");
		        chooser.setFileFilter(new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif","png"));
		        int returnVal = chooser.showOpenDialog(null);
		        if(returnVal == JFileChooser.APPROVE_OPTION) {
		        	cardBackImage = new ImageIcon(chooser.getSelectedFile().getAbsolutePath()).getImage();
		        }
		        repaint();
			}
		});
		menuCustomize.add(itemCardBack);
		
		JMenuItem itemColor = new JMenuItem("Bg color");
		itemColor.addActionListener(new ActionListener() {
			@Override
		    public void actionPerformed(ActionEvent e) { 
		        Color color = JColorChooser.showDialog(null, "Pick your color, dark color is recommended", new Color(30, 36, 48));
		        panelButton.setBackground(color);
		        panelImage.setBackground(color); 
		        textArea.setBackground(color.darker());
		        chatArea.setBackground(color.darker());
		        fontColor = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000>= 128 ? Color.black : Color.white;
		        repaint();
			} 
		});
		menuCustomize.add(itemColor);
		
		panelRight = new JPanel(new BorderLayout());
		panelRight.setPreferredSize(new Dimension(495,790));
		panelRight.setBackground(new Color(23, 31, 48));
		add(panelRight, BorderLayout.EAST);

		panelText = new JPanel(new BorderLayout());
		panelText.setPreferredSize(new Dimension(495,488));
		panelText.setBackground(Color.GREEN);
		panelRight.add(panelText,BorderLayout.NORTH);
		
		textArea = new JTextArea();
		textArea.setBackground(new Color(23, 31, 48));
		textArea.append(" Welcome\n");
		textArea.setForeground(fontColor);
		textArea.setFont(new Font("Apercu Mono",0, 15)); 
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		panelText.add(textArea, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		panelText.add(scrollPane);
		
		// Message Area
		panelChat = new JPanel(new BorderLayout());
		panelChat.setBackground(Color.GREEN);
		panelRight.add(panelChat, BorderLayout.CENTER);
		
		chatArea = new JTextArea();
		chatArea.setBackground(new Color(23, 31, 48));
		chatArea.setForeground(fontColor);
		chatArea.setFont(new Font("Apercu Mono",0, 15)); 
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		chatArea.setEditable(false);
		chatArea.append("  Chat:\n");
		panelChat.add(chatArea, BorderLayout.CENTER);
		
		JScrollPane scrollPane2 = new JScrollPane(chatArea);
		scrollPane2.setBorder(BorderFactory.createEmptyBorder());
		panelChat.add(scrollPane2);
		
		textField = new JTextField(20);
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!textField.getText().isEmpty())
					game.sendMessage(new CardGameMessage(CardGameMessage.MSG, -1,textField.getText()));
					textField.setText("");
			}
		});
		panelRight.add(textField, BorderLayout.SOUTH);
		
		
		panelLeft = new JPanel(new BorderLayout());
		panelLeft.setBackground(Color.GREEN);
		add(panelLeft, BorderLayout.CENTER);
		
		panelImage = new JPanel(){
			@Override
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				g.setColor(fontColor);
				if (!stop){
					// print Card
					for (int i = 0; i < 4; i++) {
						for (int j = 0; j < game.getPlayerList().get(i).getNumOfCards() ; j++) {
							if (i == activePlayer || game.endOfGame()) {
								if(rised[j]) {
									if (activePlayer == 0)
										g.drawImage(cardImages[game.getPlayerList().get(i).getCardsInHand().getCard(j).suit][game.getPlayerList().get(i).getCardsInHand().getCard(j).rank],  125 + 39*(j), yBorder + (i*150) - 50, 118, 180, this);	
									else
										g.drawImage(cardImages[game.getPlayerList().get(i).getCardsInHand().getCard(j).suit][game.getPlayerList().get(i).getCardsInHand().getCard(j).rank],  125 + 39*(j), yBorder + (i*150) - 80, 118, 180, this);
								}else if(selected[j]) {
									g.drawImage(cardImages[game.getPlayerList().get(i).getCardsInHand().getCard(j).suit][game.getPlayerList().get(i).getCardsInHand().getCard(j).rank],  125 + 39*(j), yBorder + (i*150) - 20, 79, 120, this);
								} else
									g.drawImage(cardImages[game.getPlayerList().get(i).getCardsInHand().getCard(j).suit][game.getPlayerList().get(i).getCardsInHand().getCard(j).rank],  125 + 39*(j), yBorder + (i*150) , 79, 120, this);
							}
							else {
								g.drawImage(cardBackImage,  125 + 39*(j), yBorder + (i*150) , 79, 120,this);
							}
						}
					}
					resetRised();
					
					// last hand on table
					Hand lastHandOnTable = (game.getHandsOnTable().isEmpty()) ? null : game.getHandsOnTable().get(show);
					if (lastHandOnTable != null) {
						for (int i = 0; i < lastHandOnTable.size() ; i++) {
							g.drawImage(cardImages[lastHandOnTable.getCard(i).suit][lastHandOnTable.getCard(i).rank],  25 + 79 *(i), yBorder + (4*150), 79, 120, this);
						}
					}
					// text description
					if (lastHandOnTable != null) {
						g.drawString("< "+ (show+1) + " / " + game.getHandsOnTable().size() +" >",79 *(5) + 110, yBorder + 650 );
						g.drawString(lastHandOnTable.getType() + " played by " + lastHandOnTable.getPlayer().getName() , 79 *(5) + 50, yBorder + 670 );
					}
				}
				
				for(int i = 0; i < game.getNumOfPlayers(); i++) {
					if(game.getPlayerList().get(i).getName() != "") {
						g.drawString(game.getPlayerList().get(i).getName(), 10, yBorder + (i*150) + 125);
						if (i == game.getCurrentIdx())
							g.drawImage(altAvatars[i],10, yBorder + (i*150) + 5 , 100, 100, this);
						else
							g.drawImage(avatars[i],10, yBorder + (i*150) + 5 , 100, 100, this);
					}
				}
				
			}
		};
		panelImage.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (!game.endOfGame()) {
					int mouseX = e.getX();
					int mouseY = e.getY();
					for(int i = game.getPlayerList().get(activePlayer).getNumOfCards() - 1; i >= 0; i--) {
						if(selected[i]) {
							if (125 + 39*(i) < mouseX && mouseX < 125 + 39*(i) + 79) {
								if (yBorder + (activePlayer*150) - 20< mouseY && mouseY < yBorder + (activePlayer*150) + 120 - 20) {//100 + 39*(j+1), 30 + (i*150) - 20 
									panelImage.repaint();
									break;
								}
							}
						}
						else {
						if (125 + 39*(i) < mouseX && mouseX < 125 + 39*(i) + 79) {
							if (yBorder + (activePlayer*150) < mouseY && mouseY < yBorder + (activePlayer*150) + 120 && !rised[i]) {//100 + 39*(j+1), 30 + (i*150) - 20 
								rised[i] = true;
								panelImage.repaint();
								break;
							}
						}
						}
					}
					panelImage.repaint();
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {}
		});
		panelImage.setPreferredSize(new Dimension(800,790));
		panelImage.setBackground(new Color(30, 36, 48));
		panelImage.addMouseListener(mL);
		panelLeft.add(panelImage, BorderLayout.CENTER);
		
		panelButton = new JPanel();
		panelButton.setBackground(new Color(30, 36, 48)); //new Color(91, 100, 117)
		panelLeft.add(panelButton, BorderLayout.SOUTH);
		
		buttonLast = new JButton("<");
		buttonLast.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				show--;
				if(show < 0)
					show = game.getHandsOnTable().size() - 1;
				panelImage.repaint();
			}
		});
		buttonLast.setPreferredSize(new Dimension(25,20));
		panelButton.add(buttonLast);

		buttonReset = new JButton("o");
		buttonReset.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				show = game.getHandsOnTable().size() - 1;
				panelImage.repaint();
			}
		});
		buttonReset.setPreferredSize(new Dimension(25,20));
		panelButton.add(buttonReset);

		buttonNext = new JButton(">");
		buttonNext.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				show++;
				if(show > game.getHandsOnTable().size() - 1)
					show = 0;
				panelImage.repaint();
			}
		});
		buttonNext.setPreferredSize(new Dimension(25,20));
		panelButton.add(buttonNext);
		
		buttonPlay = new JButton("Play");
		buttonPlay.addActionListener(new PlayButtonListener());
		panelButton.add(buttonPlay);
		
		buttonPass = new JButton("Pass");
		buttonPass.addActionListener(new PassButtonListener());
		panelButton.add(buttonPass);
		
		buttonDeselect = new JButton("Deselect");
		buttonDeselect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetSelected();
				panelImage.repaint();
			}
		});
		panelButton.add(buttonDeselect);
	}
	
	/**
	 * a method for printing end game message to the text area
	 * 
	 */
	public void end() {
		String msg = "";
		msg += "\nGame ends\n";
		for(CardGamePlayer player: game.getPlayerList()) {
			if (player.getNumOfCards() == 0) {
				msg += player.getName() + " wins the game.\n";
			}
			else {
				msg += player.getName() + " has " + player.getNumOfCards() + " cards in hand.\n";
			}
		}
		JOptionPane.showMessageDialog(null, msg);
		disable();
		game.sendMessage(new CardGameMessage(CardGameMessage.READY, -1, -1));
	}
	
	@Override
	public void setActivePlayer(int activePlayer) {
		if (activePlayer < 0 || activePlayer >= game.getPlayerList().size())
			this.activePlayer = -1;
		else
			this.activePlayer = activePlayer;
	}

	@Override
	public int[] getSelected() {
		
		int numOfSelectedCard = 0;
		for(boolean i : selected) {
			if(i)
				numOfSelectedCard++;
		}
		if(numOfSelectedCard == 0){
			return null;
		}
		int[] tmp = new int[numOfSelectedCard];
		int n = 0;
		for (int i = 0; i < selected.length; i++) {
			if(selected[i])
				tmp[n++] = i;
		}
		return tmp;
	}

	@Override
	public void resetSelected() {
		for(int i = 0; i < 13; i++) {
			selected[i] = false;
		}
	}

	private void resetRised() {
		for(int i = 0; i < 13; i++) {
			rised[i] = false;
		}
	}
	@Override
	public void printMsg(String msg) {
		textArea.append(" " + msg + "\n");
	}

	@Override
	public void clearMsgArea() {
		textArea.setText("");
	}

	@Override
	public void enable() {
		stop = false;
		panelImage.setEnabled(true);
		buttonPlay.setEnabled(true);
		buttonPass.setEnabled(true);
		buttonDeselect.setEnabled(true);
		buttonNext.setEnabled(true);
		buttonLast.setEnabled(true);
		buttonReset.setEnabled(true);
	}

	@Override
	public void disable() {
		stop = true;
		panelImage.setEnabled(false);
		buttonPlay.setEnabled(false);
		buttonPass.setEnabled(false);
		buttonDeselect.setEnabled(false);
		buttonNext.setEnabled(false);
		buttonLast.setEnabled(false);
		buttonReset.setEnabled(false);
	}

	@Override
	public void reset() {
		resetSelected();
		clearMsgArea();
		enable();
	}
	
	@Override
	public void repaint() {
		panelImage.repaint();
	}
	/**
	 * Prints the specified string to the chat area of the card game table.
	 * 
	 * @param msg the string to be printed to the message area of the card game table
	 */
	public void printChat(String msg) {
		chatArea.append(" " + msg + "\n");
	}
	/**
	 * Sets the index of the handOnTable that the player want to see
	 * 
	 * @param n the index
	 */
	public void setShow(int n) {
		this.show = n;
	}
}
