package nl.jozefbv.weatherx;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by Sergen Nurel
 * Date of creation 29-12-2016, 13:45
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: nl.jozefbv.weatherx
 * Class: nl.jozefbv.weatherx.WSServer
 * Description:
 * this class can handle multiple data storage methods and is basically a wrapper for store procedures.
 *
 * Changelog:
 * 1.0: initially created the class only to handle sql storage procedures
 */
public class StorageHandler {

    private Connection sqlConnection;
    private boolean sqlEnabled;

    /**
     * create a storage handler
     *
     * @param sqlConnection the sql connection created in main.
     * @param sqlEnabled a boolean if sql is enabled, if it is not, no data will be saved.
     */
    public StorageHandler(Connection sqlConnection, boolean sqlEnabled){
        this.sqlConnection = sqlConnection;
        this.sqlEnabled = sqlEnabled;
    }

    /**
     * store a measurement in the database
     * @param measurement the measurement to store
     */
    public void store(Measurement measurement){
        if(sqlEnabled) {
            storeSQL(measurement);
        }
    }

    /**
     * a wrapper for the store procedures from sql
     * @param measurement measurement data to store
     */
    public void storeSQL(Measurement measurement){
        if (sqlConnection == null) {
            System.out.println("SQL error! on storSQL");
            return;
        }
        try {
            Statement statement = sqlConnection.createStatement();
            String query = "INSERT INTO measurements(stn, date, time, temp, dewp, stp, slp, visib, wdsp, prcp, sndp, frshtt, cldc, wnddir) VALUES ('"
                    + measurement.getStn() + "','"
                    + measurement.getDate() + "','"
                    + measurement.getTime() + "',"
                    + measurement.getTemp() + ","
                    + measurement.getDewp() + ","
                    + measurement.getStp() + ","
                    + measurement.getSlp() + ","
                    + measurement.getVisib() + ","
                    + measurement.getWdsp() + ","
                    + measurement.getPrcp() + ","
                    + measurement.getSndp() + ",'"
                    + measurement.getFrshtt() + "',"
                    + measurement.getCldc() + ","
                    + measurement.getWnddir() + ")";
            statement.executeUpdate(query);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}