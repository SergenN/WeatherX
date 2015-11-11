package nl.jozefbv.weatherx;

import org.eclipse.jetty.websocket.api.Session;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Michaël van der Veen on 29-10-2015.
 */
public class FilterObject {
    private boolean database;                           // boolean for getting into the database
    private HashMap<String, Integer> databaseHashMap;   //HashMap for measures with delay to database
    private HashMap<Session, String[]> sessionHashMap;  //HashMap for Session getting value.
    private ArrayList<UUID> countryHashMap;
    String country,stationName;

    private Measurements measure;
    public int counter = 0;

    public FilterObject(Long stn) {
        databaseHashMap = new HashMap<>();
        sessionHashMap = new HashMap<>();
        countryHashMap = new ArrayList<UUID>();
        database = false;
        measure = null;
        init(stn);
    }

    private void init(Long stn){
        try {
            String query = "SELECT `country`,`name` FROM `stations` WHERE `stn`='"+stn.toString()+"'";
            Statement statement = Main.SQLConn.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()){
                setCountry(result.getString("country"));
                setStationName(result.getString("name"));
            }
        }
        catch (java.sql.SQLException e){
            System.err.println(e);
        }
    }



    public void setSession(Session session, String[] args){
        sessionHashMap.put(session,args);
    }
    public void setDatabase(String key,Integer delay){
                         databaseHashMap.put(key,delay);
    }
    public void setCountryDatabase(UUID uuid){
                countryHashMap.add(uuid);
    }
    public HashMap<String, Integer> getDatabaseHashMap(){
        return databaseHashMap;
    }
    public ArrayList<UUID> getCountryHashMap(){return countryHashMap;}
    public HashMap<Session,String[]> getSessionHashMap(){return sessionHashMap;}
    public Measurements getMeasure(){return measure;}
    public void setMeasure(Measurements measure){
        this.measure = measure;
    }
    public void setCountry(String country){this.country=country;}
    public void setStationName(String stationName){this.stationName=stationName;}
    public String getCountry(){return country;}
    public String getStationName(){return stationName;}
}
