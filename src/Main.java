/**
 * Created by Sergen Nurel
 * Date of creation 25-9-2015, 12:05
 *
 * Authors: Sergen Nurel,
 *
 * Version: 1.0
 * Package: default
 * Class: Main
 * Description:
 * This class is the main class of the project
 *
 * Chaneglog:
 * 1.0: class created and added thread calling
 */
public class Main {

    /**
     * Main method, this is the first method called
     * @param args, arguments given to the main method
     *
     * In this method the server thread is started.
     */
    public static void main(String[] args) {
        new Thread(new Server()).start();
    }
}
