package zad3_Si02;

import java.util.concurrent.Semaphore;

public class SiO2 {
    public static int NUM_RUN = 50;
    static Semaphore si;
    static Semaphore o;
    static Semaphore siHere;
    static Semaphore oHere;
    static Semaphore ready;

    public static void init(){
        si=new Semaphore(1);
        o=new Semaphore(2);
        siHere=new Semaphore(0);
        oHere=new Semaphore(0);
        ready=new Semaphore(0);
    }
    public static class Si extends Thread {
        public void bond() {
            System.out.println("Si is bonding now.");
        }


        @Override
        public void run() {
            for (int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void execute() throws InterruptedException {
            si.acquire();
            siHere.release(2); //2pati isto kako da sme go viknale bez argumn
            oHere.acquire(2);
            ready.release(2);
            bond();
            si.release();
        }
    }

    public static class O extends Thread {
        public void bond() {
            System.out.println("Si is bonding now.");
        }


        @Override
        public void run() {
            for (int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void execute() throws InterruptedException {
            o.acquire();
            siHere.acquire();
            oHere.release();
            ready.acquire();
            bond();
            o.release();
        }
    }
}


//Starter Code
/*
* package zad3_Si02;

import java.util.concurrent.Semaphore;

public class SiO2 {
    public static int NUM_RUN = 50;

    public static class Si extends Thread {
        public void bond() {
            System.out.println("Si is bonding now.");
        }


        @Override
        public void run() {
            for (int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void execute() throws InterruptedException {

        }
    }
    public static class O extends Thread {
        public void bond() {
            System.out.println("Si is bonding now.");
        }


        @Override
        public void run() {
            for (int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void execute() throws InterruptedException {

        }
    }
}
*/