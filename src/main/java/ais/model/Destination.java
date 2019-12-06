package ais.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.message.AisPosition;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;

public class Destination{
	private static final Logger log = LoggerFactory.getLogger(Destination.class);
	String name;
	Position dest;
	Map<Integer,LastKnown> distanceMap = new HashMap<>();
	double threshold = 2000.0;
	
	public Destination(Position dest, String name){
		this.name = name;
		this.dest = dest;
	}
	
	public Destination(double latitude, double longitude, String name){
		this.name = name;
		this.dest = Position.create(latitude, longitude);
	}
	
	public double update(AisPositionMessage msg) {
		AisPosition pos =  msg.getPos();
		Position vpos = Position.create( pos.getLatitudeDouble(), pos.getLongitudeDouble());
		
		double dist = vpos.distanceTo(dest, CoordinateSystem.CARTESIAN);
		if( dist < threshold ) {
			LastKnown l = new LastKnown(dist, LocalDateTime.now());
			distanceMap.put(msg.getUserId(), l);
		}
		return dist;
	}
	
	public void report() {
		Map<Double, Integer> dmap = new TreeMap<>();
		for(Integer id:distanceMap.keySet()) {
			dmap.put(distanceMap.get(id).getDistance(),id);
		}
		
		for(Entry<Double,Integer> e:dmap.entrySet()) {
			Integer id = e.getValue();
			Double dist = e.getKey();
			
			LocalDateTime lastTime = distanceMap.get(id).getTime();
			long ms = Duration.between(LocalDateTime.now(), lastTime).toMillis();
			double sec = ms / 1000.0;
			log.info(String.format("Id: %d,name:%s,dist:%3.1fm,time:%s,aging:%3.1f",id,name,dist,lastTime,sec));
		}
//		dmap.forEach((dist,id) -> log.info(String.format("Id: %d,name:%s,dist:%3.1fm,time:%s",id,name,dist,)));
	}
}

class LastKnown{
	Double distance;
	LocalDateTime time;
	public LastKnown(Double distance, LocalDateTime time) {
		super();
		this.distance = distance;
		this.time = time;
	}
	public Double getDistance() {
		return distance;
	}
	public LocalDateTime getTime() {
		return time;
	}
	
}