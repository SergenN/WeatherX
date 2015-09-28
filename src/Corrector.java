import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Michaël on 25-9-2015.
 * @author Michaël
 * @version 1.0.0
 */
public class Corrector {
    private  Measurements meas;
    private History history;
    int amountMeas;

    public void Corrector(Measurements meas, History history){
        this.meas       = meas;
        this.history   = history;
        amountMeas = hashMeas.size();

        correctTemp();
        correctDewp();
        correctStp();
        correctSlp();
        correctVisib();
        correctWdsp();
        correctPrcp();
        correctSndp();
        correctFrshtt();
        correctCldc();
        correctWnddir();
    }

    private void correctTemp(){
        double temp = meas.getTemp();
        double[] previousTemp = new double[amountMeas];

        for(int i = 0; i<amountMeas;i++){
            previousTemp[i] = (Measurements) history.getMeasurement(i).getTemp();
        }
        if(temp>9999.9 || temp<-9999.9||temp==0){
            meas.setTemp(getAverageOneDecimal(previousTemp));
        }
        meas.setTemp(correcting(temp, getCalculateExtrapolatie(previousTemp)));
    }

    private void correctDewp(){
        double dewp = meas.getDewp();
        double[] previousDewp = new double[amountMeas];

        if(dewp>9999.9||dewp<-9999.9||dewp==0){
            for (int i = 0; i<amountMeas;i++){
                previousDewp[i] = (Measurements) history.getMeasurement(i).getDewp();
            }
            meas.setDewp(getCalculateExtrapolatie(previousDewp));
        }
    }

    private void correctStp(){
        double stp = meas.getStp();
        double[] previousStp = new double[amountMeas];


        if(stp>9999.9||stp<0||stp==0){
            for (int i = 0; i<amountMeas;i++){
                previousStp[i] = (Measurements) history.getMeasurement(i).getStp();
            }
            meas.setSlp(getCalculateExtrapolatie(previousStp));
        }
    }

    private void correctSlp(){
        double slp = meas.getSlp();
        double[] previousSlp = new double[amountMeas];

        if(slp>9999.9||slp<-9999.9||slp==0){
            for (int i = 0; i<amountMeas;i++){
                previousSlp[i] = (Measurements) history.getMeasurement(i).getSlp();
            }
            meas.setSlp(getCalculateExtrapolatie(previousSlp));
        }
    }

    private void correctVisib(){
        double visib = meas.getVisib();
        double[] previousVisib = new double[amountMeas];


        if(visib>999.9||visib<0||visib==0){
            for (int i = 0; i<amountMeas;i++){
                previousVisib[i] = (Measurements) history.getMeasurement(i).getVisib();
            }
            meas.setVisib(getCalculateExtrapolatie(previousVisib));
        }
    }

    private void correctWdsp(){
        double wdsp = meas.getWdsp();
        double[] previousWdsp = new double[amountMeas];

        if(wdsp>999.9||wdsp<0||wdsp==0){
            for (int i = 0; i<amountMeas;i++){
                previousWdsp[i] = (Measurements) history.getMeasurement(i).getWdsp();
            }
            meas.setWdsp(getCalculateExtrapolatie(previousWdsp));
        }
    }

    private void correctPrcp(){
        double prcp = meas.getPrcp();
        double[] previousPrcp = new double[amountMeas];

        if(prcp>999.99||prcp<0.00||prcp==0){
            for (int i = 0; i<amountMeas;i++){
                previousPrcp[i] = (Measurements) history.getMeasurement(i).getPrcp();
            }
            meas.setPrcp(getCalculateExtrapolatie(previousPrcp));
        }
    }

    private void correctSndp(){
        double sndp = meas.getSndp();
        double[] previousSndp= new double[amountMeas];

        if(sndp>9999.9||sndp<-9999.9||sndp==0){
            for (int i = 0; i<amountMeas;i++){
                previousSndp[i] = (Measurements) history.getMeasurement(i).getSndp();
            }
            meas.setSndp(getCalculateExtrapolatie(previousSndp));
        }
    }

    private void correctCldc(){
        double cldc = meas.getCldc();
        double[] previousCldc = new double[amountMeas];

        if(cldc>99.9||cldc<0||cldc==0){
            for (int i = 0; i<amountMeas;i++){
                previousCldc[i] = (Measurements) history.getMeasurement(i).getCldc();
            }
            meas.setCldc(getCalculateExtrapolatie(previousCldc));
        }
    }


    private void correctWnddir(){
        double wnddir = meas.getWnddir();
        double[] previousWnddir = new double[amountMeas];

        if(wnddir>9999.9&&wnddir<-9999.9){
            for (int i = 0; i<amountMeas;i++){
                previousWnddir[i] = (Measurements) history.getMeasurement(i).getWnddir();
            }
            meas.setWnddir((int)Math.floor(getCalculateExtrapolatie(previousWnddir)));
        }
    }

    private double getCalculateExtrapolatie(double[] values){
        double differences = values[values.length];
        if(values.length>1){
            differences = 0.00;
            for (int i=0; i<(values.length-1);i++){
                differences += (values[(i+1)]- values[i]);
            }
            differences= differences/values.length-1;
            differences+=values[values.length];
        }
        return differences;
    }


    private double correcting(double measures,double avarage){
        if(measures>(avarage*1.20)||measures <(avarage*0.80)||measures==0){
            measures = avarage;
        }
        return  measures;
    }

    private double getAverageOneDecimal(double[] numbers){
        int counted = numbers.length;
        double total = 0;
        for(int i=0;i<counted;i++){
            total += numbers[i];
        }
        return (double) Math.round((total/counted)*10)/10;
    }
    private double getAverageTwoDecimal(double[] numbers){
        int counted = numbers.length;
        double total = 0;
        for(int i=0;i<counted;i++){
            total += numbers[i];
        }
        return (double) Math.round((total/counted)*100)/100;
    }
}
