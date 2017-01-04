/*
package nl.jozefbv.weatherx;

import deprecated.Transfer;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

*/
/**
 * Created by Micha�l van der Veen
 * Date of creation 4-11-2015
 *
 * Authors: Micha�l van der Veen,
 *
 * Version: 2
 * Package: default
 * Class: nl.jozefbv.weatherx.Initial
 * Description:
 * This is the initial class that creates properties files and loads the default settings for database savings.
 *
 *
 * Changelog:
 * 2 completion of Filter Class Added documentation
 *
 *
 *//*

public class Initial {


    */
/**
     * initializing properties/coastline.csv and loading the weatherstations into the Filter.
     *//*

    public static void Initial(){
        Properties properties = new Properties();
        InputStream inputStream;
        String databaseTempLocation,databaseRainLocation,databaseWindLocation;
        try{
            inputStream = new FileInputStream("default.properties");
            properties.load(inputStream);
            databaseTempLocation = properties.getProperty("DefaultDatabaseTemp");
            databaseRainLocation = properties.getProperty("DefaultDatabaseRain");
            databaseWindLocation = properties.getProperty("DefaultDatabaseWind");
            Transfer.databaseRoot = properties.getProperty("databaseFolder");
            System.out.println(databaseTempLocation);
            initTemp(databaseTempLocation);
            initCoast(databaseRainLocation);
            initWind(databaseWindLocation);
        }
        catch (FileNotFoundException e) {
            try{
                //System.err.println(e);
                System.out.println("Creating new init files.");
                createNewDefault();
                setDefaultDatabaseTemp();
                setDefaultDatabaseRain();
                setDefaultDatabaseWind();
                inputStream = new FileInputStream("default.properties");
                properties.load(inputStream);
                databaseTempLocation = properties.getProperty("DefaultDatabaseTemp");
                databaseRainLocation = properties.getProperty("DefaultDatabaseRain");
                databaseWindLocation = properties.getProperty("DefaultDatabaseWind");
                initTemp(databaseTempLocation);
                initCoast(databaseRainLocation);
                initWind(databaseWindLocation);
            }
            catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    */
/**
     * Creating new Default properties and load it with values
     * @throws IOException
     *//*

    private static void createNewDefault()throws IOException{
        Properties properties = new Properties();
        OutputStream outputStream = null;
        outputStream = new FileOutputStream("default.properties");
        properties.setProperty("DefaultDatabaseTemp", "databaseTemp.properties");
        properties.setProperty("DefaultDatabaseRain", "databaseRain.properties");
        properties.setProperty("DefaultDatabaseWind", "databaseWind.properties");
        properties.setProperty("databaseTemp","");
        properties.setProperty("databaseRain","");
        properties.setProperty("databaseWind","");
        properties.setProperty("databaseFolder", "C:/");//System.getProperty("user.dir"));
        properties.store(outputStream,null);
    }

    */
/**
     * creating new databaseTemp properties and load it with values
     * @throws IOException
     *//*

    private static void setDefaultDatabaseTemp()throws IOException{
        Properties properties = new Properties();
        OutputStream outputStream = null;
        outputStream = new FileOutputStream("databaseTemp.properties");
        properties.setProperty("Latidude","36.59");
        properties.setProperty("Longitude","127.96");
        properties.setProperty("Range","5000");
        properties.setProperty("Condition","-10.00");
        properties.setProperty("Interval","0");
        properties.setProperty("Key[0]","TEMP");
        properties.store(outputStream,null);
    }

    */
/**
     * creating new databaseRain properties and load it with values
     * @throws IOException
     *//*

    private static void setDefaultDatabaseRain()throws IOException{
        Properties properties = new Properties();
        OutputStream outputStream = null;
        outputStream = new FileOutputStream("databaseRain.properties");
        properties.setProperty("CSV","coastline_pacific.csv");
        properties.setProperty("Longitude","128.0000");
        properties.setProperty("Range","5000");
        properties.setProperty("Statement","-10.00");
        properties.setProperty("Interval","0");
        properties.setProperty("Key[0]","PRCP");
        properties.store(outputStream,null);
    }

    */
/**
     * creating new databaseWind properties and load it with values
     * @throws IOException
     *//*

    private static void setDefaultDatabaseWind()throws IOException{
        Properties properties = new Properties();
        OutputStream outputStream = null;
        outputStream = new FileOutputStream("databaseWind.properties");
        properties.setProperty("Statement","-10.00");
        properties.setProperty("Key[0]","WNDDIR");
        properties.setProperty("Key[1]","WDSP");
        properties.setProperty("Interval","0");
        properties.setProperty("Type","Average");
        properties.store(outputStream,null);
    }

    */
/**
     * initializing temperature database filter
     * @param location location of document
     *//*

    private static void initTemp(String location){
        try {
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream(location);
            properties.load(inputStream);
            Double longitude = Double.parseDouble((String) properties.get("Longitude"));
            Double latidude  = Double.parseDouble((String) properties.get("Latidude"));
            String query = "SELECT stn, " +
                    "           country, " +
                    "           ( 6371 * acos ( cos ( radians(" + latidude + ") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(" + longitude + ") ) " +
                    "           + sin ( radians(" + latidude + ") ) * sin( radians( latitude ) ) ) ) " +
                    "           AS distance " +
                    "        FROM stations  " +
                    "        WHERE country = 'JAPAN'OR country = 'CHINA'"+
                    "HAVING distance < 5000";
            try {
                Statement statement = Main.sqlConnection.createStatement();
                ResultSet result = statement.executeQuery(query);
                System.out.println("Init Radius Temp Query Executed");
                while (result.next()) {
                    Filter.setFilter(result.getInt("stn"), "TEMP", 0);
                }
            } catch (SQLException sqle) {
                System.err.println(sqle);
            }
        }
        catch(IOException e){
            System.err.println(e);
        }
    }

    */
/**
     * initializing pacific coastline Filter to database.
     * @param location location of document
     *//*

    private static void initCoast(String location){
        try {
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream(location);
            properties.load(inputStream);
            String coastFileName = (String) properties.get("CSV");
            BufferedReader coastFile=null;
            try{
                coastFile = new BufferedReader(new FileReader(coastFileName));
            }
            catch (FileNotFoundException e){
                createNewCSV();
                coastFile = new BufferedReader(new FileReader(coastFileName));
            }
            String line="";
            try{
                while((line=coastFile.readLine())!=null) {
                    Filter.setFilter(Integer.parseInt(line),"PRCP",0);
                    Filter.setCoastLine(Long.valueOf(line));
                }
            }
            catch (IOException e){
            }
        }
        catch (IOException e){
            System.err.println(e);
        }
    }

    */
/**
     * intitializing wind Database collection
     * @param location location of document
     *//*

    private static void initWind(String location) {
        String query = "SELECT `country` FROM `stations` GROUP BY `country`";
        ArrayList<String> countries = new ArrayList<>();
        try {
            Statement statement = Main.sqlConnection.createStatement();
            ResultSet result = statement.executeQuery(query);
            System.out.println("Fetched Countries List");
            while (result.next()) {
                countries.add(result.getString("country"));
                Filter.addCountry(result.getString("country"));
            }
        }
        catch (SQLException sqle) {
            System.err.println(sqle);
        }
        PreparedStatement preparedStatement=null;
        for(String country:countries){
            try{
                preparedStatement = Main.sqlConnection.prepareStatement("SELECT stn FROM stations WHERE country LIKE (?)");
                preparedStatement.setString(1,country);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    Filter.setFilter(resultSet.getInt("stn"),"WNDDIR",0);
                    Filter.setFilter(resultSet.getInt("stn"),"WDSP",0);
                }
            }
            catch (SQLException e){
                System.err.println(e);
            }
        }
    }

    */
/**
     * Creating new Coastline File.
     *//*

    private static void createNewCSV() {
         String stringArray =    "25105, 466860, 466890, 466920, 466940, 466950, 599970, " +
                "466960, 466970, 466990, 467060, 467080, 467300, 467340, 467350, 467360, 467400, 467410, 467425, " +
                "467430, 467440, 467450, 467460, 467480, 467490, 467500, 467510, 467520, 467530, 467540, 467550, " +
                "467560, 467570, 467580, 467590, 467600, 467610, 467620, 467630, 467640, 467650, 467660, 467690, " +
                "467700, 467720, 467770, 467800, 468100, 472185, 474010, 474020, 474040, 474050, 474060, 474070, " +
                "474090, 474110, 474120, 474130, 474170, 474180, 474200, 474210, 474230, 474240, 474250, 474260, " +
                "474280, 474300, 474330, 474340, 474350, 474400, 474410, 474740, 474760, 474790, 474810, 474830, " +
                "474880, 474890, 474900, 475120, 475150, 475160, 475200, 475420, 475450, 475490, 475530, 475570, " +
                "475690, 475700, 475730, 475740, 475750, 475760, 475800, 475810, 475820, 475830, 475840, 475850, " +
                "475870, 475874, 475880, 475900, 475910, 475920, 475970, 475980, 476000, 476020, 476040, 476050, " +
                "476060, 476120, 476170, 476180, 476200, 476240, 476290, 476310, 476340, 476345, 476350, 476360, " +
                "476370, 476380, 476390, 476400, 476410, 476420, 476430, 476480, 476490, 476500, 476530, 476540, " +
                "476550, 476570, 476580, 476600, 476620, 476630, 476660, 476680, 476710, 476720, 476730, 476740, " +
                "476741, 476742, 476750, 476770, 476780, 476790, 476800, 476810, 476830, 476840, 476860, 476870, " +
                "476880, 476900, 476960, 477000, 477040, 477060, 477070, 477090, 477150, 477270, 477350, 477370, " +
                "477380, 477390, 477400, 477410, 477420, 477430, 477440, 477460, 477470, 477473, 477500, 477540, " +
                "477550, 477560, 477590, 477620, 477640, 477641, 477650, 477660, 477670, 477690, 477700, 477710, " +
                "477720, 477740, 477760, 477770, 477780, 477790, 477820, 477830, 477860, 477870, 477880, 477890, " +
                "477895, 477900, 477930, 477940, 477990, 477991, 478000, 478030, 478050, 478070, 478080, 478090, " +
                "478100, 478120, 478140, 478150, 478160, 478170, 478180, 478190, 478210, 478220, 478230, 478240, " +
                "478270, 478290, 478300, 478310, 478350, 478360, 478370, 478380, 478400, 478430, 478440, 478500, " +
                "478510, 478520, 478530, 478540, 478550, 478560, 478570, 478580, 478700, 478720, 478800, 478810, " +
                "478820, 478830, 478840, 478870, 478900, 478910, 478920, 478930, 478950, 478970, 478980, 478990, " +
                "479090, 479100, 479110, 479170, 479180, 479270, 479276, 479290, 479300, 479310, 479330, 479350, " +
                "479360, 479400, 479420, 479450, 479710, 479810, 479910, 486940, 486980, 486990, 588490, 589650, " +
                "589680, 591580, 593450, 593530, 593580, 593620, 595590, 595620, 595670, 702759, 749316, 749331, " +
                "910660, 912120, 912180, 912210, 912320, 912333, 912450, 912500, 912750, 913340, 913480, 913481, " +
                "913660, 913691, 913760, 914080, 914130, 914900, 915170, 915200, 915770, 915820, 915900, 915915, " +
                "915920, 916100, 916430, 916500, 916520, 916590, 916600, 916700, 916800, 916830, 916910, 916930, " +
                "916970, 916990, 917530, 917540, 918000, 918040, 918110, 918260, 918300, 918400, 918430, 918470, " +
                "919250, 919300, 919380, 919410, 919430, 919440, 919480, 919520, 919540, 919580, 920350, 930110, " +
                "930120, 931120, 931190, 932910, 933080, 934010, 934170, 934340, 934360, 935160, 935450, 936140, " +
                "937800, 938060, 938440, 938960, 939440, 939860, 939970, 940140, 940270, 940350, 940850, 949960, " +
                "960350, 960870, 961090, 961790, 962210, 962370, 962950, 963150, 963154, 963230, 965810, 966330, " +
                "966850, 967430, 967470, 967490, 967810, 968050, 968810, 969330, 969350, 969870, 969950, 970140, " +
                "970480, 970720, 971200, 971260, 971460, 971800, 971925, 972300, 972400, 972600, 973000, 973400, " +
                "974280, 974300, 975600, 977240, 977900, 978100, 979000, 981350, 982220, 982230, 982320, 983240, " +
                "983250, 983270, 983275, 983280, 983300, 984250, 984260, 984280, 984290, 984300, 984310, 984320, " +
                "984330, 984400, 984440, 984460, 984470, 985310, 985360, 985380, 985430, 985460, 985480, 985500, " +
                "985530, 985580, 986180, 986370, 986420, 986440, 986450, 986460, 986530, 987470, 987480, 987520, " +
                "987530, 987550, 988360, 988510, 911620, 911650, 911690, 911681, 911700, 911760, 911840, 911820, " +
                "911800, 911780, 911860, 911905, 911904, 911903, 911975, 912850, 840010, 854690, 486150, 486190, " +
                "486180, 486570, 486740, 964130, 960011, 964210, 964410, 964490, 964650, 964710, 964770, 964910, " +
                "964810, 489200, 484780, 484775, 484610, 484600, 484590, 484650, 484750, 485000, 485170, 485577, " +
                "485500, 485680, 485800, 485830, 485010, 488260, 488400, 488450, 488460, 488480, 488490, 488510, " +
                "488515, 488525, 488520, 488526, 488561, 488550, 488635, 488685, 488700, 488730, 488770, 488970, " +
                "488870, 489041, 489180, 489070, 489170, 470030, 470080, 470250, 470460, 470550, 470610, 471074, " +
                "470900, 470920, 471070, 471060, 471300, 749388, 471380, 471390, 471540, 471590, 471620, 471680, " +
                "471700, 471750, 471650, 471690, 471890, 471870, 471850, 471820, 471840, 471410, 471320, 471130, " +
                "471131, 471030, 471020, 470680, 545870, 544710, 544490, 546560, 547510, 547530, 547760, 548630, " +
                "548570, 549450, 580400, 582650, 583211, 584720, 584730, 584770, 585690, 586660, 587520, 587650, " +
                "588530, 588490, 589440, 467570, 467360, 591340, 593160, 595010, 450050, 450040, 450070, 450110, " +
                "596730, 596580, 597580, 470311, 598550, 599480, 574941, 598380, 596470, 488390, 596440, 488960";
        String[] stringArray2 = stringArray.split(", ");
        int[] stns = new int[stringArray2.length];
        for(int i=0;i<stringArray2.length;i++){
            stns[i]=Integer.parseInt(stringArray2[i]);
        }
        FileWriter fileWriter=null;
        try {
             fileWriter = new FileWriter("coastline_pacific.csv");
            for (int stn : stns) {
                fileWriter.append(String.valueOf(stn));
                fileWriter.append("\n");
            }
        }
        catch (IOException e) {
        }
        finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                System.out.println("Created CSV file");
            }
            catch (IOException e) {
            }
        }
    }
}
*/
