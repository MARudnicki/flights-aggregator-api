package toki.flights.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import toki.flights.dto.FlightsDTO;

import java.io.IOException;
import java.util.Date;

public class CheapFlightsDeserializer extends StdDeserializer<FlightsDTO> {

    public CheapFlightsDeserializer() {
        this(null);
    }

    public CheapFlightsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public FlightsDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String source = node.get("departure").asText();
        String destination = node.get("arrival").asText();
        long departureTime = node.get("departureTime").asLong();
        long arrivalTime = node.get("arrivalTime").asLong();

        return new FlightsDTO(source, destination, new Date(departureTime), new Date(arrivalTime));
    }
}
