
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Client extends Thread {
    private InetAddress serverAddress;
    private int serverPort;

    public Client(InetAddress serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Socket socket = null;
        Random random = new Random();
        try {
            //prvo treba da se povrzi so serverot
            socket = new Socket(serverAddress, serverPort);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            bufferedWriter.write("hello:client\n");
            bufferedWriter.flush();


            String line = bufferedReader.readLine();
            if (!line.equals("hello:server")) {
                socket.close();
            }

            bufferedWriter.write("start:file\n");
            bufferedWriter.flush();

            bufferedReader = new BufferedReader(new FileReader("C:\\Users\\dimit\\Desktop\\4 semestar\\Operativni\\networking\\HelloZadaca\\client.txt"));
            line = null;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line+"\n");
                bufferedWriter.flush();
            }

            bufferedWriter.write("end:file\n");
            bufferedWriter.flush();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) throws UnknownHostException {
        Client client = new Client(InetAddress.getLocalHost(), 5000);
        client.start();
    }
}
