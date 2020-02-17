package ais.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * the track of a target is the collection of positions it has reported.
 * @author rdamu
 *
 */
public class AisTargetTrack implements Cloneable{
	private static final Logger log = LoggerFactory.getLogger(AisTargetTrack.class);
	private static final int MAX_SIZE = 50;

	Deque<AisTargetReport> targetReports = new ConcurrentLinkedDeque<AisTargetReport>();
	
	private AisTarget target;
	private Date timeValid = new Date();
	
	public AisTargetTrack(AisTarget target) {
		this.target = target;
	}
	
	public Date getTimeValid() {
		return timeValid;
	}
	
	public AisTarget getTarget() {
		return target;
	}

	public void setTarget(AisTarget target) {
		this.target = target;
	}

	public void addReport(AisTargetReport report) {
		timeValid = report.getDate();
		synchronized(targetReports) {
			targetReports.push(report);
			
			if( targetReports.size() > MAX_SIZE )
				targetReports.removeLast();
		}
	}
	
	public List<AisTargetReport> positions(){
		return new ArrayList<>(getTargetReports());
	}

	public Deque<AisTargetReport> getTargetReports() {
		return targetReports;
	}
	
	public void report() {
		AisTargetReport report = targetReports.peek();
		log.info(String.format("MMSI: %d, Latest AisTargetReport:%s",target.getMmsi(),report));
	}
	
	@Override
	public String toString() {
		return "AisTargetTrack [target=" + target + ", targetReports=" + targetReports + "]";
	}
	
	public double sog() {
		if( targetReports.isEmpty()) {
			return 0;
		}
		
		int size = targetReports.size();
		if( size == 1) {
			return targetReports.peek().getSOG();
		}else {
			double sum = targetReports.stream().limit(2L).mapToDouble(p -> p.getSOG()).sum();
			return sum / 2.0;
		}
	}
	
	public double cog() {
		if( targetReports.isEmpty()) {
			return 0;
		}
		
		int size = targetReports.size();
		if( size == 1) {
			return targetReports.peek().getCOG();
		}else {
			double sum = targetReports.stream().limit(2L).mapToDouble(p -> p.getCOG()).sum();
			return sum / 2.0;
		}
	}
	
	public List<Double> sog(long history){
		return targetReports.stream().limit(history).mapToDouble(p -> p.getSOG()).boxed().collect(Collectors.toList());
	}

	public double heading() {
		if( targetReports.isEmpty()) {
			return 0;
		}
		
		int size = targetReports.size();
		if( size == 1) {
			return targetReports.peek().getHeading();
		}else {
			double sum = targetReports.stream().limit(2L).mapToDouble(p -> p.getHeading()).sum();
			return sum / 2.0;
		}
	}

	public Date time() { return getTimeValid(); }
}
