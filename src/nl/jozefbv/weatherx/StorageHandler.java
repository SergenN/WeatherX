package nl.jozefbv.weatherx;

import java.io.IOException;

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
 * 1.1: made the storagehandler compatible with files
 */
public class StorageHandler {

    private FlatFileDb fileConnection;
    private boolean csvEnabled;

    /**
     * create the storage handler
     * @param fileConnection the file connection to write to
     * @param csvEnabled boolean to write to the file
     */
    public StorageHandler(FlatFileDb fileConnection, boolean csvEnabled){
        this.fileConnection = fileConnection;
        this.csvEnabled = csvEnabled;
    }

    /**
     * store a measurement in the database
     * @param measurement the measurement to store
     */
    public void store(Measurement measurement){
        if (csvEnabled){
            storeCSV(measurement);
        }
    }

    /**
     * write to the CSV file
     * @param measurement the measurement to write
     */
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
}