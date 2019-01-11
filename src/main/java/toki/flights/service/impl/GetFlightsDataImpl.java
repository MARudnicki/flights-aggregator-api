package toki.flights.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import toki.flights.dto.FlightsDTO;
import toki.flights.service.GetFlightsData;
import toki.flights.util.BusinessFlightsDeserializer;
import toki.flights.util.CheapFlightsDeserializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class GetFlightsDataImpl implements GetFlightsData {

    private static final Logger LOG = Logger.getLogger(GetFlightsDataImpl.class.getName());

    @Override
    public List<FlightsDTO> getCheapFlights() {

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("https://obscure-caverns-79008.herokuapp.com/cheap", String.class);

        if(StringUtils.isEmpty(response)) {
            LOG.warning("NO Response received for Cheap flights");
            return Collections.emptyList();
        }

        LOG.info("Cheap Flights Response String: " + response);
        return getFlightsDTOsFromJSONResponse(response, new CheapFlightsDeserializer());
    }

    @Override
    public List<FlightsDTO> getBusinessFlights() {

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("https://obscure-caverns-79008.herokuapp.com/business", String.class);

        if(StringUtils.isEmpty(response)) {
            LOG.warning("NO Response received for Business flights");
            return Collections.emptyList();
        }

        LOG.info("Business Flights Response String: " + response);
        return getFlightsDTOsFromJSONResponse(response, new BusinessFlightsDeserializer());
    }

    private List<FlightsDTO> getFlightsDTOsFromJSONResponse(String JSONResponse, StdDeserializer<FlightsDTO> deserializer) {
        List<FlightsDTO> flightsDTOS = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(FlightsDTO.class, deserializer);
        mapper.registerModule(module);

        try {
            flightsDTOS = mapper.readValue(JSONResponse, new TypeReference<List<FlightsDTO>>() {
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return flightsDTOS;
    }
}
