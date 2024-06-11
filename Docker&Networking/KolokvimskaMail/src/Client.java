import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Scanner;

public class Client extends Thread{
    private InetAddress ipAddress;
    private int port;
    private String emailTo;
    private String emailFrom;
    private String mailCC;

    public Client(InetAddress ipAddress, int port, String emailTo, String emailFrom, String mailCC) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.emailTo = emailTo;
        this.emailFrom = emailFrom;
        this.mailCC = mailCC;
    }

    @Override
    public void run() {
        try {
            Socket socket=new Socket(ipAddress,port);
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //prima start
            String line= bufferedReader.readLine();
            System.out.println(line);

            //prakja mail to <emailTo>
            bufferedWriter.write("MAIL TO:"+emailTo+"\n");
            bufferedWriter.flush();

            //prima TNX
            line= bufferedReader.readLine();
            System.out.println(line);

            //prakja mail to <emailFrom>
            bufferedWriter.write("MAIL FROM:"+emailFrom+"\n");
            bufferedWriter.flush();

            //PRIMA 200
            line= bufferedReader.readLine();
            System.out.println(line);

            //PRAKJA MAIL
            bufferedWriter.write("MAIL CC:"+mailCC+"\n");
            bufferedWriter.flush();

            //prima receivers:
            line= bufferedReader.readLine();
            System.out.println(line);

            //cita linija po linija od tastatura i gi prakja na serverot
            Scanner scanner=new Scanner(System.in);
            while (!Objects.equals(line = scanner.nextLine(), "?")){
                bufferedWriter.write(line+"\n");
                bufferedWriter.flush();
            }
            bufferedWriter.write("END\n");
            bufferedWriter.flush();

            //prima linii:
            line= bufferedReader.readLine();
            System.out.println(line);

            //vrakja EXIT
            bufferedWriter.write("EXIT\n");
            bufferedWriter.flush();




        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) throws UnknownHostException {
        Client client=new Client(InetAddress.getLocalHost(),8765,"dimitar.iliev123@gmail.com","dimitar.iliev@mail.com","dimitar.iliev@students.finki.ukim.mk");
        client.start();
    }
}
