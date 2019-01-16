package toki.flights.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import toki.flights.dto.FlightsDTO;
import toki.flights.util.BusinessFlightsDeserializer;
import toki.flights.util.CheapFlightsDeserializer;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        for (FlightsDTO flight : flights)
            if (StringUtils.equals(flight.getSource(), "Esteban Echeverria")) {
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

        for (FlightsDTO flight : flights)
            if (StringUtils.equals(flight.getSource(), "Victoria")) {
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

    @Test
    public void testFiltersBySource() {

        List<FlightsDTO> result1 = getFlightsData.getFlightsFiltered(createSampleDataForTestingFiltering(), "src:delhi");
        Assert.assertTrue(result1.size() == 2);
        Assert.assertTrue(result1.get(0).getSource().equals("delhi"));
        Assert.assertTrue(result1.get(1).getSource().equals("delhi"));

        List<FlightsDTO> result2 = getFlightsData.getFlightsFiltered(createSampleDataForTestingFiltering(), "src:kuala");
        Assert.assertTrue(result2.size() == 1);
        Assert.assertTrue(result2.get(0).getSource().equals("kuala lumpur"));
    }

    @Test
    public void testFiltersByDestination() {

        List<FlightsDTO> result = getFlightsData.getFlightsFiltered(createSampleDataForTestingFiltering(), "dest:singapore");
        Assert.assertTrue(result.size() == 2);
        Assert.assertTrue(result.get(0).getDestination().equals("singapore"));
        Assert.assertTrue(result.get(1).getDestination().equals("singapore"));
    }

    @Test
    public void testFiltersBySourceAndDestination() {

        List<FlightsDTO> result = getFlightsData.getFlightsFiltered(createSampleDataForTestingFiltering(), "src:delhi;dest:singapore");
        Assert.assertTrue(result.size() == 1);
        Assert.assertTrue(result.get(0).getSource().equals("delhi"));
        Assert.assertTrue(result.get(0).getDestination().equals("singapore"));
    }

    private List<FlightsDTO> createSampleDataForTestingFiltering() {
        List<FlightsDTO> allFlights = new ArrayList<>();
        allFlights.add(new FlightsDTO("delhi", "mumbai", new Date(), new Date()));
        allFlights.add(new FlightsDTO("abc", "pqr", new Date(), new Date()));
        allFlights.add(new FlightsDTO("delhi", "singapore", new Date(), new Date()));
        allFlights.add(new FlightsDTO("singapore", "new york", new Date(), new Date()));
        allFlights.add(new FlightsDTO("sydney", "melbourne", new Date(), new Date()));
        allFlights.add(new FlightsDTO("singapore", "kuala lumpur", new Date(), new Date()));
        allFlights.add(new FlightsDTO("mumbai", "dubai", new Date(), new Date()));
        allFlights.add(new FlightsDTO("mumbai", "singapore", new Date(), new Date()));
        allFlights.add(new FlightsDTO("melbourne", "dubai", new Date(), new Date()));
        allFlights.add(new FlightsDTO("kuala lumpur", "delhi", new Date(), new Date()));
        return allFlights;
    }

    @Test
    public void testPaginationFirstPage() {
        List<FlightsDTO> allFlights = createSampleDataForPaginationTest();
        List<FlightsDTO> result = getFlightsData.getFlightsPaginated(allFlights, 1, 10);
        Assert.assertTrue(result.size() == 10);
        Assert.assertTrue(result.containsAll(allFlights.subList(0, 10)));
    }

    @Test
    public void testPaginationSecondPage() {
        List<FlightsDTO> allFlights = createSampleDataForPaginationTest();
        List<FlightsDTO> result = getFlightsData.getFlightsPaginated(allFlights, 2, 10);
        Assert.assertTrue(result.size() == 10);
        Assert.assertTrue(result.containsAll(allFlights.subList(10, 20)));
    }

    @Test
    public void testPaginationLastPage() {
        List<FlightsDTO> allFlights = createSampleDataForPaginationTest();
        List<FlightsDTO> result = getFlightsData.getFlightsPaginated(allFlights, 3, 10);
        Assert.assertTrue(result.size() == 5);
        Assert.assertTrue(result.containsAll(allFlights.subList(20, 24)));
    }

    @Test
    public void testPaginationMoreThanLastPage() {
        List<FlightsDTO> allFlights = createSampleDataForPaginationTest();
        List<FlightsDTO> result = getFlightsData.getFlightsPaginated(allFlights, 4, 10);
        Assert.assertTrue(result.size() == 0);
    }

    private List<FlightsDTO> createSampleDataForPaginationTest() {
        List<FlightsDTO> allFlights = new ArrayList<>();
        for (int i = 0; i <= 24; i++) {
            allFlights.add(new FlightsDTO("source" + i, "destination" + i, new Date(), new Date()));
        }
        return allFlights;
    }

    @Test
    public void testFlightsSortingBySourceBySource() throws MalformedURLException {
        List<FlightsDTO> allFlights = createSampleDataForSortingTest();

        List<FlightsDTO> result = getFlightsData.getFlightsSorted(allFlights, "source");

        Assert.assertTrue(result.size() == 7);
        Assert.assertTrue(result.containsAll(allFlights));
        Assert.assertTrue(result.get(0).getSource().equals("delhi"));
        Assert.assertTrue(result.get(2).getSource().equals("dubai"));
        Assert.assertTrue(result.get(3).getSource().equals("melbourne"));
        Assert.assertTrue(result.get(6).getSource().equals("sydney"));
    }

    @Test
    public void testFlightsSortingBySourceByDestination() throws MalformedURLException {
        List<FlightsDTO> allFlights = createSampleDataForSortingTest();

        List<FlightsDTO> result = getFlightsData.getFlightsSorted(allFlights, "destination");

        Assert.assertTrue(result.size() == 7);
        Assert.assertTrue(result.containsAll(allFlights));
        Assert.assertTrue(result.get(0).getDestination().equals("cairns"));
        Assert.assertTrue(result.get(2).getDestination().equals("melbourne"));
        Assert.assertTrue(result.get(3).getDestination().equals("mumbai"));
        Assert.assertTrue(result.get(6).getDestination().equals("singapore"));
    }

    @Test
    public void testFlightsSortingBySourceByDepartureTime() throws MalformedURLException {
        List<FlightsDTO> allFlights = createSampleDataForSortingTest();

        List<FlightsDTO> result = getFlightsData.getFlightsSorted(allFlights, "departureTime");

        Assert.assertTrue(result.size() == 7);
        Assert.assertTrue(result.containsAll(allFlights));
        Assert.assertTrue(result.get(0).getSource().equals("singapore"));
        Assert.assertTrue(result.get(2).getSource().equals("delhi"));
        Assert.assertTrue(result.get(3).getSource().equals("mumbai"));
        Assert.assertTrue(result.get(6).getSource().equals("delhi"));
    }

    @Test
    public void testFlightsSortingBySourceByArrivalTime() throws MalformedURLException {
        List<FlightsDTO> allFlights = createSampleDataForSortingTest();

        List<FlightsDTO> result = getFlightsData.getFlightsSorted(allFlights, "arrivalTime");

        Assert.assertTrue(result.size() == 7);
        Assert.assertTrue(result.containsAll(allFlights));
        Assert.assertTrue(result.get(0).getSource().equals("delhi"));
        Assert.assertTrue(result.get(2).getSource().equals("melbourne"));
        Assert.assertTrue(result.get(3).getSource().equals("mumbai"));
        Assert.assertTrue(result.get(6).getSource().equals("dubai"));
    }

    private List<FlightsDTO> createSampleDataForSortingTest() {
        List<FlightsDTO> allFlights = new ArrayList<>();
        /*
        Sorted by ASC Source        : F0,F3,F4,F6,F1,F2,F5
        Sorted by ASC Departure     : F5,F2,F0,F4,F3,F6,F1
        Sorted by ASC Departure Time: F2,F4,F0,F1,F5,F6,F3
        Sorted by ASC Arrival Time  : F3,F2,F6,F1,F5,F0,F4
         */
        allFlights.add(new FlightsDTO("delhi", "mumbai", new Date(1547453693000L), new Date(1547455806000L)));   // F0
        allFlights.add(new FlightsDTO("mumbai", "singapore", new Date(1547453694000L), new Date(1547455804000L)));   // F1
        allFlights.add(new FlightsDTO("singapore", "jaipur", new Date(1547453691000L), new Date(1547455802000L)));   // F2
        allFlights.add(new FlightsDTO("delhi", "melbourne", new Date(1547453697000L), new Date(1547455801000L)));   // F3
        allFlights.add(new FlightsDTO("dubai", "mumbai", new Date(1547453692000L), new Date(1547455807000L)));   // F4
        allFlights.add(new FlightsDTO("sydney", "cairns", new Date(1547453695000L), new Date(1547455805000L)));   // F5
        allFlights.add(new FlightsDTO("melbourne", "perth", new Date(1547453696000L), new Date(1547455803000L)));   // F6
        return allFlights;
    }
}