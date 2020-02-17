package ais;

import java.util.Collection;
import java.util.List;

import ais.model.AisTargetTrack;
import ais.model.Destination;

public interface AisTrackingService {

	Collection<AisTargetTrack> tracks();

	int numberOfTargets();

	List<Destination> getDestinations();

}