package nl.jozefbv.weatherx;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pjvan on 29-10-2015.
 */
public class Filter {
    private static HashMap filtered;
    private static HashMap<Session,ArrayList<Long>> sessionList;

    public Filter() {
        sessionList = new HashMap<Session,ArrayList<Long>>();
        filtered = new HashMap<Long,FilterObject>();
    }
    public void GetWeatherStation(Session session, String weatherStationId,int delay){
        filtered.put(weatherStationId,session);
    }


    public static void sendData(Session session, String[] args) {
        //while(args[1]!="Stop"){
            try {
                String[] stn = args[1].split(",");
                for(int i=0;i<stn.length;i++) {
                    Long stnId = Long.parseLong(stn[i], 10);
                    if (filtered.containsKey(stnId)) {
                        String[] arg = null;
                        try{
                            arg = args[2].split(",");
                        }catch(ArrayIndexOutOfBoundsException e){
                            arg = new String[12];
                            arg[0]="DATE";
                            arg[1]="WNDDIR";
                            arg[2]="TEMP";
                            arg[3]="DEWP";
                            arg[4]="SLP";
                            arg[5]="STP";
                            arg[6]="VISIB";
                            arg[7]="WDSP";
                            arg[8]="PRCP";
                            arg[9]="SNDP";
                            arg[10]="CLDC";
                            arg[11]="FRSHTT";
                        }
                        FilterObject filterObject = (FilterObject) filtered.get(stnId);
                        filterObject.setSession(session, arg);
                        if (sessionList.containsKey(session)) {
                            ArrayList list = sessionList.get(session);
                            list.add(stnId);
                        } else {
                            ArrayList list = new ArrayList();
                            list.add(stnId);
                            sessionList.put(session, list);
                        }

                        filtered.put(stnId,filterObject);

                    } else {
                        session.getRemote().sendString("Weatherstation not found." + stn[i]);
                    }

                }

                //Thread.sleep(1000);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }// catch (InterruptedException e) {
               // e.printStackTrace();
            //}
      //  }
    }

    public static void stopData(Session session) {
        try{
            if(sessionList.containsKey(session)){
                ArrayList stn = sessionList.get(session);
                for(int i=0;i<stn.size();i++ ){
                    FilterObject filterObject = (FilterObject) filtered.get(stn.get(i));
                    HashMap sessionHashmap = filterObject.getSessionHashMap();
                    sessionHashmap.remove(session);
                    System.out.println("removed Session");
                }
            }
            else{
                session.getRemote().sendString("Weatherstation not found");
            }
        }
        catch(IOException e){
            System.err.println(e);
        }
    }

    public static void checkData(Measurements measure) {
        if(filtered.containsKey(measure.getStn())){
            FilterObject filterObject;
            filterObject = (FilterObject) filtered.get(measure.getStn());
            filterObject.setMeasure(measure);
            HashMap<Session,String[]> sessionHashMap = filterObject.getSessionHashMap();
            sendData(sessionHashMap,measure);
        }else{
            System.out.println("New Station " + measure.getStn());
            FilterObject filterObject = new FilterObject(measure.getStn());

            filtered.put(measure.getStn(),filterObject);
        }

    }

    private static void sendData(HashMap<Session,String[]> sessionHashMap,Measurements measure){
        for(Session session : sessionHashMap.keySet()){
            try {
                String line = "{" +
                        "\"TYPE\":\"STN\"," +
                        "\"STN\":\""+measure.getStn()+"\"," +
                        "\"TIME\":\""+measure.getTime()+"\"";
                String[] args = sessionHashMap.get(session);
                line = getData(measure,args,line);
                //line=        measure.getStn()+","+measure.getTime();
                //checkArguments(line,measure);
                line += "}";
                try {
                    session.getRemote().sendString(line);
                }
                catch (WebSocketException w){
                    System.err.println("socket has closed: bye bye.");
                    Filter.stopData(session);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}
