package ais.aishub;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ais.AisTargetTracker;
import ais.model.AisTarget;
import ais.model.AisTargetFactory;
import ais.model.AisTargetPacket;
import ais.model.AisTargetPacket.AisTargetPacketType;
import ais.model.AisTargetTrack;
import ais.model.Destination;
import dk.dma.enav.model.geometry.grid.Grid;

public class AisHubTracker implements AisTargetTracker {
	private static final Logger log = LoggerFactory.getLogger(AisHubTracker.class);
    /**
     * The Grid.
     */
    final Grid grid;
    
    Map<Integer, AisTargetTrack> tracks = new ConcurrentHashMap<>();
    List<Destination> destinations = Collections.synchronizedList(new ArrayList<>());

    /**
     * The Track stale millis.
     */
    static final long TRACK_STALE_MILLIS = Duration.of(30, MINUTES).toMillis();
    /**
     * The Track interpolation required millis.
     */
    static final long TRACK_INTERPOLATION_REQUIRED_MILLIS = Duration.of(30, SECONDS).toMillis();
    /**
     * The Interpolation time step millis.
     */
    static final long INTERPOLATION_TIME_STEP_MILLIS = Duration.of(10, SECONDS).toMillis();

    /**
     * A set of mmsi no.'s for which no messages are processed.
     */
    private final TreeSet<Integer> mmsiBlacklist = new TreeSet<>();
  
    /**
     * The approximate no. of milliseconds between each TimeEvent.
     */
    static final int TIME_EVENT_PERIOD_MILLIS = 1000;

    /**
     * Initialize the tracker.
     *
     * @param grid the grid
     */
    public AisHubTracker(Grid grid) {
        this(grid, null);
    }

    /**
     * Initialize the tracker.
     *
     * @param grid             the grid
     * @param blackListedMmsis the black listed mmsis
     */
    public AisHubTracker(Grid grid, int... blackListedMmsis) {
        this.grid = grid;

        if (blackListedMmsis != null) {
            for (int blackListedMmsi : blackListedMmsis) {
                this.mmsiBlacklist.add(blackListedMmsi);
            }
        }
    }


    public AisTargetTrack get(int mmsi) {
       return tracks.get( mmsi );
    }

    
    @Override
	public void update(AisTargetPacket packet) throws Exception {
		if( packet.getType().equals(AisTargetPacketType.Dma))
    		throw new Exception("can only process AisHub records");
    	
    	updateTracks( packet );
		//distance to destination - check threshold
		//
	}
    
    private void updateTracks(AisTargetPacket packet) {
    	int mmsi = packet.getMmsi();
    	synchronized(tracks) {
			if( !tracks.containsKey( mmsi ) ) {
				AisTarget target = AisTargetFactory.createTarget(packet);
		    	log.info("initTrack for target: " + target);
		    	tracks.put(mmsi, new AisTargetTrack(target));
			}
			//add a report after init
			tracks.get(mmsi).addReport( AisTargetFactory.createReport(packet) );
		}
    }

	@Override
	public int numberOfTargets() {
		return tracks.size();
	}

	@Override
	public AisTargetTrack getTrack(int mmsi) {
		return tracks.get(mmsi);
	}

	@Override
	public Set<Integer> targets() {
		return tracks.keySet();
	}

	@Override
	public List<AisTargetTrack> tracks() {
		return new ArrayList<AisTargetTrack>(tracks.values());
	}

	@Override
	public void addDestination(Destination destination) {
		destinations.add(destination);
	}
}
