package ais.aishub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ais.aishub.model.AISHubHeader;
import ais.aishub.model.AISHubRecord;
import ais.aishub.model.AISHubResponse;

@Singleton
public class AISHubFactory {
	public static int HEADER = 0;
	public static int RECORDLIST = 1;
	private static final Logger log = LoggerFactory.getLogger(AISHubFactory.class);
	ObjectMapper mapper = new ObjectMapper();
	
	public AISHubResponse create(JsonNode node) throws JsonParseException, JsonMappingException, IOException {
		log.info("header node: " + node.get(0).getNodeType());
		AISHubHeader header = mapper.readValue(node.get(HEADER).toString(), AISHubHeader.class);
		List<AISHubRecord> records = new ArrayList<AISHubRecord>();
		log.info("as Header: " + header); 
		if( node.get(RECORDLIST) == null )
			return new AISHubResponse(header, records);
		
		log.info("records node: " + node.get(RECORDLIST).getNodeType());
		JsonNode jsonRecordList = node.get(RECORDLIST);
		jsonRecordList.elements().forEachRemaining(record->{
			try {
				records.add(mapper.readValue(record.toString(), AISHubRecord.class));
			} catch (Exception e) {
				log.error("error: " + e);
			} 
		});
		
		return new AISHubResponse(header, records);
	}
}
