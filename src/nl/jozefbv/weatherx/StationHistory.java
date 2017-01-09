package nl.jozefbv.weatherx;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by serge on 8-1-2017.
 */
public class StationHistory {

    private Map<Long, History> stations = new HashMap<>();

    public History getHistory(long id){
        History history = stations.get(id);
        if (history == null){
            history = new History();
            stations.put(id , history);
        }
        return history;
    }
}
