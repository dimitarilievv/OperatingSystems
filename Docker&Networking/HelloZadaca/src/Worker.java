
import com.sun.net.httpserver.Request;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Worker extends  Thread{
    private Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String line=reader.readLine();
            if(!line.equals("hello:client")){
                socket.close();
            }
            writer.write("hello:server\n");
            writer.flush();

            line= reader.readLine();
            if(line.equals("start:file")){
                File file=new File("C:\\Users\\dimit\\Desktop\\4 semestar\\Operativni\\networking\\HelloZadaca\\server.txt");
                writer=new BufferedWriter(new FileWriter(file,true));
                while (!(line=reader.readLine()).equals("end:file")){
                    writer.write(line+"\n");
                    writer.flush();
                }
                writer.write("file_size:"+file.length()+"\n");
                writer.flush();
            }

            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
