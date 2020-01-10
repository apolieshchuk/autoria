import java.io.*;
import java.util.*;

public class Main {

    private static final String DB_PATH = "static/autoDb/";
    private static String auto;

    public static void main(String[] args) throws Exception {
//        /* Read DB for all args */
//        ArrayList<CarCard> carsDb = null;
//        for (int i = 0; i < args.length; i++) {
//            carsDb = getCarInfo(args[i]);
//        }
        /* Auto mark and model */
        auto = args[0];

        /* Get info about car */
        ArrayList<CarCard> carsDb = getCarInfo(auto, true);

        /* Analyze auto db */
        printCarAnalyze(carsDb, args[1]);
    }

    /**
     * Print analyze from carsDb
     *
     * @param carsDb array list of cars of the same models
     * @param arg option for report type
     */
    private static void printCarAnalyze(ArrayList<CarCard> carsDb, String arg) {
        CarAnalyze analyzer = new CarAnalyze(carsDb);

        /* Year - average price */
        if (arg.toLowerCase().equals("year")){
            HashMap<Integer, Integer> yearAveragePrice = analyzer.averagePrice(CarAnalyze.Arg.YEAR);
            System.out.println("Average price for "+ auto.toUpperCase() + " by year");
            for (Integer year: yearAveragePrice.keySet()) {
                System.out.printf("Average price: %d y. - %d$\n", year,yearAveragePrice.get(year));
            }
            System.out.println("----------------------------------------------");
        }


        /* Mileage - price */
        if (arg.toLowerCase().equals("mileage")){
            HashMap<Integer, Integer> mileagePriceDb = analyzer.averagePrice(CarAnalyze.Arg.MILEAGE);
            SortedSet<Integer> keys = new TreeSet<>(mileagePriceDb.keySet()); // sort list
            System.out.println("Price for "+ auto.toUpperCase() + " by mileage ");
            for (Integer mileage: keys) {
                System.out.printf("Price: %d тыс.км - %d$\n", mileage, mileagePriceDb.get(mileage));
            }
            System.out.println("----------------------------------------------");
        }
    }

    /**
     * Get info about car's
     *
     * @param autoName String mark and model of car
     * @param fromDatabase Get info from archive db
     * @return database with car in param
     * @throws IOException            sad shit
     * @throws ClassNotFoundException very sad shit
     */
    private static ArrayList<CarCard> getCarInfo(String autoName, boolean fromDatabase) throws IOException, ClassNotFoundException {
        /* Main url route */
        String startUrl = "https://auto.ria.com/legkovie/";

        /* Get auto from args */
        String[] auto = autoName.split(" ");

        /* Add mark and model to main route */
        startUrl += auto[0] + "/" + auto[1];

        /* Create autoria reader */
        CarsUrlReader autoRiaReader = new CarsUrlReader(startUrl);

        /* Main db of all cars */
        ArrayList<CarCard> carsDb = new ArrayList<>();

        /* DB file */
        File file = new File(DB_PATH + auto[0] + "_" + auto[1] + ".arf");

        /* Try find db in static db */
        if (file.exists() && fromDatabase) carsDb = readFromDb(file);

        /* Get total pages of this cars model/mark */
        int totalPages = autoRiaReader.getTotalPages();

        /* Parse all pages and get cars on it if we don't have already db */
        if (carsDb.size() == 0) {
            for (int i = 1; i < totalPages; i++) {
                carsDb.addAll(autoRiaReader.getCarsInUrl());
                autoRiaReader.setUrl(startUrl + "?page=" + i);
                System.out.printf("\r%d/%d ", i, totalPages - 1);
            }
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
    private static void writeInDb(ArrayList<CarCard> carsDb, File file) throws IOException {
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
    private static ArrayList<CarCard> readFromDb(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        ArrayList<CarCard> carsDb = (ArrayList<CarCard>) ois.readObject();
        ois.close();
        return carsDb;
    }
}
