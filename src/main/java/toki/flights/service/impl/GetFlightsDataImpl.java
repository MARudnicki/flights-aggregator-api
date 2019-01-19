package toki.flights.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import toki.flights.constants.FlightsConstants;
import toki.flights.dto.FlightsDTO;
import toki.flights.service.GetFlightsData;
import toki.flights.util.BusinessFlightsDeserializer;
import toki.flights.util.CheapFlightsDeserializer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GetFlightsDataImpl implements GetFlightsData {

    private static final Logger LOG = Logger.getLogger(GetFlightsDataImpl.class);
    public static final int CONNECTION_TIMEOUT_SECONDS = 10;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<FlightsDTO> getFlights() {

        List<FlightsDTO> flights = new ArrayList<>();

        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(new Thread (() -> flights.addAll(getCheapFlights())));
        es.execute(new Thread (() -> flights.addAll(getBusinessFlights())));
        es.shutdown();

        try {
            es.awaitTermination(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error("TIMEOUT ERROR: Could not retrieve flights details");
        }

        return flights;
    }

    @Override
    public List<FlightsDTO> getFlightsSorted(List<FlightsDTO> flightsToSort, String sortBy) throws MalformedURLException {

        if (StringUtils.isNotBlank(sortBy)) {

            switch (sortBy) {
                case FlightsConstants.ApiAttributes.SOURCE:
                    Collections.sort(flightsToSort, new SortBySource());
                    break;
                case FlightsConstants.ApiAttributes.DESTINATION:
                    Collections.sort(flightsToSort, new SortByDestination());
                    break;
                case FlightsConstants.ApiAttributes.DEPARTURE_TIME:
                    Collections.sort(flightsToSort, new SortByDepartureTime());
                    break;
                case FlightsConstants.ApiAttributes.ARRIVAL_TIME:
                    Collections.sort(flightsToSort, new SortByArrivalTime());
                    break;
                default:
                    throw new MalformedURLException("Can't process request because of invalid Sorting parameter. ");
            }
        }

        return flightsToSort;
    }

    @Override
    public List<FlightsDTO> getFlightsPaginated(List<FlightsDTO> flights, int page, int size) {

        // If page number exceeds MAX number of pages
        if ((page - 1) * size > flights.size()) {
            return Collections.EMPTY_LIST;
        }

        // To cater last page
        if (page * size > flights.size()) {
            return flights.subList((page - 1) * size, flights.size());
        }

        return flights.subList((page - 1) * size, page * size);
    }

    @Override
    public List<FlightsDTO> getFlightsFiltered(List<FlightsDTO> unfilteredFlights, String filters) {

        List<FlightsDTO> result = new ArrayList<>();
        String[] filterArray = filters.split(FlightsConstants.Filters.FILTERS_DELIMITER);

        for (String filter : filterArray) {
            result = new ArrayList<>();
            String key = filter.substring(0, filter.indexOf(FlightsConstants.Filters.KEY_VALUE_DELIMITER));
            String value = filter.substring(filter.indexOf(FlightsConstants.Filters.KEY_VALUE_DELIMITER) + 1, filter.length());
            switch (key) {
                case FlightsConstants.Filters.BY_SOURCE:
                    result.addAll(unfilteredFlights.stream().filter(flight ->
                            flight.getSource().toLowerCase().contains(value.toLowerCase())).collect(Collectors.toList()));
                    break;
                case FlightsConstants.Filters.BY_DESTINATION:
                    result.addAll(unfilteredFlights.stream().filter(flight ->
                            flight.getDestination().toLowerCase().contains(value.toLowerCase())).collect(Collectors.toList()));
                    break;
            }
            unfilteredFlights = result;
        }
        return result;
    }

    private class SortBySource implements Comparator<FlightsDTO> {
        public int compare(FlightsDTO a, FlightsDTO b) {
            return a.getSource().compareToIgnoreCase(b.getSource());
        }
    }

    private class SortByDestination implements Comparator<FlightsDTO> {
        public int compare(FlightsDTO a, FlightsDTO b) {
            return a.getDestination().compareToIgnoreCase(b.getDestination());
        }
    }

    private class SortByDepartureTime implements Comparator<FlightsDTO> {
        public int compare(FlightsDTO a, FlightsDTO b) {
            return a.getDepartureTime().compareTo(b.getDepartureTime());
        }
    }

    private class SortByArrivalTime implements Comparator<FlightsDTO> {
        public int compare(FlightsDTO a, FlightsDTO b) {
            return a.getArrivalTime().compareTo(b.getArrivalTime());
        }
    }

    protected List<FlightsDTO> getCheapFlights() {

        String response = restTemplate.getForObject(FlightsConstants.Resources.CHEAP_FLIGHT_URL, String.class);

        if (StringUtils.isEmpty(response)) {
            LOG.error("NO Response received for Cheap flights");
            return Collections.emptyList();
        }

        LOG.debug("Cheap Flights Response String: " + response);
        return getFlightsDTOsFromJSONResponse(response, new CheapFlightsDeserializer());
    }

    protected List<FlightsDTO> getBusinessFlights() {

        String response = StringUtils.EMPTY;
        try {
            response = restTemplate.getForObject(FlightsConstants.Resources.BUSINESS_FLIGHT_URL, String.class);
        } catch (ResourceAccessException res) {

        }

        if (StringUtils.isEmpty(response)) {
            LOG.error("NO Response received for Business flights");
            return Collections.emptyList();
        }

        LOG.debug("Business Flights Response String: " + response);
        return getFlightsDTOsFromJSONResponse(response, new BusinessFlightsDeserializer());
    }

    protected List<FlightsDTO> getFlightsDTOsFromJSONResponse(String JSONResponse, StdDeserializer<FlightsDTO> deserializer) {
        List<FlightsDTO> flightsDTOS = new ArrayList<>();

        if (StringUtils.isNotBlank(JSONResponse) && null != deserializer) {
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
