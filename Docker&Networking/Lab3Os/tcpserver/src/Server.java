import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends Thread {
    private int port;
    private static AtomicInteger messageCount = new AtomicInteger(0);

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                Socket client = serverSocket.accept();
                new Worker(client).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Worker extends Thread {
        private Socket socket;

        public Worker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String line = reader.readLine();
                if (!line.equals("login")) {
                    socket.close();
                    return;
                }

                writer.write("logged in\n");
                writer.flush();

                while ((line = reader.readLine()) != null) {
                    messageCount.incrementAndGet();
                    if (line.equals("logout")) {
                        writer.write("logged out\n");
                        writer.flush();
                        break;
                    } else {
                        writer.write("echo- " + line + "\n");
                        writer.flush();
                    }
                }

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
