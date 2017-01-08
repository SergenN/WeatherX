package nl.jozefbv.weatherx;

import java.util.LinkedList;

/**
 * Created by Sergen Nurel
 * Date of creation 28-9-2015, 16:44
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: nl.jozefbv.weatherx
 * Class: nl.jozefbv.weatherx.History
 * Description:
 * This class is to control and keep track of the measurement history of an antenna
 *
 * Changelog:
 * 1.0: class is created it will make a linkedlist and store up to HISTORY_SIZE measurements by using the push function
 */
public class History {
    private static final int HISTORY_SIZE = 30;
    private LinkedList<Measurement> history;

    /**
     * Constructor
     * creates a new history linkedlist
     */
    public History(){
        history = new LinkedList<>();
    }

    /**
     * Push
     * checks if the linkedlist has the size of 30 if true then remove the last
     * by default push a new measurement into the linkedlist
     * @param measurement measurement to insert
     */
    public void push(Measurement measurement){
        if (history.size() >= HISTORY_SIZE){
            history.removeLast();
        }
        history.push(measurement);
    }

    /**
     * getMeasurement
     * get the measurement with the given index number
     * @param index index from corresponding measurement
     * @return measurement from given index, null if index number is bigger than the linkedlist
     */
    public Measurement getMeasurement(int index){
        if (history.size() > index) {
            return history.get(index);
        }
        return null;
    }

    /**
     * getMeasurement
     * return the first measurement from the linkedlist, null if empty
     * @return first measurement or null if empty
     */
    @SuppressWarnings("unused")
    public Measurement getMeasurement(){
        if (!history.isEmpty()) {
            return history.getFirst();
        }
        return null;
    }

    /**
     * removeFirst
     * remove the last added entry from the history list
     */
    @SuppressWarnings("unused")
    public void removeFirst(){
        if (!history.isEmpty()){
            history.pop();
        }
    }

    /**
     * getSize
     * @return int of history size
     */
    public int getSize(){
        return history.size();
    }

}
