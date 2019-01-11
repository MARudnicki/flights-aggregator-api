package toki.flights.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import toki.flights.dto.FlightsDTO;
import toki.flights.service.GetFlightsData;

import java.util.ArrayList;
import java.util.List;

public class GetFlightsDataImpl implements GetFlightsData {

    @Override
    public List<FlightsDTO> getCheapFlights() {

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("https://obscure-caverns-79008.herokuapp.com/cheap", String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        List<FlightsDTO> flightsDTOS = new ArrayList<>();

        try {
            flightsDTOS = objectMapper.readValue(response, new TypeReference<List<FlightsDTO>>() {
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return flightsDTOS;
    }
}
