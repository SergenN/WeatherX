import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by Leon Wetzel
 * Date of creation 25-9-2015, 14:21
 *
 * Authors: Leon Wetzel
 *
 * Version: 1.0
 * Package: default
 * Class: Transfer
 * Description:
 * This class sends the data from Measurements objects to the database
 *
 * Changelog:
 *
 */
public class Transfer {

    // Measurement object
    private Measurements measurements = null;
    private static Connection conn = null;

    // Table name
    private static final String MEASUREMENT = "Measurement";

    public Transfer(Measurements measurements) {
        try {
            // submission to database
            this.measurements = measurements;
            transfer(measurements);
        } catch (SQLException sqle) {
            System.out.println("Could not connect to database." + sqle.getMessage());
        }
    }

    /**
     * This application transfers data from the Measurement object into the database
     * @param args
     */
    public static void main(String[] args) {

    }

    /**
     * Method to transfer the data from the Measurement object into the database
     */
    public static void transfer(Measurements measurements) throws SQLException {

        // reset Statement and Connection objects
        Statement statement = null;
        Connection conn = null;

        try {
            // create a Statement from the connection
            conn = connect();
            statement = conn.createStatement();

            // prepare statement for execution
            String s = "INSERT INTO " + MEASUREMENT + "(STN, Date, Time, Temp, Dewp, STP, SLP, VISIB, WDSP, PRCP, SNDP, FRSHTT, CLDC, WNDDIR) " + "VALUES" + "( " + measurements.getStn() + "," + measurements.getDate() + "," + measurements.getTime() + "," + measurements.getTemp() + "," + measurements.getDewp() + "," + measurements.getStp() + "," + measurements.getSlp() + "," + measurements.getVisib() + "," + measurements.getWdsp() + "," + measurements.getPrcp() + "," + measurements.getSndp() + "," + measurements.getFrshtt() + "," + measurements.getCldc() + "," + measurements.getWnddir() + ")";

            // send data to database
            statement.executeUpdate(s);

        } catch (SQLException sqle) {
            // connection could not be established
            System.out.println("Could not connect to database." + sqle.getMessage());
        } finally {
            // apply close() method for Statement and Connection objects
            closeObjects(statement, conn);
        }


    }

    /**
     * Method to connect to database
     */
    public static Connection connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "user","pass");
            return conn;
        } catch (SQLException sqle) {
            System.out.println("Could not connect to database.");
            return conn;
        }
    }

    /**
     * Method to close both a Statement and Connection object.
     * @param statement
     * @param conn
     */
    public static void closeObjects(Statement statement, Connection conn) throws SQLException {
        try {
            // close statement if statement object has been used
            if (statement != null) {
                statement.close();
            }

            // close connection if Connection object has been used
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException sqle) {
            System.out.println("Could not connect to database." + sqle.getMessage());
        }
    }
}
