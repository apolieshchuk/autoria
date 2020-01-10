import java.io.Serializable;

public class CarCard implements Serializable {

    private String title;
    private int mileage;
    private String fuel;
    private int year;
    private int price;

    public CarCard() {

    }

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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
