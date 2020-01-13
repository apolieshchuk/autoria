import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class CarsDb {

    private static final int GBO_PRICE = 500;

    /* Main url route */
    private static String startUrl = "https://auto.ria.com/legkovie/";

    /* Path to db */
    private static final String DB_PATH = "static/autoDb/";

    /**
     * Get info about car's
     *
     * @param mark String mark of car
     * @param model String model of car
     * @param fromDatabase Get info from archive db
     * @return database with car in param
     * @throws IOException            sad shit
     * @throws ClassNotFoundException very sad shit
     */
    public static CarsList<Car> getCarInfo(String mark, String model, boolean fromDatabase)
            throws IOException, ClassNotFoundException {

        /* If mark or model in kyril transliterate to latin */
        if (Pattern.matches(".*\\p{InCyrillic}.*", mark)){
            mark = transliterate(mark);
        }
        if (Pattern.matches(".*\\p{InCyrillic}.*", model)){
            model = transliterate(model);
            // model = model.replaceAll(" ", "-");
        }

        /* Add mark and model to main route */
        String markURL = startUrl + mark + "/" + model;

        /* Create autoria reader */
        CarsUrlReader autoRiaReader = new CarsUrlReader(markURL);

        /* Main db of all cars */
        CarsList<Car> carsDb = new CarsList<>();

        /* DB file */
        File file = new File(DB_PATH + mark + "_" + model + ".arf");

        /* Try find db in static db */
        if (file.exists() && fromDatabase) carsDb = readFromDb(file);

        /* Get total pages of this cars model/mark
        *  WARNING!!! Total pages for 20 per/page */
        int totalPages = autoRiaReader.getTotalPages();

        /* Parse all pages and get cars on it if we don't have already db */
        if (carsDb.size() == 0) {
            /* Auto's in page when directing
             * WARNING !!! when go on num page default there- 10 auto FOREVER */
            int AUTO_IN_PAGE = 10;
            for (int i = 1; i < totalPages * 2; i++) { // *2 bc default 10 cars on page
                CarsList<Car> carsOnPage = autoRiaReader.getCars();
                if (carsOnPage != null ) carsDb.addAll(carsOnPage);
                autoRiaReader = new CarsUrlReader(markURL + "/?page=" + i);
                System.out.printf("\r%d/%d auto's %s %s",
                        (i + 1) * AUTO_IN_PAGE, totalPages * 2 * AUTO_IN_PAGE, mark, model);
            }
            System.out.println();
        }

        /* Write db in file */
        if (!file.exists()) writeInDb(carsDb, file);

        return carsDb;
    }

    /**
     * Write db in file
     *
     * @param carsDb auto's database
     * @param file   path to file
     */
    private static void writeInDb(CarsList<Car> carsDb, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(carsDb);
        oos.close();
    }

    /**
     * Read db in file
     *
     * @param file file *.arf
     */
    private static CarsList<Car> readFromDb(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        CarsList<Car> carsDb = (CarsList<Car>) ois.readObject();
        ois.close();
        return carsDb;
    }

    /**
     * Transliterate method (c) xknvlk
     *
     * @param message russian string
     * @return eng str
     */
    private static String transliterate(String message){
        char[] abcCyr =   {' ','а','б','в','г','д','е','ё', 'ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х', 'ц','ч', 'ш','щ','ъ','ы','ь','э', 'ю','я','А','Б','В','Г','Д','Е','Ё', 'Ж','З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х', 'Ц', 'Ч','Ш', 'Щ','Ъ','Ы','Ь','Э','Ю','Я','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        String[] abcLat = {" ","a","b","v","g","d","e","e","zh","z","i","y","k","l","m","n","o","p","r","s","t","u","f","h","ts","ch","sh","sch", "","i", "","e","ju","ja","A","B","V","G","D","E","E","Zh","Z","I","Y","K","L","M","N","O","P","R","S","T","U","F","H","Ts","Ch","Sh","Sch", "","I", "","E","Ju","Ja","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++ ) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }

    public static int getGboPrice() {
        return GBO_PRICE;
    }
}

