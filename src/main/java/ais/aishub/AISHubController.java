package ais.aishub;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ais.model.AisTarget;
import ais.model.AisTargetTrack;
import ais.model.Destination;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.annotations.NonNull;

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
	
	@Get("track/{id}")
	HttpResponse<AisTargetTrack> track(@NonNull Integer id) {
		log.info("listing target track: " + id);
		AisTargetTrack track = findTrack(id);
		return HttpResponse.ok(track);
	}
	
	@Get("destinations")
	HttpResponse<Collection<Destination>> destinations() {
		log.info("listing destinations");
		return HttpResponse.ok(service.getDestinations());
	}
	
	@Get("destination/{id}")
	HttpResponse<Destination> destination(@NonNull Integer id) {
		log.info("listing destination: " + id);
		Destination dest = findDestination(id);
		return HttpResponse.ok(dest);
	}
	
	@Get("targets")
	HttpResponse<Collection<AisTarget>> targets() {
		log.info("listing targets (Vessels)");
		return HttpResponse.ok(findTargets());
	}
	
	@Get("target/{id}")
	HttpResponse<AisTarget> target(@NonNull Integer id) {
		log.info("listing target: " + id);
		AisTarget target = findTarget(id);
		
		return HttpResponse.ok(target);
	}

	Destination findDestination(Integer id) {
		Collection<Destination> destinations = service.getDestinations();
		for(Destination d:destinations) {
			if( d.getId() == id.longValue() ) {
				return d;
			}
		}
		return null;
	}

	
	AisTargetTrack findTrack(Integer id) {
		Collection<AisTargetTrack> tracks = service.tracks();
		for(AisTargetTrack t:tracks) {
			if( t.getTarget().getMmsi() == id ) {
				return t;
			}
		}
		return null;
	}
	
	AisTarget findTarget(Integer id) {
		Collection<AisTarget> targets = findTargets();
		for(AisTarget t:targets) {
			if( t.getMmsi() == id )
				return t;
		}
		return null;
	}

	Collection<AisTarget> findTargets(){
		Collection<AisTargetTrack> tracks = service.tracks();
		return tracks.stream().map(t->t.getTarget()).collect(Collectors.toList());
	}
}
