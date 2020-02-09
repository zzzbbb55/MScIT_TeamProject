package dao;

public interface DeckModel {
    /**
    *Deck access method for Deck DAO.
     */

    public void setCategory(String[] category);
    public void addCard(CardModel card);
}
