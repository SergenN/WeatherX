package nl.jozefbv.weatherx;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergen Nurel
 * Date of creation 30-12-2016, 13:45
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: nl.jozefbv.weatherx
 * Class: nl.jozefbv.weatherx.StationHistory
 * Description:
 * This class is responsible for attaching the history to a staton id.
 *
 * Changelog:
 * 1.0: initially created the class
 */
public class StationHistory {

    private Map<Long, History> stations = new HashMap<>();

    /**
     * get the history that fits with the station id.
     * @param id station id where you want the history from
     * @return History object with measurements
     */
    public History getHistory(long id){
        History history = stations.get(id);
        if (history == null){
            history = new History();
            stations.put(id , history);
        }
        return history;
    }
}