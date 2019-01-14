package toki.flights.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import toki.flights.constants.FlightsConstants;
import toki.flights.dto.FlightsDTO;
import toki.flights.exception.FlightsJsonResponseProcessingException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BusinessFlightsDeserializer extends StdDeserializer<FlightsDTO> {

    public BusinessFlightsDeserializer() {
        this(null);
    }

    public BusinessFlightsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public FlightsDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        JsonNode sourceDestinationNode = node.get(FlightsConstants.BusinessFlightsApiAttributes.SOURCE_DESTINATION);
        JsonNode departureTimeNode = node.get(FlightsConstants.BusinessFlightsApiAttributes.DEPARTURE_TIME);
        JsonNode arrivalTimeNode = node.get(FlightsConstants.BusinessFlightsApiAttributes.ARRIVAL_TIME);

        if(null == sourceDestinationNode || null == departureTimeNode || null == arrivalTimeNode){
            throw new FlightsJsonResponseProcessingException("JSON Node content mismatch");
        }

        String[] flight = sourceDestinationNode.asText().split("->");

        String source = "";
        String destination = "";
        Date departureTime = new Date();
        Date arrivalTime = new Date();

        if(flight.length > 0) {
            source = flight[0].trim();
            destination = flight[1].trim();
        } else {
            throw new FlightsJsonResponseProcessingException("ERROR occurred while deserializing flights' Source and Destination");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        try {
            departureTime = dateFormat.parse(departureTimeNode.asText());
            arrivalTime = dateFormat.parse(arrivalTimeNode.asText());
        } catch (ParseException pe) {
            //TODO throw new FlightsJsonResponseProcessingException("");
        }

        return new FlightsDTO(source, destination, departureTime, arrivalTime);
    }
}
