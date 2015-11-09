import java.io.*;
import java.util.Scanner;

/**
 * Created by Leon Wetzel
 * Date of creation 2-11-2015, 18:58
 * |
 * Authors: Leon Wetzel
 * |
 * Version: 1.0
 * Package: PACKAGE_NAME
 * Class:
 * Description:
 * |
 * |
 * Changelog:
 * 1.0:
 */
public class Main {
    public static void main(String[] args){
        File f = new File("C:/Users/Leon/Desktop/rekt.txt");
        File f2 = new File("C:/Users/Leon/Desktop/lorde3.txt");
        try {
            Scanner sc = new Scanner(f);

            BufferedWriter out = new BufferedWriter(new FileWriter(f2));
            String line;
            while (sc.hasNextLine()){
                line = sc.nextLine();
                System.out.println(line);
                String[] rekt = line.split(",");
                out.write("\""+rekt[0]+"\",\""+rekt[1]+"\",\""+rekt[2]+"\"");
                out.newLine();
            }
        }catch (IOException e){

        }

    }
}
