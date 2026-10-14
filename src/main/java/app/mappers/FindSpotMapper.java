package app.mappers;

import app.dtos.FindSpotDTO;
import app.entities.FindSpot;

public class FindSpotMapper {

    public static FindSpot convertToFindSpot(FindSpotDTO findSpotDTO) {
        return new FindSpot(findSpotDTO.getLatitude(), findSpotDTO.getLongitude(), findSpotDTO.getName());
    }
}
