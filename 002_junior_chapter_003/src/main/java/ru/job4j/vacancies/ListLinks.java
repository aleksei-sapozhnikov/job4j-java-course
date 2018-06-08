package ru.job4j.vacancies;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Example program to list links from a URL.
 */
public class ListLinks {
    public static void main(String[] args) throws IOException, ParseException {

        /*

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


        */

        String s1 = "6 июн 18, 14:21";
        String s2 = "5 июн 18, 23:51";
        String s3 = "вчера, 12:43";
        String s4 = "сегодня, 13:23";

        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher m1 = pattern.matcher(s1);
        Matcher m2 = pattern.matcher(s2);
        Matcher m3 = pattern.matcher(s3);
        Matcher m4 = pattern.matcher(s4);

        /*
        System.out.println(m1.lookingAt());
        System.out.println(m2.lookingAt());
        System.out.println(m3.lookingAt());
        System.out.println(m4.lookingAt());
*/

        DateFormat format = new SimpleDateFormat("d MMM yy, hh:mm");

        System.out.println(format.parse(s1));
        System.out.println(format.parse(s2));


    }

}
