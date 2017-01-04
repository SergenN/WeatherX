/*
package deprecated;

import deprecated.Measurements;
import org.eclipse.jetty.websocket.api.Session;

import javax.lang.model.type.ArrayType;
import java.io.IOException;
import java.security.PrivateKey;
import java.text.DecimalFormat;
import java.util.*;

*/
/**
 * Created by Micha�l van der Veen
 * Date of creation 6-11-2015
 *
 * Authors: Micha�l van der Veen,
 *
 * Version: 2
 * Package: default
 * Class: deprecated.FilterCountry
 * Description:
 * This class stores stata from a list of Stations that can be send when a specific amount of measures are set
 *
 * Commando's reveived from WebClientConn will be executed here.
 *
 *
 * Changelog:
 * 2 completion of Filter Class Added documentation
 *
 *
 *//*

public class FilterCountry {
    private Session sessionHashMap;                             //HashMap for Session getting value.
    private ArrayList<String> dataHashMap;                      //Hashmap for Measures + method.
    private String methods;                                     //Method
    private ArrayList<Long> weatherStationHashMap;              //HashMap for Weatherstations
    private HashMap<String,LinkedList<Double>> measures;        //ArrayList with Measures.
    private int count;                                          //Counted values;
    private String country;                                     //Country name
    public UUID uuid;                                           //Unique Id

    */
/**
     * Initializing FilterCountry
     * @param session session of request
     * @param uuid unique ID
     *//*

    public FilterCountry(Session session,UUID uuid){
        sessionHashMap = session;
        this.uuid = uuid;
        dataHashMap = new ArrayList<String>();
        weatherStationHashMap = new ArrayList<Long>();
        measures = new HashMap<>();
        count=0;
    }

    */
/**
     * Add weatherstations to list.
     * @param stn station ID
     *//*

    public void addWeatherstation(Long stn){
        weatherStationHashMap.add(stn);
    }


    */
/**
     * set Filter parameters and Method
     * @param arg parameters
     * @param method method of handling
     *//*

    public void setFilter(String[] arg,String method) {
        if(arg.length>0){
            for (int i = 0; i < arg.length; i++) {
                dataHashMap.add(arg[i]);
                measures.put(arg[i],new LinkedList<>());
            }
            this.methods=method;
        }
    }

    */
/**
     * measures added,
     * When the counter reached the same amount of stations,
     * then send data and reset counter.
     * @param measure incoming measure.
     *//*

    public synchronized void addMeasure(Measurements measure){
        for(int i = 0;i<dataHashMap.size();i++){
            switch (dataHashMap.get(i)) {
                case "TEMP":
                    measures.get("TEMP").add(measure.getTemp());
                    break;
                case "DEWP":
                    measures.get("DEWP").add(measure.getDewp());
                    break;
                case "STP":
                    measures.get("STP").add(measure.getStp());
                    break;
                case "SLP":
                    measures.get("SLP").add(measure.getSlp());
                    break;
                case "VISIB":
                    measures.get("VISIB").add(measure.getVisib());
                    break;
                case "WDSP":
                    measures.get("WDSP").add(measure.getWdsp());
                    break;
                case "PRCP":
                    measures.get("PRCP").add(measure.getPrcp());
                    break;
                case "SNDP":
                    measures.get("SNDP").add(measure.getSndp());
                    break;
                case "CLDC":
                    measures.get("CLDC").add(measure.getCldc());
                    break;
                case "WNDDIR":
                    measures.get("WNDDIR").add(Double.parseDouble(""+measure.getWnddir()));
                    break;
                case "FRSHTT":
                    measures.get("FRSHTT").add(Double.parseDouble(""+measure.getFrshtt()));
                    break;
                default:
                    break;
            }
        }
        count++;
        if(count>=weatherStationHashMap.size()){
            sendData();
        }
    }

    */
/**
     * Sending Data to the session with the combined value in JSON format.
     * Making a average of the data that has been send.
     *//*

    private synchronized void sendData() {
        Double total,average;
        String returned = "{\"COUNTRY\":\""+country+"\"";
        returned +=",\"TYPE\":\""+methods+"\"";
        try {
            for (int i = 0; i < dataHashMap.size(); i++) {
                total = 0.00;
                int counted =  measures.get(dataHashMap.get(i)).size();
                LinkedList<Double> list = measures.get(dataHashMap.get(i));
                for (int j = 0; j <counted; j++) {
                    Double value = list.pop();
                    total += value;
                }

                DecimalFormat f = new DecimalFormat("#.##");
                try {
                    average = Double.parseDouble(f.format(total / counted).replace(",", "."));
                }
                catch (NumberFormatException e){
                    System.err.println(e+"\n Continued\n"+counted);
                    average=total/counted;
                }
                returned += ",\"" + dataHashMap.get(i) + "\":\"" + average + "\"";
            }
            returned += "}";
            sessionHashMap.getRemote().sendStringByFuture(returned);
        }
        catch (IndexOutOfBoundsException e){
            System.err.println("not Enough measures");
        }
        this.count=0;
    }

    */
/**
     * set Country name
     * @param country country name
     *//*

    public void setCountry(String country) {
        this.country=country;
    }

    */
/**
     *
     * @return hashmap of weatherstation IDs
     *//*

    public ArrayList<Long> getWeatherStationHashMap(){return weatherStationHashMap;}

    */
/**
     * add new methods name.
     * @param method method name
     *//*

    public void setMethod(String method) {
        this.methods+=","+method;
    }
}
*/
