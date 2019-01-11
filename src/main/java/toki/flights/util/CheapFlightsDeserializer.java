package toki.flights.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import toki.flights.dto.FlightsDTO;
import toki.flights.exception.FlightsJsonResponseProcessingException;

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
            throws IOException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final JsonNode departureNode = node.get("departure");
        final JsonNode arrivalNode = node.get("arrival");
        final JsonNode departureTimeNode = node.get("departureTime");
        final JsonNode arrivalTimeNode = node.get("arrivalTime");

        if(null == departureNode || null == arrivalNode || null == departureTimeNode || null == arrivalTimeNode) {
            throw new FlightsJsonResponseProcessingException("JSON Node content mismatch");
        }

        String source = departureNode.asText();
        String destination = arrivalNode.asText();
        long departureTime = departureTimeNode.asLong();
        long arrivalTime = arrivalTimeNode.asLong();

        return new FlightsDTO(source, destination, new Date(departureTime), new Date(arrivalTime));
    }
}
