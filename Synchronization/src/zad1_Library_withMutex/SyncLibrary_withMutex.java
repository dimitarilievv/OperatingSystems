package zad1_Library_withMutex;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncLibrary_withMutex {
    List<String> books=new ArrayList<>();
    int capacity;
    public static Lock lock = new ReentrantLock();
    public SyncLibrary_withMutex(int capacity) {
        this.capacity = capacity;
    }
    //clenot vrati kniga vo bibliotekata
    public void returnBook(String book) throws InterruptedException {
        while (true){
            lock.lock();
            if(books.size()<capacity){
                books.add(book);
                lock.unlock();
                break;
            }
            lock.unlock();
        }

    }

    //clenot pozajmi kniga od bibliotekata
    public String borrowBook() throws InterruptedException {
        String book = "";
        while(true){
            lock.lock();
            if(!books.isEmpty()){
                book =  books.remove(0);
                lock.unlock();
                break;
            }
            lock.unlock();//ako e prazno predaj go klucot na nekoj dr
        }
        return book;
    }
}
