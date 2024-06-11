package zad8_carneval;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class CarnivalSolution {

    static Semaphore bina;
    static int counter;
    static Semaphore lock;
    static Semaphore canPlay;
    static int total;
    static Semaphore newCycle;

    public static void init() {
        bina=new Semaphore(10);
        lock=new Semaphore(1);
        counter=0;
        canPlay=new Semaphore(0);
        total=0;
        newCycle=new Semaphore(0);
    }

    public static class Participant extends TemplateThread {

        public Participant(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            bina.acquire();
            state.participantEnter();
            lock.acquire();
            counter++;
            if(counter==10){
                canPlay.release(10);
            }
            lock.release();

            canPlay.acquire();
            state.present();
            lock.acquire();
            counter--;

            if(counter==0){
                total+=10;
                state.endGroup();
                bina.release(10);
            }
            if(total==24){
                state.endCycle();
                newCycle.release(24);
                total=0;
            }
            lock.release();
            newCycle.acquire();
            bina.release();

        }

    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            run();
        }
    }

    static CarnivalState state = new CarnivalState();

    public static void run() {
        try {
            int numRuns = 15;
            int numThreads = 30;

            HashSet<Thread> threads = new HashSet<Thread>();

            for (int i = 0; i < numThreads; i++) {
                Participant c = new Participant(numRuns);
                threads.add(c);
            }

            init();

            ProblemExecution.start(threads, state);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}