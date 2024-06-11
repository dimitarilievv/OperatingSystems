package zad6_gym;

import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

public class GymSolution {
    static Semaphore soblekuvalna;
    static int counter;
    static int total;
    static Semaphore lock;
    static Semaphore canPlay;
    static Semaphore newCycle;
    public static void init() {
        soblekuvalna=new Semaphore(4);
        counter=0;
        total=0;
        lock=new Semaphore(1);
        canPlay=new Semaphore(0);
        newCycle=new Semaphore(0);
    }

    static int i = 0;

    public static class Player extends TemplateThread {

        public Player(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            soblekuvalna.acquire();
            state.presobleci();
            lock.acquire();
            counter++;
            if(counter==4){
                canPlay.release(4);
            }
            lock.release();
            canPlay.acquire();


            state.sportuvaj();
            lock.acquire();
            counter--;
            if(counter==0){
                total+=4;
                soblekuvalna.release(4);
            }
            if(total==12)
            {
                state.slobodnaSala();
                total=0;
            }
            lock.release();
            newCycle.acquire();
            soblekuvalna.release();
        }

    }

    static GymState state = new GymState();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            run();
        }
    }

    public static void run() {
        try {
            Scanner s = new Scanner(System.in);
            int numRuns = 1;
            int numIterations = 1200;
            s.close();

            HashSet<Thread> threads = new HashSet<Thread>();

            for (int i = 0; i < numIterations; i++) {
                Player h = new Player(numRuns);
                threads.add(h);
            }

            init();

            ProblemExecution.start(threads, state);
            System.out.println(new Date().getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}