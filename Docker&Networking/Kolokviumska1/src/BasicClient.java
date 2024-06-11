import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class BasicClient extends Thread{
    private InetAddress ipAddress;
    private int port;

    public BasicClient(InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port=port;
    }

    @Override
    public void run() {
        try {
            Socket socket=new Socket(ipAddress,port);
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            bufferedWriter.write("Hello Server!\n");
            bufferedWriter.flush();

            String responseServer=bufferedReader.readLine();
            System.out.println("Server responded:"+responseServer);
            Scanner scanner=new Scanner(System.in);
            while(true){
                String line=scanner.nextLine();

                bufferedWriter.write(line+"\n");
                bufferedWriter.flush();

                responseServer= bufferedReader.readLine();
                System.out.println("Server responded:"+responseServer);
                if(line.equals("log out")){
                    break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        Client client=new Client(InetAddress.getLocalHost(),9000);
        client.start();
    }
}







//import java.io.*;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.net.UnknownHostException;
//
//public class Client extends Thread{
//    private InetAddress ipAddress;
//    private int port;
//
//    public Client(InetAddress ipAddress, int port) {
//        this.ipAddress = ipAddress;
//        this.port = port;
//    }
//
//    @Override
//    public void run() {
//        try {
//            Socket socket=new Socket(ipAddress,port);
//            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            bufferedWriter.write("hey server!\n");
//            bufferedWriter.flush();
//
//            String response= bufferedReader.readLine();
//            System.out.println("server returns:"+response);
//            socket.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void main(String[] args) throws UnknownHostException {
//        Client client=new Client(InetAddress.getLocalHost(),9000);
//        client.start();
//    }
//}






