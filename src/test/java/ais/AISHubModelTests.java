package ais;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ais.aishub.AISHubClient;
import ais.aishub.AISHubFactory;
import ais.aishub.model.AISHubHeader;
import ais.aishub.model.AISHubRecord;
import ais.aishub.model.AISHubResponse;
import io.micronaut.test.annotation.MicronautTest;

@MicronautTest
public class AISHubModelTests {

	@Inject
	AISHubClient client;
	@Inject
	AISHubFactory factory;
	ObjectMapper mapper = new ObjectMapper();
	String record;
	String response;
	
	@BeforeEach
	void setup() {
		record = "{\r\n" + 
				"            \"MMSI\": 367353080,\r\n" + 
				"            \"TIME\": \"2019-12-25 22:09:06 GMT\",\r\n" + 
				"            \"LONGITUDE\": -122.91992,\r\n" + 
				"            \"LATITUDE\": 37.64512,\r\n" + 
				"            \"COG\": 237,\r\n" + 
				"            \"SOG\": 10.5,\r\n" + 
				"            \"HEADING\": 238,\r\n" + 
				"            \"ROT\": 0,\r\n" + 
				"            \"NAVSTAT\": 0,\r\n" + 
				"            \"IMO\": 9408126,\r\n" + 
				"            \"NAME\": \"EMPIRE STATE\",\r\n" + 
				"            \"CALLSIGN\": \"WDE4431\",\r\n" + 
				"            \"TYPE\": 35,\r\n" + 
				"            \"A\": 147,\r\n" + 
				"            \"B\": 36,\r\n" + 
				"            \"C\": 11,\r\n" + 
				"            \"D\": 21,\r\n" + 
				"            \"DRAUGHT\": 10.8,\r\n" + 
				"            \"DEST\": \"N/A\",\r\n" + 
				"            \"ETA\": \"08-01 19:00\"\r\n" + 
				"        }";
		
		response = "[\r\n" + 
				"    {\r\n" + 
				"        \"ERROR\": false,\r\n" + 
				"        \"USERNAME\": \"AH_2986_C2C9D560\",\r\n" + 
				"        \"FORMAT\": \"HUMAN\",\r\n" + 
				"        \"LATITUDE_MIN\": 37.5,\r\n" + 
				"        \"LATITUDE_MAX\": 38.5,\r\n" + 
				"        \"LONGITUDE_MIN\": -123,\r\n" + 
				"        \"LONGITUDE_MAX\": -122,\r\n" + 
				"        \"RECORDS\": 133\r\n" + 
				"    },\r\n" + 
				"    [\r\n" + 
				"        {\r\n" + 
				"            \"MMSI\": 367353080,\r\n" + 
				"            \"TIME\": \"2019-12-25 22:09:06 GMT\",\r\n" + 
				"            \"LONGITUDE\": -122.91992,\r\n" + 
				"            \"LATITUDE\": 37.64512,\r\n" + 
				"            \"COG\": 237,\r\n" + 
				"            \"SOG\": 10.5,\r\n" + 
				"            \"HEADING\": 238,\r\n" + 
				"            \"ROT\": 0,\r\n" + 
				"            \"NAVSTAT\": 0,\r\n" + 
				"            \"IMO\": 9408126,\r\n" + 
				"            \"NAME\": \"EMPIRE STATE\",\r\n" + 
				"            \"CALLSIGN\": \"WDE4431\",\r\n" + 
				"            \"TYPE\": 35,\r\n" + 
				"            \"A\": 147,\r\n" + 
				"            \"B\": 36,\r\n" + 
				"            \"C\": 11,\r\n" + 
				"            \"D\": 21,\r\n" + 
				"            \"DRAUGHT\": 10.8,\r\n" + 
				"            \"DEST\": \"N/A\",\r\n" + 
				"            \"ETA\": \"08-01 19:00\"\r\n" + 
				"        }\r\n"
				+ "]\r\n"
				+ "]";
	}
	
	@Test
	void testParseAISHubRecord() throws Exception{
		AISHubRecord aishr = mapper.readValue(record, AISHubRecord.class);
		assertEquals(aishr.getType(), 35);
		assertEquals(aishr.getHeading(), 238);
		System.out.println("record: " + aishr);
	}
	
	@Test
	void testParseAISHubResponse() throws Exception{
		JsonNode aishr = mapper.readValue(response, JsonNode.class);
		AISHubResponse response = factory.create(aishr);
		System.out.println("record: " + response);
	}
	
	@Test
	void testParseAISHubResponseAsJsonNode() throws Exception{
		JsonNode aishr = mapper.readValue(response, JsonNode.class);
		System.out.println("first node: " + aishr.get(0).getNodeType());
		System.out.println("as Header: " + mapper.readValue(aishr.get(0).toString(), AISHubHeader.class));
		System.out.println("second node: " + aishr.get(1).getNodeType());
		System.out.println("as Record: " + mapper.readValue(aishr.get(1).get(0).toString(), AISHubRecord.class));
		System.out.println("record: " + aishr);
	}

}
