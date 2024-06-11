package zad1_Library_withSemaphores;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class LibraryDemo {
     public static void main(String[] args) throws InterruptedException {
          List<Member> members=new ArrayList<>();
          SyncLibraryWithSemaphores library=new SyncLibraryWithSemaphores(10);
          for(int i=0;i<10;i++){
               Member member=new Member("M"+i,library);
               members.add(member);
          }
          for (Member member : members){
               member.start();
          }
          for (Member member : members){
               member.join();
          }
          System.out.println("Successfully");
     }
}

class Member extends Thread{
     private String name;
     private SyncLibraryWithSemaphores library;
     public Member(String name,SyncLibraryWithSemaphores library){
          this.name=name;
          this.library=library;
     }

     @Override
     public void run(){
          for (int i = 0; i < 3; i++) {
               System.out.println("Member "+i+"returns a book!");
               try {
                    library.returnBook("Book"+i);

               } catch (InterruptedException e) {
                    throw new RuntimeException(e);
               }
          }
          for (int i = 0; i < 2; i++) {
               System.out.println("Member "+i+"borrows a book!");
               try {
                    library.borrowBook();
               } catch (InterruptedException e) {
                    throw new RuntimeException(e);
               }
          }
     }
}
