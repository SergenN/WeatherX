package nl.jozefbv.weatherx;

import org.eclipse.jetty.websocket.api.Session;

import javax.lang.model.type.ArrayType;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.*;

/**
 * Created by pjvan on 6-11-2015.
 */
public class FilterCountry {
    private Session sessionHashMap;                             //HashMap for Session getting value.
    private ArrayList<String> dataHashMap;                      //Hashmap for Measures + method.
    private String methods;
    private ArrayList<Long> weatherStationHashMap;              //HashMap for Weatherstations
    private HashMap<String,LinkedList<Double>> measures;        //ArrayList with Measures.
    private int count;                                          //Counted values;
    private String country;
    public UUID uuid;

    public FilterCountry(Session session,UUID uuid){
        sessionHashMap = session;
        this.uuid = uuid;
        dataHashMap = new ArrayList<String>();
        weatherStationHashMap = new ArrayList<Long>();
        measures = new HashMap<>();
        count=0;
    }

    public void addWeatherstation(Long stn){
        weatherStationHashMap.add(stn);
        //checkSended();
    }

    private void checkSended() {
        if(weatherStationHashMap.size()==measures.size()){

        }
    }

    public void setFilter(String[] arg,String method) {

        if(arg.length>0){
            for (int i = 0; i < arg.length; i++) {
                dataHashMap.add(arg[i]);
                measures.put(arg[i],new LinkedList<>());
            }
            this.methods=method;
        }
    }


    public synchronized void addMeasure(Measurements measure){
        for(int i = 0;i<dataHashMap.size();i++){
            switch (dataHashMap.get(i)) {
                case "TEMP":
                    measures.get("TEMP").add(measure.getTemp());
                    /*if (measures.get("TEMP").size() > weatherStationHashMap.size()){
                        measures.get("TEMP").removeLast();
                    }*/
                    break;
                case "DEWP":
                    measures.get("DEWP").add(measure.getDewp());
                    /*if (measures.get("DEWP").size() > weatherStationHashMap.size()){
                        measures.get("DEWP").removeLast();
                    }*/
                    break;
                case "STP":
                    measures.get("STP").add(measure.getStp());
                    /*if (measures.get("STP").size() > weatherStationHashMap.size()){
                        measures.get("STP").removeLast();
                    }*/
                    break;
                case "SLP":
                    measures.get("SLP").add(measure.getSlp());
                    /*if (measures.get("SLP").size() > weatherStationHashMap.size()){
                        measures.get("SLP").removeLast();
                    }*/
                    break;
                case "VISIB":
                    measures.get("VISIB").add(measure.getVisib());
                    /*if (measures.get("VISIB").size() > weatherStationHashMap.size()){
                        measures.get("VISIB").removeLast();
                    }*/
                    break;
                case "WDSP":
                    measures.get("WDSP").add(measure.getWdsp());
                    /*if (measures.get("WDSP").size() > weatherStationHashMap.size()){
                        measures.get("WDSP").removeLast();
                    }*/
                    break;
                case "PRCP":
                    measures.get("PRCP").add(measure.getPrcp());
                    /*if (measures.get("PRCP").size() > weatherStationHashMap.size()){
                        measures.get("PRCP").removeLast();
                    }*/
                    break;
                case "SNDP":
                    measures.get("SNDP").add(measure.getSndp());
                    /*if (measures.get("SNDP").size() > weatherStationHashMap.size()){
                        measures.get("SNDP").removeLast();
                    }*/
                    break;
                case "CLDC":
                    measures.get("CLDC").add(measure.getCldc());
                    /*if (measures.get("CLDC").size() > weatherStationHashMap.size()){
                        measures.get("CLDC").removeLast();
                    }*/
                    break;
                case "WNDDIR":
                    measures.get("WNDDIR").add(Double.parseDouble(""+measure.getWnddir()));
                    /*if (measures.get("WNDDIR").size() > weatherStationHashMap.size()){
                        measures.get("DNDDIR").removeLast();
                    }*/
                    break;
                case "FRSHTT":
                    measures.get("FRSHTT").add(Double.parseDouble(""+measure.getFrshtt()));
                    /*if (measures.get("FRSHTT").size() > weatherStationHashMap.size()){
                        measures.get("FRSHTT").removeLast();
                    }*/
                    break;
                default:
                    measures.get("UNKNOWN").add(Double.parseDouble("" + measure.getStn()));
                    /*if (measures.get("UNKNOWN").size() > weatherStationHashMap.size()){
                        measures.get("UNKNOWN").removeLast();
                    }*/
                    break;
            }
        }
        count++;
        if(count>=weatherStationHashMap.size()){

            sendData();

        }
    }

    private void sendData() {
        Double total,average;
        String returned = "{\"COUNTRY\":\""+country+"\"";
        returned +=",\"TYPE\":\""+methods+"\"";
        try {
            for (int i = 0; i < dataHashMap.size(); i++) {
                total = 0.00;
                LinkedList<Double> list = measures.get(dataHashMap.get(i));
                for (int j = 0; j < measures.get(dataHashMap.get(i)).size(); j++) {
                    Double value = list.pop();
                    total += value;
                }
                average = total / measures.get(dataHashMap.get(i)).size();
                returned += ",\"" + dataHashMap.get(i) + "\":\"" + average + "\"";
            }
            returned += "}";
            sessionHashMap.getRemote().sendStringByFuture(returned);
        }
        catch (IndexOutOfBoundsException e){
            System.err.println("not Enouth measures");
        }
        this.count=0;
    }

    public void setCountry(String country) {
        this.country=country;
    }
    public Session getSessionHashMap(){return sessionHashMap;}
    public ArrayList<Long> getWeatherStationHashMap(){return weatherStationHashMap;}
}
