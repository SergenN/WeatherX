import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Sergen Nurel
 * Date of creation 25-9-2015, 13:50
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: default
 * Class: Connection
 * Description:
 * This class handles the connection thread of te client
 *
 * Changelog:
 * 1.0: class created and added a reader, while reading this class will take all lines and combine them into one line.
 * Then send that line to an XML converter which will convert it to a HashMap.
 * Once the conversion is done the HashMap will be converted into an object and sent to the correction processor.
 */
public class Connection implements Runnable{

    private BufferedReader in;
    private History history;

    /**
     * Connection constructor
     * This constructor will create a buffered reader.
     * @param client the client that made connection
     */
    public Connection(Socket client){
        history = new History();
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }catch(IOException e){
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
    public void run(){
        try {
            HashMap<String, String> data = new HashMap<>();

            String line;
            while ((line = in.readLine()) != null) {

                if (line.contains("<MEASUREMENT>")) {
                    data.clear();
                    continue;
                }

                if (line.contains("</MEASUREMENT>")) {

                    Measurements measure = new Measurements(data);
                    Corrector.correct(measure, history);
                    history.push(measure);
                    Transfer.store(measure);
                    continue;
                }

                String test = line;
                test = test.replaceAll("<[^>]+>", "");

                if (!test.equals("")){
                    String newline = line.substring(1, line.length() - 1);
                    newline = newline.replace("<", ",");
                    newline = newline.replace(">", ",");
                    String[] words = newline.split(",");

                    data.put(words[1], words[2]);
                }
            }
                in.close();
                Thread.currentThread().interrupt();

        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
