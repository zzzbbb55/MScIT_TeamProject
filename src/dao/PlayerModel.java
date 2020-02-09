package dao;

public interface PlayerModel {
    /**
     *Player access method for Statistic DAO.
     */
    public boolean isHuman();
    public int getRoundsWon();
}
