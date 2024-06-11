import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{
    private InetAddress ipAddress;
    private int port;
    private String username;
    private String password;

    public Client(InetAddress ipAddress, int port, String username, String password) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        Socket socket= null;
        try {
            socket = new Socket(ipAddress,port);
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //prima CONNECT <client_ip_address>:
            String line= bufferedReader.readLine();
            System.out.println("Server:"+line);

            //prakja LOGIN <username> <password>:
            bufferedWriter.write("LOGIN "+username+" "+password+"\n");
            bufferedWriter.flush();

            //prima WELCOME <username>
            line= bufferedReader.readLine();
            System.out.println("Server:"+line);





        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static void main(String[] args) throws UnknownHostException {
        Client client=new Client(InetAddress.getLocalHost(),5678);
        client.start();
    }
}
