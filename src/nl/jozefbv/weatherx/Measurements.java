package nl.jozefbv.weatherx;

import java.util.Map;

/**
 * Created by Sergen Nurel
 * Date of creation 25-9-2015, 14:21
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: default
 * Class: nl.jozefbv.weatherx.Measurements
 * Description:
 * This server class will convert all incoming data and save it.
 *
 * Changelog:
 * 1.0: this class will convert a given HashMap with 14 values into the right data types and save them.
 */
public class Measurements {

    private int wnddir;
    private long stn = -1;
    private double temp, dewp, stp, slp, visib, wdsp, prcp, sndp, cldc;
    private String date, time, frshtt;

    public Measurements(Map<String, String> data){
        try {
            setStn(convertLong(data.get("STN")));
            setWnddir(convertInt(data.get("WNDDIR")));
            setTemp(convertDouble(data.get("TEMP")));
            setDewp(convertDouble(data.get("DEWP")));
            setStp(convertDouble(data.get("STP")));
            setSlp(convertDouble(data.get("SLP")));
            setVisib(convertDouble(data.get("VISIB")));
            setWdsp(convertDouble(data.get("WDSP")));
            setPrcp(convertDouble(data.get("PRCP")));
            setSndp(convertDouble(data.get("SNDP")));
            setCldc(convertDouble(data.get("CLDC")));
            setDate(data.get("DATE"));
            setTime(data.get("TIME"));
            setFrshtt(data.get("FRSHTT"));
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
    }

    private long convertLong(String number){
        long num;
        try{
            num = Long.parseLong(number);
        }catch(NumberFormatException n){
            return -1000L;
        }catch(NullPointerException e){
            return -1000L;
        }
        return num;
    }

    private double convertDouble(String number){
        Double num;
        try{
            num = Double.parseDouble(number);
        }catch(NumberFormatException n){
            return -1000.0D;
        }catch(NullPointerException e){
            return -1000.0D;
        }
        return num;
    }

    private int convertInt(String number){
        int num;
        try{
            num = Integer.parseInt(number);
        }catch(NumberFormatException n){
            return -1000;
        }catch(NullPointerException e){
            return -1000;
        }
        return num;
    }

    public void setStn(long stn) {
        this.stn = stn;
    }

    public void setWnddir(int wnddir) {
        this.wnddir = wnddir;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setDewp(double dewp) {
        this.dewp = dewp;
    }

    public void setStp(double stp) {
        this.stp = stp;
    }

    public void setSlp(double slp) {
        this.slp = slp;
    }

    public void setVisib(double visib) {
        this.visib = visib;
    }

    public void setWdsp(double wdsp) {
        this.wdsp = wdsp;
    }

    public void setPrcp(double prcp) {
        this.prcp = prcp;
    }

    public void setSndp(double sndp) {
        this.sndp = sndp;
    }

    public void setCldc(double cldc) {
        this.cldc = cldc;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setFrshtt(String frshtt) {
        this.frshtt = frshtt;
    }

    public double getPrcp() {
        return prcp;
    }

    public long getStn() {
        return stn;
    }

    public int getWnddir() {
        return wnddir;
    }

    public double getTemp() {
        return temp;
    }

    public double getDewp() {
        return dewp;
    }

    public double getStp() {
        return stp;
    }

    public double getSlp() {
        return slp;
    }

    public double getVisib() {
        return visib;
    }

    public double getWdsp() {
        return wdsp;
    }

    public double getSndp() {
        return sndp;
    }

    public double getCldc() {
        return cldc;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getFrshtt() {
        return frshtt;
    }
}
