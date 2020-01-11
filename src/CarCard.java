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
    private String mark;
    private String model;
    private String url;

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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
