import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread{
    private InetAddress ipAddress;
    private int port;

    public Client(InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port=port;
    }

    @Override
    public void run() {
        try {
            Socket socket=new Socket(ipAddress,port);
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner scanner=new Scanner(System.in);
            String responseServer=null;
            //klientot prakja poraka hello:223008
            String line=scanner.nextLine();
            if(!(line.equals("hello:223008"))){
                socket.close();
            }
            bufferedWriter.write(line+"\n");
            bufferedWriter.flush();

            //serverot treba da vrati 223008:hello
            String message= bufferedReader.readLine();
            if(!message.equals("223008:hello")){
                socket.close();
            }
            System.out.println("Server:"+message);

            //klientot prakja poraka 223008:receive
            bufferedWriter.write("223008:receive\n");
            bufferedWriter.flush();

            //serverot treba da vrati 223008:send:filename.txt
            responseServer= bufferedReader.readLine();
            System.out.println("Server:"+responseServer);
            String fileName=responseServer.split(":")[2];
            String filePath="C:\\Users\\dimit\\Desktop\\4 semestar\\Operativni\\networking\\Kolokviumska1\\";

            File file=new File(filePath+fileName);
            BufferedWriter fileWriter=new BufferedWriter(new FileWriter(file));
            while (true){
                message= bufferedReader.readLine();
                if(message.equals("223008:over")){
                    break;
                }
                fileWriter.write(message+"\n");
                fileWriter.flush();
            }

            //klientot isprakja 223008:size:filesize
            long fileSize=file.length();
            bufferedWriter.write("223008:size:"+fileSize+"\n");
            bufferedWriter.flush();

            socket.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        Client client=new Client(InetAddress.getLocalHost(),9000);
        client.start();
    }
}















