package ais;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ais.model.AisTargetTrack;
import ais.model.Destination;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.micronaut.scheduling.annotation.Async;
import io.micronaut.scheduling.annotation.Scheduled;

public abstract class AbstractAisTrackingService implements AisTrackingService {
	private static final Logger log = LoggerFactory.getLogger(AbstractAisTrackingService.class);
	protected boolean initialized;
	protected AisTargetTracker tracker;
	@Inject
	protected DestinationService destinationService;
	
	@Override
	public Collection<AisTargetTrack> tracks() {
		return tracker.tracks();
	}

	@Override
	public int numberOfTargets() {
		return tracker.numberOfTargets();
	}
	
	@Override
	public List<Destination> getDestinations(){
		return destinationService.getDestinations();
	}
	
	@Async
	@EventListener
	public void onApplicationEvent(ServerStartupEvent event) {
		log.info("starting AbstractAisTrackingService");
		initialized = init();
	}
	
	@Scheduled(fixedRate="10s")
	public void process() {
		if( !initialized  ) {
			log.info("not initialized");
			return;
		}
		log.info("processing tracks");

		processTracks();
		report();
	}

	abstract protected boolean init();
	abstract protected void processTracks();
	abstract protected void report();
}
