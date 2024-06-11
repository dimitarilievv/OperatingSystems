import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread{
    private int port;
    private String filePath;

    public Server(int port, String filePath) {
        this.port = port;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            ServerSocket socket=new ServerSocket(port);
            while (true){
                Socket clientSocket=socket.accept();
                Worker worker=new Worker(clientSocket,filePath);
                worker.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static class Worker extends Thread{
        private Socket socket;
        private String filePath;
        public Worker(Socket socket,String filePath) {
            this.socket = socket;
            this.filePath=filePath;
        }

        @Override
        public void run() {
            try {
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner=new Scanner(System.in);
                File logFile=new File(filePath+"\\logs.txt");
                if(!logFile.exists()){
                    logFile.createNewFile();
                }
                BufferedWriter logWriter=new BufferedWriter(new FileWriter(logFile,true));
                //server prakja ready
                bufferedWriter.write("READY\n");
                bufferedWriter.flush();

                //serverot prima email
                String line=bufferedReader.readLine();
                System.out.println("Client:"+line);
                logWriter.write("Client:"+line+"\n");
                logWriter.flush();
                if(!line.contains("@")){
                    socket.close();
                    throw new RuntimeException();
                }
                //server prakja 250
                bufferedWriter.write("250\n");
                bufferedWriter.flush();


                //prima mail
                line=bufferedReader.readLine();
                System.out.println("Client:"+line);
                logWriter.write("Client:"+line+"\n");
                logWriter.flush();
                if(!line.contains("@")){
                    socket.close();
                    throw new RuntimeException();
                }
                //server prakja THANK YOU
                bufferedWriter.write("THANK YOU\n");
                bufferedWriter.flush();


                //prima subject
                line=bufferedReader.readLine();
                System.out.println("Client:"+line);
                logWriter.write("Client:"+line+"\n");
                logWriter.flush();
                if(line.length()>200){
                    socket.close();
                    throw new RuntimeException();
                }

                //vrakja OK
                bufferedWriter.write("OK\n");
                bufferedWriter.flush();

                //citame linija po linija od klient i gi zapisuva vo poseben fajl
                File file=new File("C:\\Users\\dimit\\Desktop\\4 semestar\\Operativni\\networking\\Kolokviumska4\\poraki.txt");
                if(!file.exists()){
                    file.createNewFile();
                }
                BufferedWriter fileWriter=new BufferedWriter(new FileWriter(file,true));
                while(true){
                    line=bufferedReader.readLine();
                    if(!line.equals("END")){
                        logWriter.write("Client:"+line+"\n");
                        logWriter.flush();
                        fileWriter.write(line+"\n");
                        fileWriter.flush();
                    }else{
                        break;
                    }
                }
                fileWriter.close();

                //prakja golemina na klient
                int fileSize = (int) file.length();
                bufferedWriter.write("RECEIVED "+fileSize+" bytes\n");
                bufferedWriter.flush();

                //prima BYE
                line=bufferedReader.readLine();
                logWriter.write("Client:"+line+"\n");
                logWriter.flush();
                socket.close();

                logWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) {
        Server server=new Server(5432,"C:\\Users\\dimit\\Desktop\\4 semestar\\Operativni\\networking\\Kolokviumska4");
        server.start();
    }
}
