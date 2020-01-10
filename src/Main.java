import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        CarsUrlReader autoRiaReader = new CarsUrlReader("https://auto.ria.com/legkovie/?page=1");
        // autoRiaReader.getCarsInUrl();
        System.out.println(autoRiaReader.getTotalPages());
    }
}
