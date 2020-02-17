package ais.model;

public class AisTarget {
	private final int mmsi;
	private String imo;
	private String name;
	private String callsign;
	private Integer type;
	private Integer a;
	private Integer b;
	private Integer c;
	private Integer d;
	private Float draught;

	public AisTarget(int mmsi) {
		this.mmsi = mmsi;
	}

	public int getMmsi() {
		return mmsi;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mmsi;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AisTarget other = (AisTarget) obj;
		if (mmsi != other.mmsi)
			return false;
		return true;
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

	@Override
	public String toString() {
		return "AisTarget [mmsi=" + mmsi + ", imo=" + imo + ", name=" + name + ", callsign=" + callsign + ", type="
				+ type + ", a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", draught=" + draught + "]";
	}
	
	
}
