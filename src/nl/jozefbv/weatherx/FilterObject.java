package nl.jozefbv.weatherx;

import org.eclipse.jetty.websocket.api.Session;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created by Michaël van der Veen on 29-10-2015.
 */
public class FilterObject {
    private boolean database;                           // boolean for getting into the database
    private HashMap<String[], Integer> databaseHashMap; //HashMap for STN with delay to database
    private HashMap<Session, String[]> sessionHashMap;  //HashMap for Session getting value.

    private Double longi, lati;
    private String country;
    private Measurements measure;

    public FilterObject(Long stn) {
        databaseHashMap = new HashMap<>();
        sessionHashMap = new HashMap<>();
        database = false;
        measure = null;
        init(stn);
        //database_init();
    }

    private void init(Long stn){
        try {
            String query = "SELECT `country`,`latitude`,`longitude` FROM `stations` WHERE `stn`='"+stn.toString()+"'";
            Statement statement = Main.SQLConn.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()){
                setLongiLati(result.getDouble("longitude"),result.getDouble("latitude"));
                setCountry(result.getString("country"));
            }
        }
        catch (java.sql.SQLException e){
            System.err.println(e);
        }
    }



    public void setSession(Session session, String[] args){
        /*if(!sessionHashMap.containsKey(session)){
            sessionHashMap = new HashMap<>();
        }*/
        sessionHashMap.put(session,args);
    }

    public void setDatabase(String[] stn,Integer delay){
        if(databaseHashMap==null){
            databaseHashMap = new HashMap<>();
        }
        databaseHashMap.put(stn,delay);
    }

    public HashMap<String[], Integer> getDatabaseHashMap(){
        return databaseHashMap;
    }

    public HashMap<Session,String[]> getSessionHashMap(){
        return sessionHashMap;
    }
    public void setLongiLati(Double longi,Double lati){this.longi=longi;this.lati=lati;}
    public void setCountry(String country){this.country=country;}
    public Measurements getMeasure(){return measure;}
    public void setMeasure(Measurements measure){
        this.measure = measure;
    }
    public String getCountry(){return country;}

}
