package toki.flights.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import toki.flights.dto.CheapFlightsDTO;
import toki.flights.service.GetFlightsData;

import java.util.ArrayList;
import java.util.List;

public class GetFlightsDataImpl implements GetFlightsData {

    @Override
    public List<CheapFlightsDTO> getCheapFlights() {

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("https://obscure-caverns-79008.herokuapp.com/cheap", String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        List<CheapFlightsDTO> cheapFlightsDTOS = new ArrayList<>();

        try {
            cheapFlightsDTOS = objectMapper.readValue(response, new TypeReference<List<CheapFlightsDTO>>() {
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return cheapFlightsDTOS;
    }
}
