import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket socket=new ServerSocket(port);
            while(true){
                Socket clientSocket=socket.accept();
                Worker worker=new Worker(clientSocket);
                worker.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static class Worker extends Thread{
        private Socket socket;

        public Worker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                //server CONNECT <client_ip_address>
                bufferedWriter.write("CONNECT "+socket.getRemoteSocketAddress()+"\n");
                bufferedWriter.flush();

                //prima LOGIN username password
                String line= bufferedReader.readLine();
                System.out.println("Client:"+line);

                String username=line.split("\\s+")[1];
                String password=line.split("\\s+")[2];

                if(!line.startsWith("LOGIN")){
                    throw new RuntimeException();
                }

                //prakja WELCOME <username>
                bufferedWriter.write("WELCOME "+username+"\n");
                bufferedWriter.flush();



            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) {
        Server server=new Server(5678);
        server.start();
    }
}
