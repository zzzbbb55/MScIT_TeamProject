package commandline;


import dao.DeckTextDao;
import dao.StatisticSQLDao;
import game.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Top Trumps command line application
 */
public class TopTrumpsCLIApplication {

	/**
	 * This main method is called by TopTrumps.java when the user specifies that they want to run in
	 * command line mode. The contents of args[0] is whether we should write game logs to a file.
 	 * @param args
	 */
	public static void main(String[] args) {

		boolean writeGameLogsToFile = false; // Should we write game logs to file?
		if (args[0].equalsIgnoreCase("true")) writeGameLogsToFile=true; // Command line selection
		
		// State
		boolean userWantsToQuit = false; // flag to check whether the user wants to quit the application

		Log log = new Log(writeGameLogsToFile);

		try {
			log.initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Loop until the user wants to exit the game
		while(!userWantsToQuit) {
			Deck deck = new Deck();
			DeckTextDao deckDao = new DeckTextDao("StarCitizenDeck.txt");
			Scanner sc = new Scanner(System.in);
			deckDao.initialize();
			deckDao.readDeck(deck);
			deckDao.close();

			StatisticSQLDao statisticSQLDao = new StatisticSQLDao("org.postgresql.Driver","jdbc:postgresql://52.24.215.108:5432/newTeamCoolName","newTeamCoolName","newTeamCoolName");

			//For assessment testing: The contents of the complete deck once it has been read in

			log.write("Cards in the deck:");
			log.write(deck.toString());



			deck.shuffle();
			//For assessment testing: The contents of the complete deck when it has been shuffled

			log.write("Shuffle the deck:");
			log.write(deck.toString());



			Game game = new Game(deck,5);
			boolean nextRound = true;
			do {
				game.startRound();
				System.out.println("Rounds: " + game.getRounds());
				System.out.println("------------------------------------------");
				log.write("Rounds: " + game.getRounds());

				//For assessment testing:The contents of the current cards in play
				// the cards from the top of the user��s deck and the computer��s deck(s)
				System.out.println("Deck size: " + game.getHumanPlayer().getDeck().getSize());
				System.out.println("Cards in the your hand:");
				System.out.println(game.getHumanPlayer().getDeck().categoryToString());
				System.out.println(game.getHumanPlayer().getHand().toString());
				System.out.println("------------------------------------------");
				log.write("Deck size: " + game.getHumanPlayer().getDeck().getSize());
				log.write("Cards in the your hand: " + game.getHumanPlayer().getHand().toString());


				int ca;
				if (game.getCurrentPlayer().isHuman()) {
					System.out.println("Choose a category: ");
					ca = sc.nextInt();
					sc.nextLine();
				} else {
					ca = game.getCurrentPlayer().chooseCategory();
				}

				System.out.println("Player " + game.getCurrentPlayer().getId() + " chose category " + game.getCurrentPlayer().getDeck().getCategory()[ca]);
				log.write("Player " + game.getCurrentPlayer().getId() + " chose category " + game.getCurrentPlayer().getDeck().getCategory()[ca]);
				System.out.println("------------------------------------------");
				game.setCurrentCategory(ca);

				Player winner = game.checkRoundResult();

				if (winner == null) {
					System.out.println("Draw");
					log.write("Draw");
				} else {
					System.out.println("Winner is Player " + winner.getId());
					log.write("Winner is Player " + winner.getId());
				}
				System.out.println("------------------------------------------");

				Iterator<Player> it = game.getPlayers().iterator();
				System.out.println("Display all cards: ");
				log.write("Display all cards: ");
				System.out.println("           " + game.getCurrentPlayer().getDeck().categoryToString());
				log.write("           " + game.getCurrentPlayer().getDeck().categoryToString());
				while (it.hasNext()) {
					Player player = it.next();
					System.out.println("Player " + player.getId() + ":  " + player.getHand());
					log.write("Player " + player.getId() + ":  " + player.getHand());
				}
				System.out.println("------------------------------------------");
				String pass = "";
				boolean end = game.checkGameEnd();
				if(game.isHumanFailed()){
					System.out.println("You failed");
					log.write("Player has failed");
				}
				if (end){
					if (game.isHumanWon()){
						System.out.println("You win");
						log.write("Player 1 has won");
					}
					else {
						System.out.println("Player "+game.getWinner().getId()+" Win");
						log.write("Player "+game.getWinner().getId()+" Win");
					}
					break;
				}
				do {
					System.out.println("Next Round: (y/n)");
					pass = sc.nextLine();
					if (pass.equals("y")) {
						nextRound = true;
						log.write("Next Round");
						break;
					} else if (pass.equals("n")) {
						nextRound = false;
						log.write("Stop Game");
						break;
					}
				} while (true);
			} while (nextRound);

			statisticSQLDao.initialize();
			statisticSQLDao.writeGame(game);
			LinkedList<Player> players =game.getAllPlayer();
			Iterator<Player> it = players.iterator();
			while(it.hasNext()){
				statisticSQLDao.writePlayer(it.next());
			}
			statisticSQLDao.close();
			log.write("Game Statistic Saved");

			String quit = "";
			do {
				System.out.println("Quit? : (y/n)");
				quit = sc.nextLine();
				if (quit.equals("y")) {
					userWantsToQuit= true;
					log.write("Next Game");
					break;
				} else if (quit.equals("n")) {
					userWantsToQuit = false;
					log.write("Quit Game");
					break;
				}
			} while (true);

		}
		try {
			log.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
