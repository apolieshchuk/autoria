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

    private Document document;
    private ArrayList<CarCard> cars;

    private final boolean CARS_LOG = false;

    public CarsUrlReader(String urlString) throws IOException {
        document = getUrlDom(urlString);
        cars = getCarsInUrl();
    }

    /**
     * Get total number of pages
     *
     * @return max number of pages
     */
    public int getTotalPages() throws IOException {
        try{
            String totalPages = document.select(".pager span:nth-last-child(3) a").text();
            totalPages = totalPages.replaceAll(" ","");
            return Integer.parseInt(totalPages);
        }catch (Exception e){
            return 1;
        }
    }

    /**
     * Get DOM from url
     * @param urlString urlString -address
     * @return Document DOM
     * @throws IOException error
     */
    private Document getUrlDom(String urlString) throws IOException {
        /* String to url */
        URL url = new URL(urlString);

        /* Read html form url stream (commons-io-1.3.2.jar library) */
        String htmlString = IOUtils.toString(url.openStream());

        /* Get DOM Html (jsoup-1.12.1.jar library)*/
        return Jsoup.parse(htmlString);
    }

    /**
     * Get array list with cars class in url
     */
    private ArrayList<CarCard> getCarsInUrl() {
        /* Create arraylist of results */
        ArrayList<CarCard> cars = new ArrayList<>();

        /* Get all cars-cards in html */
        Elements tickets = document.body().getElementsByClass("ticket-item");

        /* Scan all cars and get info */
        for (Element ticket : tickets) {

            /* Create CarCard class */
            CarCard car = new CarCard();

            /* Title and url */
            Elements card_title = ticket.select(".ticket-title a");
            String title = card_title.attr("title");
            String url = card_title.attr("href");
            if (CARS_LOG) System.out.println(title);
            car.setTitle(title);
            car.setUrl(url);

            /* Price */
            Elements card_price = ticket.select(".price-ticket");
            String price = card_price.attr("data-main-price");
//            String price = cutInfoFromDiv("data-main-price=\"([^\"]+)", card_price);
            if (CARS_LOG) System.out.println(price + "$");
            try {
                car.setPrice(Integer.parseInt(price));
            } catch (Exception e) {
                car.setPrice(0);
            }

            /* Mileage */
            String card_mileage = ticket.select(".characteristic li:first-child").text();
            String mileage = cutInfoFromDiv("(\\d+)", card_mileage);
            if (CARS_LOG) System.out.println(mileage + " тыс. км");
            try {
                car.setMileage(Integer.parseInt(mileage));
            } catch (Exception e) {
                car.setMileage(0);
            }

            /* Fuel */
            String card_fuel = ticket.select(".characteristic li:nth-child(3)").text();
            String fuel = card_fuel.contains(",") ? card_fuel.substring(0, card_fuel.indexOf(",")) : "";
            if (CARS_LOG) System.out.println(fuel);
            car.setFuel(fuel);

            /* Auto year, mark and model */
            Elements card_attr= ticket.select("div:first-child");
            car.setMark(card_attr.attr("data-mark-name"));
            car.setModel(card_attr.attr("data-model-name"));
            String year = card_attr.attr("data-year");
            if (CARS_LOG) System.out.println(year);
            try {
                car.setYear(Integer.parseInt(year));
            } catch (Exception e) {
                car.setYear(0);
            }

            cars.add(car);
            if (CARS_LOG) System.out.println("---------------------------------------------");
        }

        return cars;
    }

    /**
     * Cut part of String from the DOM div according to regex
     *
     * @param regex regex for cutting
     * @param div   block from what we cut
     * @return cut string
     */
    private String cutInfoFromDiv(String regex, String div) {
        Matcher m = Pattern.compile(regex).matcher(div);
        if (!m.find()) return "";
        return m.group(1);
    }

    /* GETTERS & SETTERS */

    public ArrayList<CarCard> getCars() {
        return cars;
    }
}
