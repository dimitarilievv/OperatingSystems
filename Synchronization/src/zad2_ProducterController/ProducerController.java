package zad2_ProducterController;

import java.util.concurrent.Semaphore;

public class ProducerController {
    public static int NUM_RUN=50;
    static Semaphore accessBuffer;
    static Semaphore lock;
    static Semaphore canCheck;
    public static void init(){
        // TODO: inicijalizacija
        accessBuffer=new Semaphore(1);
        lock=new Semaphore(1);
        canCheck=new Semaphore(10);

    }
    public static class Buffer{
        public int numChecks=0;

        public void produce(){
            System.out.println("Producer is producing...");
        }
        public void check(){
            System.out.println("Controller is checking...");
        }
    }
    public static class Producer extends Thread{
        private final Buffer buffer;

        public Producer(Buffer buffer) {
            this.buffer = buffer;
        }
        public void execute() throws InterruptedException{
            // TODO: execution
            accessBuffer.acquire();
            this.buffer.produce();
            accessBuffer.release();
        }
        @Override
        public void run(){
            for (int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static class Controller extends Thread{
        private final Buffer buffer;

        public Controller(Buffer buffer) {
            this.buffer = buffer;
        }

        public void execute() throws InterruptedException{
            // TODO: execution
            lock.acquire(); //za zastita na numchecks promenl
            if(this.buffer.numChecks==0){
                accessBuffer.acquire();
                //ceka da bide otvoren semaforot
                //proveruva dali controllerot e PRV
            }
            this.buffer.numChecks++; //pocnal so proverki
            lock.release();

            canCheck.acquire(); //dali e nadminat br od 10aktivni kontroleri
            this.buffer.check();
            lock.acquire();
            this.buffer.numChecks--;
            if(this.buffer.numChecks==0){
                //proveruva dali controllerot e POSLEDEN
                accessBuffer.release();
            }
            lock.release();
            canCheck.release();


        }
        @Override
        public void run(){
            for (int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
