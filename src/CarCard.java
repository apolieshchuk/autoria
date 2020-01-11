import java.io.Serializable;

public class CarCard implements Serializable {

    private final int GBO_PRICE = 500;

    private String title;
    private int mileage;
    private String fuel;
    private int year;
    private int price;
    private int priceWithGbo;
    private Boolean gbo;

    /* Getters and setters*/
    public String getTitle() {
        return title;
    }

    public int getMileage() {
        return mileage;
    }

    public String getFuel() {
        return fuel;
    }

    public int getYear() {
        return year;
    }

    public int getPrice() {
        return price;
    }

    public Boolean getGbo() {
        return gbo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;

        /* Did auto have gbo? */
        gbo = fuel.toLowerCase().contains("газ");

        /* Auto price with/wo gbo */
        if (gbo){
            priceWithGbo = this.price;
            price = priceWithGbo - GBO_PRICE;
        }
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriceWithGbo() {
        return priceWithGbo;
    }
}
