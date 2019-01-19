package toki.flights.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import toki.flights.constants.FlightsConstants;
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

        final JsonNode sourceNode = node.get(FlightsConstants.CheapFlightsApiAttributes.SOURCE);
        final JsonNode destinationNode = node.get(FlightsConstants.CheapFlightsApiAttributes.DESTINATION);
        final JsonNode departureTimeNode = node.get(FlightsConstants.CheapFlightsApiAttributes.DEPARTURE_TIME);
        final JsonNode arrivalTimeNode = node.get(FlightsConstants.CheapFlightsApiAttributes.ARRIVAL_TIME);

        if(null == sourceNode || null == destinationNode || null == departureTimeNode || null == arrivalTimeNode) {
            throw new FlightsJsonResponseProcessingException("JSON Node content mismatch");
        }

        String source = sourceNode.asText();
        String destination = destinationNode.asText();
        long departureTime = departureTimeNode.asLong();
        long arrivalTime = arrivalTimeNode.asLong();

        return new FlightsDTO(source, destination, new Date(departureTime), new Date(arrivalTime));
    }
}
