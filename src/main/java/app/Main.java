package app;

import app.services.FindSpotService;

public class Main {
    static void main() throws Exception {
        FindSpotService findSpotService = new FindSpotService();

        System.out.println(findSpotService.getFindSpotDetails("birkerød"));
    }
}
