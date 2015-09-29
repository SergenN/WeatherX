import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Michaël on 25-9-2015.
 * @author Michaël
 * @version 1.0.0
 */
public class Corrector {
    //private static Measurements meas;
    //private static History history;
    private static int amountMeas;
    private static DecimalFormat oneDecimal,twoDecimal;

    public static void correct(Measurements meas, History history){
        //this.meas       = meas;
        //this.history   = history;
        oneDecimal = new DecimalFormat("#.0");
        twoDecimal = new DecimalFormat("#.00");

        amountMeas = history.getSize();
        if(history.getSize()!=0) {
            correctTemp(meas, history);
            correctDewp(meas, history);
            correctStp(meas, history);
            correctSlp(meas, history);
            correctVisib(meas, history);
            correctWdsp(meas, history);
            correctPrcp(meas, history);
            correctSndp(meas, history);
            correctFrshtt(meas, history);
            correctCldc(meas, history);
            correctWnddir(meas, history);
        }
    }

    private static void correctTemp(Measurements meas, History history){
        double temp = meas.getTemp();
        double[] previousTemp = new double[amountMeas];

        for(int i = 0; i<amountMeas;i++){
            previousTemp[i] = history.getMeasurement(i).getTemp();
        }
        if(temp>9999.9 || temp<-9999.9||temp==0){
            //meas.setTemp(getCalculateExtrapolatie(previousTemp));
            double value = Math.round(correcting(temp, getCalculateExtrapolatie(previousTemp)) * 10);
            meas.setTemp(value/10);
        }

    }

    private static void correctDewp(Measurements meas, History history){
        double dewp = meas.getDewp();
        double[] previousDewp = new double[amountMeas];

        if(dewp>9999.9||dewp<-9999.9||dewp==0){
            for (int i = 0; i<amountMeas;i++){
                previousDewp[i] = history.getMeasurement(i).getDewp();
            }
            double value = Math.round(correcting(dewp, getCalculateExtrapolatie(previousDewp)) * 10);
            meas.setDewp(value / 10);
        }
    }

    private static void correctStp(Measurements meas, History history){
        double stp = meas.getStp();
        double[] previousStp = new double[amountMeas];


        if(stp>9999.9||stp<0||stp==0){
            for (int i = 0; i<amountMeas;i++){
                previousStp[i] =  history.getMeasurement(i).getStp();
            }
            double value = Math.round(correcting(stp, getCalculateExtrapolatie(previousStp)) * 10);
            meas.setStp(value / 10);
        }
    }

    private static void correctSlp(Measurements meas, History history){
        double slp = meas.getSlp();
        double[] previousSlp = new double[amountMeas];

        if(slp>9999.9||slp<-9999.9||slp==0){
            for (int i = 0; i<amountMeas;i++){
                previousSlp[i] = history.getMeasurement(i).getSlp();
            }
            double value = Math.round(correcting(slp, getCalculateExtrapolatie(previousSlp)) * 10);
            meas.setSlp(value / 10);
        }
    }

    private static void correctVisib(Measurements meas, History history){
        double visib = meas.getVisib();
        double[] previousVisib = new double[amountMeas];


        if(visib>999.9||visib<0||visib==0){
            for (int i = 0; i<amountMeas;i++){
                previousVisib[i] =  history.getMeasurement(i).getVisib();
            }
            double value = Math.round(correcting(visib, getCalculateExtrapolatie(previousVisib)) * 10);
            meas.setVisib(value / 10);
        }
    }

    private static void correctWdsp(Measurements meas, History history){
        double wdsp = meas.getWdsp();
        double[] previousWdsp = new double[amountMeas];

        if(wdsp>999.9||wdsp<0||wdsp==0){
            for (int i = 0; i<amountMeas;i++){
                previousWdsp[i] =  history.getMeasurement(i).getWdsp();
            }
            double value = Math.round(correcting(wdsp, getCalculateExtrapolatie(previousWdsp)) * 10);
            meas.setWdsp(value / 10);
        }
    }

    private static void correctPrcp(Measurements meas, History history){
        double prcp = meas.getPrcp();
        double[] previousPrcp = new double[amountMeas];

        if(prcp>999.99||prcp<0.00||prcp==0){
            for (int i = 0; i<amountMeas;i++){
                previousPrcp[i] =  history.getMeasurement(i).getPrcp();
            }
            double value = Math.round(correcting(prcp, getCalculateExtrapolatie(previousPrcp)) * 100);
            meas.setPrcp(value / 100);
        }
    }

    private static void correctSndp(Measurements meas, History history){
        double sndp = meas.getSndp();
        double[] previousSndp= new double[amountMeas];

        if(sndp>9999.9||sndp<-9999.9||sndp==0){
            for (int i = 0; i<amountMeas;i++){
                previousSndp[i] = history.getMeasurement(i).getSndp();
            }
            double value = Math.round(correcting(sndp, getCalculateExtrapolatie(previousSndp)) * 10);
            meas.setSndp(value / 10);
        }
    }

    private static void correctFrshtt(Measurements meas, History history){
        if(meas.getFrshtt()==null){

            meas.setFrshtt(history.getMeasurement(history.getSize() - 1).getFrshtt());
        }
    }

    private static void correctCldc(Measurements meas, History history){
        double cldc = meas.getCldc();
        double[] previousCldc = new double[amountMeas];

        if(cldc>99.9||cldc<0||cldc==0){
            for (int i = 0; i<amountMeas;i++){
                previousCldc[i] =  history.getMeasurement(i).getCldc();
            }
            //meas.setCldc(getCalculateExtrapolatie(previousCldc));
            double value = Math.round(correcting(cldc, getCalculateExtrapolatie(previousCldc)) * 10);
            meas.setCldc(value / 10);
        }
    }


    private static void correctWnddir(Measurements meas, History history){
        double wnddir = meas.getWnddir();
        double[] previousWnddir = new double[amountMeas];

        if(wnddir>9999.9&&wnddir<-9999.9){
            for (int i = 0; i<amountMeas;i++){
                previousWnddir[i] = history.getMeasurement(i).getWnddir();
            }
            meas.setWnddir((int)Math.floor(getCalculateExtrapolatie(previousWnddir)));
        }
    }

    private static double getCalculateExtrapolatie(double[] values){
        double differences = values[values.length-1];
        if(values.length>1){
            differences = 0.00;
            for (int i=0; i<(values.length-1);i++){
                differences += (values[(i+1)]- values[i]);
            }
            differences= differences/values.length-1;
            differences+=values[values.length-1];
            System.out.println("Correcting....");
        }
        return (double) differences;
    }


    private static double correcting(double measures,double avarage){
        if(measures>(avarage*1.20)||measures <(avarage*0.80)||measures==0){
            measures = avarage;
        }

        return  measures;
    }

}
