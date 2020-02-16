package game;

import dao.PlayerModel;

public class Player implements PlayerModel {
	private Card hand;
	private Deck deck;
	private int id;
	private String playerName;
	private boolean isHuman;
	private int roundsWon;

	public Player(Deck deck, boolean isHuman, int id) {
		this.deck = deck;
		this.isHuman = isHuman;
		this.id = id;
		if(isHuman){
			this.playerName = "You";
		}else{
			this.playerName = "AIPlayer_"+ id;
		}
	}

	public int chooseCategory() {
		int index = 0;
		int max = hand.getValue(0);
		for (int i = 0; i < deck.getCategory().length; i++){
			int tmp =  hand.getValue(i);
			if(tmp > max){
				max = tmp;
				index = i;
			}
		}
		return index;
	}

	public void draw(){
		hand = deck.getTopCard();
	}
	public void wonRound() {
		roundsWon++;
	}

	@Override
	public int getRoundsWon() {
		return roundsWon;
	}

	public Deck getDeck(){
		return deck;
	}

	public Card getHand(){
		return hand;
	}

	public boolean isHuman() {
		return isHuman;
	}

	public int getId() {
		return id;
	}

	public String getPlayerName() {
		return playerName;
	}
}
