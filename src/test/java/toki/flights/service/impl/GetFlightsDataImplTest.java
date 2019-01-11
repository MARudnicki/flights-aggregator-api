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
        Assert.assertTrue(flights.size() > 0);

        LOG.info("Number of Entries retrieved: " + flights.size());
        LOG.info("Response Entries: " + flights.toString());
    }
}