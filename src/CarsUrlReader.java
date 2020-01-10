import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CarsUrlReader {

    public CarsUrlReader() {

    }

    public Document getUrlDom(String urlString) throws IOException {
        URL url = new URL(urlString);

        /* Read html form url stream (commons-io-1.3.2.jar library) */
        String htmlString = IOUtils.toString(url.openStream());

        /* Get DOM Html (jsoup-1.12.1.jar library)*/
        return Jsoup.parse(htmlString);
    }

    /**
     * Get array list with cars class in url
     *
     * @param urlString url as String
     */
    public ArrayList<CarCard> getCarsInUrl (String urlString) throws IOException {
        /* Get html DOM */
        Document document = getUrlDom(urlString);

        /* Create arraylist of results */
        ArrayList<CarCard> cars = new ArrayList<>();

        /* Get all cars-cards in html */
        Elements tickets = document.body().getElementsByClass("ticket-item");

        /* Scan all cars and get info */
        for (Element ticket: tickets) {

            /* Create CarCard class */
            CarCard car = new CarCard();

            /* Title */
            Elements card_title = ticket.select(".ticket-title a");
            String title = card_title.attr("title");
//            String title = cutInfoFromDiv("title=\"([^\"]+)", card_title);
            System.out.println(title);
            car.setTitle(title);

            /* Price */
            Elements card_price = ticket.select(".price-ticket");
            String price = card_price.attr("data-main-price");
//            String price = cutInfoFromDiv("data-main-price=\"([^\"]+)", card_price);
            System.out.println(price + "$");
            car.setPrice(Integer.parseInt(price));

            /* Mileage */
            String card_mileage = ticket.select(".characteristic li:first-child").text();
            String mileage = cutInfoFromDiv("(\\d+)", card_mileage);
            System.out.println(mileage + " тыс. км");
            car.setMileage(Integer.parseInt(mileage));

            /* Fuel */
            String card_fuel = ticket.select(".characteristic li:nth-child(3)").text();
            String fuel = card_fuel.substring(0, card_fuel.indexOf(","));
            System.out.println(fuel);
            car.setFuel(fuel);

            /* Year */
            Elements card_year = ticket.select("div:first-child");
            String year = card_year.attr("data-year");
            System.out.println(year);
            car.setYear(Integer.parseInt(year));

            System.out.println("---------------------------------------------");
            cars.add(car);
        }

        return cars;
    }

    /**
     * Cut part of String from the DOM div according to regex
     *
     * @param regex regex for cutting
     * @param div block from what we cut
     * @return cut string
     *
     */
    private String cutInfoFromDiv(String regex, String div) {
        Matcher m = Pattern.compile(regex).matcher(div);
        if (!m.find()) return "";
        return m.group(1);
    }
}
