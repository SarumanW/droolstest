package lavka.drools.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class Translator {
    private static final String CLIENT_ID = "olgasergeevna0803@gmail.com";
    private static final String CLIENT_SECRET = "b91dc1fbfea94804bb7398c0f8a5049f";
    private static final String ENDPOINT = "http://api.whatsmate.net/v1/translation/translate";

    public static void main(String[] args) {
        String fromLang = "en";
        String toLang = "ru";
        String text = "Bananas, overripe, raw";

        String translate = Translator.translate(fromLang, toLang, text);
        System.out.println(translate);
    }

    public static String translate(String fromLang, String toLang, String text) {
        String output = "";

        // TODO: Should have used a 3rd party library to make a JSON string from an object
        String jsonPayload = new StringBuilder()
                .append("{")
                .append("\"fromLang\":\"")
                .append(fromLang)
                .append("\",")
                .append("\"toLang\":\"")
                .append(toLang)
                .append("\",")
                .append("\"text\":\"")
                .append(text)
                .append("\"")
                .append("}")
                .toString();

        try {
            URL url = new URL(ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-WM-CLIENT-ID", CLIENT_ID);
            conn.setRequestProperty("X-WM-CLIENT-SECRET", CLIENT_SECRET);
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(jsonPayload.getBytes());
            os.flush();
            os.close();

            int statusCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (statusCode == 200) ? conn.getInputStream() : conn.getErrorStream()
            ));

            output = br.lines().collect(Collectors.joining());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

}
