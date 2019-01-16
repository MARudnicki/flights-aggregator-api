package toki.flights.service;

import toki.flights.dto.FlightsDTO;

import java.net.MalformedURLException;
import java.util.List;

public interface GetFlightsData {

    /**
     * Returns aggregated list of all the available flights from multiple resources
     *
     * @return List of Flights DTOs
     */
    List<FlightsDTO> getFlights();

    /**
     * Sorts the list of flights based on sortBy paramater
     *
     * @param flightsToSort
     * @param sortBy
     * @return List of Sorted Flights DTOs
     * @throws MalformedURLException
     */
    List<FlightsDTO> getFlightsSorted(List<FlightsDTO> flightsToSort, String sortBy) throws MalformedURLException;

    /**
     * Returns list of flight for given page number and size of the pages
     *
     * @param flights
     * @param page
     * @param size
     * @return List of flights for page number:page
     */
    List<FlightsDTO> getFlightsPaginated(List<FlightsDTO> flights, int page, int size);

    /**
     * Filters the flights based on the filters
     *
     * @param unfilteredFlights
     * @param filters
     * @return List of Filtered flights
     */
    List<FlightsDTO> getFlightsFiltered(List<FlightsDTO> unfilteredFlights, String filters);

}

