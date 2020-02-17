package ais;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ais.model.Destination;
import dk.dma.enav.model.geometry.Position;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.micronaut.scheduling.annotation.Async;

@Singleton
public class DestinationService {
	private static final Logger log = LoggerFactory.getLogger(DestinationService.class);
	List<Destination> destinations;
	
	@Async
	@EventListener
	public void onApplicationEvent(ServerStartupEvent event) {
		log.info("starting DestinationService");
		initDestinations();
	}
	
	private void initDestinations() {
		destinations = new ArrayList<Destination>();
		destinations.add( new Destination( Position.create(37.791166, -122.294209), "Alameda Ferry Terminal") );
		destinations.add( new Destination( Position.create(37.794237, -122.391166), "SF Bay Ferry - Alameda") );
		destinations.add( new Destination( Position.create(37.796086, -122.392505), "SF Bay Ferry - Larkspur Ferry"));
		destinations.add( new Destination( Position.create(37.796840, -122.393223), "SF Bay Ferry - Tiburon"));
	}

	public List<Destination> getDestinations() {
		return destinations;
	}
}
