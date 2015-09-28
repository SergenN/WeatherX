import java.util.Map;

/**
 * Created by Sergen Nurel
 * Date of creation 25-9-2015, 14:21
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: default
 * Class: Measurements
 * Description:
 * This server class will convert all incoming data and save it.
 *
 * Changelog:
 * 1.0: this class will convert a given HashMap with 14 values into the right data types and save them.
 */
public class Measurements {

    private int wnddir;
    private long stn;
    private double temp, dewp, stp, slp, visib, wdsp, prcp, sndp, cldc;
    private String date, time, frshtt;

    public Measurements(Map<String, String> data){
        try {
            setStn(exists(data.get("STN")) ? Long.parseLong(data.get("STN")) : -100L);
            setWnddir(exists(data.get("WNDDIR")) ? Integer.parseInt(data.get("WNDDIR")) : -100);
            setTemp(exists(data.get("TEMP")) ? Double.parseDouble(data.get("TEMP")) : -100D);
            setDewp(exists(data.get("DEWP")) ? Double.parseDouble(data.get("DEWP")) : -100D);
            setStp(exists(data.get("STP")) ? Double.parseDouble(data.get("STP")) : -100D);
            setSlp(exists(data.get("SLP")) ? Double.parseDouble(data.get("SLP")) : -100D);
            setVisib(exists(data.get("VISIB")) ? Double.parseDouble(data.get("VISIB")) : -100D);
            setWdsp(exists(data.get("WDSP")) ? Double.parseDouble(data.get("WDSP")) : -100D);
            setPrcp(exists(data.get("PRCP")) ? Double.parseDouble(data.get("PRCP")) : -100D);
            setSndp(exists(data.get("SNDP")) ? Double.parseDouble(data.get("SNDP")) : -100D);
            setCldc(exists(data.get("CLDC")) ? Double.parseDouble(data.get("CLDC")) : -100D);
            setDate(exists(data.get("DATE")) ? data.get("DATE") : null);
            setTime(exists(data.get("TIME")) ? data.get("TIME") : null);
            setFrshtt(exists(data.get("FRSHTT")) ? data.get("FRSHTT") : null);
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
    }

    private boolean exists(String data){
        return data == null || data.equalsIgnoreCase("") || data.isEmpty();
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
