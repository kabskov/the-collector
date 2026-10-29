package app.services;

import app.dtos.FindSpotDTO;
import app.exceptions.ApiException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FindSpotService {

    private final APIService apiService = new APIService();

    public FindSpotDTO getFindSpotDetails(String keyword) {
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String url = "https://nominatim.openstreetmap.org/search?q=" + encodedKeyword + "&format=json";

        try {
            Thread.sleep(1000); //1 sec. delay because Nominatim only allows 1 call/sec.
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException(500, "Interrupted while waiting to call Nominatim", e);
        }

        FindSpotDTO[] results = apiService.fetchAndConvert(url, FindSpotDTO[].class);
        if (results.length == 0) {
            throw new ApiException(404, "No location found for keyword: " + keyword);
        }

        return results[0];
    }
}
