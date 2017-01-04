package nl.jozefbv.weatherx;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by serge on 4-1-2017.
 */
public class StorageHandler {

    Connection sqlConnection;
    boolean sqlEnabled, csvEnabled;

    public StorageHandler(Connection sqlConnection, boolean sqlEnabled, boolean csvEnabled){
        this.sqlConnection = sqlConnection;
        this.sqlEnabled = sqlEnabled;
        this.csvEnabled = csvEnabled;
    }

    public synchronized void store(Measurement measurement){
        if(sqlEnabled) {
            storeSQL(measurement);
        }
        if (csvEnabled){
            storeCSV(measurement);
        }
    }


    public synchronized void storeCSV(Measurement measurement){

    }

    public synchronized void storeSQL(Measurement measurement){
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