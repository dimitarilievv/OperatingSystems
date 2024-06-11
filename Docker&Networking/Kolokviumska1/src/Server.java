
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
            System.out.println("Server started on port:" + port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                Socket clientSocket = socket.accept();
                Worker worker = new Worker(clientSocket);
                worker.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    static public class Worker extends Thread {
        private Socket socket;

        public Worker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                Scanner scanner = new Scanner(System.in);

                //ja citame poraka hello:223008
                String clientRequest = bufferedReader.readLine();
                int index = Integer.parseInt(clientRequest.split(":")[1]);
                System.out.println("Client:"+clientRequest);

                bufferedWriter.write(index + ":hello\n");
                bufferedWriter.flush();

                //ja citame porakata 223008:receive
                clientRequest = bufferedReader.readLine();
                System.out.println("Client:"+clientRequest);
                if (!clientRequest.equals(index + ":receive")) {
                    socket.close();
                    return;
                }

                //223008:send:filename.txt
                //sodrzina pa 223008:over
                File file = new File("C:\\Users\\dimit\\Desktop\\4 semestar\\Operativni\\networking\\server.txt");
                BufferedReader fileReader = new BufferedReader(new FileReader(file));

                bufferedWriter.write(index + ":send:"+file.getName() +"\n");
                bufferedWriter.flush();

                String line;
                while ((line= fileReader.readLine())!=null){
                    bufferedWriter.write(line+"\n");
                    bufferedWriter.flush();
                }
                bufferedWriter.write(index+":over"+"\n");
                bufferedWriter.flush();

                //ja citame porakata 223008:filesize
                clientRequest=bufferedReader.readLine();
                System.out.println("Client:"+clientRequest);
                System.out.println(clientRequest);
                socket.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(9000);
        server.start();
    }
}