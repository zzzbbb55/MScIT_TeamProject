package online.dwResources;

import dao.Statistc;
import game.Card;

import game.Game;
import game.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class DisplaySource {
    //btn display
    private boolean[] isDisplayBtns;

    //btn content
    private String[] dropbtn;
    //game content
    private String roundProgress;
    private String currentPlayer;
    private String categorySelection;
    private String winMessage;
    private int round;
    //player & card
    private String[] playerName;
    private boolean[] isDisplayCard;
    private LinkedList<Player> players;
    private Game game;

    public DisplaySource(){}

    // methods
    public void initGameScreen(Game game){
        this.players = game.getPlayers();
        this.playerName = new String[game.getNumOfPlayers()];
        playerName[0] = "You";
        for(int i=1; i<game.getNumOfPlayers(); i++) {
            playerName[i] = "AIPlayer " + i;
        }
        isDisplayBtns = new boolean[5];
        isDisplayCard = new boolean[game.getNumOfPlayers()];
        //dropbtn = new String[]
        //this.round=game.getRounds();
    }

    //public void initStatisticScreen(Statistc stat){ }

    public Map<String, Object> pack(){
        Map<String, Object> root = new HashMap<>();
        root.put("isDisplayDropBotton",isDisplayBtns[0]);
        root.put("isDisplayBtnShowBoard",isDisplayBtns[1]);
        root.put("isDisplayBtnShowWinner",isDisplayBtns[2]);
        root.put("isDisplayBtnNextRound",isDisplayBtns[3]);
        root.put("isDisplayBtnGameOver",isDisplayBtns[4]);
        //for(int k=0;k<;k++){root.put("dropbtn"+k,dropbtn[k])}
        root.put("roundProgress", roundProgress);
        root.put("currentPlayer", currentPlayer);
        root.put("categorySelection",categorySelection);
        root.put("winMessage",winMessage);
        root.put("round",round);
        root.put("",);
        // cards. named as "card"+index
        Map<String,Object>[] cardPlayer = new HashMap[players.size()];
        for(int i=0;i<players.size();i++){
            Card card = players.get(i).getHand();
            cardPlayer[i] = new HashMap();
            cardPlayer[i].put("isDisplay",isDisplayCard[i]);
            cardPlayer[i].put("playerName",playerName[i]);
            cardPlayer[i].put("cardTitle",card.getTitle());
            //cardPlayer[i].put("cardImage",card.getImageUrl());
            for(int j=0; j<card.getAttribute().length; j++){
                //cardPlayer[i].put("AttributeName"+j,attributeNames[j]);
                cardPlayer[i].put("AttributeValue"+j,card.getValue(j));
            }
            cardPlayer[i].put("playerDeckSize",players.get(i).getDeck().getSize());
            root.put("card"+i,cardPlayer[i]);
        }
        return root;
    }

    public void setStatus(int i){
        for(int j=0;j<5;j++){isDisplayBtns[j]=false;}
        for(int k=0;k<game.getNumOfPlayers();k++){isDisplayCard[k]=false;}
        isDisplayBtns[i]=true;
        round =game.getRounds();
        if(i==0){
            roundProgress="Round: "+round+". Waiting on you to a category";
            currentPlayer="Active player is "+playerName[game.getCurrentPlayer().getId()];
            categorySelection="";
            winMessage="";
            isDisplayCard[0]=true;
        }else if(i==1){
            roundProgress="Round: "+round+". Player have drawn their cards";
            currentPlayer="Active player is "+playerName[game.getCurrentPlayer().getId()];
            categorySelection="";
            winMessage="";
            isDisplayCard[0]=true;
        }else if(i==2){
            roundProgress="Round: "+round+". "+playerName[game.getCurrentPlayer().getId()]+" have made the selection";
            currentPlayer="Active player is "+playerName[game.getCurrentPlayer().getId()];
            categorySelection=playerName[game.getCurrentPlayer().getId()]+" selected ";
            winMessage="";
            for(int k=0;k<game.getNumOfPlayers();k++){isDisplayCard[k]=true;}
        }else if(i==3){
            roundProgress= "Round: "+round+". "+game.getRoundWinner()+" wins this round.";
            currentPlayer="";
            categorySelection="";
            winMessage="";
        }else{
            roundProgress="Round: "+round+". "+game.getRoundWinner()+" wins this round.";
            currentPlayer="Game Over";
            categorySelection="";
            winMessage=game.getRoundWinner()+" wins";
            for(int k=0;k<game.getNumOfPlayers();k++){isDisplayCard[k]=true;}
        }
    }
    // getters & setters
}
