package ais;

import java.io.InputStream;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

import ais.model.Destination;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPosition;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;
import io.micronaut.context.annotation.Value;
import io.micronaut.runtime.server.event.ServerStartupEvent;

//@Singleton
public class AisPositionService {
	private static final Logger log = LoggerFactory.getLogger(AisPositionService.class);
	@Value("${serial.port}")
	String portDescriptor;
	@Value("${serial.baud}")
	Integer baudRate;
	SerialPort comPort;
	DatagramSocket udpPort;
	InputStream inputStream;
	AisReader reader;
	MessageStore store = new MessageStore();
	List<Destination> destinations = new ArrayList<>();
	enum PortType{ Serial, Udp }
	PortType portType;
	

	//37.791166, -122.294209
	Position alamedaFerry = Position.create(37.791166, -122.294209);
	private boolean initialized = false;
		
	public Position getAlamedaFerry() {
		return alamedaFerry;
	}
	
	AisPositionService(){
		destinations.add( new Destination(alamedaFerry, "ALAMEDA FERRY TERMINAL") );
		portType = PortType.Udp;
	}

//	@Async
//	@EventListener
	public void onApplicationEvent(ServerStartupEvent event) {
		log.debug("starting AisPositionService");
		initAisReader();
	}
	
	private void initAisReader() {
		if( portType.equals(PortType.Udp) ) {
			reader = AisReaders.createUdpReader(5001);
		}else if( portType.equals(PortType.Serial) ) {
			if( initComPort() )
				reader = AisReaders.createReaderFromInputStream( inputStream );	
		}
		if( reader != null ) {
			reader.registerHandler(new PositionHandler());
			reader.start();
			initialized = true;
		}
	}

	private boolean initComPort() {
		comPort = SerialPort.getCommPort(portDescriptor);
		comPort.openPort();
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		comPort.setBaudRate(baudRate);
		
		inputStream = comPort.getInputStream();
		return inputStream != null;
	}
	
//	@Scheduled(fixedRate="1s")
	public void process() {
		if( !initialized  ) {
			log.info("not initialized");
			return;
		}
		log.info("processing store");
		Map<Double,Integer> distanceMap = new TreeMap<>();
//		store.keySet().forEach( 
//				id -> 
//				AisPosition p = store.get(id).peek().getPos() );
		for(Integer id:store.keySet()) {
			AisPositionMessage msg = store.get(id).peek();
			AisPosition pos =  msg.getPos();
			Position vpos = Position.create( pos.getLatitudeDouble(), pos.getLongitudeDouble());
			double dist = vpos.distanceTo(alamedaFerry, CoordinateSystem.CARTESIAN);
			double velKts = msg.getSog() / 10.0;
			double velMs = velKts * 0.514;
			double eta_sec = dist / velMs;
			double eta_min = eta_sec / 60;
			double eta_hrs = eta_sec / 3600;
			if( dist < 50 )
				log.info(String.format("id:%d,AT ALAMEDA",id));
			else
				log.info(String.format("id:%d,dist:%3.1f,eta:hrs:%3.1f,min:%3.1f,sec:%3.1f",id,dist,eta_hrs,eta_min,eta_sec));
		}
	}
	
	public MessageStore getStore() {
		return store;
	}
	
	class PositionHandler implements Consumer<AisMessage>{

		@Override
		public void accept(AisMessage t) {
			
			if( t instanceof AisPositionMessage ) {
				logPosition( t );
			}
		}
		
		private void logPosition(AisMessage aisMessage) {
			AisPositionMessage p = (AisPositionMessage)aisMessage;
			AisPosition pos =  p.getPos();
			Position vpos = Position.create( pos.getLatitudeDouble(), pos.getLongitudeDouble());
			double dist = vpos.distanceTo(alamedaFerry, CoordinateSystem.CARTESIAN);
			String s = String.format("type:%d,id:%d,dist:%3.1f,sog:%3.1f,cog:%3.1f,hdg:%d,lat:%3.5f,lon:%3.5f",
					aisMessage.getMsgId(),aisMessage.getUserId(), dist,(p.getSog() / 10.0f),
					(p.getCog() / 10.0f ), p.getTrueHeading(),pos.getLatitudeDouble(), pos.getLongitudeDouble());
			log.info(s);
			store.add( p );
		}
	}
}

class MessageStore{
	private static final Logger log = LoggerFactory.getLogger(MessageStore.class);
	Map<Integer, Deque<AisPositionMessage>> store;
	
	MessageStore(){
		store = new HashMap<>();
	}
	public void add(AisPositionMessage msg) {
		put(msg.getUserId(), msg);
	}
	
	protected void put(Integer id, AisPositionMessage msg) {
		if( !store.containsKey(id) ) store.put(id, new ConcurrentLinkedDeque<>());
		store.get(id).push(msg);
		log.info("store[" +id +"],size: " + store.get(id).size() );
		if( store.get(id).size() > 1000 ) store.get(id).removeLast();
	}
	public boolean containsKey(Object key) {
		return store.containsKey(key);
	}
	public Set<Entry<Integer, Deque<AisPositionMessage>>> entrySet() {
		return store.entrySet();
	}

	public Deque<AisPositionMessage> get(Object key) {
		return store.get(key);
	}
	public boolean isEmpty() {
		return store.isEmpty();
	}
	public Set<Integer> keySet() {
		return store.keySet();
	}
	public Deque<AisPositionMessage> put(Integer key, Deque<AisPositionMessage> value) {
		return store.put(key, value);
	}
	public int size() {
		return store.size();
	}
	public Collection<Deque<AisPositionMessage>> values() {
		return store.values();
	}
	@Override
	public String toString() {
		return "MessageStore [store=" + store + "]";
	}
}
