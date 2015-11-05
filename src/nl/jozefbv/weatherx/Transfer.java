package nl.jozefbv.weatherx;

import org.bson.Document;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
            //System.out.println(query);
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
        bsonDoc.append("stn", measurement.getStn());
        bsonDoc.append("date", measurement.getDate());
        bsonDoc.append("time", measurement.getTime());
        bsonDoc.append("temp", measurement.getTemp());
        bsonDoc.append("dewp", measurement.getDewp());
        bsonDoc.append("stp", measurement.getStp());
        bsonDoc.append("slp", measurement.getSlp());
        bsonDoc.append("visib", measurement.getVisib());
        bsonDoc.append("wdsp", measurement.getWdsp());
        bsonDoc.append("prcp", measurement.getPrcp());
        bsonDoc.append("sndp", measurement.getPrcp());
        bsonDoc.append("frshtt", measurement.getFrshtt());
        bsonDoc.append("cldc", measurement.getCldc());
        bsonDoc.append("wnddir", measurement.getWnddir());

        Main.MDBConn.getCollection("measurements").insertOne(bsonDoc);

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
