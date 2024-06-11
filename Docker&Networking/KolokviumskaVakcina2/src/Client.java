import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Objects;
import java.util.Scanner;

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

            //prima send daily data
            line= bufferedReader.readLine();
            System.out.println(line);

            //cita od tastatura  i prakja na server
            Scanner scanner=new Scanner(System.in);
            while (!(line = scanner.nextLine()).equals("!")){
                bufferedWriter.write(line+"\n");
                bufferedWriter.flush();
            }
            bufferedWriter.write("END\n");
            bufferedWriter.flush();

            //prima ok
            line= bufferedReader.readLine();
            System.out.println(line);

            //prakja quit
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
