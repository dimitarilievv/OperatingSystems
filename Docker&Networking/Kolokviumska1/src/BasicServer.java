
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BasicServer extends Thread{
    private int port;

    public BasicServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        ServerSocket socket=null;
        try {
            socket=new ServerSocket(port);
            System.out.println("Server started on port:"+port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true){
            try {
                Socket clientSocket=socket.accept();
                Worker worker=new Worker(clientSocket);
                worker.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public class Worker extends Thread{
        private Socket socket;

        public Worker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String clientRequest= bufferedReader.readLine();
                if(!clientRequest.equals("Hello Server!")){
                    socket.close();
                    return;
                }
                System.out.println("Client:"+clientRequest);
                bufferedWriter.write("Hello client!\n");
                bufferedWriter.flush();

                while ((clientRequest= bufferedReader.readLine())!=null)
                {
                    System.out.println("Client:"+clientRequest);
                    if(clientRequest.equals("login")){
                        bufferedWriter.write("logged in\n");
                        bufferedWriter.flush();
                    }else if(clientRequest.equals("log out")){
                        bufferedWriter.write("logged out\n");
                        bufferedWriter.flush();
                        break;
                    }else{
                        bufferedWriter.write("echo-"+clientRequest+"\n");
                        bufferedWriter.flush();
                    }
                }

                socket.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) {
        Server server=new Server(9000);
        server.start();
    }
}
