import java.util.*;

public class ManualInput {


    private static String mark;
    private static String model;
    private static int year;
    private static int mileage;
    private static boolean gbo;

    public static void main(String[] args) throws Exception {

        /* Indicators */
        mark = args[0];
        model = args[1];
        year = Integer.parseInt(args[2]);
        mileage = Integer.parseInt(args[3]);
        gbo = args[4].toLowerCase().contains("gbo");

        /* Get info about car */
        CarsList<Car> carsDb = new CarsDb().getCarInfo(mark, model, true);

        /* Analyze auto db */
        printCarAnalyze(carsDb);
    }

    /**
     * Print analyze from carsDb
     *
     * @param carsDb array list of cars of the same models
     */
    private static void printCarAnalyze(CarsList<Car> carsDb) {
        CarAnalyzer analyzer = new CarAnalyzer(carsDb, gbo);

        System.out.printf("Average price for %s %s %dг. %d тыс км ГБО - %b:\n ",
                mark.toUpperCase(), model.toUpperCase(), year, mileage, gbo);
        System.out.println("----------------------------------------------");

        /* Year - average price */
        System.out.printf("Average price: %d г. - %d$\n",
                year, analyzer.averagePriceByIndicator(CarAnalyzer.Arg.YEAR,year));
        System.out.printf("Average price: %d г. - %d$\n",
                year-1, analyzer.averagePriceByIndicator(CarAnalyzer.Arg.YEAR,year-1));
        System.out.printf("Average price: %d г. - %d$\n",
                year+1, analyzer.averagePriceByIndicator(CarAnalyzer.Arg.YEAR,year+1));

        System.out.println("----------------------------------------------");

        System.out.printf("Average price: %d тыс.км - %d$\n",
                mileage, analyzer.averagePriceByIndicator(CarAnalyzer.Arg.MILEAGE, mileage));
        int mileageMinus = mileage - analyzer.getMileageGradation();
        System.out.printf("Average price: %d тыс.км - %d$\n",
                mileageMinus, analyzer.averagePriceByIndicator(CarAnalyzer.Arg.MILEAGE,mileageMinus));
        int mileagePlus = mileage + analyzer.getMileageGradation();
        System.out.printf("Average price: %d тыс.км - %d$\n",
                mileagePlus, analyzer.averagePriceByIndicator(CarAnalyzer.Arg.MILEAGE,mileagePlus));

    }
}
