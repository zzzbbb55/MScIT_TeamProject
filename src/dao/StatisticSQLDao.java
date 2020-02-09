package dao;

import com.sun.org.glassfish.external.statistics.Statistic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

public class StatisticSQLDao {
    private String url;
    private String username;
    private String password;
    private Connection conn;
    private String driverClass;
    public StatisticSQLDao(String driverClass, String url, String username, String password){
        this.driverClass = driverClass;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void close(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initialize(){
        try {
            Class.forName(driverClass);
            conn = DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void writeGame(GameModel game){
        String sql = "insert into game(humanwon,draws,rounds)(values(?,?,?));";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1,game.isHumanWon());
            ps.setInt(2,game.getDraws());
            ps.setInt(3,game.getRounds());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void writePlayer(PlayerModel player){
        String sql = "insert into player(ishuman,roundswon) (values(?,?));";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1,player.isHuman());
            ps.setInt(2,player.getRoundsWon());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Statistc readStatistic(){
        String sql1 = "select count(id), avg(draws), max(rounds) from game;";
        String sql2 = "select sum(roundswon) from player where ishuman=true;";
        String sql3 = "select sum(roundswon) from player where ishuman=false;";
        Statistc stat = null;
        try {
            Statement s1 = conn.createStatement();
            Statement s2 = conn.createStatement();
            Statement s3 = conn.createStatement();
            ResultSet r1 = s1.executeQuery(sql1);
            ResultSet r2 = s2.executeQuery(sql2);
            ResultSet r3 = s3.executeQuery(sql3);
            r1.next();r2.next();r3.next();
            stat = new Statistc(r1.getInt(1),r2.getInt(1),r3.getInt(1),r1.getDouble(2),r1.getInt(3));
            r1.close();s1.close();
            r2.close();s2.close();
            r3.close();s3.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stat;
    }
}
