import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Created by Sergen Nurel
 * Date of creation 25-9-2015, 12:05
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: default
 * Class: Main
 * Description:
 * This class is the main class of the project
 *
 * Changelog:
 * 1.0: class created and added thread calling
 * 2.0: changed MySQL connection with MongoDB Connection
 */
public class Main {

    public static MongoDatabase conn;

    /**
     * Main
     * In this method the server thread is started.
     * @param args, arguments given to the main method
     */
    public static void main(String[] args) {
        Main.conn = connect();
        new Thread(new Server()).start();
    }



    /**
     * Method to connect to database
     */
    public static MongoDatabase connect() {
        MongoClient mongoClient = new MongoClient();
        return mongoClient.getDatabase("unwdmi");
    }
}
