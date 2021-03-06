import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.print.PrinterException;
import java.io.Console;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.IntStream;

public class AutoScan extends Thread{

    /* Error message */
    private static final String ANSI_RED = "\u001B[31m"; // color changers for console text
    private static final String ANSI_RESET = "\u001B[0m"; // color changers for console text
    private static final String ANSI_GREEN = "\033[0;32m"; // color changers for console text

    private static int SCAN_LAST_AUTO = 2;
    private static int YEAR = 2008;
    private static int MAX_PRICE = 7000;

    private static String FILTER_URL;


    public AutoScan(int numOfAuto, int year, int maxPrice) {

        FILTER_URL = "https://auto.ria.com/search/?year[0].gte=" + year
                + "&categories.main.id=1&" +
                "region.id[0]=16&city.id[0]=16&price.USD.lte=" + maxPrice +
                "&price.currency=1&sort[0].order=dates.created.desc&" +
                "abroad.not=0&custom.not=1&page=0&size=" + numOfAuto;
    }

    public void run(){
        System.out.println("Auto-scan thread running");
        try {
            doAutoScan();
        } catch (IOException | ClassNotFoundException | BadLocationException | PrinterException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return console with log
     *
     * @return Return console with log
     */
    public void doAutoScan() throws IOException, ClassNotFoundException, BadLocationException, PrinterException, URISyntaxException {

        /* Get auto in filtered url */
        CarsList<Car> newAutos = new CarsUrlReader(FILTER_URL).getCars();

        /* LOG */
        // MyConsole cmd = new MyConsole(GUI_autoria.WIDTH / 3, GUI_autoria.HEIGHT);

        /* For every auto */
        int counter = 1;
        for (Car car : newAutos) {

            /* Get database for auto mark and model */
            CarsList<Car> autoDb = CarsDb.getCarInfo(car.getMark(),car.getModel(),true);

            /* Analyzer */
            CarAnalyzer analyzer = new CarAnalyzer(autoDb, car.getGbo());

            /* Year - average price */
            int averagePriceYear = calcAveragePrice(car, analyzer, CarAnalyzer.Arg.YEAR);

            /* Mileage - average price */
            int averagePriceMileage = calcAveragePrice(car, analyzer, CarAnalyzer.Arg.MILEAGE);

            /* Message color */
            Color color = Color.BLACK;
            if (car.getPrice() <= averagePriceYear && car.getPrice() <= averagePriceMileage){
                color = Color.decode("#00CC00");
            }

            /* Print log */
            MyConsole.printLog(String.format("%d. %s %s %d$ %d г. %d тыс.км ГБО - %b\n",
                    counter, car.getMark().toUpperCase(), car.getModel().toUpperCase(), car.getPrice(),
                    car.getYear(), car.getMileage(), car.getGbo()), color);
            MyConsole.printLog(String.format("   Average price per year - %d$\n", averagePriceYear), color);
            MyConsole.printLog(String.format("   Average price per mileage - %d$\n", averagePriceMileage), color);
            MyConsole.printLog("   " + car.getUrl() + "\n");

            counter++;
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, BadLocationException, PrinterException, URISyntaxException {
        new AutoScan(SCAN_LAST_AUTO, YEAR, MAX_PRICE).doAutoScan();

    }

    /**
     * Calculate average price for arg
     *
     * @param car car
     * @param analyzer analyzer
     * @param arg Year or Mileage
     * @return counted average price
     */
    private static int calcAveragePrice(Car car, CarAnalyzer analyzer, CarAnalyzer.Arg arg) {

        /* Get indicators according to arg */
        int indicator = 0;
        int increment = 0;
        switch (arg){
            case YEAR:
                indicator = car.getYear();
                increment = 1;
                break;
            case MILEAGE:
                indicator = car.getMileage();
                increment = analyzer.getMileageGradation();
                break;
        }

        /* Get Plus and Minus indicators */
        int [] averagePrices = new int[] {
                analyzer.averagePriceByIndicator(arg, indicator),
                analyzer.averagePriceByIndicator(arg, indicator + increment),
                analyzer.averagePriceByIndicator(arg, indicator - increment)
        };

        /* Count correct results */
        int correctResults = 0;
        for (int i: averagePrices) if (i != 0) correctResults++;

        /* Count sum */
        int sum = IntStream.of(averagePrices).sum();

        return correctResults != 0 ? sum / correctResults : 0;
    }


}
