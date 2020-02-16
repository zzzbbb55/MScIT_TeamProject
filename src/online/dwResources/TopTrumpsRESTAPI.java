package online.dwResources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//import javax.ws.rs.Consumes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;

import dao.DaoFactory;
import dao.DeckTextDao;
import dao.Statistic;
import dao.StatisticSQLDao;
import game.*;
import io.dropwizard.jersey.sessions.Session;
import online.configuration.TopTrumpsJSONConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import online.dwViews.GameScreenView;
import online.dwViews.StatisticsView;

@Path("/toptrumps") // Resources specified here should be hosted at http://localhost:7777/toptrumps
//@Produces(MediaType.APPLICATION_JSON) // This resource returns JSON content
//@Consumes(MediaType.APPLICATION_JSON) // This resource can take JSON content as input
/**
 * This is a Dropwizard Resource that specifies what to provide when a user
 * requests a particular URL. In this case, the URLs are associated to the
 * different REST API methods that you will need to expose the game commands
 * to the Web page.
 * 
 * Below are provided some sample methods that illustrate how to create
 * REST API methods in Dropwizard. You will need to replace these with
 * methods that allow a TopTrumps game to be controlled from a Web page.
 */
public class TopTrumpsRESTAPI {


	/** A Jackson Object writer. It allows us to turn Java objects
	 * into JSON strings easily. */
	ObjectWriter oWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
	TopTrumpsJSONConfiguration conf;
	/**
	 * Constructor method for the REST API. This is called first. It provides
	 * a TopTrumpsJSONConfiguration from which you can get the location of
	 * the deck file and the number of AI players.
	 * @param conf
	 */
	public TopTrumpsRESTAPI(TopTrumpsJSONConfiguration conf) {
		// Add relevant initialization here
		this.conf = conf;
        //view = new DisplaySource();

	}
	// Add relevant API methods here



    @GET
	@Path("/newGame")
	public GameScreenView newGame(@Session HttpSession session) {
		DaoFactory daoFactory =conf.getDao();
		DeckTextDao deckDao =daoFactory.getDeckTextDao();
		deckDao.initialize();
		Deck deck = new Deck();
		deckDao.readDeck(deck);
		deckDao.close();
		deck.shuffle();
		Game game = new Game(deck, (conf.getNumAIPlayers()+1));
        LinkedList<Player> players = new LinkedList<Player>();

		//view.initGameScreen(game);
		GameScreenView gameScreenView = new GameScreenView();
		gameScreenView.setDropBtn(deck.getCategory());

		game.startRound();
		for(int j=0;j<5;j++){ gameScreenView.setBtnDisplay(j,false); }
		players.clear(); players.add(game.getHumanPlayer());
		gameScreenView.setPlayers(players);
		if (game.getCurrentPlayer().isHuman()) {
			//view.setStatus(0);
			gameScreenView.setRoundProgress("Round: "+game.getRounds()+". Waiting on you to a category");
			gameScreenView.setBtnDisplay(1,true);
		}else{
            //view.setStatus(1);
			gameScreenView.setRoundProgress("Round: "+game.getRounds()+". Player have drawn their cards");
			gameScreenView.setBtnDisplay(0,true);
		}
		gameScreenView.setCurrentPlayer("Active player is "+game.getCurrentPlayer().getPlayerName());
		gameScreenView.setCategorySelection("");
		session.setAttribute("game",game);
		session.setAttribute("gameView",gameScreenView);
		session.setAttribute("players",players);

		return gameScreenView;
	}

	@GET
	@Path("/displayAISelection")
	public GameScreenView displayAISelection(@Session HttpSession session){

		Game game = (Game)session.getAttribute("game");
		LinkedList<Player> players = (LinkedList<Player>)session.getAttribute("players");
		GameScreenView gameScreenView = (GameScreenView)session.getAttribute("gameView");

		game.chooseCategory();

		//view.setStatus(2);
		for(int j=0;j<5;j++){ gameScreenView.setBtnDisplay(j,false); }
		players.clear();
		players.addAll(game.getPlayers());
		gameScreenView.setPlayers(players);
		gameScreenView.setRoundProgress("Round: "+game.getRounds()+". "+game.getCurrentPlayer().getPlayerName()+" have made the selection");
		gameScreenView.setCurrentPlayer("Active player is "+game.getCurrentPlayer().getPlayerName());
		gameScreenView.setCategorySelection(game.getCurrentPlayer().getPlayerName()+" selected "+game.getCurrentPlayer().getDeck().getCategory()[game.getCurrentCategory()]);

		gameScreenView.setBtnDisplay(2,true);

		//view.pack();
		return gameScreenView;
	}
	@GET
	@Path("/toSelectCategory")
	public GameScreenView toSelectCategory(@Session HttpSession session,@QueryParam("dropBtn") int index){

		Game game = (Game)session.getAttribute("game");
		LinkedList<Player> players = (LinkedList<Player>)session.getAttribute("players");
		GameScreenView gameScreenView = (GameScreenView)session.getAttribute("gameView");

		game.setCurrentCategory(index);
        //view.setStatus(2);
		for(int j=0;j<5;j++){ gameScreenView.setBtnDisplay(j,false); }
		players.clear();
		players.addAll(game.getPlayers());
		gameScreenView.setPlayers(players);
		gameScreenView.setRoundProgress("Round: "+game.getRounds()+". "+game.getCurrentPlayer().getPlayerName()+" have made the selection");
		gameScreenView.setCurrentPlayer("Active player is "+game.getCurrentPlayer().getPlayerName());
		gameScreenView.setCategorySelection(game.getCurrentPlayer().getPlayerName()+" selected "+game.getCurrentPlayer().getDeck().getCategory()[game.getCurrentCategory()]);

		gameScreenView.setBtnDisplay(2,true);

		return gameScreenView;
	}

	@GET
	@Path("/showWinner")
	public GameScreenView showWinner(@Session HttpSession session){

		Game game = (Game)session.getAttribute("game");
		LinkedList<Player> players = (LinkedList<Player>)session.getAttribute("players");
		GameScreenView gameScreenView = (GameScreenView)session.getAttribute("gameView");

		game.checkRoundResult();
		game.checkGameEnd();
		gameScreenView.setCategorySelection("");
		for(int j=0;j<5;j++){ gameScreenView.setBtnDisplay(j,false); }
		if( game.isHumanFailed()) {
			//view.setStatus(4);
			gameScreenView.setRoundProgress("Round: " + game.getRounds() + ". You lose the game.");
			gameScreenView.setCurrentPlayer("Game Over.");
			gameScreenView.setCategorySelection("So Sad.");
			gameScreenView.setBtnDisplay(4,true);
			players.clear();
			players.addAll(game.getPlayers());
			gameScreenView.setPlayers(players);

			StatisticSQLDao statisticSQLDao = conf.getDao().getStaticDao();
			statisticSQLDao.initialize();
			statisticSQLDao.writeGame(game);
			Iterator<Player> it = game.getAllPlayer().iterator();
			while(it.hasNext()){
				statisticSQLDao.writePlayer(it.next());
			}
			statisticSQLDao.close();
		}else if(game.checkGameEnd()){
			gameScreenView.setRoundProgress("Round: " + game.getRounds() + ". " + game.getPlayers().getFirst().getPlayerName() + " wins this round.");
			gameScreenView.setCurrentPlayer("Game Over.");
			gameScreenView.setCategorySelection(game.getPlayers().getFirst().getPlayerName() + " wins the game!");
			gameScreenView.setBtnDisplay(4,true);
			players.clear();
			players.addAll(game.getPlayers());
			gameScreenView.setPlayers(players);

			StatisticSQLDao statisticSQLDao = conf.getDao().getStaticDao();
			statisticSQLDao.initialize();
			statisticSQLDao.writeGame(game);
			Iterator<Player> it = game.getAllPlayer().iterator();
			while(it.hasNext()){
				statisticSQLDao.writePlayer(it.next());
			}
			statisticSQLDao.close();
		}else if (game.getWinner()==null){
			gameScreenView.setRoundProgress("Round: "+game.getRounds()+". Draw - cards sent to next round.");
			gameScreenView.setCurrentPlayer("");
			gameScreenView.setCategorySelection("");
			gameScreenView.setBtnDisplay(3,true);
			players.clear();
			gameScreenView.setPlayers(players);
		}else{
			//view.setStatus(3);
			gameScreenView.setRoundProgress("Round: "+game.getRounds()+". "+game.getWinner().getPlayerName()+" wins this round.");
			gameScreenView.setCurrentPlayer("");
			gameScreenView.setCategorySelection("");
			gameScreenView.setBtnDisplay(3,true);
			players.clear();
			gameScreenView.setPlayers(players);
		}
		//view.pack();
		return gameScreenView;
	}

	@GET
	@Path("/nextRound")
	public GameScreenView nextRound(@Session HttpSession session){
		Game game = (Game)session.getAttribute("game");
		LinkedList<Player> players = (LinkedList<Player>)session.getAttribute("players");
		GameScreenView gameScreenView = (GameScreenView)session.getAttribute("gameView");

		game.startRound();
		for(int j=0;j<5;j++){ gameScreenView.setBtnDisplay(j,false); }
		players.clear(); players.add(game.getHumanPlayer());
		gameScreenView.setPlayers(players);
		if (game.getCurrentPlayer().isHuman()) {
			//view.setStatus(0);
			gameScreenView.setRoundProgress("Round: "+game.getRounds()+". Waiting on you to a category");
			gameScreenView.setBtnDisplay(1,true);

		}else{
			//view.setStatus(1);
			gameScreenView.setRoundProgress("Round: "+game.getRounds()+". Player have drawn their cards");
			gameScreenView.setBtnDisplay(0,true);
		}
		gameScreenView.setCurrentPlayer("Active player is "+game.getCurrentPlayer().getPlayerName());
		gameScreenView.setCategorySelection("");
		return gameScreenView;
	}

	@GET
	@Path("/stats")
	public StatisticsView statisticsView(){
		StatisticSQLDao statisticSQLDao = conf.getDao().getStaticDao();
		statisticSQLDao.initialize();
		Statistic statistic = statisticSQLDao.readStatistic();
		statisticSQLDao.close();
		return new StatisticsView(statistic);
	}
}
