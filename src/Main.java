import java.io.*;
import java.util.*;

public class Main {

    private static final String DB_PATH = "static/autoDb/";
    private static String auto;
    private static int year;
    private static int mileage;
    private static boolean gbo;

    public static void main(String[] args) throws Exception {



        /* Indicators */
        auto = args[0];
        year = Integer.parseInt(args[1]);
        mileage = Integer.parseInt(args[2]);
        gbo = args[3].toLowerCase().contains("gbo");

        /* Get info about car */
        ArrayList<CarCard> carsDb = getCarInfo(auto, true);

        /* Analyze auto db */
        printCarAnalyze(carsDb);
    }

    /**
     * Print analyze from carsDb
     *
     * @param carsDb array list of cars of the same models
     */
    private static void printCarAnalyze(ArrayList<CarCard> carsDb) {
        CarAnalyze analyzer = new CarAnalyze(carsDb, gbo);

        System.out.printf("Average price for %s %dг. %d тыс км ГБО - %b:\n ", auto.toUpperCase(), year, mileage, gbo);
        System.out.println("----------------------------------------------");

        /* Year - average price */
        HashMap<Integer, Integer> yearAveragePrice = analyzer.averagePriceByGradations(CarAnalyze.Arg.YEAR);
        System.out.printf("Average price: %d г. - %d$\n", year, yearAveragePrice.get(year));
        System.out.printf("Average price: %d г. - %d$\n", year - 1, yearAveragePrice.get(year - 1));
        System.out.printf("Average price: %d г. - %d$\n", year + 1, yearAveragePrice.get(year + 1));

        System.out.println("----------------------------------------------");

        /* Mileage - average price */
        HashMap<Integer, Integer> mileagePriceDb = analyzer.averagePriceByGradations(CarAnalyze.Arg.MILEAGE);
        System.out.printf("Price: до %d тыс.км - %d$\n",
                mileage, mileagePriceDb.get(analyzer.nearestGradation(mileage)));
        /* - mileage */
        int minusMileage = analyzer.nearestGradation(mileage - analyzer.getMileageGradation());
        System.out.printf("Price: до %d тыс.км - %d$\n", minusMileage, mileagePriceDb.get(minusMileage));
        /* + mileage */
        int plusMileage = analyzer.nearestGradation( mileage + analyzer.getMileageGradation());
        System.out.printf("Price: до %d тыс.км - %d$\n", plusMileage, mileagePriceDb.get(plusMileage));

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
    private static ArrayList<CarCard> getCarInfo(String autoName, boolean fromDatabase)
            throws IOException, ClassNotFoundException {
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
                int autoInPage = 20;
                System.out.printf("\r%d/%d auto's  ", i * autoInPage, (totalPages - 1) * autoInPage);
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
