package game;

import dao.GameModel;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class Game implements GameModel {

	private Deck communalPile;
	private Deck board;
	private Player human;
	private LinkedList<Player> players;
	private LinkedList<Player> allPlayer;
	private Player currentPlayer;
	private int numOfPlayer;
	private int totalRounds;
	private int totalDraws;
	private boolean randomStart;
	private boolean isHumanFailed;
	private int currentCategory;
	private Player winner;


	public Game(Deck deck, int numOfPlayer) {
		this.numOfPlayer = numOfPlayer;
		communalPile = new Deck(deck.getCategory());
		board = new Deck(deck.getCategory());
		players = new LinkedList<Player>();
		allPlayer = new LinkedList<Player>();
		initPlayerDecks(deck);
		randomStart = true;
		isHumanFailed = false;
	}

	private Player choosePlayer() {
		return players.get(new Random().nextInt(players.size()));
	}

	public int chooseCategory(){return currentCategory=currentPlayer.chooseCategory();}

	private void initPlayerDecks(Deck deck) {
		Deck[] decks = deck.dealCard(numOfPlayer);
		human = new Player(decks[0],true, 0);
		players.add(human);
		allPlayer.add(human);
		for(int i = 1; i < numOfPlayer; i++){
			Player player = new Player(decks[i], false, i);
			players.add(player);
			allPlayer.add(player);
		}
	}


	public void startRound(){
		totalRounds++;
		if(randomStart) {
			currentPlayer = choosePlayer();
		}
		Iterator<Player> it = players.iterator();
		while (it.hasNext()){
			Player player = it.next();
			player.draw();
			board.addCard(player.getHand());
		}
	}

	public Player checkRoundResult() {
		boolean draw = false;
		Iterator<Player> it = players.iterator();
		Player player = it.next();
		int maxValue = player.getHand().getValue(currentCategory);
		Player winner = player;
		while (it.hasNext()){
			player = it.next();
			int tmp = player.getHand().getValue(currentCategory);
			if (maxValue < tmp){
				maxValue = tmp;
				winner = player;
				draw = false;
			}
			else if (maxValue == tmp){draw = true;}
		}
		if(draw){
			totalDraws++;
			randomStart = true;
			transferCard(board, communalPile);
			this.winner = null;
			return null;
		}
		else {
			winner.wonRound();
			randomStart = false;
			currentPlayer = winner;
			transferCard(board,winner.getDeck());
			transferCard(communalPile,winner.getDeck());
			this.winner = winner;
			return winner;
		}
	}

	public boolean checkGameEnd() {
		Iterator<Player> it = players.iterator();
		while (it.hasNext()){
			Player player = it.next();
			if(player.getDeck().isEmpty()){
				if(player.equals(human)){isHumanFailed = true;}
				it.remove();
			}
		}
		if(players.size()<=1){
			return true;
		}
		return false;
	}

	public void transferCard(Deck from, Deck to) {
		to.addDeck(from);
		from.removeAll();
	}

	public Player getHumanPlayer() {
		return human;
	}

	public Deck getCommunalPile(){
		return communalPile;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	@Override
	public int getRounds() {
		return totalRounds;
	}

	@Override
	public int getDraws() {
		return totalDraws;
	}

	public Player getPlayer(int i){
		return players.get(i);
	}

	public LinkedList<Player> getPlayers(){
		return players;
	}

	public int getNumOfPlayers() {
		return numOfPlayer;
	}

	public boolean isHumanFailed() {
		return isHumanFailed;
	}

	@Override
	public boolean isHumanWon(){
		return !isHumanFailed;
	}

	public int getCurrentCategory(){
		return currentCategory;
	}

	public void setCurrentCategory(int currentCategory){
		this.currentCategory = currentCategory;
	}

	public Player getWinner(){
	    return winner;
    }

    public LinkedList<Player> getAllPlayer() {
		return allPlayer;
	}
}
