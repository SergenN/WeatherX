package nl.jozefbv.weatherx;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;

import java.sql.*;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Sergen Nurel
 * Date of creation 25-9-2015, 12:05
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: default
 * Class: nl.jozefbv.weatherx.Main
 * Description:
 * This class is the main class of the project
 *
 * Changelog:
 * 1.0: class created and added thread calling
 * 2.0: changed MySQL connection with MongoDB nl.jozefbv.weatherx.ClientConnection
 */
public class Main {
    public static Connection SQLConn;
    public static MongoDatabase MDBConn;
    public static Filter filter;
    public static WebClientServer webServer;
    public static Double centralLatitude = 37.00,centralLongitude = 127.00,centralRange = 20.00;


    /**
     * nl.jozefbv.weatherx.Main
     * In this method the server thread is started.
     * @param args, arguments given to the main method
     */
    public static void main(String[] args) {
        //System.out.println(System.currentTimeMillis()"");
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        Main.SQLConn = connectSQL();
        Main.MDBConn = connectMongoDB();
        filter = new Filter();
        Initial.Initial();
        new Thread(new WSServer()).start();
        try {
            WebClientServer.main();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Method to connect to database
     */
    public static Connection connectSQL() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/weatherxweb?user=root&password=");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to connect to MongoDB database
     */
    public static MongoDatabase connectMongoDB() {
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(1000);
        MongoClientOptions options = builder.build();
        MongoClient mongoClient = new MongoClient("localhost", options);
        return mongoClient.getDatabase("WeatherX");
    }
}
