package game;

import dao.CardModel;
import dao.DeckModel;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;


public class Deck implements DeckModel {

	private LinkedList<Card> deck;
	private String[] category;

	public Deck(){
		deck = new LinkedList<Card>();
	}

	public Deck(String[] category){
		this.category = category;
		deck = new LinkedList<Card>();
	}


	@Override
	public void addCard(CardModel card){
		addCardTop((Card) card);
	}

	public void addCardTop(Card card){
		deck.addFirst(card);
	}

	public void addCardBottom(Card card){
		deck.addLast(card);
	}

	public LinkedList<Card> getDeck(){
		return deck;
	}

	public void removeAll(){
		deck = new LinkedList<Card>();
	}

	public void addDeck(Deck deck){
		this.deck.addAll(deck.getDeck());
	}

	public void shuffle(){
		Collections.shuffle(deck);
	}

	public Card getTopCard(){
		return deck.pollFirst();
	}

	public Deck[] dealCard(int num){
		Deck[] decks = new Deck[num];
		for (int i = 0; i < num; i++){
			decks[i] = new Deck(category);
		}
		int i = 0;
		while(!this.deck.isEmpty()){
			decks[i].addCardTop(this.getTopCard());
			i++;
			if (i == num){i = 0;}
		}
		return decks;
	}

	public Boolean isEmpty(){
		return deck.isEmpty();
	}
	
	public int getSize(){
		return deck.size();
	}

	public String getCategoryName(int index) {
		return category[index];
	}

	public String[] getCategory() {
		return category;
	}

	@Override
	public void setCategory(String[] category) { this.category = category;}

	public String toString() {
		String out = "";
		Iterator<Card> it = deck.iterator();
		while(it.hasNext()){
			out += it.next().getTitle();
		}
		return out;
	}

	public String categoryToString(){
		String out = String.format("%16s","Description");
		for (int i = 0; i < category.length; i++){
			out += "\t" + String.format("%10s",category[i]);
		}
		return out;
	}
}