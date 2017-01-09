package nl.jozefbv.weatherx;

import java.io.*;

/**
 * Created by Sergen Nurel
 * Date of creation 30-12-2016, 13:52
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: nl.jozefbv.weatherx
 * Class: nl.jozefbv.weatherx.FlafFileDb
 * Description:
 * This class is responsible for creating a file, initializing it and creating it's readers/writers
 *
 * Changelog:
 * 1.0: initially created the classw
 */
public class FlatFileDb {

    private File file = new File("measurements.csv");
    private final String header = "stn,date,time,temp,dewp,stp,slp,visib,wdsp,prcp,sndp,frshtt,cldc,wnddir";

    /**
     * calling the constructor will create the file and write it's header.
     */
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


    /**
     * this will write a line to the file, create a line break and close the writer
     * @param line line to write to the file
     * @throws IOException
     */
    public synchronized void writeLine(String line) throws IOException{
        BufferedWriter writer = getWriter();
        writer.write(line);
        writer.newLine();
        writer.close();
    }

    /**
     * this will create the writer for the file.
     * @return the writer
     * @throws IOException
     */
    private synchronized BufferedWriter getWriter() throws IOException {
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        return bufferedWriter;
    }

    /**
     * this will create the reader for the file.
     * @return the reader
     * @throws FileNotFoundException
     */
    private synchronized BufferedReader getReader() throws FileNotFoundException {
        FileReader fileReader = new FileReader(file.getAbsoluteFile());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader;
    }
}
