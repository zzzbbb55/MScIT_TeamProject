package game;

import dao.CardModel;

public class Card implements CardModel{

    private String title;
    private int[] attribute;

    public Card(String title, int[] attribute){
        this.title = title;
        this.attribute = attribute;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int[] getAttribute() {
        return attribute;
    }

    public void setAttribute(int[] attribute) {
        this.attribute = attribute;
    }

	public String toString(){
        String out = String.format("%16s",title);
        for(int i = 0; i < attribute.length; i++){
            out +="\t"+String.format("%10d",attribute[i]);
        }
		return out;
	}

	public int getValue(int index){
		return attribute[index];
	}

}



