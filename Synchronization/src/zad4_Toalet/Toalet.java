package zad4_Toalet;

import java.util.concurrent.Semaphore;

public class Toalet {
    static Semaphore toiletSem;
    static Semaphore mLock;
    static Semaphore zLock;

    static int numM;
    static int numZ;
    public static class Toilet{
        public void vlezi(){
            System.out.println("vleguva...");
        }
        public void izlezi(){
            System.out.println("izleguva...");
        }
    }
    public static void init(){
        toiletSem=new Semaphore(1);
        mLock=new Semaphore(1);
        zLock=new Semaphore(1);
        numM=0;
        numZ=0;
    }
    public static class Man extends Thread{
        private Toilet toilet;
        public Man(Toilet toilet){
            this.toilet=toilet;
        }
        public void enter() throws InterruptedException {
            mLock.acquire();
            if(numM==0){
                toiletSem.acquire();
            }
            numM++;
            this.toilet.vlezi();
            mLock.release();
        }
        public void exit() throws InterruptedException {
            mLock.acquire();
            this.toilet.izlezi();
            numM--;
            if(numM==0){
                toiletSem.release();
            }
            mLock.release();
        }
        @Override
        public void run() {
            super.run();
        }
    }
    public static class Woman extends Thread{
        private Toilet toilet;
        public Woman(Toilet toilet){
            this.toilet=toilet;
        }
        public void enter() throws InterruptedException {
            zLock.acquire();
            if(numZ==0){
                toiletSem.acquire();
            }
            numZ++;
            this.toilet.vlezi();
            zLock.release();
        }
        public void exit() throws InterruptedException {
            zLock.acquire();
            this.toilet.izlezi();
            numZ--;
            if(numZ==0){
                toiletSem.release();
            }
            zLock.release();
        }

        @Override
        public void run() {
            super.run();
        }
    }
}
