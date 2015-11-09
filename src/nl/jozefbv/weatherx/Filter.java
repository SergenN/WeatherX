package nl.jozefbv.weatherx;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by pjvan on 29-10-2015.
 */
public class Filter {
    private static HashMap<Long,FilterObject> filteredStation;                                //ID + filterObject
    private static HashMap<Session,ArrayList<Long>> sessionStationList;    //Session+ ID's
    private static HashMap<Session,ArrayList<UUID>> sessionCountryList;                      //Session+CountriesID's
    private static HashMap<UUID,FilterCountry> filteredCountries;                       //CountriesID + filteredCountry

    public Filter() {
        sessionStationList = new HashMap<Session,ArrayList<Long>>();
        sessionCountryList = new HashMap<Session,ArrayList<UUID>>();
        filteredStation = new HashMap<Long,FilterObject>();
        filteredCountries = new HashMap<UUID,FilterCountry>();
    }
    public void GetWeatherStation(Session session, String weatherStationId,int delay){
        //filteredStation.put(Long.valueOf(weatherStationId,10), session);
    }


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

    public static void checkData(Measurements measure) {
        if(filteredStation.containsKey(measure.getStn())){
            FilterObject filterObject;
            filterObject = (FilterObject) filteredStation.get(measure.getStn());
            filterObject.setMeasure(measure);
            HashMap<Session,String[]> sessionStationHashMap = filterObject.getSessionHashMap();
            ArrayList<UUID> countrylist = filterObject.getCountryHashMap();
            for(int i = 0; i<countrylist.size();i++){
                UUID uuid = countrylist.get(i);
                FilterCountry filterCountry = filteredCountries.get(uuid);
                filterCountry.addMeasure(measure);
            }
            sendData(sessionStationHashMap,measure);
            filterObject.counter++;
        }else{
            System.out.println("New Station " + measure.getStn());
            FilterObject filterObject = new FilterObject(measure.getStn());
            filteredStation.put(measure.getStn(),filterObject);
        }
    }

    private static void sendData(HashMap<Session,String[]> sessionHashMap,Measurements measure){
        for(Session session : sessionHashMap.keySet()){
            //try {
                String line = "{" +
                        "\"TYPE\":\"STN\"," +
                        "\"STN\":\""+measure.getStn()+"\"," +
                        "\"TIME\":\""+measure.getTime()+"\"";
                String[] args = sessionHashMap.get(session);
                line = getData(measure,args,line);
                line += "}";
                try {
                    //session.getRemote().sendString(line);
                    session.getRemote().sendStringByFuture(line);
                }
                catch (WebSocketException w){
                    System.err.println("socket has closed: bye bye.");
                    Filter.stopData(session);
                }
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}
        }
    }

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

    public static void setTempFilter(int stn) {
        System.out.println("Initializing " + stn);
        FilterObject filterObject = new FilterObject(Long.valueOf(stn));
        filterObject.setDatabase("TEMP",1);
        filteredStation.put(Long.valueOf(stn), filterObject);
    }

    public static FilterObject checkDatabase(Long stn) {
        FilterObject filterObject;
        filterObject = (FilterObject) filteredStation.get(stn);
        return filterObject;

    }

    public static void sendCountry(Session session, String[] args) {
        try {
            UUID uuid = UUID.randomUUID();
            String[] country = args[1].split(",");
            String countryArray = "('"+country[0]+"'";
            if(country.length>1){
                for (int i = 1; i < country.length; i++) {
                    countryArray += ",'"+country[i]+"'";
                }
            }
            FilterCountry filterCountry = new FilterCountry(session,uuid);
            ArrayList<Long> stns = new ArrayList<Long>();
            String query = "SELECT `stn` FROM `stations` WHERE `country` IN "+countryArray+")";
            System.out.println(query);
            Statement statement = Main.SQLConn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                stns.add(resultSet.getLong("stn"));
            }
            if(args[2].equalsIgnoreCase("RAW")){
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
                sendData(session, arg);
            }else {
                String[] values = args[2].split(",");
                if (args[3].equalsIgnoreCase("RAW")) {
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
                } else {
                    //for(String countryOne:country) {
                        filterCountry.setFilter(values, args[3]);
                        filterCountry.setCountry("Landje");
                        int k = 0;
                        while (stns.size() > k) {
                            if (filteredStation.containsKey(stns.get(k))) {
                                filterCountry.addWeatherstation(stns.get(k));
                                FilterObject filterObject = (FilterObject) filteredStation.get(stns.get(k));
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
                        System.out.println("No RAW ");
                    }
               // }
            }
        }
        catch (SQLException e){
            System.err.println();
        }
    }
}
