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
        setStn(Long.parseLong(data.get("STN")));
        setWnddir(Integer.parseInt(data.get("WNDDIR")));
        setTemp(Double.parseDouble(data.get("TEMP")));
        setDewp(Double.parseDouble(data.get("DEWP")));
        setStp(Double.parseDouble(data.get("STP")));
        setSlp(Double.parseDouble(data.get("SLP")));
        setVisib(Double.parseDouble(data.get("VISIB")));
        setWdsp(Double.parseDouble(data.get("WDSP")));
        setPrcp(Double.parseDouble(data.get("PRCP")));
        setSndp(Double.parseDouble(data.get("SNDP")));
        setCldc(Double.parseDouble(data.get("CLDC")));
        setDate(data.get("DATE"));
        setTime(data.get("TIME"));
        setFrshtt(data.get("FRSHTT"));
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
