import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        CarsUrlReader carsUrlReader = new CarsUrlReader();
        carsUrlReader.getCarsInUrl("https://auto.ria.com/legkovie/hyundai/");
    }
}
