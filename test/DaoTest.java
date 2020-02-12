import dao.DeckTextDao;
import dao.Statistic;
import dao.StatisticSQLDao;
import game.Deck;

public class DaoTest {
    public static void main(String[] args){
        DeckTextDao dao = new DeckTextDao("StarCitizenDeck.txt");
        Deck deck = new Deck();
        try{
            dao.initialize();
            deck = (Deck) dao.readDeck(deck);
        }catch(Exception e){
            e.printStackTrace();
        }

        StatisticSQLDao dao1 = new StatisticSQLDao("org.postgresql.Driver","jdbc:postgresql://localhost:5432/TopTrumps","postgres","postgres");
        dao1.initialize();
        Statistic statistic = dao1.readStatistic();
        dao1.close();
        System.out.println(statistic.getOverallPlayed());
        System.out.println(statistic.getCompWons());
        System.out.println(statistic.getHumanWons());
        System.out.println(statistic.getAvgDraws());
        System.out.println(statistic.getMaxRounds());
    }
}
