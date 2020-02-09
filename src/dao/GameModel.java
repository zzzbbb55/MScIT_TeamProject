package dao;

public interface GameModel {
    /**
     *Game access method for Statistic DAO.
     */

    public boolean isHumanWon();
    public int getDraws();
    public int getRounds();
}
