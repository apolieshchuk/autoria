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

    private URL url;
    private Document document;
    private final boolean CARS_LOG = false;

    public CarsUrlReader(String urlString) throws IOException {
        url = new URL(urlString);
        document = getUrlDom(url);
    }

    /**
     * Get total number of pages
     *
     * @return max number of pages
     */
    public int getTotalPages(){
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
     * @param url url -address
     * @return Document DOM
     * @throws IOException error
     */
    public Document getUrlDom(URL url) throws IOException {

        /* Read html form url stream (commons-io-1.3.2.jar library) */
        String htmlString = IOUtils.toString(url.openStream());

        /* Get DOM Html (jsoup-1.12.1.jar library)*/
        return Jsoup.parse(htmlString);
    }

    /**
     * Get array list with cars class in url
     */
    public ArrayList<CarCard> getCarsInUrl() {
        /* Create arraylist of results */
        ArrayList<CarCard> cars = new ArrayList<>();

        /* Get all cars-cards in html */
        Elements tickets = document.body().getElementsByClass("ticket-item");

        /* Scan all cars and get info */
        for (Element ticket : tickets) {

            /* Create CarCard class */
            CarCard car = new CarCard();

            /* Title */
            Elements card_title = ticket.select(".ticket-title a");
            String title = card_title.attr("title");
//            String title = cutInfoFromDiv("title=\"([^\"]+)", card_title);
            if (CARS_LOG) System.out.println(title);
            car.setTitle(title);

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

            /* Year */
            Elements card_year = ticket.select("div:first-child");
            String year = card_year.attr("data-year");
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

    public void setUrl(String urlString) throws IOException {
        this.url = new URL(urlString);
        this.document = getUrlDom(url);
    }

    public URL getUrl() {
        return url;
    }

}
