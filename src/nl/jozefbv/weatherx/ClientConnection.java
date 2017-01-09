package nl.jozefbv.weatherx;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;

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

    private Unmarshaller unmarshaller;
    private Socket client;
    private StorageHandler storageHandler;
    private Main main;


    /**
     * nl.jozefbv.weatherx.ClientConnection constructor
     * This constructor will create a buffered reader.
     *
     * @param client the client that made connection
     */
    public ClientConnection(Socket client, Main main) {
        try {
            unmarshaller = main.getJaxbContext().createUnmarshaller();
            this.main = main;
            this.client = client;
            storageHandler = main.getStorageHandler();
        } catch (JAXBException e) {
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
        String line;
        StringBuilder data = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while ((line = in.readLine()) != null) {
                if (line.contains("<MEASUREMENT>")) {
                    data.setLength(0);
                    data.append(line);
                    continue;
                }

                data.append(line);

                if (line.contains("</MEASUREMENT>")) {
                    Measurement measurement = (Measurement) unmarshaller.unmarshal(new StringReader(data.toString()));

                    if (measurement.getStn() == -1) {
                        continue;
                    }

                    History history = main.getStationHistory().getHistory(measurement.getStn());
                    Corrector.correct(measurement, history);
                    history.push(measurement);
                    storageHandler.store(measurement);
                }
            }
        }catch (IOException | JAXBException e){
            e.printStackTrace();
        }
    }
}