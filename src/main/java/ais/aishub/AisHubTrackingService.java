package ais.aishub;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import ais.AbstractAisTrackingService;
import ais.aishub.model.AISHubRecord;
import ais.aishub.model.AISHubResponse;
import ais.model.AisTarget;
import ais.model.AisTargetPacket;
import ais.model.AisTargetTrack;
import ais.model.Destination;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.geometry.grid.Grid;
import io.micronaut.scheduling.annotation.Scheduled;

@Singleton
public class AisHubTrackingService extends AbstractAisTrackingService{
	private static final Logger log = LoggerFactory.getLogger(AisHubTrackingService.class);
	private final double DISTANCE_ARRIVAL = 75;//meters
	private final double DISTANCE_APPROACH = 200;//meters
	SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssZ");
	
	@Inject
	AISHubClient client;
	@Inject
	AISHubFactory factory;
	//local cache
	List<AISHubRecord> records = Collections.synchronizedList(new ArrayList<>());
	
	@Override
	protected void processTracks() {
		synchronized (records) {
			LocalDateTime time = LocalDateTime.now();
			if( records.isEmpty() ) {
				log.info("No records to process, time: " + time);
			}else {
				log.info("about to processTracks, size: " + records.size() + " time: " + time);
				records.forEach(r->updateTracking(r));
				log.info("done processing, clearing cache");
				records.clear();
			}
		}
	}

	private void updateTracking(AISHubRecord r) {
		updateDestinations(r);
		updateTracks(r);
	}
	
	void updateDestinations(AISHubRecord record) {
		int mmsi = record.getMmsi();
		Position pos = Position.create(record.getLatitude(), record.getLongitude());
		for(Destination d:destinationService.getDestinations()) {
			d.update( mmsi, pos );
		}
	}
	
	private void updateTracks(AISHubRecord r) {
		try {
			tracker.update(new AisTargetPacket(r));
		} catch (Exception e) {
			log.error("cannot update tracks " + e);
		}
	}

	@Override
	protected boolean init() {
		tracker = new AisHubTracker(Grid.GRID_1_DEGREE);
		return tracker != null;
	}
	
	@Scheduled(fixedRate="1m")
	public void fetch() {
		if( !initialized  ) {
			log.info("not initialized");
			return;
		}
		log.info("fetching AISHub data");
		try {
			updateRecords(client.request());
		} catch (Exception e) {
			log.error("error fetching: " + e);
		}
	}

	private void updateRecords(JsonNode request) throws JsonParseException, JsonMappingException, IOException {
		log.debug("made request:" + request);
		AISHubResponse response = factory.create( request );
		log.debug("received response:" + response);
		synchronized (records) {
			if( !response.getHeader().getError() ) {
				response.getRecords().forEach(r->records.add(r));
			}else {
				log.error(response.getHeader().getErrorMessage());
			}
		}
	}

	@Override
	protected void report() {
		log.info("reporting...");
		List<Destination> destinations = destinationService.getDestinations();
		Collection<AisTargetTrack> tracks = tracker.tracks();
		//now consolidate
		for(Destination d:destinations) {
			Map<Double,Integer> m = d.distances();
			log.info("^^^^^^^^^^^^^^^^^");
			log.info("DESTINATION: " + d.getName());
			log.info("-----------------");
			for(Double distance:m.keySet()) {
				int mmsi = m.get(distance);
				
				if( mmsiExists(mmsi, tracks) ) {
					AisTargetTrack track = trackFor(mmsi, tracks);
					AisTarget target = track.getTarget();
					String name = target.getName();
					double sog = track.sog();
					double cog = track.cog();
					double hdg = track.heading();
					String time = df.format( track.time() );
					List<Double> speed = track.sog(3L);
					log.debug(String.format("Speed History: MMSI:%d,name:%s,speedvec:%s", mmsi,name,speed));
					String data = String.format("MMSI:%d,name:%s,dist:%3.2fm,sog:%2.1fkts,cog:%3.1f,heading:%3.1f,time:%s",
							mmsi,name,distance,sog,cog,hdg,time);
					if( distance < DISTANCE_ARRIVAL ) {
						log.info("ARRIVED: " + data);
						log.info("-----------------");
					}else if( speed.size() > 1 && distance > DISTANCE_ARRIVAL && distance <= DISTANCE_APPROACH ) {
						//order should be most recent is first element
						double dv = speed.get(0) - speed.get( speed.size() - 1 );
						if( dv < 0 ) {//decreasing
							log.info("ARRIVING: " + data);
							log.info("-----------------");
						}else {//increasing
							log.info("DEPARTING: " + data);
							log.info("-----------------");
						}
					}else {
						log.info(data);
					}
				}
			}
		}
	}
	
	private void printAll() {
		List<Destination> destinations = destinationService.getDestinations();
		Collection<AisTargetTrack> tracks = tracker.tracks();
		tracks.forEach( AisTargetTrack::report );
		destinations.forEach( Destination::report );
	}
	
	AisTargetTrack trackFor(int mmsi, Collection<AisTargetTrack> tracks) {
		for(AisTargetTrack t:tracks) {
			if( t.getTarget().getMmsi() == mmsi ) return t;
		}
		return null;
	}
	
	boolean mmsiExists(int mmsi, Collection<AisTargetTrack> tracks) {
		for(AisTargetTrack t:tracks) {
			if( t.getTarget().getMmsi() == mmsi ) return true;
		}
		return false;
	}
}
