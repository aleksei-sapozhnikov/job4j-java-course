package ru.job4j.vacancies;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Example program to list links from a URL.
 */
public class ListLinks {
    public static void main(String[] args) throws IOException {

        for (int i = 1; i <= 1; i++) {
            String url = String.format("http://www.sql.ru/forum/job-offers/%s", i);
            Document doc = Jsoup.connect(url).get();
            Elements rows = doc.select("tr");
            System.out.println("Parsing url: " + url);
            System.out.println();
            for (Element row : rows) {
                String sTheme = null;
                String sUrl = null;
                String sTime = null;
                Element theme = row.selectFirst("td.postslisttopic a[href]");
                if (theme != null) {
                    String lower = theme.text().toLowerCase();
                    if (lower.contains("java") && !lower.contains("script")) {
                        sTheme = theme.text();
                        sUrl = theme.attr("abs:href");
                    }
                }
                Element date = row.selectFirst("td.altCol[style='text-align:center']");
                if (date != null) {
                    sTime = date.text();
                }
                if (sTime != null && sTheme != null) {
                    String output = String.format("%s%n%s%n%s%n", sTime, sTheme, sUrl);
                    System.out.println(output);
                }
            }
        }
    }

}
