package ais.model;

import ais.aishub.model.AISHubRecord;
import dk.dma.enav.model.geometry.Position;

public class AisTargetFactory {

	public static AisTarget createTarget(AisTargetPacket p) {
		switch(p.getType()) {
		case AisHub: return createAisHubTarget(p);
		default: return null;		
		}
	}

	private static AisTarget createAisHubTarget(AisTargetPacket p) {
		AisTarget t = new AisTarget(p.getMmsi());
		AISHubRecord r = p.getRecord();
		t.setA(r.getA());
		t.setB(r.getB());
		t.setC(r.getC());
		t.setD(r.getD());
		t.setCallsign(r.getCallsign().trim());
		t.setDraught(r.getDraught());
		t.setName(r.getName().trim());
		t.setImo(r.getImo().trim());
		return t;
	}
	
	public static AisTargetReport createReport(AisTargetPacket p) {
		switch(p.getType()) {
		case AisHub: return createAisHubReport(p);
		default: return null;		
		}
	}

	private static AisTargetReport createAisHubReport(AisTargetPacket p) {
		AISHubRecord r = p.getRecord();
		Position pos = Position.create(r.getLatitude(), r.getLongitude());
		return new AisTargetReport(r.getTime(), pos, r.getSog(), r.getCog(), r.getHeading());
	}
}
