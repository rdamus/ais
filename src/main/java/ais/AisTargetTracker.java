package ais;

import java.util.List;
import java.util.Set;

import ais.model.AisTargetPacket;
import ais.model.AisTargetTrack;
import ais.model.Destination;

public interface AisTargetTracker{
	void update(AisTargetPacket packet) throws Exception;
    AisTargetTrack getTrack(int mmsi);
    Set<Integer> targets();
    List<AisTargetTrack> tracks();
    int numberOfTargets();
    void addDestination(Destination destination);
}
