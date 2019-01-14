package toki.flights.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toki.flights.constants.FlightsConstants;
import toki.flights.dto.FlightsDTO;
import toki.flights.service.GetFlightsData;

import java.net.MalformedURLException;
import java.util.*;

@RestController
public class FlightsController {

    private ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    private GetFlightsData getFlightsData = (GetFlightsData) context.getBean("getFlightsData");

    @RequestMapping(value = "/flights", method = RequestMethod.GET)
    public List<FlightsDTO> getAllFlights(@RequestParam(value = "sort", required = false) String sortBy) throws MalformedURLException{

        List<FlightsDTO> flights = new ArrayList<>();
        flights.addAll(getFlightsData.getCheapFlights());
        flights.addAll(getFlightsData.getBusinessFlights());

        if(StringUtils.isNotBlank(sortBy)) {

            switch (sortBy) {
                case FlightsConstants.ApiAttributes.SOURCE:
                    Collections.sort(flights, new SortBySource());
                    break;
                case FlightsConstants.ApiAttributes.DESTINATION:
                    Collections.sort(flights, new SortByDestination());
                    break;
                case FlightsConstants.ApiAttributes.DEPARTURE_TIME:
                    Collections.sort(flights, new SortByDepartureTime());
                    break;
                case FlightsConstants.ApiAttributes.ARRIVAL_TIME:
                    Collections.sort(flights, new SortByArrivalTime());
                    break;
                default:
                    throw new MalformedURLException("Can't process request because of invalid Sorting parameter. ");
            }
        }

        return flights;
    }

    public class SortBySource implements Comparator<FlightsDTO> {
        public int compare(FlightsDTO a, FlightsDTO b)
        {
            return a.getSource().compareToIgnoreCase(b.getSource());
        }
    }

    public class SortByDestination implements Comparator<FlightsDTO> {
        public int compare(FlightsDTO a, FlightsDTO b)
        {
            return a.getDestination().compareToIgnoreCase(b.getDestination());
        }
    }

    public class SortByDepartureTime implements Comparator<FlightsDTO> {
        public int compare(FlightsDTO a, FlightsDTO b)
        {
            return a.getDepartureTime().compareTo(b.getDepartureTime());
        }
    }

    public class SortByArrivalTime implements Comparator<FlightsDTO> {
        public int compare(FlightsDTO a, FlightsDTO b)
        {
            return a.getArrivalTime().compareTo(b.getArrivalTime());
        }
    }
}
