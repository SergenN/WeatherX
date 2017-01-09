package nl.jozefbv.weatherx;

import java.sql.Connection;
import java.sql.SQLException;
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

    private boolean sqlEnabled;
    private int batchCounter = 0;
    private Statement statement;
    private Connection conn = null;
    /**
     * create a storage handler
     *
     * @param sqlEnabled a boolean if sql is enabled, if it is not, no data will be saved.
     */
    public StorageHandler(boolean sqlEnabled){
        this.sqlEnabled = sqlEnabled;
    }

    /**
     * store a measurement in the database
     * @param measurement the measurement to store
     */
    public void store(Measurement measurement){
        if(sqlEnabled) {
            storeBatchSQL(measurement);
        }
    }

    private void establishConnection(){
        conn = Main.getConnection();
    }


    @SuppressWarnings("Duplicates")
    public void storeSQL(Measurement measurement){
        if (conn == null) {
            establishConnection();
        }
        String query = "";
        try {
            query = "INSERT INTO measurements(stn, date, time, temp, dewp, stp, slp, visib, wdsp, prcp, sndp, frshtt, cldc, wnddir) VALUES ("
                    + measurement.getStn() + ",'"
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
            statement = conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            statement.setFetchSize(Integer.MIN_VALUE);
            statement.execute(query);
            conn.close();
        } catch (java.sql.SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }

    /**
     * a wrapper for the store procedures from sql
     * @param measurement measurement data to store
     */
    public void storeBatchSQL(Measurement measurement){

        if (conn == null) {
            establishConnection();
        }

        if (batchCounter == 0){
            try {
                statement = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        String query = "";
        try {
            query = "INSERT INTO measurements(stn, date, time, temp, dewp, stp, slp, visib, wdsp, prcp, sndp, frshtt, cldc, wnddir) VALUES ("
                    + measurement.getStn() + ",'"
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
            statement.addBatch(query);
            batchCounter++;
        } catch (java.sql.SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }

        if (batchCounter == 50){
            try {
                batchCounter = 0;
                statement.executeBatch();
                conn.close();
                conn = null;
            } catch (SQLException e) {
                System.out.println(query);
                e.printStackTrace();
            }
        }
    }
}