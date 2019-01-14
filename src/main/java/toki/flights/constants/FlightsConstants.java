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
}
