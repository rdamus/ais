package ais.model;

import java.util.Date;

import dk.dma.enav.model.geometry.Position;

public class AisTargetReport{
	Date time;
	Position position;
	float sog;
	float cog;
	float heading;
	
	
	public AisTargetReport(Date time, Position position, float sog, float cog, float heading) {
		super();
		this.time = time;
		this.position = position;
		this.sog = sog;
		this.cog = cog;
		this.heading = heading;
	}

	public long getTime() {
		return time.getTime();
	}
	
	public Date getDate() {
		return time;
	}

	public Position getPosition() {
		return position;
	}

	public float getCOG() {
		return cog;
	}

	public float getSOG() {
		return sog;
	}

	public float getHeading() {
		return heading;
	}

	@Override
	public String toString() {
		return "AisTargetReport [time=" + time + ", position=" + position + ", sog=" + sog + ", cog=" + cog
				+ ", heading=" + heading + "]";
	}
}
