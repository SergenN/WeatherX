/*
package deprecated;

import deprecated.Measurements;
import nl.jozefbv.weatherx.Main;
import org.eclipse.jetty.websocket.api.Session;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

*/
/**
 * Created by Micha�l van der Veen
 * Date of creation 29-10-2015
 *
 * Authors: Micha�l van der Veen,
 *
 * Version: 1.5
 * Package: default
 * Class: nl.jozefbv.weatherx.Corrector
 * Description:
 * This class takes care of the handling of data into the database or session data requests.
 * In this class there is a check to see if the incoming measurements are listed to be send to a session or database.
 *
 * Commando's reveived from WebClientConn will be executed here.
 *
 *
 * Changelog:
 *  1.5 Documentation added
 *
 *
 *//*


public class FilterObject {
    private boolean database;                           // boolean for getting into the database
    private HashMap<String, Integer> databaseHashMap;   //HashMap for measures with delay to database
    private HashMap<Session, String[]> sessionHashMap;  //HashMap for Session getting value.
    private ArrayList<UUID> countryHashMap;
    String country,stationName;

    private Measurements measure;
    public int counter = 0;

    */
/**
     * Initializing the filterObject
     * @param stn station id
     *//*

    public FilterObject(Long stn) {
        databaseHashMap = new HashMap<>();
        sessionHashMap = new HashMap<>();
        countryHashMap = new ArrayList<UUID>();
        database = false;
        measure = null;
        init(stn);
    }

    */
/**
     * Continue initializing
     * load the default values: CountryName, StationName,
     * @param stn station id
     *//*

    private void init(Long stn){
        try {
            String query = "SELECT `country`,`name` FROM `stations` WHERE `stn`='"+stn.toString()+"'";
            Statement statement = Main.sqlConnection.createStatement();
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


    */
/**
     * adding a session to be handled by Filter class.
     * sessions listed here will be receiving data on new measures.
     * @param session session of request
     * @param args requested parameters
     *//*

    public void setSession(Session session, String[] args){
        sessionHashMap.put(session,args);
    }

    */
/**
     * set new parameter for database
     * @param key parameter
     * @param delay delay in measures (1 measure = 1 second)
     *//*

    public void setDatabase(String key,Integer delay){
        databaseHashMap.put(key,delay);
    }

    */
/**
     * adding a country's unique ID to be handled by Filter class.
     * @param uuid unique country requests ID
     *//*

    public void setCountryDatabase(UUID uuid){
                countryHashMap.add(uuid);
    }

    */
/**
     * @return list of database parameters with counter
     *//*

    public HashMap<String, Integer> getDatabaseHashMap(){
        return databaseHashMap;
    }

    */
/**
     *
     * @return countryHashMap
     *//*

    public ArrayList<UUID> getCountryHashMap(){
        return countryHashMap;
    }

    */
/**
     *
     * @return sessionHashMap
     *//*

    public HashMap<Session,String[]> getSessionHashMap(){
        return sessionHashMap;
    }

    */
/**
     *
     * @param country set country name
     *//*

    public void setCountry(String country){this.country=country;}

    */
/**
     *
     * @param stationName set Station name
     *//*

    public void setStationName(String stationName){this.stationName=stationName;}

    */
/**
     *
     * @return return country name
     *//*

    public String getCountry(){return country;}

    */
/**
     *
     * @return station name
     *//*

    public String getStationName(){return stationName;}
}
*/
