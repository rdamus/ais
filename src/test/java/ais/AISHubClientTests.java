package ais;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import ais.aishub.AISHubClient;
import ais.aishub.AISHubFactory;
import ais.aishub.model.AISHubRecord;
import io.micronaut.test.annotation.MicronautTest;

@MicronautTest
public class AISHubClientTests {

	@Inject
	AISHubClient client;
	@Inject 
	AISHubFactory factory;
	
	@Test
	void testGet() throws JsonParseException, JsonMappingException, IOException {
		JsonNode records = client.request();
		System.out.println("retrieved: " + factory.create(records));
	}
}
