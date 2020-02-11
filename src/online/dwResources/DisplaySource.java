package online.dwResources;

import game.Card;

import game.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class DisplaySource {

    private String btnDisplaySelsction;
    private LinkedList dropBotton;
    private String btnShowWinner;
    private String btnNextRound;

    private String roundProgress;
    private String currentPlayer;
    private String categorySelection;
    private String winMessage;
    private int round;

    private Card[] card;
    private LinkedList<Player> players;

    public DisplaySource(){

    }

    public HashMap<String, Object> pack(){
        Map root = new HashMap();
        root.put(roundProgress, )
        return root;
    }

    public void setStatus(int i){
        if(i==1){

        }else if(i==2){

        }else if(i==3){

        }else if(i==4){

        }else{

        }
    }

    public void setPlayers(LinkedList<Player> players){
        this.players = players;
    }


}
