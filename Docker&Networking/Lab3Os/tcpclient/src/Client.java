import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread {
    private InetAddress serverAddress;
    private int serverPort;

    public Client(InetAddress serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter message (login to start): ");
            String input = scanner.nextLine();
            writer.write(input + "\n");
            writer.flush();

            String response = reader.readLine();
            System.out.println("Server response: " + response);

            if (!response.equals("logged in")) {
                return;
            }

            while (true) {
                System.out.print("Enter message: ");
                input = scanner.nextLine();
                writer.write(input + "\n");
                writer.flush();

                response = reader.readLine();
                System.out.println("Server response: " + response);

                if (input.equals("logout")) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        Client client = new Client(InetAddress.getLocalHost(), 9000);
        client.start();
    }
}
