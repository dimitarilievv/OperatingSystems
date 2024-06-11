import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Server extends Thread{
    private int port;
    private String fileOutput;
    public Server(int port,String fileOutput) {
        this.port = port;
        this.fileOutput=fileOutput;
    }

    @Override
    public void run() {
        try {
            ServerSocket socket=new ServerSocket(port);
            File file=new File(fileOutput);
            if(!file.exists()){
                file.createNewFile();
            }
            while (true){
                Socket clientSocket=socket.accept();
                System.out.println("Started the server on port:"+port);
                Worker worker=new Worker(clientSocket,fileOutput);
                worker.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static class Worker extends Thread{
        private Socket socket;
        private String fileOutput;
        private static Semaphore semaphore=new Semaphore(1);
        public Worker(Socket socket,String fileOutput) {
            this.socket = socket;
            this.fileOutput=fileOutput;
        }

        @Override
        public void run() {
            try {
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                Scanner scanner=new Scanner(System.in);

                //server prakja HELLO<client_ip_address>
                bufferedWriter.write("HELLO <"+socket.getRemoteSocketAddress()+">\n");
                bufferedWriter.flush();

                //prima poraka so HELLO <localport>
                String line=bufferedReader.readLine();
                int localPort= Integer.parseInt(line.split("<")[1].split(">")[0]);
                if(!line.equals("HELLO <"+localPort+">\n")){
                    socket.close();
                }

                //prakja poraka SEND DAILY DATA
                bufferedWriter.write("SEND DAILY DATA\n");
                bufferedWriter.flush();

                //poraka za:
                //Number of new covid cases,Number of hospitalized cases,Number of recovered patients
                //10, 15, 20
                line= bufferedReader.readLine();
                LocalDate date=LocalDate.now();
                BufferedWriter fileWriter=new BufferedWriter(new FileWriter(fileOutput,true));

                semaphore.acquire();
                fileWriter.write(date+","+line+"\n");
                fileWriter.flush();
                semaphore.release();

                bufferedWriter.write("OK\n");
                bufferedWriter.flush();

                line=bufferedReader.readLine();
                if(line.equals("QUIT")){
                    socket.close();
                }


            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        Server server=new Server(8888,"C:\\Users\\dimit\\Desktop\\4 semestar\\Operativni\\networking\\Kolokviumska3");
        server.start();
    }
}
