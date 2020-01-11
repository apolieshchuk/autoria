import java.io.*;
import java.util.*;

public class ManualInput {

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
        CarAnalyzer analyzer = new CarAnalyzer(carsDb, gbo);

        System.out.printf("Average price for %s %dг. %d тыс км ГБО - %b:\n ", auto.toUpperCase(), year, mileage, gbo);
        System.out.println("----------------------------------------------");

        /* Year - average price */
        HashMap<Integer, Integer> yearAveragePrice = analyzer.averagePriceByGradations(CarAnalyzer.Arg.YEAR);
        System.out.printf("Average price: %d г. - %d$\n", year, yearAveragePrice.get(year));
        System.out.printf("Average price: %d г. - %d$\n", year - 1, yearAveragePrice.get(year - 1));
        System.out.printf("Average price: %d г. - %d$\n", year + 1, yearAveragePrice.get(year + 1));

        System.out.println("----------------------------------------------");

        /* Mileage - average price */
        HashMap<Integer, Integer> mileagePriceDb = analyzer.averagePriceByGradations(CarAnalyzer.Arg.MILEAGE);
        System.out.printf("Price: до %d тыс.км - %d$\n",
                mileage, mileagePriceDb.get(analyzer.nearestGradation(mileage)));
        /* - mileage */
        int minusMileage = analyzer.nearestGradation(mileage - analyzer.getMileageGradation());
        System.out.printf("Price: до %d тыс.км - %d$\n", minusMileage, mileagePriceDb.get(minusMileage));
        /* + mileage */
        int plusMileage = analyzer.nearestGradation( mileage + analyzer.getMileageGradation());
        System.out.printf("Price: до %d тыс.км - %d$\n", plusMileage, mileagePriceDb.get(plusMileage));

    }
}
