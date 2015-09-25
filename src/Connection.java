import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

/**
 * Created by serge on 25-9-2015.
 */
public class Connection implements Runnable{

    private BufferedReader in;
    private String xml;

    public Connection(Socket client){
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            String line = in.readLine();

            if (line.startsWith("<?xml")){
                xml = "";
            }

            xml += line;

            if (line.startsWith("</WEATHERDATA>")){
                Map<String, String> data = XMLConverter.convertNodesFromXml(xml);
                Measurements measure = new Measurements(data);
            }
        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
