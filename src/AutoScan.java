import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AutoScan {

    private static final int SCAN_LAST_AUTO = 5;

    private static final String FILTER_URL = "https://auto.ria.com/search/?year[0].gte=2008&categories.main.id=1&" +
            "region.id[0]=16&city.id[0]=16&price.USD.lte=7000&price.currency=1&" +
            "sort[0].order=dates.created.desc&abroad.not=0&custom.not=1&page=0&size=" + SCAN_LAST_AUTO;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        /* Get auto in filtered url */
        ArrayList<CarCard> newAutos = new CarsUrlReader(FILTER_URL).getCars();

        /* For every auto */
        int counter = 1;
        for (CarCard car : newAutos) {

            /* Get database for auto mark and model */
            ArrayList<CarCard> autoDb = new CarsDb().getCarInfo(car.getMark(),car.getModel(),true);

            /* Analyzer */
            CarAnalyzer analyzer = new CarAnalyzer(autoDb, car.getGbo());

            /* Year - average price */
            int averagePriceThisYear = analyzer.averagePriceByIndicator(CarAnalyzer.Arg.YEAR, car.getYear());
            int averagePricePlusYear = analyzer.averagePriceByIndicator(CarAnalyzer.Arg.YEAR, car.getYear() + 1);
            int averagePriceMinusYear = analyzer.averagePriceByIndicator(CarAnalyzer.Arg.YEAR, car.getYear() - 1);
            int averagePriceYear = (averagePriceThisYear + averagePricePlusYear + averagePriceMinusYear) / 3;

            /* Mileage - average price */
            int averagePriceThisMileage = analyzer.averagePriceByIndicator(CarAnalyzer.Arg.MILEAGE, car.getMileage());
            int averagePricePlusMileage = analyzer.averagePriceByIndicator(CarAnalyzer.Arg.MILEAGE,
                    car.getMileage() + analyzer.getMileageGradation());
            int averagePriceMinusMileage = analyzer.averagePriceByIndicator(CarAnalyzer.Arg.MILEAGE,
                    car.getMileage() - analyzer.getMileageGradation());
            int averagePriceMileage = (averagePriceThisMileage + averagePricePlusMileage + averagePriceMinusMileage) / 3;

            /* Print log */
            System.out.printf("%d. %s %s %d$ %d г. %d тыс.км ГБО - %b\n",
                    counter, car.getMark().toUpperCase(), car.getModel().toUpperCase(), car.getPrice(),
                    car.getYear(), car.getMileage(), car.getGbo());
            System.out.printf("   Average price buy year - %d$\n", averagePriceYear);
            System.out.printf("   Average price buy mileage - %d$\n", averagePriceMileage);
            System.out.println("   " + car.getUrl());

            counter++;
        }
    }

}
