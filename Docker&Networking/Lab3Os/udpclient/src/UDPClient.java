import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPClient extends Thread {
    private String ipAddress;
    private int port;

    public UDPClient(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            DatagramSocket datagramSocket = new DatagramSocket();

            // Send login message
            String line = "login";
            byte[] buffer = line.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ipAddress), port);
            datagramSocket.send(datagramPacket);

            // Receive login confirmation
            buffer = new byte[1024];
            datagramPacket = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(datagramPacket);
            String response = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
            System.out.println("Server: " + response);

            Scanner sc = new Scanner(System.in);
            while (true) {
                line = sc.nextLine();
                buffer = line.getBytes();
                datagramPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ipAddress), port);
                datagramSocket.send(datagramPacket);

                buffer = new byte[1024];
                // Receive server response
                datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);
                response = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println("Server: " + response);

                if (line.equals("logout")) {
                    break;
                }
            }

            datagramSocket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UDPClient udpClient = new UDPClient("localhost", 9090);
        udpClient.start();
    }
}

