package nl.jozefbv.weatherx;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by pjvan on 4-11-2015.
 */
public class Initial {


    public static void main(String[] args){
        Properties properties = new Properties();
        InputStream inputStream = null;
        try{
            inputStream = new FileInputStream("default.properties");
            properties.load(inputStream);
            String  databaseTempLocation = properties.getProperty("DefaultDatabaseTemp"),
                    databaseRainLocation = properties.getProperty("DefaultDatabaseRain"),
                    databaseWindLocation = properties.getProperty("DefaultDatabaseWind");
            if(properties.get("databaseTemp")!=""){databaseTempLocation=properties.getProperty("databaseTemp");}
            if(properties.get("databaseRain")!=""){databaseRainLocation=properties.getProperty("databaseRain");}
            if(properties.get("databaseWind")!=""){databaseWindLocation=properties.getProperty("databaseWind");}
            initTemp(databaseTempLocation);
        }
        catch (FileNotFoundException e) {
            try{
                createNewDefault();
                setDefaultDatabaseTemp();
                setDefaultDatabaseRain();
                setDefaultDatabaseWind();
            }
            catch (IOException ioe){
                System.err.println(ioe);
            }
        }
        catch (IOException e){
            System.err.println(e);
        }
    }

    private static void createNewDefault()throws IOException{
        Properties properties = new Properties();
        OutputStream outputStream = null;
        outputStream = new FileOutputStream("default.properties");
        properties.setProperty("DefaultDatabaseTemp", "databaseTemp.properties");
        properties.setProperty("DefaultDatabaseRain", "databaseRain.properties");
        properties.setProperty("DefaultDatabaseWind", "databaseWind.properties");
        properties.setProperty("databaseTemp","");
        properties.setProperty("databaseRain","");
        properties.setProperty("databaseWind","");
        properties.store(outputStream,null);
    }

    private static void setDefaultDatabaseTemp()throws IOException{
        Properties properties = new Properties();
        OutputStream outputStream = null;
        outputStream = new FileOutputStream("databaseTemp.properties");
        properties.setProperty("Latidude","36.59");
        properties.setProperty("Longitude","127.96");
        properties.setProperty("Range","5000");
        properties.setProperty("Condition","-10.00");
        properties.setProperty("Interval","0");
        properties.setProperty("Key[0]","TEMP");
        properties.store(outputStream,null);
    }

    private static void setDefaultDatabaseRain()throws IOException{
        Properties properties = new Properties();
        OutputStream outputStream = null;
        outputStream = new FileOutputStream("databaseRain.properties");
        properties.setProperty("Latitude","36.0000");
        properties.setProperty("Longitude","128.0000");
        properties.setProperty("Range","5000");
        properties.setProperty("Statement","-10.00");
        properties.setProperty("Interval","60");
        properties.setProperty("Key[0]","PRCP");
        properties.store(outputStream,null);
    }

    private static void setDefaultDatabaseWind()throws IOException{
        Properties properties = new Properties();
        OutputStream outputStream = null;
        outputStream = new FileOutputStream("databaseWind.properties");
        properties.setProperty("Statement","-10.00");
        properties.setProperty("Key[0]","WNDDIR");
        properties.setProperty("Key[1]","WDSP");
        properties.setProperty("Interval","600");
        properties.setProperty("Type","Average");
        properties.store(outputStream,null);
    }

    private static void initTemp(String location)throws FileNotFoundException{
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream(location);
        Double longitude = Double.parseDouble((String) properties.get("Longitude"));
        Double latidude = Double.parseDouble((String) properties.get("Latitude"));


        String query =  "SELECT stn, " +
                "           country, " +
                "           ( 6371 * acos ( cos ( radians("+latidude+") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians("+longitude+") ) " +
                "           + sin ( radians("+latidude+") ) * sin( radians( latitude ) ) ) ) " +
                "           AS distance " +
                "        FROM stations  " +
                        "HAVING distance < 5000";
        try {
            Statement statement = Main.SQLConn.createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                Filter.setTempFilter(result.getInt("stn"));
            }
        }
        catch (SQLException sqle){
            System.err.println(sqle);
        }
    }
}
