package ais;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ais.AisPositionService.PortType;
import ais.AisPositionService.PositionHandler;
import ais.model.Destination;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPosition;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.ais.tracker.Tracker;
import dk.dma.ais.tracker.eventEmittingTracker.EventEmittingTrackerImpl;
import dk.dma.ais.tracker.eventEmittingTracker.Track;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.geometry.grid.Grid;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.micronaut.scheduling.annotation.Async;
import io.micronaut.scheduling.annotation.Scheduled;

@Singleton
public class AisUdpTrackingService {
	private static final Logger log = LoggerFactory.getLogger(AisUdpTrackingService.class);
	EventEmittingTrackerImpl tracker;
	AisReader reader;
	List<Destination> destinations;
	private boolean initialized;
	
	Position alamedaFerry = Position.create(37.791166, -122.294209);
	@Async
	@EventListener
	public void onApplicationEvent(ServerStartupEvent event) {
		log.info("starting AisUdpTrackingService");
		initAisReader();
		initTracker();
		initDestinations();
	}
	
	private void initDestinations() {
		destinations = new ArrayList<Destination>();
		destinations.add( new Destination(alamedaFerry, "Alameda Ferry Terminal") );
	}

	private void initTracker() {
		tracker = new EventEmittingTrackerImpl(Grid.GRID_1_DEGREE);
	}

	private void initAisReader() {
		reader = AisReaders.createUdpReader(5001);
		if( reader != null ) {
			reader.registerPacketHandler( new AisPacketHandler() );
			reader.registerHandler( new AisMessageHandler() );
			reader.start();
			initialized = true;
		}
	}
	
	@Scheduled(fixedRate="1s")
	public void process() {
		if( !initialized  ) {
			log.info("not initialized");
			return;
		}
		log.info("processing tracks");

//		tracker.getTracks().forEach( t -> log.info("track: " + t.toString()));
		tracker.getTracks().forEach( t -> print(t) );
		destinations.forEach( Destination::report );
	}
	
	void print(Track t) {
		StringBuilder b = new StringBuilder();
		b.append("id: " + t.getMmsi()).append(",");
		b.append("callsign: " + t.getCallsign()).append(",");
		b.append("pos: " + t.getPosition()).append(",");
		b.append(String.format("cog: %3.1f,sog:%3.1f,time:%s",t.getCourseOverGround(),t.getSpeedOverGround(),LocalDateTime.now()));
		log.info(b.toString());
	}
	
	class AisPacketHandler implements Consumer<AisPacket>{
		@Override
		public void accept(AisPacket packet) {
			tracker.update( packet );
		}
	}
	
	class AisMessageHandler implements Consumer<AisMessage>{
		@Override
		public void accept(AisMessage m) {
			if( m instanceof AisPositionMessage ) {
				for(Destination d:destinations) {
					d.update((AisPositionMessage)m);
				}				
			}
		}
	}

	public Collection<Track> getTracks() {
		return tracker.getTracks();
	}

	public int getNumberOfTracks() {
		return tracker.size();
	}
	
	public List<Destination> getDestinations(){
		return destinations;
	}
}
