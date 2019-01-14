package toki.flights.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import toki.flights.dto.FlightsDTO;
import toki.flights.util.BusinessFlightsDeserializer;
import toki.flights.util.CheapFlightsDeserializer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;

public class GetFlightsDataImplTest {

    private static final Logger LOG = Logger.getLogger(GetFlightsDataImplTest.class.getName());

    GetFlightsDataImpl getFlightsData = new GetFlightsDataImpl();

    @Before
    public void setLog() {
        BasicConfigurator.configure();
    }

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

    @Test
    public void testGetFlightsDTOsFromJSONResponseEmptyString() {

        List<FlightsDTO> flights = getFlightsData.getFlightsDTOsFromJSONResponse("", new CheapFlightsDeserializer());
        Assert.assertTrue(flights.size() == 0);
    }

    @Test
    public void testGetFlightsDTOsFromJSONResponseNullInputs() {

        List<FlightsDTO> flights = getFlightsData.getFlightsDTOsFromJSONResponse(null, null);
        Assert.assertTrue(flights.size() == 0);
    }

    @Test
    public void testGetFlightsDTOsFromJSONResponseTestCheapDeserializerSuccess() {

        String testReponse = "[{\"id\":1856794264216194048,\"departure\":\"Esteban Echeverria\",\"arrival\":\"Villa Amelia\",\"departureTime\":1547454088544,\"arrivalTime\":1547462346538}," +
                "{\"id\":758835356858114048,\"departure\":\"Constituyentes\",\"arrival\":\"Balnearia\",\"departureTime\":1547453691725,\"arrivalTime\":1547462326226}]";
        List<FlightsDTO> flights = getFlightsData.getFlightsDTOsFromJSONResponse(testReponse, new CheapFlightsDeserializer());

        Assert.assertTrue(flights.size() == 2);

        for(FlightsDTO flight : flights)
            if(StringUtils.equals(flight.getSource(), "Esteban Echeverria")) {
                Assert.assertTrue(StringUtils.endsWithIgnoreCase(flight.getDestination(), "Villa Amelia"));
                Assert.assertTrue(flight.getDepartureTime().getTime() - 1547454088544L == 0);
                Assert.assertTrue(flight.getArrivalTime().getTime() - 1547462346538L == 0);
            } else {
                Assert.assertTrue(StringUtils.endsWithIgnoreCase(flight.getSource(), "Constituyentes"));
                Assert.assertTrue(StringUtils.endsWithIgnoreCase(flight.getDestination(), "Balnearia"));
                Assert.assertTrue(flight.getDepartureTime().getTime() - 1547453691725L == 0);
                Assert.assertTrue(flight.getArrivalTime().getTime() - 1547462326226L == 0);
        }
    }

    @Test
    public void testGetFlightsDTOsFromJSONResponseTestBusinessDeserializerSuccess() throws ParseException {

        String testReponse = "[{\"uuid\":\"dc710c58-a70e-42be-8fd1-0f7d51b5aa62\",\"flight\":\"Victoria -> Villa Paranacito\",\"departure\":\"2019-01-14T00:00:00.000Z\",\"arrival\":\"2019-01-14T11:32:31.805Z\"}," +
                "{\"uuid\":\"bb302a3b-e651-4b18-a8d7-a37faed52f14\",\"flight\":\"Bernasconi -> Rio Segundo\",\"departure\":\"2019-01-14T08:45:54.967Z\",\"arrival\":\"2019-01-14T10:41:51.189Z\"}]";
        List<FlightsDTO> flights = getFlightsData.getFlightsDTOsFromJSONResponse(testReponse, new BusinessFlightsDeserializer());

        Assert.assertTrue(flights.size() == 2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        for(FlightsDTO flight : flights)
            if(StringUtils.equals(flight.getSource(), "Victoria")) {
                Assert.assertTrue(StringUtils.endsWithIgnoreCase(flight.getDestination(), "Villa Paranacito"));
                Assert.assertTrue(flight.getDepartureTime().getTime() - dateFormat.parse("2019-01-14T00:00:00.000Z").getTime() == 0);
                Assert.assertTrue(flight.getArrivalTime().getTime() - dateFormat.parse("2019-01-14T11:32:31.805Z").getTime() == 0);
            } else {
                Assert.assertTrue(StringUtils.endsWithIgnoreCase(flight.getSource(), "Bernasconi"));
                Assert.assertTrue(StringUtils.endsWithIgnoreCase(flight.getDestination(), "Rio Segundo"));
                Assert.assertTrue(flight.getDepartureTime().getTime() - dateFormat.parse("2019-01-14T08:45:54.967Z").getTime() == 0);
                Assert.assertTrue(flight.getArrivalTime().getTime() - dateFormat.parse("2019-01-14T10:41:51.189Z").getTime() == 0);
            }
    }

    @Test
    public void testGetFlightsDTOsFromJSONResponseTestCheapDeserializerException() {

        String testReponse = "[{\"id\":1856794264216194048,\"departureSource\":\"Esteban Echeverria\",\"arrival\":\"Villa Amelia\",\"departureTime\":1547454088544,\"arrivalTime\":1547462346538}]";

        List<FlightsDTO> flights = getFlightsData.getFlightsDTOsFromJSONResponse(testReponse, new CheapFlightsDeserializer());

        Assert.assertTrue(flights.size() == 0);
    }
}