package ais.aishub;

import com.fasterxml.jackson.databind.JsonNode;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("${aishub.url}")
public interface AISHubClient {
	@Get("/")
	JsonNode request();
}
