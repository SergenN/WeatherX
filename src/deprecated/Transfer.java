/*
package deprecated;

import nl.jozefbv.weatherx.Main;
import nl.jozefbv.weatherx.Measurement;

import java.io.*;
import java.sql.Statement;
import java.text.DateFormatSymbols;
import java.util.HashMap;

*/
/**
 * Created by Leon on 25-9-2015.
 * Class for transferring data from the Hashmap to the database
 *//*


*/
/**
 * Created by Leon Wetzel
 * Date of creation 25-9-2015, 12:05
 *
 * Authors: Sergen Nurel, Leon Wetzel,Micha�l van der Veen
 *
 * Version: 1.0
 * Package: default
 * Class: deprecated.Transfer
 * Description:
 * This class is dedicated to tranfering processed or unprocessed data into the database
 *
 * Changelog:
 * 1.0: SQL outfit
 * 2.0: Deprecated SQL transfer() method, implemented MongoDB support
 *//*

public class Transfer {
    private static int tempInterval=1,windInterval=600,rainInterval=60;

    // Measurement object
    private Measurement measurement;

    // Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    public static String databaseRoot = System.getProperty("user.dir");

    // CSV file header
    private static final String FILE_HEADER = "stn,date,time,temp,dewp,stp,slp,visib,wdsp,prcp,sndp,frshtt,cldc,wnddir";

    // table name
    private static final String MEASUREMENT = "measurements";

    public Transfer(Measurement measurement) {
        this.measurement = measurement;
    }

    */
/**
     * This application transfers data from the Measurement object into the database
     * @param measurement measurement to push into the database
     *//*

    public void store(Measurement measurement) {
        transferSQL(measurement);
    }


    public static synchronized void storeData(Measurement measurement,HashMap<String,Integer> database){
        //System.out.println("StoreData asked");
        for(String data:database.keySet()){
            //System.out.println(data);
            int counted;
            switch (data) {
                case "TEMP":
                    if ((database.get("TEMP") % tempInterval) == 0) {
                        //System.out.println(database.get("TEMP")%tempInterval+""+database.get("TEMP"));
                        storeTemp(measurement);
                    }
                    counted = database.get("TEMP");
                    database.put("TEMP", counted + 1);
                    break;
                case "PRCP":
                    if ((database.get("PRCP") % rainInterval )== 0) {
                        //System.out.println((database.get("PRCP")%rainInterval)+" "+database.get("PRCP"));
                        storeRain(measurement);
                    }
                    database.put("PRCP", database.get("PRCP") + 1);
                    break;
                case "WDSP":
                    counted = database.get("WDSP");
                    if ((database.get("WDSP") % windInterval)== 0) {
                        //System.out.println("Storring  WDSP");
                        storeWind(measurement);
                    }
                    database.put("WDSP",counted + 1);
                    break;
                default:
                    break;
            }
        }
    }

    private static void storeTemp(Measurement measurement) {
        if(measurement.getTemp()<-10) {
            FileWriter fileWriter = null;
            String[] date = measurement.getDate().split("-");
            String month = new DateFormatSymbols().getMonths()[Integer.parseInt(date[1])];
            try {
                //fileWriter.append("stn,country,name,TEMP,Time,Date");
                FileWriter cvsFile = new FileWriter(databaseRoot + "/database/" + date[0] + "/" + month + "/" + date[2] + "/temperature.csv", true);
                cvsFile.append(measurement.getDate());
                cvsFile.append(COMMA_DELIMITER);
                cvsFile.append(measurement.getTime());
                cvsFile.append(COMMA_DELIMITER);
                cvsFile.append(String.valueOf(measurement.getStn()));
                cvsFile.append(COMMA_DELIMITER);
                cvsFile.append(String.valueOf(measurement.getTemp()));
                cvsFile.append(NEW_LINE_SEPARATOR);
                cvsFile.flush();
                cvsFile.close();

            } catch (FileNotFoundException e) {
                folderCheck(measurement);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
    private static void storeRain(Measurement measurement){
        FileWriter fileWriter = null;
        String[] date = measurement.getDate().split("-");
        String month = new DateFormatSymbols().getMonths()[Integer.parseInt(date[1])];
        try {
            //fileWriter.append("stn,country,name,TEMP,Time,Date");
            FileWriter cvsFile = new FileWriter(databaseRoot + "/database/" + date[0] + "/" + month + "/" + date[2] + "/rainfall.csv", true);
            cvsFile.append(measurement.getDate());
            cvsFile.append(COMMA_DELIMITER);
            cvsFile.append(String.valueOf(measurement.getTime()));
            cvsFile.append(COMMA_DELIMITER);
            cvsFile.append(String.valueOf(measurement.getStn()));
            cvsFile.append(COMMA_DELIMITER);
            cvsFile.append(String.valueOf(measurement.getPrcp()));
            cvsFile.append(NEW_LINE_SEPARATOR);
            cvsFile.flush();
            cvsFile.close();

        } catch (FileNotFoundException e) {
            folderCheck(measurement);
        }
        catch (IOException e){
            System.err.println(e);
        }
    }
    private static void storeWind(Measurement measurement){
        FileWriter fileWriter = null;
        String[] date = measurement.getDate().split("-");
        String month = new DateFormatSymbols().getMonths()[Integer.parseInt(date[1])];
        try {
            //fileWriter.append("stn,country,name,TEMP,Time,Date");
            FileWriter cvsFile = new FileWriter(databaseRoot + "/database/" + date[0] + "/" + month + "/" + date[2] + "/wind.csv", true);
            cvsFile.append(measurement.getDate());
            cvsFile.append(COMMA_DELIMITER);
            cvsFile.append(String.valueOf(measurement.getTime()));
            cvsFile.append(COMMA_DELIMITER);
            cvsFile.append(String.valueOf(measurement.getStn()));
            cvsFile.append(COMMA_DELIMITER);
            cvsFile.append(String.valueOf(measurement.getWdsp()));
            cvsFile.append(COMMA_DELIMITER);
            cvsFile.append(String.valueOf(measurement.getWnddir()));
            cvsFile.append(NEW_LINE_SEPARATOR);
            cvsFile.flush();
            cvsFile.close();

        } catch (FileNotFoundException e) {
            System.err.println(e);
            folderCheck(measurement);

        }
        catch (IOException e){
            System.err.println(e);
        }
    }



    private static void folderCheck(Measurement measurement) {
        String[] dateArray = measurement.getDate().split("-");
        String month = new DateFormatSymbols().getMonths()[Integer.parseInt(dateArray[1])];
        String path = databaseRoot + "/database/"+dateArray[0]+"/"+month+"/"+dateArray[2]+"/wind.csv";
        File file = new File(path);
        if(!file.exists()) {
            path = databaseRoot + "/database";
            file = new File(path);
            if (!file.exists()) {
                System.out.println("Creating Database Directory in: " + path);
                file.mkdir();
            }
            dateArray = measurement.getDate().split("-");


            file = new File(path += "/" + dateArray[0]);
            if (!file.exists()) {
                System.out.println("Creating new Year Directory");
                file.mkdir();
            }


            file = new File(path += "/" + month);
            if (!file.exists()) {
                System.out.println("Creating new Month Directory");
                file.mkdir();
            }
            file = new File(path += "/" + dateArray[2]);
            if (!file.exists()) {
                System.out.println("Creating new Day Directory");
                file.mkdir();
            }
            file = new File(path + "temperature.csv");
            if (!file.exists()) {
                createTemp(path);
            }
            file = new File(path + "rainfall.csv");
            if (!file.exists()) {
                createRain(path);
            }
            file = new File(path + "wind.csv");
            if (!file.exists()) {
                createWind(path);
            }
        }
    }

    private static void createTemp(String path){
        System.out.println("Creating new temperature.csv");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(path+"/temperature.csv");
            fileWriter.append("DATE");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("TIME");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("STATION_NUMBER");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("TEMPERATURE");
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
        }
    }
    private static void createRain(String path){
        System.out.println("Creating new temperature.csv");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(path+"/rainfall.csv");
            fileWriter.append("DATE");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("TIME");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("STATION_NUMBER");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("PRECIPITATION");
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
        }
    }
    private static void createWind(String path){
        System.out.println("Creating new temperature.csv");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(path+"/wind.csv");
            fileWriter.append("DATE");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("TIME");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("STATION_NUMBER");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("WIND SPEED");
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("WIND DIRECTION");
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
        }
    }

    */
/**
     * Method to transfer the data from the Measurement object into the database
     *//*

    @Deprecated
    public void transferSQL(Measurement measurement) {
        if (Main.sqlConnection == null){
            System.out.println("SQL error! on deprecated.Transfer()");
            return;
        }
        try {
            Statement statement = Main.sqlConnection.createStatement();

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

    */
/**
     * Method for inserting data in a CSV file
     *//*

*/
/*    @Deprecated
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

    */
/**
     * Method for inserting data in a Mongo database
     *//*

*/
/*    @Deprecated
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

    */
/**
     * SQL query which displays the average windspeed in the Netherlands
     *//*

 */
/*   @Deprecated
    public static void getAverageWindspeed() {
        if (Main.SQLConn == null){
            System.out.println("SQL error! on deprecated.Transfer() getAverageWindspeed()!");
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
    }*//*



}
*/
