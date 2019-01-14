package toki.flights.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;
import toki.flights.dto.FlightsDTO;
import toki.flights.service.GetFlightsData;
import toki.flights.util.BusinessFlightsDeserializer;
import toki.flights.util.CheapFlightsDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetFlightsDataImpl implements GetFlightsData {

    private static final Logger LOG = Logger.getLogger(GetFlightsDataImpl.class);

    @Override
    public List<FlightsDTO> getCheapFlights() {

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("https://obscure-caverns-79008.herokuapp.com/cheap", String.class);

        if(StringUtils.isEmpty(response)) {
            LOG.error("NO Response received for Cheap flights");
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
            LOG.error("NO Response received for Business flights");
            return Collections.emptyList();
        }

        LOG.info("Business Flights Response String: " + response);
        return getFlightsDTOsFromJSONResponse(response, new BusinessFlightsDeserializer());
    }

    protected List<FlightsDTO> getFlightsDTOsFromJSONResponse(String JSONResponse, StdDeserializer<FlightsDTO> deserializer) {
        List<FlightsDTO> flightsDTOS = new ArrayList<>();

        if(StringUtils.isNotBlank(JSONResponse) && null != deserializer) {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(FlightsDTO.class, deserializer);
            mapper.registerModule(module);

            try {
                flightsDTOS = mapper.readValue(JSONResponse, new TypeReference<List<FlightsDTO>>() {
                });
            } catch (IOException e) {
                LOG.error("Could not map the Response to: " + FlightsDTO.class.getName()
                        + " using Deserializer: " + deserializer.getClass().getName()
                        + " for Response String: " + JSONResponse);
            }
        }

        return flightsDTOS;
    }
}
