package commandline;


import dao.DeckTextDao;
import dao.Statistic;
import dao.StatisticSQLDao;
import game.*;
import org.eclipse.jetty.util.preventers.AppContextLeakPreventer;

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
			Scanner sc = new Scanner(System.in);


			DeckTextDao deckDao = new DeckTextDao("StarCitizenDeck.txt");

			StatisticSQLDao statisticSQLDao = new StatisticSQLDao("org.postgresql.Driver","jdbc:postgresql://52.24.215.108:5432/newTeamCoolName","newTeamCoolName","newTeamCoolName");


			System.out.println("New Game or Statistic? (n/s)");
			String in = sc.nextLine();

			if (in.equals("s")){
				statisticSQLDao.initialize();
				Statistic statistic = statisticSQLDao.readStatistic();
				statisticSQLDao.close();
				System.out.println("Total Games: "+statistic.getOverallPlayed());
				System.out.println("Total Human Wins: "+statistic.getHumanWons());
				System.out.println("Total Comps Wins: "+statistic.getCompWons());
				System.out.println("Average Draws: "+statistic.getAvgDraws());
				System.out.println("Max Rounds: "+statistic.getMaxRounds());
			}
			else if (in.equals("n")){
				Deck deck = new Deck();
				deckDao.initialize();
				deckDao.readDeck(deck);
				deckDao.close();

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

					log.write("Human Deck: ");
					log.write(game.getHumanPlayer().getDeck().toString());

					Iterator<Player> it = game.getAllPlayer().iterator();
					it.next();

					while (it.hasNext()){
						Player player = it.next();
						log.write("Player "+player.getId()+" Deck:");
						log.write(player.getDeck().toString());
					}

					log.write("Communal Pile: ");
					log.write(game.getCommunalPile().toString());

					game.startRound();
					System.out.println("Rounds: " + game.getRounds());
					System.out.println("------------------------------------------");
					log.write("Rounds: " + game.getRounds());

					//For assessment testing: The contents of the current cards in play
					// the cards from the top of the users deck and the computers deck(s)
					System.out.println("Deck size: " + game.getHumanPlayer().getDeck().getSize());
					System.out.println("Cards in the your hand:");
					System.out.println(game.getHumanPlayer().getDeck().categoryToString());
					System.out.println(game.getHumanPlayer().getHand().toString());
					System.out.println("------------------------------------------");
					log.write("Deck size: " + game.getHumanPlayer().getDeck().getSize());
					log.write("Cards in the your hand: " + game.getHumanPlayer().getHand().toString());


					int ca;
					if (game.getCurrentPlayer().isHuman()) {
						System.out.println("Choose a category from 0-4 : ");
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

					it = game.getPlayers().iterator();
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
						System.out.println("You have failed");
						log.write("Player has failed");
					}
					if (end){
						System.out.println("Game End");
						if (game.isHumanWon()){
							System.out.println("You win");
							log.write("Player 1 has won");
						}
						else if(game.getWinner() == null){
							System.out.println("Draw Game, No Winner");
							log.write("Game End at Draw");
						}
						else {
							System.out.println("Player "+game.getWinner().getId()+" Win");
							log.write("Player "+game.getWinner().getId()+" Win");
						}

						System.out.println("Game Score:");
						it = game.getAllPlayer().iterator();
						while(it.hasNext()){
							Player player = it.next();
							System.out.println(player.getPlayerName()+" has won : "+player.getRoundsWon()+" rounds");
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
			}

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
