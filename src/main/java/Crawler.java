import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import static org.apache.commons.io.FileUtils.writeStringToFile;

public class Crawler {

    private static final HashSet<String> URLS = new HashSet<>();
    private static int i = 0;
    private static final String PAGES = "src/main/resources/pages/";
    private static final File INDEX_FILE = new File("src/main/resources/index.txt");

    public static void main(String[] args) {
        processUrl("https://www.delivery-club.ru/kzn");
    }

    public static void processUrl(String url) {
        if (!URLS.contains(url) && URLS.size() <= 100) {
            try {
                URLS.add(url);
                Document document = Jsoup.connect(url).get();
                Elements linksOnPage = document.select("a[href]");

                File fileName = new File(PAGES + i++ + ".txt");
                writeStringToFile(fileName, document.text(), true);

                String fullLine = i + " " + url + "\n";
                writeStringToFile(INDEX_FILE, fullLine, true);

                for (Element page : linksOnPage) {
                    processUrl(page.attr("abs:href"));
                }
            } catch (IOException e) {
                System.out.println(String.format("Error for url %s : %s", url, e.getMessage()));
            }
        }
    }
}