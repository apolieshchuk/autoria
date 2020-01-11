import java.util.ArrayList;
import java.util.HashMap;

public class CarAnalyze {

    private ArrayList<CarCard> carsDb;
    private int averageAutoPrice;
    private HashMap <Integer, ArrayList<Integer>> yearPriceDb;
    private HashMap <Integer, ArrayList<Integer>> mileagePriceDb;

    enum Arg { YEAR, MILEAGE }

    public CarAnalyze(ArrayList<CarCard> carsDb) {
        /* Main db for analyze */
        this.carsDb = carsDb;

        /* Average auto price */
        int sum = 0;
        for (CarCard car : carsDb) sum += car.getPrice();
        averageAutoPrice = sum / carsDb.size();

        /* year-price db */
        this.yearPriceDb = priceDb(Arg.YEAR);

        /* mileage-price db */
        this.mileagePriceDb = priceDb(Arg.MILEAGE);

    }

    /**
     * Price db
     *
     * @return HashMap <Integer, ArrayList<Integer>> yearPriceDb
     * @param arg arg for looking price
     */
    private  HashMap <Integer, ArrayList<Integer>> priceDb(Arg arg){
        HashMap <Integer, ArrayList<Integer>> result = new HashMap<>();
        for (CarCard car: carsDb) {
            /* Bad price filter */
            if (car.getPrice() > averageAutoPrice * 2) continue;

            /* Fill in hashmap */
            int indicator = arg == Arg.YEAR ? car.getYear() : car.getMileage();
            if (result.containsKey(indicator)){
                result.get(indicator).add(car.getPrice());
            }else{
                ArrayList<Integer> list = new ArrayList<Integer>() {{ car.getPrice(); }};
                result.put(indicator, list);
            }
        }
        return result;
    }

    /**
     * Average price
     *
     * @return HashMap<arg, price>
     */
    public HashMap<Integer, Integer> averagePrice(Arg arg){
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
}
