package toki.flights.service.impl;

import org.junit.Assert;
import org.junit.Test;
import toki.flights.dto.FlightsDTO;

import java.util.List;
import java.util.logging.Logger;

public class GetFlightsDataImplTest {

    private static final Logger LOG = Logger.getLogger(GetFlightsDataImplTest.class.getName());

    GetFlightsDataImpl getFlightsData = new GetFlightsDataImpl();

    @Test
    public void getCheapFlights() {

        List<FlightsDTO> flights = getFlightsData.getCheapFlights();
        Assert.assertNotNull(flights);

        LOG.info("Number of Cheap Flights retrieved: " + flights.size());
        LOG.info("Cheap Flights Entries: " + flights.toString());
    }

    @Test
    public void getBusinessFlights() {

        List<FlightsDTO> flights = getFlightsData.getBusinessFlights();
        Assert.assertNotNull(flights);

        LOG.info("Number of Business Flights retrieved: " + flights.size());
        LOG.info("Business Flights Entries: " + flights.toString());
    }
}