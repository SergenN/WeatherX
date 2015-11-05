package nl.jozefbv.weatherx;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bson.Document;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
 * 2.0: Deprecated SQL transfer() method, implementig MongoDB support
 */
public class Transfer {
    // Measurement object
    private Measurements measurement;

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
        new Transfer(measurement).transferMongo();
    }

    /**
     * transfer
     * Method to transfer the data from the Measurement object into the database
     */
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
     * Method for inserting data in a Mongo database
     */
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
                    Main.MDBConn.getCollection("measurements").insertOne(bsonDoc);
                    System.out.println("Sended to Mongo");
                }
            }
        }
    }

    /**
     * SQL query which displays the average windspeed in the Netherlands
     */
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
