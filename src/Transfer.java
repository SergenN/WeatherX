import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by Leon on 25-9-2015.
 * Class for transferring data from the Hashmap to the database
 */
public class Transfer {

    // Measurement object
    private Measurements measurement;
    private static Connection conn = null;

    // table name
    private static final String MEASUREMENT = "Measurement";

    public Transfer(Measurements measurement) {
        this.measurement = measurement;
    }

    /**
     * This application transfers data from the Measurement object into the database
     * @param args
     */
    public static void main(String[] args) {
        // connect to database
        connect();

        //decompose(hashMap);
    }

    /**
     * Method to send decomposed data to the database

    public void send(Connection conn) {
        try {
            // create a Statement from the connection
            Statement statement = conn.createStatement();

            statement.executeUpdate("INSERT INTO " + TABLE + " VALUES ()");
        } catch (SQLException sqle) {
            System.out.println("SQL error!");
        }
    }
     */

    /**
     * Method to decompose the data from the hashmap

    public static void decompose(HashMap<String, String> hashMap) {
        // itereer over de hashmap
        Iterator it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());

            it.remove();
        }
    }
    */

    /**
     * transfer
     * Method to transfer the data from the Measurement object into the database
     * @param conn open SQL connection
     */
    public void transfer(Connection conn) {
        // itereer over de hashmap
        try {
            // create a Statement from the connection
            Statement statement = conn.createStatement();

            String query = "INSERT INTO " + MEASUREMENT + " VALUES ("
                            + measurement.getStn() + "," + measurement.getDate() + "," + measurement.getTime() + "," + measurement.getTemp() + ","
                            + measurement.getDewp() + "," + measurement.getStp() + "," + measurement.getSlp() + "," + measurement.getVisib() + "," + measurement.getWdsp() + ","
                            + measurement.getPrcp() + "," + measurement.getSndp() + "," + measurement.getFrshtt() + "," + measurement.getCldc() + "," + measurement.getWnddir() + ")";

            // send data to database
            statement.executeUpdate(query);
        } catch (SQLException sqle) {
            System.out.println("SQL error!");
        }
    }

    /**
     * Method to connect to database
     */
    public static void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "user","pass");
        } catch (SQLException sqle) {
            System.out.println("Could not connect to database.");
        }
    }
}
