package nl.jozefbv.weatherx;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bson.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Leon on 25-9-2015.
 * Class for transferring data from the Hashmap to the database
 */

/**
 * Created by Leon Wetzel
 * Date of creation 25-9-2015, 12:05
 *
 * Authors: Sergen Nurel, Leon Wetzel
 *
 * Version: 1.0
 * Package: default
 * Class: nl.jozefbv.weatherx.Transfer
 * Description:
 * This class is dedicated to tranfering processed or unprocessed data into the database
 *
 * Changelog:
 * 1.0: SQL outfit
 * 2.0: Deprecated SQL transfer() method, implemented MongoDB support
 */
public class Transfer {
    // Measurement object
    private Measurements measurement;

    // Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    public static String databaseRoot = System.getProperty("user.dir");

    // CSV file header
    private static final String FILE_HEADER = "stn,date,time,temp,dewp,stp,slp,visib,wdsp,prcp,sndp,frshtt,cldc,wnddir";

    // table name
    private static final String MEASUREMENT = "measurements";

    public Transfer(Measurements measurement) {
        this.measurement = measurement;
    }

    /**
     * This application transfers data from the Measurement object into the database
     * @param measurement measurement to push into the database
     */
    public static void store(Measurements measurement) {
        //new Transfer(measurement).transferMongo();
    }

    private static void folderCheck(Measurements measurement){
        String path = databaseRoot+"/database";
        File file = new File(path);

        if(!file.exists()){
            System.out.println("Creating Database Directory in: "+path);
            file.mkdir();
        }
        //DateTime date = new Date();
        String[] datearray = measurement.getDate().split("-");
        //date.setTime();

        //if()
    }


    /**
     * Method to transfer the data from the Measurement object into the database
     */
    @Deprecated
    public void transferSQL() {
        if (Main.SQLConn == null){
            System.out.println("SQL error! on nl.jozefbv.weatherx.Transfer()");
            return;
        }
        try {
            Statement statement = Main.SQLConn.createStatement();

            String query = "INSERT INTO " + MEASUREMENT + " VALUES ("
                    + "'" + measurement.getStn() + "'" + ",'" + measurement.getDate() + "','" + measurement.getTime() + "'," + measurement.getTemp() + ","
                    + measurement.getDewp() + "," + measurement.getStp() + "," + measurement.getSlp() + "," + measurement.getVisib() + "," + measurement.getWdsp() + ","
                    + measurement.getPrcp() + "," + measurement.getSndp() + ",'" + measurement.getFrshtt() + "'," + measurement.getCldc() + "," + measurement.getWnddir() + ")";
            System.out.println(query);
            statement.executeUpdate(query);
        }catch (java.sql.SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Method for inserting data in a CSV file
     */
    @Deprecated
    public void transferCSV() {
        FileWriter fileWriter = null;
        String fileName = measurement.getStn() + "_" + measurement.getDate() + "_" + measurement.getTime();
        try {
            fileWriter = new FileWriter(fileName);
            //Write the CSV file header
            fileWriter.append(FILE_HEADER);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            //Write a new student object list to the CSV file
            fileWriter.append(String.valueOf(measurement.getStn()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(measurement.getDate()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(measurement.getTime()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(measurement.getTemp()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(measurement.getDewp()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(measurement.getStp()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(measurement.getSlp()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(measurement.getVisib()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(measurement.getWdsp()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(measurement.getPrcp()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(measurement.getSndp()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(measurement.getFrshtt()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(measurement.getCldc()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(measurement.getWnddir()));
            fileWriter.append(COMMA_DELIMITER);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Method for inserting data in a Mongo database
     */
    @Deprecated
    public void transferMongo() {
        if (Main.MDBConn == null){
            System.out.println("Could not establish database connection.");
            return;
        }
        Document bsonDoc = new Document();
        FilterObject filterObject = Filter.checkDatabase(measurement.getStn());
        Boolean toDatabase = false;
        bsonDoc.append("STN", measurement.getStn());
        bsonDoc.append("DATE", measurement.getDate());
        bsonDoc.append("TIME", measurement.getTime());
        if(filterObject!=null) {
            if(filterObject.getDatabaseHashMap()!=null) {
                if (filterObject.getDatabaseHashMap().containsKey("TEMP")) {
                    if (filterObject.counter % filterObject.getDatabaseHashMap().get("TEMP") == 0 && measurement.getTemp() < -10) {
                        bsonDoc.append("TEMP", measurement.getTemp());
                        toDatabase = true;
                    }
                }
                if (filterObject.getDatabaseHashMap().containsKey("DEWP")) {
                    if (filterObject.counter % filterObject.getDatabaseHashMap().get("DEWP") == 0) {
                        bsonDoc.append("DEWP", measurement.getDewp());
                        toDatabase = true;
                    }
                }
                if (filterObject.getDatabaseHashMap().containsKey("STP")) {
                    if (filterObject.counter % filterObject.getDatabaseHashMap().get("STP") == 0) {
                        bsonDoc.append("STP", measurement.getStp());
                        toDatabase = true;
                    }
                }
                if (filterObject.getDatabaseHashMap().containsKey("SLP")) {
                    if (filterObject.counter % filterObject.getDatabaseHashMap().get("SLP") == 0) {
                        bsonDoc.append("SLP", measurement.getSlp());
                        toDatabase = true;
                    }
                }
                if (filterObject.getDatabaseHashMap().containsKey("VISIB")) {
                    if (filterObject.counter % filterObject.getDatabaseHashMap().get("VISIB") == 0) {
                        bsonDoc.append("VISIB", measurement.getVisib());
                        toDatabase = true;
                    }
                }
                if (filterObject.getDatabaseHashMap().containsKey("WDSP")) {
                    if (filterObject.counter % filterObject.getDatabaseHashMap().get("WDSP") == 0) {
                        bsonDoc.append("WDSP", measurement.getWdsp());
                        toDatabase = true;
                    }
                }
                if (filterObject.getDatabaseHashMap().containsKey("PRCP")) {
                    if (filterObject.counter % filterObject.getDatabaseHashMap().get("PRCP") == 0) {
                        bsonDoc.append("PRCP", measurement.getPrcp());
                        toDatabase = true;
                    }
                }
                if (filterObject.getDatabaseHashMap().containsKey("SNDP")) {
                    if (filterObject.counter % filterObject.getDatabaseHashMap().get("SNDP") == 0) {
                        bsonDoc.append("SNDP", measurement.getSndp());
                        toDatabase = true;
                    }
                }
                if (filterObject.getDatabaseHashMap().containsKey("FRSHTT")) {
                    if (filterObject.counter % filterObject.getDatabaseHashMap().get("FRSHTT") == 0) {
                        bsonDoc.append("FRSHTT", measurement.getFrshtt());
                        toDatabase = true;
                    }
                }
                if (filterObject.getDatabaseHashMap().containsKey("CLDC")) {
                    if (filterObject.counter % filterObject.getDatabaseHashMap().get("CLDC") == 0) {
                        bsonDoc.append("CLDC", measurement.getCldc());
                        toDatabase = true;
                    }
                }
                if (filterObject.getDatabaseHashMap().containsKey("WNDDIR")) {
                    if (filterObject.counter % filterObject.getDatabaseHashMap().get("WNDDIR") == 0) {
                        bsonDoc.append("WNDDIR", measurement.getWnddir());
                        toDatabase = true;
                    }
                }
                if (toDatabase) {
                    //Main.MDBConn.getCollection("measurements").insertOne(bsonDoc);
                    //System.out.println("mongo.");
                }
            }
        }
    }

    /**
     * SQL query which displays the average windspeed in the Netherlands
     */
    @Deprecated
    public static void getAverageWindspeed() {
        if (Main.SQLConn == null){
            System.out.println("SQL error! on nl.jozefbv.weatherx.Transfer() getAverageWindspeed()!");
            return;
        }

        try {
            Statement stmt = Main.SQLConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT AVG('wdsp') FROM measurements JOIN stations ON measurements.stn = stations.stn WHERE country = 'NETHERLANDS';");

            while (rs.next()) {
                double avgWindspeed = rs.getDouble("wdsp");
                System.out.println(avgWindspeed + "\n");
            }
        } catch (SQLException sqle) {
            System.out.println("SQL error ");
        }
    }


}
