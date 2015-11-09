package nl.jozefbv.weatherx;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by pjvan on 6-11-2015.
 */
public class FilterCountry {
    private Session sessionHashMap;   //HashMap for Session getting value.
    private ArrayList<String> dataHashMap;         //Hashmap for Measures + method.
    private String methods;
    private ArrayList<Long> weatherStationHashMap;//HashMap for Weatherstations
    private HashMap<String,ArrayList<Object>> measures;     //ArrayList with Measures.
    private int count;                                  //Counted values;
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
                measures.put(arg[i],new ArrayList<>());
            }
            this.methods=method;
        }
    }


    public void addMeasure(Measurements measure){
        count++;
        for(int i = 0;i<dataHashMap.size();i++){
            switch (dataHashMap.get(i)) {
                case "TEMP":
                    measures.get("TEMP").add(measure.getTemp());
                    //System.out.println("AddMeasure");
                    break;
                case "DEWP":
                    measures.get("DEWP").add(measure.getDewp());
                    //System.out.println("AddMeasure");
                    break;
                case "STP":
                    measures.get("STP").add(measure.getStp());
                    //System.out.println("AddMeasure");
                    break;
                case "SLP":
                    measures.get("SLP").add(measure.getSlp());
                    //System.out.println("AddMeasure");
                    break;
                case "VISIB":
                    measures.get("VISIB").add(measure.getVisib());
                    //System.out.println("AddMeasure");
                    break;
                case "WDSP":
                    measures.get("WDSP").add(measure.getWdsp());
                    //System.out.println("AddMeasure");
                    break;
                case "PRCP":
                    measures.get("PRCP").add(measure.getPrcp());
                    //System.out.println("AddMeasure");
                    break;
                case "SNDP":
                    measures.get("SNDP").add(measure.getSndp());
                    //System.out.println("AddMeasure");
                    break;
                case "CLDC":
                    measures.get("CLDC").add(measure.getCldc());
                    //System.out.println("AddMeasure");
                    break;
                case "WNDDIR":
                    measures.get("WNDDIR").add(measure.getWnddir());
                    //System.out.println("AddMeasure");
                    break;
                case "FRSHTT":
                    measures.get("FRSHTT").add(measure.getFrshtt());
                    //System.out.println("AddMeasure");
                    break;
                default:
                    measures.get("UNKNOWN").add(measure.getStn());
                    //System.out.println("AddMeasure");
                    break;
            }
        }

        if(count>=weatherStationHashMap.size()){
            count=0;
            sendData();

        }
    }

    private void sendData() {
        Double total,average;
        System.out.println(dataHashMap.size()+"Size of Hashmap");
        String returned = "{\"COUNTRY\":\""+country+"\"";
        for(int i = 0;i<dataHashMap.size();i++){
            switch (dataHashMap.get(i)) {
                case "TEMP":
                    total=0.00;
                    for(int j = 0; j<weatherStationHashMap.size();j++){
                        total = (Double) measures.get("TEMP").get(j);
                    }
                    average = total/weatherStationHashMap.size();
                    returned  +=   ",\"TEMP\":\""+average+"\"";
                    break;
                case "DEWP":
                    total=0.00;
                    for(int j = 0; j<weatherStationHashMap.size();j++){
                        total = (Double) measures.get("DEWP").get(j);
                    }
                    average = total/weatherStationHashMap.size();
                    returned += ",\"DEWP\":\""+average+"\"";
                    break;
                case "STP":
                    total=0.00;
                    for(int j = 0; j<weatherStationHashMap.size();j++){
                        total = (Double) measures.get("STP").get(j);
                    }
                    average = total/weatherStationHashMap.size();
                    returned += ",\"STP\":\""+average+"\"";
                    break;
                case "SLP":
                    total=0.00;
                    for(int j = 0; j<weatherStationHashMap.size();j++){
                        total = (Double) measures.get("SLP").get(j);
                    }
                    average = total/weatherStationHashMap.size();
                    returned += ",\"SLP\":\""+average+"\"";
                    break;
                case "VISIB":
                    total=0.00;
                    for(int j = 0; j<weatherStationHashMap.size();j++){
                        total = (Double) measures.get("VISIB").get(j);
                    }
                    average = total/weatherStationHashMap.size();
                    returned += ",\"VISIB\":\""+average+"\"";
                    break;
                case "WDSP":
                    total=0.00;
                    for(int j = 0; j<weatherStationHashMap.size();j++){
                        total = (Double) measures.get("WDSP").get(j);
                    }
                    average = total/weatherStationHashMap.size();
                    returned += ",\"WDSP\":\""+average+"\"";
                    break;
                case "PRCP":
                    total=0.00;
                    for(int j = 0; j<weatherStationHashMap.size();j++){
                        total = (Double) measures.get("PRCP").get(j);
                    }
                    average = total/weatherStationHashMap.size();
                    returned += ",\"PRCP\":\""+average+"\"";
                    break;
                case "SNDP":
                    total=0.00;
                    for(int j = 0; j<weatherStationHashMap.size();j++){
                        total = (Double) measures.get("SNDP").get(j);
                    }
                    average = total/weatherStationHashMap.size();
                    returned += ",\"SNDP\":\""+average+"\"";
                    break;
                case "CLDC":
                    total=0.00;
                    for(int j = 0; j<weatherStationHashMap.size();j++){
                        total = (Double) measures.get("CLDC").get(j);
                    }
                    average = total/weatherStationHashMap.size();
                    returned += ",\"CLDC\":\""+average+"\"";
                    break;
                case "WNDDIR":
                    total=0.00;
                    for(int j = 0; j<weatherStationHashMap.size();j++){
                        total = (Double) measures.get("WNDDIR").get(j);
                    }
                    average = total/weatherStationHashMap.size();
                    returned += ",\"WNDDIR\":\""+average+"\"";
                    break;
                case "FRSHTT":
                    total=0.00;
                    for(int j = 0; j<weatherStationHashMap.size();j++){
                        total = (Double) measures.get("FRSHTT").get(j);
                    }
                    average = total/weatherStationHashMap.size();
                    returned += ",\"FRSHTT\":\""+average+"\"";
                    break;
                default:
                    break;
            }
        }
        returned += "}";
        sessionHashMap.getRemote().sendStringByFuture(returned);

    }

    public void setCountry(String country) {
        this.country=country;
    }
    public Session getSessionHashMap(){return sessionHashMap;}
    public ArrayList<Long> getWeatherStationHashMap(){return weatherStationHashMap;}
}
