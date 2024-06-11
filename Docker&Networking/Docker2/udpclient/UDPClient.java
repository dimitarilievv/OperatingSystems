

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

        byte[] buffer = new byte[256];

        try {
            DatagramSocket datagramSocket = new DatagramSocket();

            String line = "login";
            buffer = line.getBytes();

            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ipAddress), port);
            datagramSocket.send(datagramPacket);

            datagramPacket = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(datagramPacket);
            String response = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
            System.out.println(response);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                line = scanner.nextLine();
                buffer = line.getBytes();
                datagramPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ipAddress), port);
                datagramSocket.send(datagramPacket);

                datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);
                response = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println(response);

                if (line.equals("logout")) {
                    break;
                }
            }

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UDPClient udpClient = new UDPClient("localhost", 9090);
        udpClient.start();
    }
}
