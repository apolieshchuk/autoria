import java.util.ArrayList;
import java.util.HashMap;

public class CarAnalyze {
    private final int MILEAGE_GRADATION = 20;

    private ArrayList<CarCard> carsDb;
    private boolean accordingGbo;
    private int averageAutoPrice;
    private int averageAutoMileage;
    private HashMap <Integer, ArrayList<Integer>> yearPriceDb;
    private HashMap <Integer, ArrayList<Integer>> mileagePriceDb;

    enum Arg { YEAR, MILEAGE, PRICE }

    public CarAnalyze(ArrayList<CarCard> carsDb, boolean gbo) {
        /* according to gbo */
        accordingGbo = gbo;

        /* Main db for analyze */
        this.carsDb = carsDb;

        /* Average auto price */
        averageAutoPrice = avrageByAllAuto(Arg.PRICE);

        /* Average auto mileage */
        averageAutoMileage = avrageByAllAuto(Arg.MILEAGE);

        /* year-price db */
        this.yearPriceDb = priceDb(Arg.YEAR);

        /* mileage-price db */
        this.mileagePriceDb = priceDb(Arg.MILEAGE);

    }

    /* Average indicator for all cars */
    private int avrageByAllAuto(Arg arg){
        int sum = 0;
        for (CarCard car : carsDb){
            int indicator = 0;
            switch (arg){
                case YEAR:
                    break;
                case MILEAGE:
                    indicator = car.getMileage();
                    break;
                case PRICE:
                    indicator = car.getPrice();
                    break;
            }
            sum += indicator;
        }
        return sum / carsDb.size();
    }

    /**
     *  Nearest gradation for mileage
     *
     * @return   Return nearest gradation for mileage
     */
    public int nearestGradation(int mileage){
        return (mileage / MILEAGE_GRADATION + 1) * MILEAGE_GRADATION;
    }

    /**
     * Price db
     *
     * @return HashMap <Integer, ArrayList<Integer>> PriceDb
     * @param arg arg for looking price
     */
    private  HashMap <Integer, ArrayList<Integer>> priceDb(Arg arg){
        HashMap <Integer, ArrayList<Integer>> result = new HashMap<>();
        for (CarCard car: carsDb) {
            /* Bad price and mileage filter */
            if (car.getPrice() > averageAutoPrice * 2 || car.getPrice() < averageAutoPrice / 2) continue;
            if (car.getMileage() > averageAutoMileage * 2) continue;

            /* Get indicator */
            int indicator = 0;
            switch (arg){
                case YEAR:
                    indicator = car.getYear();
                    break;
                case MILEAGE:
                    indicator = nearestGradation(car.getMileage());
                    break;
            }

            /* Fill in hashmap */
            if (result.containsKey(indicator)){
                result.get(indicator).add(car.getPrice());
            }else{
                int price = accordingGbo ? car.getPriceWithGbo() : car.getPrice();
                ArrayList<Integer> list = new ArrayList<>();
                list.add(price);
                result.put(indicator, list);
            }
        }
        return result;
    }

    /**
     * Average price in all gradations
     *
     * @return HashMap<arg, price>
     */
    public HashMap<Integer, Integer> averagePriceByGradations(Arg arg){
        HashMap<Integer,Integer> result = new HashMap<>();
        HashMap <Integer, ArrayList<Integer>> priceDb = arg == Arg.YEAR ? yearPriceDb : mileagePriceDb;

        for(Integer indicator : priceDb.keySet()){
            /* average */
            int sum = 0;
            for (Integer price: priceDb.get(indicator)) {
                sum += price;
            }
            if (priceDb.get(indicator).size() != 0 ){ // 0 division exception
                result.put(indicator, sum/priceDb.get(indicator).size());
            }
        }
        return result;
    }

    /* GETTERS & SETTERS */
    public HashMap<Integer, ArrayList<Integer>> getYearPriceDb() {
        return yearPriceDb;
    }

    public HashMap<Integer, ArrayList<Integer>> getMileagePriceDb() {
        return mileagePriceDb;
    }

    public int getMileageGradation() {
        return MILEAGE_GRADATION;
    }
}
