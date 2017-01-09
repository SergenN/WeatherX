package nl.jozefbv.weatherx;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Stack;

/**
 * Created by Sergen Nurel
 * Date of creation 25-9-2015, 12:05
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: nl.jozefbv.weatherx
 * Class: nl.jozefbv.weatherx.Main
 * Description:
 * This class is the main class of the project
 *
 * Changelog:
 * 1.0: class created and added thread calling
 * 1.1: adjusted to class to make use of SQL pooling
 */

public class Main {
    private StationHistory stationHistory;
    private JAXBContext jaxbContext;
    private static ComboPooledDataSource comboPooledDataSource;
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
        comboPooledDataSource = createComboPool();
        System.out.println("Created Connections");
        stationHistory = new StationHistory();
        try {
            jaxbContext = JAXBContext.newInstance(Measurement.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        new Thread(new WSServer(this)).start();
    }

    public StorageHandler getStorageHandler(){
        return new StorageHandler(true);
    }

    public StationHistory getStationHistory(){
        return stationHistory;
    }

    public JAXBContext getJaxbContext(){
        return jaxbContext;
    }

    public static Connection getConnection(){
        try {
            return comboPooledDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to connect to database
     */
    private ComboPooledDataSource createComboPool() {
        try {
            ComboPooledDataSource cpds = new ComboPooledDataSource();
            cpds.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver
            cpds.setJdbcUrl( "jdbc:mysql://localhost/weatherxweb?rewriteBatchedStatements=true" );
            cpds.setUser("root");
            cpds.setPassword("");
            cpds.setMinPoolSize(3);
            cpds.setAcquireIncrement(5);
            cpds.setMaxPoolSize(5000);
            return cpds;
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return null;
    }
}
