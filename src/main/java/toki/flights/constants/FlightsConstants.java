package toki.flights.constants;

public class FlightsConstants {

    public class ApiAttributes {
        public static final String SOURCE = "source";
        public static final String DESTINATION = "destination";
        public static final String DEPARTURE_TIME = "departureTime";
        public static final String ARRIVAL_TIME = "arrivalTime";
    }

    public class CheapFlightsApiAttributes {
        public static final String SOURCE = "departure";
        public static final String DESTINATION = "arrival";
        public static final String DEPARTURE_TIME = "departureTime";
        public static final String ARRIVAL_TIME = "arrivalTime";
    }

    public class BusinessFlightsApiAttributes {
        public static final String SOURCE_DESTINATION = "flight";
        public static final String DEPARTURE_TIME = "departure";
        public static final String ARRIVAL_TIME = "arrival";
    }

    public class Resources {
        public static final String CHEAP_FLIGHT_URL = "https://obscure-caverns-79008.herokuapp.com/cheap";
        public static final String BUSINESS_FLIGHT_URL = "https://obscure-caverns-79008.herokuapp.com/business";
    }

    public class Filters {
        public static final String FILTERS_DELIMITER = ";";
        public static final String KEY_VALUE_DELIMITER = ":";
        public static final String BY_SOURCE = "src";
        public static final String BY_DESTINATION = "dest";
    }
}
