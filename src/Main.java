import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

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

        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE);

        Main.conn = connect();
        new Thread(new Server()).start();
    }

    /**
     * Method to connect to MongoDB database
     */
    public static MongoDatabase connect() {
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(1000);
        MongoClientOptions options = builder.build();
        MongoClient mongoClient = new MongoClient("localhost", options);
        //MongoClient mongoClient = new MongoClient();
        return mongoClient.getDatabase("WeatherX");
    }
}
