package zad7_kindergarden;
import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class KindergartenShow {

    static Semaphore bina;
    static int count;
    static Semaphore lock;
    static Semaphore canPlay;
    static int total;
    static Semaphore newCycle;
    public static void init() {
        bina=new Semaphore(6);
        count=0;
        lock=new Semaphore(1);
        canPlay=new Semaphore(0);
        total=0;
        newCycle=new Semaphore(0);
    }

    public static class Child extends TemplateThread {

        public Child(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            bina.acquire();

            state.participantEnter();
            lock.acquire();
            count++;
            if(count==6){
                canPlay.release(6);
            }
            lock.release();
            canPlay.acquire();
            state.present();
            lock.acquire();
            count--;
            total++;
            if(count==0){
                total+=6;
                state.endGroup();
                bina.release(6);
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

    static KindergartenShowState state = new KindergartenShowState();

    public static void run() {
        try {
            int numRuns = 24;
            int numIterations = 24;
            numExecutions = 0;
            sumPermits = 0;
            sumQueue = 0;

            HashSet<Thread> threads = new HashSet<Thread>();

            for (int i = 0; i < numIterations; i++) {
                Child c = new Child(numRuns);
                threads.add(c);
            }

            init();

            ProblemExecution.start(threads, state);
            System.out.println(((double) sumPermits) / numExecutions);
            System.out.println(((double) sumQueue) / numExecutions);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}