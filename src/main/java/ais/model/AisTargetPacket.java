package ais.model;

import ais.aishub.model.AISHubRecord;
import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.packet.AisPacket;

/**
 * container for the different packets we process: {@link AISHubRecord} and {@link AisPacket}
 * @author rdamu
 *
 */
public class AisTargetPacket {
	public enum AisTargetPacketType{
		AisHub,
		Dma
	};
	
	AisTargetPacketType type;
	
	AISHubRecord record;
	AisPacket packet;
	
	public AisTargetPacket(AISHubRecord record) {
		this.type = AisTargetPacketType.AisHub;
		this.record = record;
	}

	public AisTargetPacket(AisPacket packet) {
		this.type = AisTargetPacketType.Dma;
		this.packet = packet;
	}
	
	public AisTargetPacketType getType() {
		return type;
	}

	public int getMmsi() {
		switch(type) {
		case AisHub: return record.getMmsi();
		case Dma: try {
				return packet.getAisMessage().getUserId();
			} catch (AisMessageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SixbitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	public AISHubRecord getRecord() {
		return record;
	}
}
