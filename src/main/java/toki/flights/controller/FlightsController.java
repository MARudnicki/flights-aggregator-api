package toki.flights.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toki.flights.dto.FlightsDTO;
import toki.flights.service.GetFlightsData;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/flights-api")
public class FlightsController {

    private ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    private GetFlightsData getFlightsData = (GetFlightsData) context.getBean("getFlightsData");

    @RequestMapping("/all")
    public List<FlightsDTO> getAllFlights() {

        List<FlightsDTO> flights = new ArrayList<>();
        flights.addAll(getFlightsData.getCheapFlights());
        flights.addAll(getFlightsData.getBusinessFlights());
        return flights;
    }
}
