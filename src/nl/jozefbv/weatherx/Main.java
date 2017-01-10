package nl.jozefbv.weatherx;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * Created by Sergen Nurel
 * Date of creation 25-9-2015, 12:05
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: nl.jozefbv.weatherx
 * Class: nl.jozefbv.weatherx.Main
 * Description:
 * This class is the main class of the project
 *
 * Changelog:
 * 1.0: class created and added thread calling
 * 1.1: adjusted to class to make use of SQL pooling
 * 1.2: adjusted the class to use flat file database
 */

public class Main {
    private StationHistory stationHistory;
    private JAXBContext jaxbContext;

    /**
     * the first method called by Java
     * @param args, arguments given to the main method
     */
    public static void main(String[] args) {
        new Main();
    }

    /**
     * In this method a sql connection is established and the server thread is started.
     */
    public Main(){
        stationHistory = new StationHistory();
        try {
            jaxbContext = JAXBContext.newInstance(Measurement.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        new Thread(new WSServer(this)).start();
    }

    public StorageHandler getStorageHandler(){
        return new StorageHandler(new FlatFileDb(), true);
    }

    public StationHistory getStationHistory(){
        return stationHistory;
    }

    public JAXBContext getJaxbContext(){
        return jaxbContext;
    }
}
