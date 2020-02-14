package online.dwResources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
//import javax.ws.rs.core.MediaType;

import dao.DaoFactory;
import dao.DeckTextDao;
import game.*;
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
	Game game;
	GameScreenView gameScreenView;
	LinkedList<Player> players;
	StatisticsView statisticsView;
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
	public GameScreenView newGame() {
		DaoFactory daoFactory =conf.getDao();
		DeckTextDao deckDao =daoFactory.getDeckTextDao();
		deckDao.initialize();
		Deck deck = new Deck();
		deckDao.readDeck(deck);
		deckDao.close();
		deck.shuffle();
		game = new Game(deck, (conf.getNumAIPlayers()+1));
        players = new LinkedList<Player>();

		//view.initGameScreen(game);
		gameScreenView = new GameScreenView();
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


		return gameScreenView;
	}

	@GET
	@Path("/displayAISelection")
	public GameScreenView displayAISelection(){
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
	public GameScreenView toSelectCategory(@QueryParam("select") int index){
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
	public GameScreenView showWinner(){
		game.checkRoundResult();
		gameScreenView.setCategorySelection("");
		for(int j=0;j<5;j++){ gameScreenView.setBtnDisplay(j,false); }
		if(game.checkGameEnd() || game.isHumanFailed()) {
			//view.setStatus(4);
			gameScreenView.setRoundProgress("Round: " + game.getRounds() + ". " + game.getWinner().getPlayerName() + " wins this round.");
			gameScreenView.setCurrentPlayer("Game Over.");
			gameScreenView.setCategorySelection(game.getWinner().getPlayerName() + " wins the game!");

			gameScreenView.setBtnDisplay(4,true);
			players.clear();
			players.addAll(game.getPlayers());
			gameScreenView.setPlayers(players);
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
	public GameScreenView nextRound(){
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
	@Path("/helloJSONList")
	/**
	 * Here is an example of a simple REST get request that returns a String.
	 * We also illustrate here how we can convert Java objects to JSON strings.
	 * @return - List of words as JSON
	 * @throws IOException
	 */
	public String helloJSONList() throws IOException {
		
		List<String> listOfWords = new ArrayList<String>();
		listOfWords.add("Hello");
		listOfWords.add("World!");
		
		// We can turn arbatory Java objects directly into JSON strings using
		// Jackson seralization, assuming that the Java objects are not too complex.
		String listAsJSONString = oWriter.writeValueAsString(listOfWords);
		
		return listAsJSONString;
	}
	
	@GET
	@Path("/helloWord")
	/**
	 * Here is an example of how to read parameters provided in an HTML Get request.
	 * @param Word - A word
	 * @return - A String
	 * @throws IOException
	 */
	public String helloWord(@QueryParam("Word") String Word) throws IOException {
		return "Hello "+Word;
	}
	
}
