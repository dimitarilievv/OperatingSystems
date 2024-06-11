import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer extends Thread {
    private int port;

    public UDPServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(port);
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());

                System.out.println("Client: " + received);

                String response;
                if (received.equals("login")) {
                    response = "logged in";
                } else if (received.equals("logout")) {
                    response = "logged out";
                } else {
                    response = "echo- " + received;
                }

                buffer = response.getBytes();
                DatagramPacket packetToSend = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort());
                datagramSocket.send(packetToSend);

                // Stop server if logout message is received
                if (received.equals("logout")) {
                    break;
                }
            }

            datagramSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UDPServer server = new UDPServer(9090);
        server.start();
    }
}
