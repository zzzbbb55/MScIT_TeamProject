package dao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DaoFactory {
    /**
     * Dao factory for building Deck DAO and Statistic DAO
     * imports dao configuration from TopTrumps.json.
     */

    String deckFile;
    String driverClass;
    String url;
    String username;
    String password;

    public DaoFactory(){}

    public DaoFactory(String decfile, String driverClass, String url, String username, String password) {
        this.deckFile = decfile;
        this.driverClass = driverClass;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @JsonProperty
    public String getDeckFile() {
        return deckFile;
    }

    @JsonProperty
    public void setDeckFile(String deckFile) {
        this.deckFile = deckFile;
    }

    @JsonProperty
    public String getDriverClass() {
        return driverClass;
    }

    @JsonProperty
    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    @JsonProperty
    public String getUrl() {
        return url;
    }

    @JsonProperty
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty
    public String getUsername() {
        return username;
    }

    @JsonProperty
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public DeckTextDao getDeckTextDao(){
        return new DeckTextDao(deckFile);
    }
    public StatisticSQLDao getStaticDao(){
        return new StatisticSQLDao(driverClass,url,username,password);
    }
}
