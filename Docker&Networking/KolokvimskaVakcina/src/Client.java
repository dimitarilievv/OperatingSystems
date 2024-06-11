import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
    private InetAddress ipAddress;
    private int port;

    public Client(InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Socket socket=new Socket(ipAddress,port);
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //prima hello poraka od server
            String line= bufferedReader.readLine();
            System.out.println(line);

            //prakja client hello port kon server
            bufferedWriter.write("HELLO "+port+"\n");
            bufferedWriter.flush();

            //prima daily data red po red se do END
            File file=new File("C:\\Users\\dimit\\Desktop\\4 semestar\\Operativni\\networking\\KolokvimskaVakcina\\data.csv");
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedWriter fileWriter=new BufferedWriter(new FileWriter(file,true));
            while (!(line= bufferedReader.readLine()).equals("END")){
                fileWriter.write(line+"\n");
                fileWriter.flush();
            }
            fileWriter.close();
            if(line.equals("END")){
                bufferedWriter.write("OVER\n");
                bufferedWriter.flush();
            }

            //PRIMA OK
            line= bufferedReader.readLine();
            System.out.println(line);

            //PRAKJA QUIT
            bufferedWriter.write("QUIT\n");
            bufferedWriter.flush();
            socket.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) throws UnknownHostException {
        Client client=new Client(InetAddress.getLocalHost(),5555);
        client.start();
    }
}
