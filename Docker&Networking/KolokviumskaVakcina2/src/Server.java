import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;

public class Server extends Thread {
    private int port;
    private File file;

    public Server(int port, File file) {
        this.port = port;
        this.file = file;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Worker worker = new Worker(clientSocket, file);
                worker.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Worker extends Thread {
        private Socket socket;
        private File file;

        public Worker(Socket socket, File file) {
            this.socket = socket;
            this.file = file;
        }

        @Override
        public void run() {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                //server prakja na client hello client
                bufferedWriter.write("HELLO " + socket.getRemoteSocketAddress() + "\n");
                bufferedWriter.flush();

                //prima od client hello port
                String line = bufferedReader.readLine();
                System.out.println(line);

                //prakja poraka send daily data
                bufferedWriter.write("SEND DAILY DATA\n");
                bufferedWriter.flush();

                //prima red po red i gi zapisuva vo data.csv
                File file = new File("C:\\Users\\dimit\\Desktop\\4 semestar\\Operativni\\networking\\KolokviumskaVakcina2\\data.csv");
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file, true));
                LocalDate date = LocalDate.now();
                while (!(line = bufferedReader.readLine()).equals("END")) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        fileWriter.write(date + "," + line + "\n");
                        fileWriter.flush();
                    } else {
                        throw new RuntimeException();
                    }
                }
                fileWriter.close();

                bufferedWriter.write("OK\n");
                bufferedWriter.flush();


                //prima quit
                line = bufferedReader.readLine();
                System.out.println(line);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(5555, new File("C:\\Users\\dimit\\Desktop\\4 semestar\\Operativni\\networking\\KolokvimskaVakcina\\test.txt"));
        server.start();
    }
}
