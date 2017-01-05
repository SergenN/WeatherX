package nl.jozefbv.weatherx;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by serge on 4-1-2017.
 */
public class StorageHandler {

    Connection sqlConnection;
    FlatFileDb fileConnection;
    boolean sqlEnabled, csvEnabled;

    public StorageHandler(Connection sqlConnection, FlatFileDb fileConnection, boolean sqlEnabled, boolean csvEnabled){
        this.sqlConnection = sqlConnection;
        this.fileConnection = fileConnection;
        this.sqlEnabled = sqlEnabled;
        this.csvEnabled = csvEnabled;
    }

    public void store(Measurement measurement){
        if(sqlEnabled) {
            storeSQL(measurement);
        }
        if (csvEnabled){
            storeCSV(measurement);
        }
    }


    public void storeCSV(Measurement measurement){
        if (fileConnection == null) {
            System.out.println("File error! ono file connection detected!");
            return;
        }
        try {
            fileConnection.writeLine(measurement.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void storeSQL(Measurement measurement){
        if (sqlConnection == null) {
            System.out.println("SQL error! no sql connection detected!");
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