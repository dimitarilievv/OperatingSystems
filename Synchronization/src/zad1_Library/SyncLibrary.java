package zad1_Library;

import java.util.ArrayList;
import java.util.List;

public class SyncLibrary {
    List<String> books=new ArrayList<>();
    int capacity;

    public SyncLibrary(int capacity) {
        this.capacity = capacity;
    }
    //clenot vrati kniga vo bibliotekata
    public synchronized void returnBook(String book) throws InterruptedException {
        while(books.size()==capacity){
            wait();
        }
        books.add(book);
        notifyAll(); //razbudi gi tie so cekaat z da pozajmat
    }

    //clenot pozajmi kniga od bibliotekata
    public synchronized String borrowBook() throws InterruptedException {
        String book = "";
        while(books.isEmpty()){
            wait();
        }
        book =  books.remove(0);
        notifyAll();
        return book;
    }
}
