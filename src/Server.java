import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Sergen Nurel
 * Date of creation 25-9-2015, 13:24
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: default
 * Class: Server
 * Description:
 * This server class will wait on incoming client connections
 *
 * Changelog:
 * 1.0: this class will wait on incoming client connections and then start a new thread for the client.
 */
public class Server implements Runnable{

    ServerSocket socket;

    /**
     * Constructor Server
     * the constructor will open a socket on port 25566
     */
    public Server(){
        try {
            socket = new ServerSocket(25566);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Run
     * once the port is opened this thread will accept connections,
     * and make a new thread once there is an incoming connection.
     */
    public void run(){
        try{
            System.out.println("Server running");//TODO debug

            while (true){
                Socket client = socket.accept();
                new Thread(new Connection(client)).start();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
