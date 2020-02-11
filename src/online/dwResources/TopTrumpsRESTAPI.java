package online.dwResources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import game.*;
import online.configuration.TopTrumpsJSONConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Path("/toptrumps") // Resources specified here should be hosted at http://localhost:7777/toptrumps
@Produces(MediaType.APPLICATION_JSON) // This resource returns JSON content
@Consumes(MediaType.APPLICATION_JSON) // This resource can take JSON content as input
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
	DisplaySource view;
	private int numOfPlayer;
	/**
	 * Constructor method for the REST API. This is called first. It provides
	 * a TopTrumpsJSONConfiguration from which you can get the location of
	 * the deck file and the number of AI players.
	 * @param conf
	 */
	public TopTrumpsRESTAPI(TopTrumpsJSONConfiguration conf) {
		// Add relevant initialization here
       this.numOfPlayer = conf.getNumAIPlayers()+1;
       view = new DisplaySource();
	}

	// Add relevant API methods here
    @GET
	@Path("/newGame")
	public void newGame() throws IOException{
		Deck deck = new Deck();
		deck.shuffle();
		game = new Game(deck, numOfPlayer);
		view.setPlayers(game.getPlayers());
		game.startRound();
		if (game.getCurrentPlayer().isHuman()) {
			view.setStatus(1);
		}else{
            view.setStatus(2);
		}
	}

	@GET
	@Path("/displayAISelection")
	public void displayAISelection(){
		game.chooseCategory();
		view.setStatus(3);
	}
	@GET
	@Path("/toSelectCategory")
	public void toSelectCategory(@QueryParam("00000000") int index){
        game.setcategory(index);
        view.setStatus(3);
	}

	@GET
	@Path("/showWinner")
	public void showWinner(){
		if(game.checkGameEnd() || game.isHumanFailed()){
			view.setStatus(5);
		}else{
			game.checkRoundResult();
			view.setStatus(4);
		}
	}
	@GET
	@Path("/nextRound")
	public void nextRound(){
        game.startRound();
		if (!game.getCurrentPlayer().isHuman()) {
			view.setStatus(1);
		}else{
			view.setStatus(2);
		}
	}


	@GET
	@Path("/returnToSelectionScreen")
	public void returnToSelectionScreen(){

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
0000000000000000000000000000000000000000000000000000000000