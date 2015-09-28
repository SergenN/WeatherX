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
        new Transfer(measurement).transfer();
    }

    /**
     * transfer
     * Method to transfer the data from the Measurement object into the database
     */
    public void transfer() {
        if (Main.conn == null){
            System.out.println("SQL error! on Transfer()");
            return;
        }

        try {
            Statement statement = Main.conn.createStatement();

            String query = "INSERT INTO " + MEASUREMENT + " VALUES ("
                            + "'"+ measurement.getStn() + "'"+ ",'" + measurement.getDate() + "','" + measurement.getTime() + "'," + measurement.getTemp() + ","
                            + measurement.getDewp() + "," + measurement.getStp() + "," + measurement.getSlp() + "," + measurement.getVisib() + "," + measurement.getWdsp() + ","
                            + measurement.getPrcp() + "," + measurement.getSndp() + ",'" + measurement.getFrshtt() + "'," + measurement.getCldc() + "," + measurement.getWnddir() + ")";
            System.out.println(query);
            statement.executeUpdate(query);
        } catch (SQLException sqle) {
            System.out.println("SQL error! on Query()");
            sqle.printStackTrace();
        }
    }


}
