import org.w3c.dom.css.Counter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.IllegalFormatCodePointException;
import java.util.concurrent.Semaphore;

public class Server extends Thread{
    private int port;
    private File file;
    public Server(int port,File file) {
        this.port = port;
        this.file=file;
    }

    @Override
    public void run() {
        try {
            ServerSocket socket=new ServerSocket(port);
            while (true){
                Socket clientSocket=socket.accept();
                Worker worker=new Worker(clientSocket,file);
                worker.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public class Worker extends Thread{
        private Socket socket;
        private static int counter;
        private Semaphore semaphore=new Semaphore(1);
        private File file;
        public Worker(Socket socket,File file) {
            this.socket = socket;
            this.file=file;
        }

        @Override
        public void run() {
            try {
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter fileWriter=new BufferedWriter(new FileWriter(file,true));
                //prakja START <client ip address>
                bufferedWriter.write("START"+socket.getRemoteSocketAddress()+"\n");
                bufferedWriter.flush();


                //prima MAIL TO:
                String line= bufferedReader.readLine();
                System.out.println(line+"\n");
                fileWriter.write(line+"\n");
                String mailTo=line.split(":")[1];
                //prakja TNX
                if(!line.contains("@")){
                    throw new RuntimeException();
                }
                bufferedWriter.write("TNX\n");
                bufferedWriter.flush();

                //PRIMA MAIL FROM:
                line= bufferedReader.readLine();
                System.out.println(line+"\n");
                fileWriter.write(line+"\n");
                if(!line.contains("@")){
                    throw new RuntimeException();
                }

                //PRAKJA 200
                bufferedWriter.write("200\n");
                bufferedWriter.flush();

                //PRIMA MAIL CC
                line= bufferedReader.readLine();
                System.out.println(line+"\n");
                fileWriter.write(line+"\n");
                if(!line.contains("@")){
                    throw new RuntimeException();
                }
                String mailCC=line.split(":")[1];

                //prakja receivers
                bufferedWriter.write("RECEIVERS:"+mailTo+","+mailCC+"\n");
                bufferedWriter.flush();


                //prima linija po linija i broi

                while (!(line= bufferedReader.readLine()).equals("END")){
                    System.out.println(line);
                    fileWriter.write(line+"\n");
                    semaphore.acquire();
                    counter++;
                    semaphore.release();
                }

                //prakja kolku linii
                bufferedWriter.write("RECEIVED "+counter+" lines\n");
                bufferedWriter.flush();


                line= bufferedReader.readLine();
                System.out.println(line+"\n");
                fileWriter.write(line+"\n");
                fileWriter.flush();


            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        Server server=new Server(8765,new File("C:\\Users\\dimit\\Desktop\\4 semestar\\Operativni\\networking\\KolokvimskaMail\\server.txt"));
        server.start();
    }
}
