import java.sql.*;

/**
 * Created by Sergen Nurel
 * Date of creation 25-9-2015, 12:05
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: default
 * Class: Main
 * Description:
 * This class is the main class of the project
 *
 * Changelog:
 * 1.0: class created and added thread calling
 */
public class Main {

    public static java.sql.Connection conn;

    /**
     * Main
     * In this method the server thread is started.
     * @param args, arguments given to the main method
     */
    public static void main(String[] args) {
        Main.conn = connect();
        new Thread(new Server()).start();
    }

    /**
     * Method to connect to database
     */
    public static java.sql.Connection connect() {
        try {
            String  url = "jdbc:mysql://localhost:3306/",
                    dbName="unwdmi",
                    driver="com.mysql.jdbc.Driver",
                    userName="root",
                    passWord="";
            Class.forName(driver).newInstance();
            return DriverManager.getConnection(url+dbName,userName,passWord);
        } catch (SQLException sqle) {
            System.out.println("Could not connect to database.");
            System.err.println(sqle);
            return null;
        }
        catch (ClassNotFoundException cnfe){
            System.err.println(cnfe);
            return null;
        }
        catch (InstantiationException ie){
            System.err.println(ie);
            return null;
        }
        catch (IllegalAccessException iae){
            System.err.println(iae);
            return null;
        }

    }
}
