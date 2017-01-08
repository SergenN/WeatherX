package nl.jozefbv.weatherx;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Sergen Nurel
 * Date of creation 25-9-2015, 13:50
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: nl.jozefbv.weatherx
 * Class: nl.jozefbv.weatherx.ClientConnection
 * Description:
 * This class handles the connection thread of te client
 *
 * Changelog:
 * 1.0: class created and added a reader, while reading this class will take all lines and combine them into one line.
 * Then send that line to an XML converter which will convert it to a HashMap.
 * Once the conversion is done the HashMap will be converted into an object and sent to the correction processor.
 * 1.1: adjusted the connection to work with the new Measurement class and the storage handler.
 */
public class ClientConnection implements Runnable {

    private BufferedReader in;
    private HashMap<Long, History> clusterHistory;
    private Main main;

    /**
     * nl.jozefbv.weatherx.ClientConnection constructor
     * This constructor will create a buffered reader.
     *
     * @param client the client that made connection
     */
    public ClientConnection(Socket client, Main main) {
        clusterHistory = new HashMap<>();
        this.main = main;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run
     * the thread will keep running and waiting on input
     * once input is given it will combine this to one xml string and output it to the xml convertor
     * which will convert it to a HashMap
     * then the HashMap will go to the measurements class and convert into an object
     */
    public void run() {
        try {
            StringBuilder data = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) {

                if (line.contains("<MEASUREMENT>")) {
                    data.setLength(0);
                    data.append(line);
                    continue;
                }

                data.append(line);

                if (line.contains("</MEASUREMENT>")) {

                    //System.out.println(data.toString());

                    JAXBContext jaxbContext = JAXBContext.newInstance(Measurement.class);
                    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

                    StringReader reader = new StringReader(data.toString());
                    Measurement measurement = (Measurement) unmarshaller.unmarshal(reader);

                    if (measurement.getStn() == -1) {
                        continue;
                    }

                    if (!clusterHistory.containsKey(measurement.getStn())) {
                        clusterHistory.put(measurement.getStn(), new History());
                    }

                    Corrector.correct(measurement, clusterHistory.get(measurement.getStn()));
                    clusterHistory.get(measurement.getStn()).push(measurement);
                    main.getStorageHandler().store(measurement);
                }
            }
            in.close();
            Thread.currentThread().interrupt();
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
    }
}
