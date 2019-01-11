package toki.flights.exception;

import java.io.IOException;

public class FlightsJsonResponseProcessingException extends IOException {

    public FlightsJsonResponseProcessingException(String message) {
        super(message);
    }
}
