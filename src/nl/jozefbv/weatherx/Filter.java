package nl.jozefbv.weatherx;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Michaël van der Veen
 * Date of creation 29-10-2015
 *
 * Authors: Michaël van der Veen,
 *
 * Version: 2
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
 * 2 completion of Filter Class Added documentation
 *
 *
 */

public class Filter {
    private static HashMap<Long,FilterObject> filteredStation;              //ID + FilterObject
    private static HashMap<Session,ArrayList<Long>> sessionStationList;     //Session+ ID's
    private static HashMap<Session,ArrayList<UUID>> sessionCountryList;     //Session+CountriesID's
    private static HashMap<UUID,FilterCountry> filteredCountries;           //CountriesID + FilteredCountry
    private static ArrayList<Long> coastLine;                               //Pacific coastline stations
    private static ArrayList<String> countryList;                           //List of countries in the world, that has a weatherstation.

    /**
     * Initializing Filter by creating new HashMaps and ArrayLists.
     * These are necessary to be used for retreiving data, and updating the current sessions that request these data.
     */
    public Filter() {
        sessionStationList = new HashMap<Session,ArrayList<Long>>();
        sessionCountryList = new HashMap<Session,ArrayList<UUID>>();
        filteredStation = new HashMap<Long,FilterObject>();
        filteredCountries = new HashMap<UUID,FilterCountry>();
        coastLine = new ArrayList<>();
        countryList = new ArrayList<>();
    }

    /**
     * The Method that adds a stations number into the list, to let the future measurements be parsed through the sessions.
     * In this method individual stations can be set to a session.
     * The WebClientConn class would send a session and a array of arguments.
     * The arguments would be split into multiple values.
     * The mask of a argument can be: (pipeline separated in the example)
     * GET|STATION_NUMBER(S)|ARGUMENTS(S/optional)
     * The (S) values can be a comma separated array.
     * STATION_NUMBERS can be multiple, and for every STATION_NUMBER a loop will be completed. Arguments set would be shared.
     * ARGUMENTS can be multiple, and for every ARGUMENT a Filter would be set. Saving only the arguments that the clients need,
     *          to be handled in the future. These arguments are ment to be for specific data.
     * If the (specific)Arguments are empty. Then fill the arguments in with all the measurements that can be sended.
     * Inside the method there is a check if a STATION_NUMBER is already set into the filteredStation Hashmap.
     * If there is already a list, then add to this list the session and arguments that the session needs.
     * Else send a weatherStation not found reply.
     * @param session session of request
     * @param args commando's send
     */
    public static void sendData(Session session, String[] args) {
        //while(args[1]!="Stop"){
            try {
                String[] stn = args[1].split(",");
                for(int i=0;i<stn.length;i++) {
                    Long stnId = Long.parseLong(stn[i], 10);
                    if (filteredStation.containsKey(stnId)) {
                        String[] arg = null;
                        try{
                            arg = args[2].split(",");
                        }catch(ArrayIndexOutOfBoundsException e){
                            arg = new String[12];
                            arg[0]="DATE";arg[1]="WNDDIR";arg[2]="TEMP"; arg[3]="DEWP";
                            arg[4]="SLP"; arg[5]="STP";   arg[6]="VISIB";arg[7]="WDSP";
                            arg[8]="PRCP";arg[9]="SNDP";  arg[10]="CLDC";arg[11]="FRSHTT";
                        }
                        FilterObject filterObject = (FilterObject) filteredStation.get(stnId);
                        filterObject.setSession(session, arg);
                        if (sessionStationList.containsKey(session)) {
                            ArrayList list = sessionStationList.get(session);
                            list.add(stnId);
                        } else {
                            ArrayList list = new ArrayList();
                            list.add(stnId);
                            sessionStationList.put(session, list);
                        }
                        filteredStation.put(stnId,filterObject);
                    } else {
                        session.getRemote().sendString("Weatherstation not found." + stn[i]);
                    }
                }
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
    }

    /**
     * This method is to handle session request to stop data to be sended to the session.
     * Sessions would be searched and removed from the hashmaps, to be not participating into the future.
     * @param session session of request
     */
    public static void stopData(Session session) {
        try{
            if(sessionCountryList.containsKey(session)){
                ArrayList countries = sessionCountryList.get(session);
                for(int i=0;i<countries.size();i++){

                    FilterCountry filteredCountry =  filteredCountries.get(countries.get(i));
                    ArrayList<Long> stns = filteredCountry.getWeatherStationHashMap();
                    for(Long stnId:stns){
                        FilterObject filterObject = filteredStation.get(stnId);
                        ArrayList<UUID> uuids = filterObject.getCountryHashMap();
                        uuids.remove(countries.get(i));
                    }
                }
                sessionCountryList.remove(session);
            }
            if(sessionStationList.containsKey(session)){
                ArrayList stn = sessionStationList.get(session);
                for(int i=0;i<stn.size();i++ ){
                    FilterObject filterObject =  filteredStation.get(stn.get(i));
                    HashMap sessionHashmap = filterObject.getSessionHashMap();
                    sessionHashmap.remove(session);
                }
            }else{
                session.getRemote().sendString("Weatherstation not found");
            }
            System.out.print("Cleared empty links");
        }

        catch(IOException e){
            System.err.println(e);
        }
    }

    /**
     * The most important method in this class.
     * Checking if a incoming measure should be send and/or saved into a session/database.
     * These checks would be done by searching the hashmaps for keys and arguments.
     * The values that needs to be checked are saved into FilterObject.
     * @param measure incoming measurement
     */
    public static void checkData(Measurements measure) {
        if(filteredStation.containsKey(measure.getStn())){
            FilterObject filterObject;
            filterObject = filteredStation.get(measure.getStn());
            HashMap<Session,String[]> sessionStationHashMap = filterObject.getSessionHashMap();
            ArrayList<UUID> countrylist = filterObject.getCountryHashMap();
            for(int i = 0; i<countrylist.size();i++){
                UUID uuid = countrylist.get(i);
                FilterCountry filterCountry = filteredCountries.get(uuid);
                filterCountry.addMeasure(measure);
            }
            sendData(sessionStationHashMap,measure);
            HashMap<String,Integer> databashHashMap =filterObject.getDatabaseHashMap();
            Transfer.storeData(measure,databashHashMap);

            filterObject.counter++;
        }else{
            System.out.println("New Station " + measure.getStn());
            FilterObject filterObject = new FilterObject(measure.getStn());
            filteredStation.put(measure.getStn(),filterObject);
        }
    }

    /**
     * Sending the single station data in JSON format to the WebClient.
     * For every session in the sessionHashMap, is a new reply to there session with the measures requested.
     * The requested parameters would be handled in getData;
     * @param sessionHashMap HashMap of sessions that are listed to get data from the Measure.
     * @param measure The Measure that is received.
     */
    private static void sendData(HashMap<Session,String[]> sessionHashMap,Measurements measure){
        for(Session session : sessionHashMap.keySet()) {
            String line = "{" +
                    "\"TYPE\":\"STN\"," +
                    "\"STN\":\""  + measure.getStn()  + "\"," +
                    "\"TIME\":\"" + measure.getTime() + "\","+
                    "\"COUNTRY\":\"" + filteredStation.get(measure.getStn()).getCountry() + "\"," +
                    "\"NAME\":\""    + filteredStation.get(measure.getStn()).getStationName()+"\""
                    ;
            String[] args = sessionHashMap.get(session);
            line = getData(measure, args, line);
            line += "}";
            try {
                session.getRemote().sendStringByFuture(line);
            } catch (WebSocketException w) {
                System.err.println("socket has closed: bye bye.");
                Filter.stopData(session);
            }
        }
    }

    /**
     * Filling the JSON line with the parameters requested from the session.
     * @param measure The Measure that is received
     * @param args  The Arguments that are needed from measurement
     * @param line  the JSON line that would be updated.
     * @return String line
     */
    private static String getData(Measurements measure,String[] args,String line) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "DATE":
                    line += ",\"DATE\":\"" + measure.getDate() + "\"";
                    break;
                case "TEMP":
                    line += ",\"TEMP\":\"" + measure.getTemp() + "\"";
                    break;
                case "DEWP":
                    line += ",\"DEWP\":\"" + measure.getDewp() + "\"";
                    break;
                case "STP":
                    line += ",\"STP\":\"" + measure.getStp() + "\"";
                    break;
                case "SLP":
                    line += ",\"SLP\":\"" + measure.getSlp() + "\"";
                    break;
                case "VISIB":
                    line += ",\"VISIB\":\"" + measure.getVisib() + "\"";
                    break;
                case "WDSP":
                    line += ",\"WDSP\":\"" + measure.getWdsp() + "\"";
                    break;
                case "PRCP":
                    line += ",\"PRCP\":\"" + measure.getPrcp() + "\"";
                    break;
                case "SNDP":
                    line += ",\"SNDP\":\"" + measure.getSndp() + "\"";
                    break;
                case "CLDC":
                    line += ",\"CLDC\":\"" + measure.getCldc() + "\"";
                    break;
                case "WNDDIR":
                    line += ",\"WNDDIR\":\"" + measure.getWnddir() + "\"";
                    break;
                case "FRSHTT":
                    line += ",\"FRSHTT\":\"" + measure.getFrshtt() + "\"";
                    break;
                default:
                    line += ",\"UNKNOWN\":\"" + measure.getStn() + "\"";
                    break;
            }
        }
        return line;
    }

    /**
     * set a Database filter to save data into the database.
     * With the requested parameters and time delay for saving data.
     * @param stn station ID
     * @param value Measure that needs to be saved
     * @param delay Delay of saving measure
     */
    public static void setFilter(int stn,String value,int delay) {
        FilterObject filterObject = null;
        if(filteredStation.containsKey(Long.valueOf(stn))){
            filterObject = filteredStation.get(Long.valueOf(stn));
            filterObject.setDatabase(value,delay);
        }else{
            filterObject = new FilterObject(Long.valueOf(stn));
            filterObject.setDatabase(value,delay);
            filteredStation.put(Long.valueOf(stn),filterObject);
        }
    }

    /**
     * Request a group of stations to send a shared or individual request of data.
     * Inside the args parameter would be countries named.
     * To receive which countries has which stations, a SQL request would be made.
     * The request would be done by a prepared statement.
     * The arguments are send by the WebClientConn.
     * Arguments can be split in: (Pipelined example)
     * GET_COUNTRY  |COUNTRY_NAME(S)        |ARGUMENT(S/optional)|METHOD
     * GET_COAST    |COUNTRY_NAMES(prepared)|ARGUMENT(S/optional)|METHOD
     * GET_WORLD    |COUNTRY_NAMES(ALL)     |ARGUMENT(S/optional)|METHOD(AVG)
     * The (S) values can be a comma separated array.
     * The prepared and ALL values are predefined.
     * COUNTRY_NAME can be multiple, and for every COUNTRY_NAME a loop will be completed. Arguments set would be shared.
     * ARGUMENTS can be multiple, and for every ARGUMENT a Filter would be set. Saving only the arguments that the clients need,
     *          to be handled in the future. These arguments are mend to be for specific data.
     * If the (specific)Arguments are empty. Then fill the arguments in with all the measurements that can be send.
     * METHOD can be set by AVG (average) or RAW (raw unedited data)
     * Inside the method there is a unique identifier that is the key of the FilterCountry object.
     * @param session Session of request
     * @param args parameters of measurements needed
     */
    public static void sendCountry(Session session, String[] args) {
        try {
            args[1]=args[1].replaceAll("_"," ");
            String[] countries = args[1].split(",");
            for(int a = 0;a<countries.length;a++) {
                String prepared = "(?";
                UUID uuid = UUID.randomUUID();
                String[] country = countries[a].split("&");
                String countryArray = ""+country[0]+"";
                if(country.length>1){
                    for (int i = 1; i < country.length; i++) {
                        countryArray += ","+country[i]+"";
                        prepared += ",?";
                    }
                }
                prepared+=")";
                FilterCountry filterCountry = new FilterCountry(session, uuid);
                ArrayList<Long> stns = new ArrayList<Long>();
                filterCountry.setCountry(countryArray);
                String selectmysql = "SELECT stn FROM stations WHERE country IN "+prepared;
                PreparedStatement prep = null;
                try {
                    prep = Main.connectSQL().prepareStatement(selectmysql);
                }
                catch (com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException e){
                    System.err.println(e);
                    try{
                        prep = Main.connectSQL().prepareStatement(selectmysql);
                    }
                    catch (com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException ee){
                        System.err.println("SECOND TIME: "+ee);
                    }
                }
                for(int i =0;i<country.length;i++){
                    prep.setString(i+1,country[i]);
                }
                ResultSet resultSet = prep.executeQuery();
                while (resultSet.next()) {
                    stns.add(resultSet.getLong("stn"));
                }
                if(stns.size()==0){
                    System.out.println("Empty");
                }
                if (args[2].equalsIgnoreCase("RAW")) {
                    // for GET_COUNTRY <COUNTRYNAME> RAW
                    sendCountryRAWOnly(stns, session);
                } else {
                    String[] values = args[2].split(",");
                    if (args[3].equalsIgnoreCase("RAW")) {
                        // for GET_COUNTRY <COUNTRYNAME> <ARGUMENTS> RAW
                        sendCountryRAW(stns, session, args);
                    } else {
                        // For GET_COUNTRY <COUNTRYNAME> <ARGUMENTS> AVG
                        sendCountryAVG(filterCountry, args, values, stns, uuid, session);
                    }
                }
            }
        }
        catch (SQLException e){
            System.err.println(e);
        }
    }

    /**
     * Handling the RAW only data to be send by sendCountry method.
     * For this method the next command was send:
     * GET_COUNTRY  |COUNTRY_NAME(s)    |RAW
     * GET_COAST    | RAW
     * The GET_COUNTRY | COUNTRY_NAME(S) and GET_COAST are replaced by:
     * GET | STATION_NUMBERS
     * after this, the sendData class is set.
     * preparing to send individual replies of every station in this list.
     * @param stns list of Station ids
     * @param session session of request
     */
    private static void sendCountryRAWOnly(ArrayList<Long>stns,Session session){
        String[] arg = new String[2];
        arg[0]="GET";
        boolean first = true;
        String stnids="";
        int k =0;
        while (stns.size()>k) {
            if(first) {
                stnids = ""+stns.get(k);
                first=false;
            }else{
                stnids += ","+stns.get(k);
            }
            k++;
        }
        arg[1]=stnids;
        System.out.println(arg[0]+"|"+arg[1]);
        sendData(session, arg);
    }

    /**
     * Handling the RAW with ARGUMENTS data to be send by sendCountry method.
     * For this method the next command was send:
     * GET_COUNTRY  |COUNTRY_NAME(s) |ARGUMENT(S)   |RAW
     * GET_COAST    |ARGUMENT(S)                    |RAW
     * The GET_COUNTRY | COUNTRY_NAME(S) and GET_COAST are replaced by:
     * GET | STATION_NUMBERS | ARGUMENT(S)
     * after this, the sendData class is set.
     * preparing to send individual replies of every station in this list.
     * @param stns list of Station ids
     * @param session   session of request
     * @param args parameters needed
     */
    private static void sendCountryRAW(ArrayList<Long>stns,Session session,String[] args){
        int l = 0;
        String stnids = "";
        int k = 0;
        boolean first = true;
        while (stns.size() > k) {
            if (first) {
                stnids = "" + stns.get(k);
                first = false;
            } else {
                stnids += "," + stns.get(k);
            }
            k++;
        }
        args[1] = stnids;
        sendData(session, args);
    }

    /**
     * Handling the Average data to be send.
     * putting the arguments, and values of the STATION_NUMBERS into the FilterCountry object.
     * Afterwards update the filteredCounties HashMap.
     * @param filterCountry object
     * @param args  arguments
     * @param values values of needs
     * @param stns list of station ids
     * @param uuid unique key identifier
     * @param session session of request
     */
    private static void sendCountryAVG(FilterCountry filterCountry,
                                       String[] args,
                                       String[] values,
                                       ArrayList<Long>stns,
                                       UUID uuid,
                                       Session session){
        filterCountry.setFilter(values, args[3]);
        if(args[0].equalsIgnoreCase("GET_WORLD")){
            args[1]="null";
        }
        int k = 0;
        while (stns.size() > k) {
            if (filteredStation.containsKey(stns.get(k))) {
                filterCountry.addWeatherstation(stns.get(k));
                FilterObject filterObject = filteredStation.get(stns.get(k));
                filterObject.setCountryDatabase(uuid);
                k++;
            }
        }
        ArrayList<UUID> arrayUUID;
        if (!sessionCountryList.containsKey(session)) {
            arrayUUID = new ArrayList<UUID>();
            arrayUUID.add(uuid);
            sessionCountryList.put(session, arrayUUID);
        } else {
            arrayUUID = (ArrayList) sessionCountryList.get(session);
        }
        arrayUUID.add(uuid);
        filteredCountries.put(uuid, filterCountry);
    }

    /**
     * Request a group of stations in a radius to send a shared or individual request of data.
     * The arguments are send by the WebClientConn.
     * Arguments can be split in: (Pipelined example)
     * GET_RAD      |LONGITUDE,LATITUDE,RANGE   |ARGUMENT(S/optional)|METHOD
     * The (S) values can be a comma separated array.
     * ARGUMENTS can be multiple, and for every ARGUMENT a Filter would be set. Saving only the arguments that the clients need,
     *          to be handled in the future. These arguments are mend to be for specific data.
     * If the (specific)Arguments are empty. Then fill the arguments in with all the measurements that can be send.
     * METHOD can be set by AVG (average) or RAW (raw unedited data)
     * The range would be calculated by calculateLong method. and returns a ArrayList of STATION_NUMBERS
     * The method would parse the data to sendCountryRAWOnly/sendCountryRAW/sendCountryAVG when completion.
     * @param session session of request
     * @param args parameters needed
     */
    public static void sendRadius(Session session, String[] args) {
        Double longitude,latidude,range;
        String[] latlong=args[1].split(",");
        latidude=Double.parseDouble(latlong[0]);
        longitude=Double.parseDouble(latlong[1]);
        range = Double.parseDouble(latlong[2]);
        ArrayList<Long> stns =calculateLong(latidude,longitude,range);
        if(args[2].equalsIgnoreCase("RAW")){
            //For GET_RAD <Lat,Lon,Rad> RAW
            sendCountryRAWOnly(stns, session);
        }else{
            if(args[3].equalsIgnoreCase("RAW")){
                //For GET_RAD <Lat,Lon,Rad> <arguments> RAW
                sendCountryRAW(stns, session, args);
            }else{
                String[] values = args[2].split(",");
                UUID uuid = UUID.randomUUID();
                FilterCountry filterCountry= new FilterCountry(session,uuid);
                //For GET_RAD <Lat,Lon,Rad> <arguments> AVG
                filterCountry.setMethod("Radius");
                sendCountryAVG(filterCountry, args, values, stns, uuid, session);
            }
        }
    }

    /**
     * Calculating the radius of the arguments set, returning the STNS
     * @param latidude latitude
     * @param longitude longitude
     * @param range range
     * @return arrayList
     */
    private static ArrayList<Long> calculateLong(Double latidude, Double longitude, Double range) {
        String query = "SELECT stn, " +
                "           country, " +
                "           ( 6371 * acos ( cos ( radians(" + latidude + ") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(" + longitude + ") ) " +
                "           + sin ( radians(" + latidude + ") ) * sin( radians( latitude ) ) ) ) " +
                "           AS distance " +
                "        FROM stations  " +
                "HAVING distance < "+range+"";
        ArrayList<Long> stns = new ArrayList<Long>();
        try {
            Statement statement = Main.SQLConn.createStatement();
            ResultSet result = statement.executeQuery(query);
            System.out.println("query has executed");

            while (result.next()) {
                stns.add(Long.parseLong(result.getString("stn"),10));
            }
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
        return stns;
    }

    /**
     * initializing the coastline by adding STATIONS
     * @param stn station id
     */
    public static void setCoastLine(Long stn){
        coastLine.add(stn);
    }

    /**
     * initializing the countries by adding COUNTRY_NAMES
     * @param country country name
     */
    public static void addCountry(String country) {
        countryList.add(country);
    }

    /**
     * Request a group of stations at the pacific coast to send a shared or individual request of data.
     * The arguments are send by the WebClientConn.
     * Arguments can be split in: (Pipelined example)
     * GET_COAST   |ARGUMENT(S/optional)|METHOD
     * The (S) values can be a comma separated array.
     * ARGUMENTS can be multiple, and for every ARGUMENT a Filter would be set. Saving only the arguments that the clients need,
     *          to be handled in the future. These arguments are mend to be for specific data.
     * If the (specific)Arguments are empty. Then fill the arguments in with all the measurements that can be send.
     * METHOD can be set by AVG (average) or RAW (raw unedited data)
     * The method would parse the data to sendCountry when completion.
     * @param session session of request
     * @param args arguments needed
     */
    public static void sendCoast(Session session, String[] args) {
        if(args[1].equalsIgnoreCase("RAW")){
            //For GET_COAST RAW
            for(Long stn:coastLine){
                args[1]=""+stn;
                sendData(session,args);
            }
        }else{
            if(args[2].equalsIgnoreCase("RAW")){
                //For GET_COAST <DATA> RAW
                String[] newArgs = new String[4];
                newArgs[0]="GET_COUNTRY";
                newArgs[1]="COASTLINE";
                newArgs[2]=args[1];
                newArgs[3]=args[2];
                sendCountryRAW(coastLine,session,newArgs);
            }else{
                //For GET_COAST <DATA> AVG
                String[] values = args[1].split(",");
                UUID uuid=UUID.randomUUID();
                FilterCountry filterCountry = new FilterCountry(session,uuid);
                //filterCountry.setFilter(values,"AVG");
                filterCountry.setMethod("COAST");
                filterCountry.setCountry("Pacific Area");
                String[] newArgs=new String[4];
                newArgs[0]="GET";
                newArgs[1]="COAST";
                newArgs[2]=args[1];
                newArgs[3]=args[2];
                if(coastLine==null){
                    System.out.println("No Coast");
                }else{
                    System.out.println("There is a Coast "+coastLine.size());
                }
                sendCountryAVG(filterCountry, newArgs, values, coastLine, uuid, session);
            }
        }
    }

    /**
     * Request a group of stations in the world to send a shared or grouped(average) data.
     * The arguments are send by the WebClientConn.
     * Arguments can be split in: (Pipelined example)
     * GET_WORLD   |ARGUMENT(S/optional)|METHOD
     * The (S) values can be a comma separated array.
     * ARGUMENTS can be multiple, and for every ARGUMENT a Filter would be set. Saving only the arguments that the clients need,
     *          to be handled in the future. These arguments are mend to be for specific data.
     * If the (specific)Arguments are empty. Then fill the arguments in with all the measurements that can be send.
     * METHOD can be set by AVG (average) or RAW (raw unedited data)
     * The method would parse the data to sendCountry when completion.
     * @param session session of requested
     * @param args arguments needed
     */
    public static void sendWorld(Session session, String[] args) {
        boolean first = true;
        String countries="";
        for(String country:countryList){
            country = country.replaceAll(" ","_");
            if(first){
                countries=""+country+"";
                first=false;
            }else {
                countries+=","+country+"";
            }
        }
        String[]newArg=new String[4];
        newArg[0]="GET_WORLD";
        newArg[1]=countries;
        newArg[2]=args[1];
        newArg[3]="AVG";
        if(!args[2].equalsIgnoreCase("RAW")){
            newArg[1]=newArg[1].replaceAll(",","&");
        }
        sendCountry(session, newArg);
    }
}
