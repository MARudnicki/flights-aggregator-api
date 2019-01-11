package toki.flights.service.impl;

import org.junit.Assert;
import org.junit.Test;
import toki.flights.dto.CheapFlightsDTO;

import java.util.List;
import java.util.logging.Logger;

public class GetFlightsDataImplTest {

    private static final Logger LOG = Logger.getLogger(GetFlightsDataImplTest.class.getName());

    GetFlightsDataImpl getFlightsData = new GetFlightsDataImpl();
    @Test
    public void getCheapFlights() {

        List<CheapFlightsDTO> cheapFlightsDTOS = getFlightsData.getCheapFlights();
        Assert.assertNotNull(cheapFlightsDTOS);
        Assert.assertTrue(cheapFlightsDTOS.size() > 0);

        LOG.info("Number of Entries retrieved: " + cheapFlightsDTOS.size());
        LOG.info("Response Entries: " + cheapFlightsDTOS.toString());
    }
}