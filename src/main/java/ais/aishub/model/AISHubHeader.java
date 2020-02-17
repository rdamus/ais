package ais.aishub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AISHubHeader {
	@JsonProperty("ERROR")
	Boolean error;
	@JsonProperty("USERNAME")
	String username;
	@JsonProperty("FORMAT")
	String format;
	@JsonProperty("ERROR_MESSAGE")
	String errorMessage;
	@JsonProperty("LATITUDE_MIN")
	Float latitudeMin;
	@JsonProperty("LATITUDE_MAX")
	Float latitudeMax;
	@JsonProperty("LONGITUDE_MIN")
	Float longitudeMin;
	@JsonProperty("LONGITUDE_MAX")
	Float longitudeMax;
	@JsonProperty("RECORDS")
	Integer records;
	

	public AISHubHeader() {
		super();
	}
	
	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public Float getLatitudeMin() {
		return latitudeMin;
	}

	public void setLatitudeMin(Float latitudeMin) {
		this.latitudeMin = latitudeMin;
	}

	public Float getLatitudeMax() {
		return latitudeMax;
	}

	public void setLatitudeMax(Float latitudeMax) {
		this.latitudeMax = latitudeMax;
	}

	public Float getLongitudeMin() {
		return longitudeMin;
	}

	public void setLongitudeMin(Float longitudeMin) {
		this.longitudeMin = longitudeMin;
	}

	public Float getLongitudeMax() {
		return longitudeMax;
	}

	public void setLongitudeMax(Float longitudeMax) {
		this.longitudeMax = longitudeMax;
	}

	public Integer getRecords() {
		return records;
	}

	public void setRecords(Integer records) {
		this.records = records;
	}

	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "AISHubHeader [error=" + error + ", username=" + username + ", format=" + format + ", errorMessage="
				+ errorMessage + ", latitudeMin=" + latitudeMin + ", latitudeMax=" + latitudeMax + ", longitudeMin="
				+ longitudeMin + ", longitudeMax=" + longitudeMax + ", records=" + records + "]";
	}

	
	
}
