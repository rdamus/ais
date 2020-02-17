package ais.aishub.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class AISHubResponse{
	AISHubHeader header;
	List<AISHubRecord> records;
	
	public AISHubResponse(){}

	public AISHubResponse(AISHubHeader header, List<AISHubRecord> records) {
		super();
		this.header = header;
		this.records = records;
	}

	public AISHubHeader getHeader() {
		return header;
	}

	public void setHeader(AISHubHeader header) {
		this.header = header;
	}

	public List<AISHubRecord> getRecords() {
		return records;
	}

	public void setRecords(List<AISHubRecord> records) {
		this.records = records;
	}

	@Override
	public String toString() {
		return "AISHubResponse [header=" + header + ", records=" + records + "]";
	}
}
