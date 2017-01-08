package nl.jozefbv.weatherx;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Michael van der Veen
 * Fixed by Leon Wetzel and Sergen Nurel - Man man man!
 * Date of creation 25-9-2015, 12:54
 *
 * Authors: Michael van der Veen, Leon Wetzel
 *
 * Version: 1.0.2
 * Package: default
 * Class: nl.jozefbv.weatherx.Corrector
 * Description:
 * This class is to correct and check the values given by the measurements.
 * On a false or null value, the corrector would edit the value to a better value
 *
 * Changelog:
 * 1.0.1: class is created
 * 1.0.2: Added documentary
 * 1.1.0  Fixed logical issues for correcting data
 * 1.1.1  Added proper documentation
 */
public class Corrector {
    /**
     * Initializing the Corrector Class,
     * For Every method this class has, the corrector will send the measurements to a correcting method.
     * In these methods the measurements will be adjusted if it is incorrect.
     * Measures that are incorrect are: Above or Below 20% of the previouse measures or empty values.
     * @param measurement
     * @param history
     */
    public Corrector(Measurement measurement, History history){
        correctTemperature(measurement, history);
        correctDewpoint(measurement, history);
        correctSTP(measurement, history);
        correctSLP(measurement, history);
        correctVisibility(measurement, history);
        correctWDSP(measurement, history);
        correctPRCP(measurement, history);
        correctSNDP(measurement, history);
        correctFRSHTT(measurement, history);
        correctCLDC(measurement, history);
        correctWNDDIR(measurement, history);
    }

    /**
     * This Method would receive data from the ClientConnection.java Class.
     * If the history size is equal or longer than 30 measures.
     * Then start the correction. By creating a new Corrector.
     * @param measurement
     * @param history
     */
    public static void correct(Measurement measurement, History history){
        //if it isn't the first time then use the correct
        if(history.getSize() >= 29) {
            new Corrector(measurement, history);
        }
    }

    /**
     * Correct the temperature
     * @param measurement
     * @param history
     */
    private void correctTemperature(Measurement measurement, History history){
        double temperature = measurement.getTemp();
        ArrayList<Double> previousTemperatures = new ArrayList<>();

        for(int i = 0; i<history.getSize()-1;i++){
            previousTemperatures.add(history.getMeasurement(i).getTemp());
        }

        double extrapolation = extrapolate(previousTemperatures);
        double average = calculateMargin(extrapolation);

        double low = extrapolation-average;
        double high = extrapolation+average;

        if(average < 0) {
            if(temperature < high || temperature > low){
                measurement.setTemp(extrapolation);
            }
        } else {
            if(temperature > high || temperature < low){
                measurement.setTemp(extrapolation);
            }
        }
    }

    /**
     * Correct the dew point
     * @param measurement
     * @param history
     */
    private void correctDewpoint(Measurement measurement, History history){
        double dewpoint = measurement.getDewp();
        ArrayList<Double> previousDewpoints = new ArrayList<>();

        for(int i = 0; i<history.getSize()-1;i++){
            previousDewpoints.add(history.getMeasurement(i).getDewp());
        }

        double extrapolation = extrapolate(previousDewpoints);
        double average = calculateMargin(extrapolation);

        double low = extrapolation-average;
        double high = extrapolation+average;

        if(average < 0) {
            if(dewpoint < high || dewpoint > low){
                measurement.setDewp(extrapolation);
            }
        } else {
            if(dewpoint > high || dewpoint < low){
                measurement.setDewp(extrapolation);
            }
        }

    }

    /**
     * Correct the air pressure at station level
     * @param measurement
     * @param history
     */
    private void correctSTP(Measurement measurement, History history){
        double STP = measurement.getStp();
        ArrayList<Double> previousSTP = new ArrayList<>();

        for(int i = 0; i<history.getSize()-1;i++){
            previousSTP.add(history.getMeasurement(i).getStp());
        }

        double extrapolation = extrapolate(previousSTP);
        double average = calculateMargin(extrapolation);

        if(STP>extrapolation+average || STP<extrapolation-average) {
            measurement.setStp(extrapolation);
        }
    }

    /**
     * Correct the air pressure at sea level
     * @param measurement
     * @param history
     */
    private void correctSLP(Measurement measurement, History history){
        double SLP = measurement.getSlp();
        ArrayList<Double> previousSLP = new ArrayList<>();

        for(int i = 0; i<history.getSize()-1;i++){
            previousSLP.add(history.getMeasurement(i).getSlp());
        }

        double extrapolation = extrapolate(previousSLP);
        double average = calculateMargin(extrapolation);

        if(SLP>extrapolation+average || SLP<extrapolation-average){
            measurement.setSlp(extrapolation);
        }
    }

    /**
     * Correct the visibility
     * @param measurement
     * @param history
     */
    private void correctVisibility(Measurement measurement, History history){
        double visibility = measurement.getVisib();
        ArrayList<Double> previousVisibility = new ArrayList<>();

        for(int i = 0; i<history.getSize()-1;i++){
            previousVisibility.add(history.getMeasurement(i).getVisib());
        }

        double extrapolation = extrapolate(previousVisibility);
        double average = calculateMargin(extrapolation);

        if(visibility>extrapolation+average || visibility<extrapolation-average) {
            measurement.setVisib(extrapolation);
        }
    }

    /**
     * Corect the wind speed
     * @param measurement
     * @param history
     */
    private void correctWDSP(Measurement measurement, History history){
        double WDSP = measurement.getWdsp();
        ArrayList<Double> previousWDSP = new ArrayList<>();

        for(int i = 0; i<history.getSize()-1;i++){
            previousWDSP.add(history.getMeasurement(i).getWdsp());
        }

        double extrapolation = extrapolate(previousWDSP);
        double average = calculateMargin(extrapolation);

        if(WDSP>extrapolation+average || WDSP<extrapolation-average){
            measurement.setWdsp(extrapolation);
        }
    }

    /**
     * Correct the precipitation
     * @param measurement
     * @param history
     */
    private void correctPRCP(Measurement measurement, History history){
        double PRCP = measurement.getPrcp();
        if(PRCP < 0) {
            PRCP = 0;
        }
        ArrayList<Double> previousPRCP = new ArrayList<>();

        for(int i = 0; i < history.getSize()-1; i++){
            previousPRCP.add(history.getMeasurement(i).getPrcp());
        }

        double extrapolation = extrapolate(previousPRCP);
        double average = calculateMargin(extrapolation);

        if(PRCP>extrapolation+average || PRCP<extrapolation - average){
            measurement.setPrcp(extrapolation);
        }
    }

    /**
     * Correct the amount of snow
     * @param measurement
     * @param history
     */
    private void correctSNDP(Measurement measurement, History history){
        double SNDP = measurement.getSndp();
        if(SNDP < 0) {
            SNDP = 0;
        }
        ArrayList<Double> previousSNDP = new ArrayList<>();

        for(int i = 0; i<history.getSize()-1;i++){
            previousSNDP.add(history.getMeasurement(i).getSndp());
        }

        double extrapolation = extrapolate(previousSNDP);
        double average = calculateMargin(extrapolation);

        if(SNDP>extrapolation+average || SNDP<extrapolation-average){
            measurement.setSndp(extrapolation);
        }
    }

    /**
     * Correct the information for freezing, raining, snowing, hailing, thunder and/or tornado
     * @param measurements
     * @param history
     */
    private void correctFRSHTT(Measurement measurements, History history){
        if(measurements.getFrshtt() == null || measurements.getFrshtt() == ""){
            measurements.setFrshtt(history.getMeasurement(history.getSize() - 1).getFrshtt());
        }
    }

    /**
     * Correct the cloudiness
     * @param measurement
     * @param history
     */
    private void correctCLDC(Measurement measurement, History history){
        double CLDC = measurement.getCldc();
        ArrayList<Double> previousCLDC = new ArrayList<>();

        for(int i = 0; i<history.getSize()-1;i++){
            previousCLDC.add(history.getMeasurement(i).getCldc());
        }

        double extrapolation = extrapolate(previousCLDC);
        double average = calculateMargin(extrapolation);

        if(CLDC>extrapolation+average || CLDC<extrapolation-average) {
            measurement.setCldc(Math.round(extrapolation));
        }
    }

    /**
     * Correct the wind direction
     * @param measurement
     * @param history
     */
    private void correctWNDDIR(Measurement measurement, History history){
        double WNDDIR = measurement.getWnddir();
        ArrayList<Double> previousWNDDIR = new ArrayList<>();

        for(int i = 0; i<history.getSize()-1;i++){
            previousWNDDIR.add((double)history.getMeasurement(i).getWnddir());
        }

        double extrapolation = extrapolate(previousWNDDIR);
        double average = calculateMargin(extrapolation);

        if(WNDDIR>extrapolation+average || WNDDIR<extrapolation-average) {
            measurement.setWnddir((int)Math.round(extrapolation));
        }
    }

    /**
     * Extrapolate values to return a legit value for a measurement
     * @param values
     * @return new value for missing value
     */
    private double extrapolate(ArrayList<Double> values){
        if (values.size() <= 0){
            return 0;
        }
        if (values.size() == 1){
            return values.get(0);
        }

        double sum = 0;
        for (double d : values) sum += d;
        sum = sum / values.size();

        DecimalFormat f = new DecimalFormat("#.##");
        return Double.parseDouble(f.format(sum).replace(",","."));
    }

    /**
     * Calculate the margin for a specific value
     * @param value
     * @return margin
     */
    private double calculateMargin(double value) {
        return (value/100) * 40;
    }
}
