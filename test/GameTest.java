import dao.DeckTextDao;
import game.Deck;
import game.Game;
import game.Player;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;


public class GameTest {


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		Deck deck = new Deck();
		DeckTextDao deckDao = new DeckTextDao("StarCitizenDeck.txt");

		deckDao.initialize();
		deckDao.readDeck(deck);
		deckDao.close();

		deck.shuffle();
		Game game = new Game(deck, 5);

		do{
			game.startRound();

			if (!game.isHumanFailed()){
				System.out.println("Player Hand: "+game.getHumanPlayer().getHand());
			}

			int ca;
			if(game.getCurrentPlayer().isHuman()){
				ca = sc.nextInt();
			}
			else{ca = game.getCurrentPlayer().chooseCategory();}

			System.out.println("Choser: "+game.getCurrentPlayer().getId());
			System.out.println("Choose: "+ca);

			Player winner = game.checkRoundResult(ca);

			if (winner == null) {System.out.println("Draw");}
			else {
				System.out.println("Winner: " + winner.getId());
			}

			Iterator<Player> it = game.getPlayers().iterator();
			while(it.hasNext()){
				System.out.println(it.next().getHand());
			}

		}
		while (!game.checkGameEnd());


		

	  System.out.println();
	}

}
