import java.util.ArrayList;

/**
 * Created by Michaël van der Veen
 * Date of creation 25-9-2015, 12:54
 *
 * Authors: Michaël van der Veen,
 *
 * Version: 1.0.2
 * Package: default
 * Class: Corrector
 * Description:
 * This class is to correct and check the values given by the measurements.
 * On a false or null value, the corrector would edit the value to a better value
 *
 * Changelog:
 * 1.0.1: class is created
 * 1.0.2: Added documentary
 */
public class Corrector {

    private static int amountMeas;

    public static void correct(Measurements meas, History history){
        // get the size of the History object (stack)
        amountMeas = history.getSize()-1;
        //if it isn't the first time then use the correct
        if(history.getSize() >= 29) {
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
    /**
     *  Correcting the temperature
     *  @param meas
     *  @param history
     */
    private static void correctTemp(Measurements meas, History history){
        double temp = meas.getTemp();
        ArrayList<Double> previousTemp = new ArrayList<>();
        //double[] previousTemp = new double[amountMeas];

        for(int i = 0; i<amountMeas;i++){
            previousTemp.add(history.getMeasurement(i).getTemp());
        }

        if(temp>9999.9 || temp<-9999.9||temp==0){
            double value = Math.round(correcting(temp, getCalculateExtrapolatie(previousTemp)) * 10);
            meas.setTemp(value/10);
        }

    }

    private static void correctDewp(Measurements meas, History history){
        double dewp = meas.getDewp();
        ArrayList<Double> previousTemp = new ArrayList<>();
        //double[] previousDewp = new double[amountMeas];

        if(dewp>9999.9||dewp<-9999.9||dewp==0){
            for (int i = 0; i<amountMeas;i++){
                previousTemp.add(history.getMeasurement(i).getDewp());
            }
            double value = Math.round(correcting(dewp, getCalculateExtrapolatie(previousTemp)) * 10);
            meas.setDewp(value / 10);
        }
    }

    private static void correctStp(Measurements meas, History history){
        double stp = meas.getStp();
        ArrayList<Double> previousTemp = new ArrayList<>();
        //double[] previousStp = new double[amountMeas];


        if(stp>9999.9||stp<0||stp==0){
            for (int i = 0; i<amountMeas;i++){
                previousTemp.add(history.getMeasurement(i).getStp());
            }
            double value = Math.round(correcting(stp, getCalculateExtrapolatie(previousTemp)) * 10);
            meas.setStp(value / 10);
        }
    }

    private static void correctSlp(Measurements meas, History history){
        double slp = meas.getSlp();
        ArrayList<Double> previousTemp = new ArrayList<>();
        //double[] previousSlp = new double[amountMeas];

        if(slp>9999.9||slp<-9999.9||slp==0){
            for (int i = 0; i<amountMeas;i++){
                previousTemp.add(history.getMeasurement(i).getSlp());
            }
            double value = Math.round(correcting(slp, getCalculateExtrapolatie(previousTemp)) * 10);
            meas.setSlp(value / 10);
        }
    }

    private static void correctVisib(Measurements meas, History history){
        double visib = meas.getVisib();
        ArrayList<Double> previousTemp = new ArrayList<>();
        //double[] previousVisib = new double[amountMeas];


        if(visib>999.9||visib<0||visib==0){
            for (int i = 0; i<amountMeas;i++){
                previousTemp.add(history.getMeasurement(i).getVisib());
            }
            double value = Math.round(correcting(visib, getCalculateExtrapolatie(previousTemp)) * 10);
            meas.setVisib(value / 10);
        }
    }

    private static void correctWdsp(Measurements meas, History history){
        double wdsp = meas.getWdsp();
        ArrayList<Double> previousTemp = new ArrayList<>();
        //double[] previousWdsp = new double[amountMeas];

        if(wdsp>999.9||wdsp<0||wdsp==0){
            for (int i = 0; i<amountMeas;i++){
                previousTemp.add(history.getMeasurement(i).getWdsp());
            }
            double value = Math.round(correcting(wdsp, getCalculateExtrapolatie(previousTemp)) * 10);
            meas.setWdsp(value / 10);
        }
    }

    private static void correctPrcp(Measurements meas, History history){
        double prcp = meas.getPrcp();
        ArrayList<Double> previousTemp = new ArrayList<>();
        //double[] previousPrcp = new double[amountMeas];

        if(prcp>999.99||prcp<0.00||prcp==0){
            for (int i = 0; i<amountMeas;i++){
                previousTemp.add(history.getMeasurement(i).getPrcp());
            }
            double value = Math.round(correcting(prcp, getCalculateExtrapolatie(previousTemp)) * 100);
            meas.setPrcp(value / 100);
        }
    }

    private static void correctSndp(Measurements meas, History history){
        double sndp = meas.getSndp();
        ArrayList<Double> previousTemp = new ArrayList<>();
        //double[] previousSndp = new double[];

        if(sndp>9999.9||sndp<-9999.9||sndp==0){
            for (int i = 0; i<amountMeas;i++){
                previousTemp.add(history.getMeasurement(i).getSndp());
            }
            double value = Math.round(correcting(sndp, getCalculateExtrapolatie(previousTemp)) * 10);
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
        ArrayList<Double> previousTemp = new ArrayList<>();
        //double[] previousCldc = new double[amountMeas];

        if(cldc>99.9||cldc<0||cldc==0){
            for (int i = 0; i<amountMeas;i++){
                previousTemp.add(history.getMeasurement(i).getCldc());
            }
            //meas.setCldc(getCalculateExtrapolatie(previousCldc));
            double value = Math.round(correcting(cldc, getCalculateExtrapolatie(previousTemp)) * 10);
            meas.setCldc(value / 10);
        }
    }


    private static void correctWnddir(Measurements meas, History history){
        double wnddir = meas.getWnddir();
        ArrayList<Double> previousTemp = new ArrayList<>();
        //double[] previousWnddir = new double[amountMeas];

        if(wnddir>9999.9&&wnddir<-9999.9){
            for (int i = 0; i<amountMeas;i++){
                previousTemp.get(history.getMeasurement(i).getWnddir());
            }
            meas.setWnddir((int)Math.floor(getCalculateExtrapolatie(previousTemp)));
        }
    }

    private static double getCalculateExtrapolatie(ArrayList<Double> values){

        if (values.size() <= 0){
            return 0;
        }

        double differences = values.get(values.size()-1);
        if(values.size()>2){
            differences = 0.00;
            for (int i=0; i<(values.size()-2);i++){
                differences += (values.get(i+1)- values.get(i));
            }
            differences= differences/values.size()-1;
            differences+=values.get(values.size()-1);
        }
        return differences;
    }


    private static double correcting(double measures,double avarage){
        if(measures>(avarage*1.20)||measures <(avarage*0.80)||measures==0){
            measures = avarage;
        }

        return  measures;
    }

}
