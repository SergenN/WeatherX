package nl.jozefbv.weatherx;

import java.io.*;
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
        properties.setProperty("Latidude","36.0000");
        properties.setProperty("Longitude","128.0000");
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
        properties.setProperty("Latidude","36.0000");
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

    private static void initTemp(String location){

    }
}
