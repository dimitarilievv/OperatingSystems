package zad5_FinkiUpisi;

import java.util.concurrent.Semaphore;

public class Finki {

    static Semaphore slobodnoUpisnoMesto;
    static Semaphore enter;
    static Semaphore here;
    static Semaphore done;

    public static void init(){
        slobodnoUpisnoMesto=new Semaphore(4);
        enter=new Semaphore(1);
        here=new Semaphore(1);
        done=new Semaphore(1);
    }
    public static class Clen extends Thread{
        public void execute() throws InterruptedException{
            slobodnoUpisnoMesto.acquire();
            int i = 10;
            while(i>0){
                //zapisuvaj novi studenti
                enter.release();
                here.acquire();
                zapisi();
                done.release();
                i--;
            }
            slobodnoUpisnoMesto.release();
        }
        public void zapisi(){
            System.out.println("Zapisav student");
        }
        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static class Student extends Thread{
        public void execute() throws InterruptedException{
            enter.acquire();
            ostaviDokumenti();
            here.release();
            done.acquire();
        }
        public void ostaviDokumenti(){
            System.out.println("Ostavam dokument.");
        }
        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
