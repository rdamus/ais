package ais;

import java.util.Collection;

import javax.inject.Inject;

import ais.model.AisTargetTrack;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

public class AisController {

	
//	AisPositionService service;
	@Inject AisTrackingService service;
	
	@Get("/")
	HttpStatus index() {
		return HttpStatus.OK;
	}
	
	@Get("list")
	HttpResponse<Collection<AisTargetTrack>> list() {
		return HttpResponse.ok(service.tracks());
	}
//	HttpResponse<List<String>> list() {
//		MessageStore store = service.getStore();
//		List<String> vessels = new ArrayList<String>();
//		for(Integer id:store.keySet()) {
//			AisPositionMessage msg = store.get(id).peek();
//			AisPosition pos =  msg.getPos();
//			Position vpos = Position.create( pos.getLatitudeDouble(), pos.getLongitudeDouble());
//			double dist = vpos.distanceTo(service.getAlamedaFerry(), CoordinateSystem.CARTESIAN);
//			double velKts = msg.getSog() / 10.0;
//			double velMs = velKts * 0.514;
//			double eta_sec = dist / velMs;
//			double eta_min = eta_sec / 60;
//			double eta_hrs = eta_sec / 3600;
//			vessels.add(String.format("id:%d,dist:%3.1f,eta:hrs:%3.1f,min:%3.1f,sec:%3.1f",id,dist,eta_hrs,eta_min,eta_sec));
//		}
//		return HttpResponse.ok(vessels);
//	}
}
