package zadNiski;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Execution into the main class");
        //glavna niska
        Incrementator incrementator1=new Incrementator();
       // zadNiski.Incrementator incrementator2=new zadNiski.Incrementator();

        ThreadClass t1=new ThreadClass("T1",incrementator1);
        ThreadClass t2=new ThreadClass("T2",incrementator1);
        t1.start(); //ke se izvrsuvaat naizmenicno t1 i t2
        t2.start();

        t1.join();  //se dodeka ne zavrse niskata t1 mainot da ceka //moze i brojka kolku
        t2.join();  //se dodeka ne zavrse niskata t2 mainot da ceka

        if(t1.isAlive() && t2.isAlive()){
            System.out.println("threads are still alive");
            t1.interrupt();
            t2.interrupt();
        }
       // System.out.println("end of program"); //ova ke se izvrsi prvo ako nema join a taka posl

        System.out.println(incrementator1.getCount());
     //   System.out.println(incrementator2.getCount());
    }
}
class Incrementator{
    private static int count=0;
    private static Lock lock= new ReentrantLock();
    public static void unsafeincrement() throws InterruptedException {
        count++;
        Thread.sleep(5);
    }
    public synchronized void safeIncrement(){
        count++;
//        synchronized (this){
//            count++;
//        }
    }
    public void safeClassIncrement(){ //na nivo na klasa safe opcija
        synchronized (Incrementator.class){
            count++;
        }

    }
    public void safeMutexIncrement(){
        lock.lock();
        count++;
        lock.unlock();
    }
    public static int getCount() {
        return count;
    }

}
class ThreadClass extends Thread{
    String name;
    Incrementator incrementator;
    public ThreadClass(String name,Incrementator incrementator){
        this.name=name;
        this.incrementator=incrementator;
    }
    @Override
    public void run() {
        //System.out.println("Execution into the Thread class");
        for (int i = 0; i < 20; i++) {
          //  System.out.println("Thread "+name+" "+i);
            incrementator.safeMutexIncrement();
        }
    }
}