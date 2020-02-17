package ais;

import java.util.function.Consumer;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ais.aishub.AisHubTracker;
import ais.model.AisTargetPacket;
import ais.model.AisTargetTrack;
import ais.model.Destination;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.geometry.grid.Grid;

//@Singleton
public class AisUdpTrackingService extends AbstractAisTrackingService {
	private static final Logger log = LoggerFactory.getLogger(AisUdpTrackingService.class);
	AisReader reader;
		
	protected boolean init() {
		return initAisReader() && initTracker();
	}
	
	private boolean initTracker() {
		tracker = new AisHubTracker(Grid.GRID_1_DEGREE);
		return tracker != null;
	}

	private boolean initAisReader() {
		reader = AisReaders.createUdpReader(5001);
		if( reader != null ) {
			reader.registerPacketHandler( new AisPacketHandler() );
			reader.registerHandler( new AisMessageHandler() );
			reader.start();
			return true;
		}
		return false;
	}
	
	protected void processTracks() {
		tracker.tracks().forEach( t -> print(t) );
	}
	
	void print(AisTargetTrack t) {
		StringBuilder b = new StringBuilder();
		b.append("id: " + t.getTarget().getMmsi()).append(",");
		b.append("callsign: " + t.getTarget().getCallsign()).append(",");
//		b.append("pos: " + t.getPosition()).append(",");
//		b.append(String.format("cog: %3.1f,sog:%3.1f,time:%s",t.getCourseOverGround(),t.getSpeedOverGround(),LocalDateTime.now()));
		log.info(b.toString());
	}
	
	class AisPacketHandler implements Consumer<AisPacket>{
		@Override
		public void accept(AisPacket packet) {
			try {
				tracker.update( new AisTargetPacket(packet) );
			} catch (Exception e) {
				log.error("could not update target track " + e);
			}
		}
	}
	
	class AisMessageHandler implements Consumer<AisMessage>{
		@Override
		public void accept(AisMessage m) {
			if( m instanceof AisPositionMessage ) {
				for(Destination d:destinationService.getDestinations()) {
					AisPositionMessage p = (AisPositionMessage)m;
					d.update(p.getUserId(), 
							Position.create(
									p.getPos().getLatitudeDouble(), 
									p.getPos().getLongitudeDouble()));
				}				
			}
		}
	}

	@Override
	protected void report() {
		destinationService.getDestinations().forEach( Destination::report );
	}
}
