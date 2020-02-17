package ais.aishub.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AISHubRecord {
	@JsonProperty("MMSI")
	Integer mmsi;
	@JsonProperty("TIME")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z", timezone = "GMT")
    Date time;
	@JsonProperty("LONGITUDE")
    Float longitude;
	@JsonProperty("LATITUDE")
    Float latitude;
	@JsonProperty("COG")
    Float cog;
	@JsonProperty("SOG")
    Float sog;
	@JsonProperty("HEADING")
    Float heading;
	@JsonProperty("ROT")
    Float rot;
	@JsonProperty("NAVSTAT")
    Integer navstat;
	@JsonProperty("IMO")
    String imo;
	@JsonProperty("NAME")
    String name;
	@JsonProperty("CALLSIGN")
    String callsign;
	@JsonProperty("TYPE")
    Integer type;
	@JsonProperty("A")
    Integer a;
	@JsonProperty("B")
    Integer b;
	@JsonProperty("C")
    Integer c;
	@JsonProperty("D")
    Integer d;
	@JsonProperty("DRAUGHT")
    Float draught;
	@JsonProperty("DEST")
    String dest;
    @JsonProperty("ETA")
    String eta;
    
    
	public AISHubRecord() {
		super();
	}
	
	public Integer getMmsi() {
		return mmsi;
	}
	public void setMmsi(Integer mmsi) {
		this.mmsi = mmsi;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Float getLongitude() {
		return longitude;
	}
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	public Float getLatitude() {
		return latitude;
	}
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	public Float getCog() {
		return cog;
	}
	public void setCog(Float cog) {
		this.cog = cog;
	}
	public Float getSog() {
		return sog;
	}
	public void setSog(Float sog) {
		this.sog = sog;
	}
	public Float getHeading() {
		return heading;
	}
	public void setHeading(Float heading) {
		this.heading = heading;
	}
	public Float getRot() {
		return rot;
	}
	public void setRot(Float rot) {
		this.rot = rot;
	}
	public Integer getNavstat() {
		return navstat;
	}
	public void setNavstat(Integer navstat) {
		this.navstat = navstat;
	}
	public String getImo() {
		return imo;
	}
	public void setImo(String imo) {
		this.imo = imo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCallsign() {
		return callsign;
	}
	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getA() {
		return a;
	}
	public void setA(Integer a) {
		this.a = a;
	}
	public Integer getB() {
		return b;
	}
	public void setB(Integer b) {
		this.b = b;
	}
	public Integer getC() {
		return c;
	}
	public void setC(Integer c) {
		this.c = c;
	}
	public Integer getD() {
		return d;
	}
	public void setD(Integer d) {
		this.d = d;
	}
	public Float getDraught() {
		return draught;
	}
	public void setDraught(Float draught) {
		this.draught = draught;
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
	}
	public String getEta() {
		return eta;
	}
	public void setEta(String eta) {
		this.eta = eta;
	}

	@Override
	public String toString() {
		return "AISHubRecord [mmsi=" + mmsi + ", time=" + time + ", longitude=" + longitude + ", latitude=" + latitude
				+ ", cog=" + cog + ", sog=" + sog + ", heading=" + heading + ", rot=" + rot + ", navstat=" + navstat
				+ ", imo=" + imo + ", name=" + name + ", callsign=" + callsign + ", type=" + type + ", a=" + a + ", b="
				+ b + ", c=" + c + ", d=" + d + ", draught=" + draught + ", dest=" + dest + ", eta=" + eta + "]";
	}
    
	
}
