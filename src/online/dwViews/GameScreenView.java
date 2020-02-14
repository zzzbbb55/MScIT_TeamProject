package online.dwViews;

import game.Game;
import game.Player;
import io.dropwizard.views.View;

import java.util.LinkedList;

/**
 * Each HTML page that is specified in GameWebPagesResource first needs a class that extends
 * View, which is Dropwizard's internal representation of the page. This then points to a
 * separate file found in the resource directory that contains the actual HTML/Javascript.
 * 
 * The HTML/Javascript page for this View can be found in resources/dwViews/GameScreen.ftl
 * 
 * You do not need to edit this class. You will need to complete GameScreen.ftl.
 * 
 * Note: The HTML/Javascript file is actially a freemarker file (https://freemarker.apache.org/),
 * however we do not expect you to use the additional functionality that freemarker provides.
 */
public class GameScreenView extends View {
    private boolean[] cardDisplay;
    private boolean[] btnDisplay;

    private String[] dropBtn;

    private String roundProgress;
    private String currentPlayer;
    private String categorySelection;
    private String winMessage;


	/**
	 * Simple Constructor method, it simply specifies where the HTML page is to return.
	 */
    public GameScreenView(Game game)  {
        super("GameScreen.ftl");
        btnDisplay = new boolean[5];
        dropBtn = new String[5];
        cardDisplay = new boolean[5];
    }

    // getters and setters

    public boolean[] getCardDisplay() {
        return cardDisplay;
    }

    public void setCardDisplay(boolean[] cardDisplay) {
        this.cardDisplay = cardDisplay;
    }

    public boolean[] getBtnDisplay() {
        return btnDisplay;
    }

    public void setBtnDisplay(boolean[] btnDisplay) {
        this.btnDisplay = btnDisplay;
    }

    public String[] getDropBtn() {
        return dropBtn;
    }

    public void setDropBtn(String[] dropBtn) {
        this.dropBtn = dropBtn;
    }

    public String getRoundProgress() {
        return roundProgress;
    }

    public void setRoundProgress(String roundProgress) {
        this.roundProgress = roundProgress;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getCategorySelection() {
        return categorySelection;
    }

    public void setCategorySelection(String categorySelection) {
        this.categorySelection = categorySelection;
    }

    public String getWinMessage() {
        return winMessage;
    }

    public void setWinMessage(String winMessage) {
        this.winMessage = winMessage;
    }

    public boolean getCardDisplay(int index){
        return cardDisplay[index];
    }

    public void setCardDisplay(int index,boolean b){
        cardDisplay[index]= b;
    }

    public boolean getBtnDisplay(int index){
        return btnDisplay[index];
    }

    public void setBtnDisplay(int index, boolean b){
        btnDisplay[index]= b;
    }

}
