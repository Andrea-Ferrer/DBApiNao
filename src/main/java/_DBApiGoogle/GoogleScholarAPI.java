package _DBApiGoogle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleScholarAPI {
    private static final String API_KEY = "faf8ad7142ae099e73069e817e5d67f7ecd5e2da53e977625410a95c1d3f46ac";
    private static final String BASE_URL = "https://serpapi.com/search.json?engine=google_scholar&q=";

    public static String searchGoogleScholar(String query) throws IOException {
        String apiUrl = BASE_URL + query + "&api_key=" + API_KEY;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }
}


