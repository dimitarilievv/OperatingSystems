package zad1_Library_withSemaphores;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncLibraryWithSemaphores {
    List<String> books = new ArrayList<>();
    int capacity;
    Semaphore coordinator = new Semaphore(1);
    Semaphore returnBookSemaphore = new Semaphore(10);
    Semaphore borrowBookSemaphore = new Semaphore(10);

    public SyncLibraryWithSemaphores(int capacity) {
        this.capacity = capacity;
    }

    //clenot vrati kniga vo bibliotekata
    public void returnBook(String book) throws InterruptedException {
        returnBookSemaphore.acquire(); //m1 m2 m3 .. mn ke im dadi kluc
        coordinator.acquire(); //zaklucuvame kriticen domen
        while (books.size() == capacity) {
            coordinator.release();
            Thread.sleep(1000);
            coordinator.acquire();
        }
        books.add(book);
        coordinator.release(); //da go oslobodi kritciniot domen
        returnBookSemaphore.release();

    }

    //clenot pozajmi kniga od bibliotekata
    public String borrowBook() throws InterruptedException {
        borrowBookSemaphore.acquire();
        String book = "";
        while (books.isEmpty()) {
            coordinator.release();
            Thread.sleep(1000);
            coordinator.acquire();
        }
        book = books.remove(0);
        borrowBookSemaphore.release();
        return book;
    }
}