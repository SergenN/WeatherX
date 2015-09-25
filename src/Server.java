import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by serge on 25-9-2015.
 */
public class Server implements Runnable{

    ServerSocket socket;

    public Server(){
        try {
            socket = new ServerSocket(25566);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            Socket client = socket.accept();
            new Thread(new Connection(client)).start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
