package app.services;

import app.dtos.FindSpotDTO;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FindSpotService {

    private final APIService apiService = new APIService();

    public FindSpotDTO getFindSpotDetails(String keyword) throws Exception {
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String url = "https://nominatim.openstreetmap.org/search?q=" + encodedKeyword + "&format=json";

        try {
            Thread.sleep(1000); //1 sec. delay because nominatim only allows 1 call/sec.
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw e;
        }

        FindSpotDTO[] findSpotDTOArray = apiService.fetchAndConvert(url, FindSpotDTO[].class);
        if (findSpotDTOArray.length < 1) {
            throw new Exception("Could not find a location to match the keyword");
        }

        return findSpotDTOArray[0];
    }
}
