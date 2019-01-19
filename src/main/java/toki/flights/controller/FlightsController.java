package toki.flights.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toki.flights.dto.FlightsDTO;
import toki.flights.service.GetFlightsData;

import java.net.MalformedURLException;
import java.util.*;

@RestController
public class FlightsController {

    private ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    private GetFlightsData getFlightsData = (GetFlightsData) context.getBean("getFlightsData");

    @RequestMapping(value = "/flights", method = RequestMethod.GET)
    public List<FlightsDTO> getFlights(@RequestParam(value = "sort", required = false) String sortBy,
                                       @RequestParam(value = "page", required = false) Integer page,
                                       @RequestParam(value = "size", required = false) Integer size,
                                       @RequestParam(value = "flt", required = false) String conditions) throws MalformedURLException {

        List<FlightsDTO> flights = getFlightsData.getFlights();

        // Filtering
        if (StringUtils.isNotBlank(conditions)) {
            flights = getFlightsData.getFlightsFiltered(flights, conditions);
        }

        // Sorting
        if (StringUtils.isNotBlank(sortBy)) {
            flights = getFlightsData.getFlightsSorted(flights, sortBy);
        }

        // Pagination
        if (null != page && null != size && page > 0 && size >= 0) {
            flights = getFlightsData.getFlightsPaginated(flights, page, size);
        }

        return flights;
    }
}
