package nl.jozefbv.weatherx;

import java.sql.*;
import java.sql.Connection;

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
    private Connection sqlConnection;
    private StorageHandler storageHandler;
    private FlatFileDb fileConnection;

    /**
     * the first method called by Java
     * @param args, arguments given to the main method
     */
    public static void main(String[] args) {
        new Main();
    }

    /**
     * In this method a sql connection is established and the server thread is started.
     */
    public Main(){
        sqlConnection = connectSQL();
        fileConnection = new FlatFileDb();
        storageHandler = new StorageHandler(sqlConnection, fileConnection, true, true);
        new Thread(new WSServer(this)).start();
    }

    public StorageHandler getStorageHandler(){
        return storageHandler;
    }

    /**
     * Method to connect to database
     */
    public static Connection connectSQL() {
        try {
            System.out.println("Connect");
            return DriverManager.getConnection("jdbc:mysql://localhost/weatherxweb?user=root&password=");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
