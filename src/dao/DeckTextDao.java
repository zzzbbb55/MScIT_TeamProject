package dao;

import game.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class DeckTextDao{
    String filename;
    FileReader fileReader;
    public DeckTextDao(String filename){
        this.filename = filename;
    }

    public void initialize() {
        try {
            this.fileReader = new FileReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public DeckModel readDeck(DeckModel deck) {
        Scanner scanner = new Scanner(fileReader);
        deck.setCategory(readCategory(scanner));
        while(scanner.hasNextLine()){
            deck.addCard(readCard(scanner));
        }
        return deck;
    }
    private String[] readCategory(Scanner scanner){
        String[] spl = scanner.nextLine().split(" ");
        String[] cate = new String[spl.length-1];
        System.arraycopy(spl,1,cate ,0, spl.length-1);
        return cate;
    }
    private CardModel readCard(Scanner scanner){
        String name = scanner.next();
        int[] values = new int[5];
        values[0] = scanner.nextInt();
        values[1] = scanner.nextInt();
        values[2] = scanner.nextInt();
        values[3] = scanner.nextInt();
        values[4] = scanner.nextInt();
        if(scanner.hasNextLine()){
            scanner.nextLine();
        }
        CardModel card = new Card(name, values);
        return card;
    }


    public void close() {
        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
