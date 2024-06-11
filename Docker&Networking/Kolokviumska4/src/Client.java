import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread{
    private InetAddress ipAddress;
    private int port;
    private String emailFrom;
    private String emailTo;

    public Client(InetAddress ipAddress, int port, String emailFrom, String emailTo) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
    }

    @Override
    public void run() {
        try {
            Socket socket=new Socket(ipAddress,port);
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner;

            //ja prima vleznata poraka ready
            String line=bufferedReader.readLine();
            if(!line.equals("READY")){
                socket.close();
            }
            System.out.println("Server:"+line);

            //vrakja MAIL FROM: <mailfrom>
            bufferedWriter.write("MAIL FROM: "+emailFrom+"\n");
            bufferedWriter.flush();

            //prima 250
            line=bufferedReader.readLine();
            System.out.println("Server:"+line);

            //prakja do TO
            bufferedWriter.write("MAIL TO:"+emailTo+"\n");
            bufferedWriter.flush();

            //prima thank you
            line=bufferedReader.readLine();
            System.out.println("Server:"+line);

            //prakja subject
            bufferedWriter.write("SUBJECT:BAZI OPERATIVNI\n");
            bufferedWriter.flush();

            //prima OK
            line=bufferedReader.readLine();
            System.out.println("Server:"+line);


            scanner=new Scanner(System.in);
            while (true){
                line=scanner.nextLine();
                if(line.equals("!")){
                    break;
                }
                bufferedWriter.write(line+"\n");
                bufferedWriter.flush();
            }
            bufferedWriter.write("END\n");
            bufferedWriter.flush();

            //prima received
            line=bufferedReader.readLine();
            System.out.println("Server:"+line);

            //prakja bye
            bufferedWriter.write("BYE\n");
            bufferedWriter.flush();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        Client client=new Client(InetAddress.getLocalHost(),5432,"dimi@gmail.com","bube@gmail.com");
        client.start();
    }
}
