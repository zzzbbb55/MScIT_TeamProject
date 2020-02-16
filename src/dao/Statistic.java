package dao;

public class Statistic {
    /**
     *  Statistic data object for statistic view.
     */

    int overallPlayed;
    int compWons;
    int humanWons;
    double avgDraws;
    int maxRounds;

    public Statistic(int overallPlayed, int humanWons, int compWons, double avgDraws, int maxRounds) {
        this.overallPlayed = overallPlayed;
        this.compWons = compWons;
        this.humanWons = humanWons;
        this.avgDraws = avgDraws;
        this.maxRounds = maxRounds;
    }

    public int getOverallPlayed() {
        return overallPlayed;
    }

    public void setOverallPlayed(int overallPlayed) {
        this.overallPlayed = overallPlayed;
    }

    public int getCompWons() {
        return compWons;
    }

    public void setCompWons(int compWons) {
        this.compWons = compWons;
    }

    public int getHumanWons() {
        return humanWons;
    }

    public void setHumanWons(int humanWons) {
        this.humanWons = humanWons;
    }

    public double getAvgDraws() {
        return avgDraws;
    }

    public void setAvgDraws(double avgDraws) {
        this.avgDraws = avgDraws;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }
}
