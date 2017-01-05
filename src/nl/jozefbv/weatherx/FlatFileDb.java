package nl.jozefbv.weatherx;

import java.io.*;
import java.nio.file.Files;

/**
 * Created by serge on 5-1-2017.
 */
public class FlatFileDb {

    private File file = new File("measurements.csv");
    private final String header = "stn,date,time,temp,dewp,stp,slp,visib,wdsp,prcp,sndp,frshtt,cldc,wnddir";

    public FlatFileDb() {
        try {
            if (!file.exists()) {
                file.createNewFile();
                writeLine(header);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized void writeLine(String line) throws IOException{
        BufferedWriter writer = getWriter();
        writer.write(line);
        writer.newLine();
        writer.close();
    }

    private synchronized BufferedWriter getWriter() throws IOException {
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        return bufferedWriter;
    }

    private synchronized BufferedReader getReader() throws FileNotFoundException {
        FileReader fileReader = new FileReader(file.getAbsoluteFile());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader;
    }
}
