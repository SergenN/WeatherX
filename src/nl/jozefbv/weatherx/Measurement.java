package nl.jozefbv.weatherx;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MEASUREMENT")
public class Measurement {

    private int wnddir;
    private long stn = -1;
    private double temp, dewp, stp, slp, visib, wdsp, prcp, sndp, cldc;
    private String date, time, frshtt;

    public int getWnddir() {
        return wnddir;
    }

    @XmlElement(name = "WNDDIR")
    public void setWnddir(int wnddir) {
        this.wnddir = wnddir;
    }

    public long getStn() {
        return stn;
    }

    @XmlElement(name = "STN")
    public void setStn(long stn) {
        this.stn = stn;
    }

    public double getTemp() {
        return temp;
    }

    @XmlElement(name = "TEMP")
    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getDewp() {
        return dewp;
    }

    @XmlElement(name = "DEWP")
    public void setDewp(double dewp) {
        this.dewp = dewp;
    }

    public double getStp() {
        return stp;
    }

    @XmlElement(name = "STP")
    public void setStp(double stp) {
        this.stp = stp;
    }

    public double getSlp() {
        return slp;
    }

    @XmlElement(name = "SLP")
    public void setSlp(double slp) {
        this.slp = slp;
    }

    public double getVisib() {
        return visib;
    }

    @XmlElement(name = "VISIB")
    public void setVisib(double visib) {
        this.visib = visib;
    }

    public double getWdsp() {
        return wdsp;
    }

    @XmlElement(name = "WDSP")
    public void setWdsp(double wdsp) {
        this.wdsp = wdsp;
    }

    public double getPrcp() {
        return prcp;
    }

    @XmlElement(name = "PRCP")
    public void setPrcp(double prcp) {
        if(prcp < 0) {
            prcp = 0;
        }
        this.prcp = prcp;
    }

    public double getSndp() {
        return sndp;
    }

    @XmlElement(name = "SNDP")
    public void setSndp(double sndp) {
        if(sndp < 0) {
            sndp = 0;
        }
        this.sndp = sndp;
    }

    public double getCldc() {
        return cldc;
    }

    @XmlElement(name = "CLDC")
    public void setCldc(double cldc) {
        this.cldc = cldc;
    }

    public String getDate() {
        return date;
    }

    @XmlElement(name = "DATE")
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    @XmlElement(name = "TIME")
    public void setTime(String time) {
        this.time = time;
    }

    public String getFrshtt() {
        return frshtt;
    }

    @XmlElement(name = "FRSHTT")
    public void setFrshtt(String frshtt) {
        this.frshtt = frshtt;
    }
}
