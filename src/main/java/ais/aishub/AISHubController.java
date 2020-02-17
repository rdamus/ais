package ais.aishub;

import java.util.Collection;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ais.model.AisTargetTrack;
import ais.model.Destination;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/aishub")
public class AISHubController {
	private static final Logger log = LoggerFactory.getLogger(AISHubController.class);

	@Inject
	AisHubTrackingService service;
	@Get("/")
	HttpStatus index() {
		return HttpStatus.OK;
	}
	
	@Get("tracks")
	HttpResponse<Collection<AisTargetTrack>> tracks() {
		log.info("listing target tracks");
		return HttpResponse.ok(service.tracks());
	}
	
	@Get("destinations")
	HttpResponse<Collection<Destination>> destinations() {
		log.info("listing destinations");
		return HttpResponse.ok(service.getDestinations());
	}

}
