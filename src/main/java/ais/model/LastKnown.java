package ais.model;

import java.time.LocalDateTime;

public class LastKnown{
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