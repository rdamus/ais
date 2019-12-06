package ais;

import java.io.InputStream;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisPosition;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.reader.AisReader;
import dk.dma.ais.reader.AisReaders;
import dk.dma.enav.model.dto.PositionDTO;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;
import io.micronaut.test.annotation.MicronautTest;

@MicronautTest
public class AisLibTests {
	private static final Logger log = LoggerFactory.getLogger(AisLibTests.class);
	
	String portDescriptor = "COM4";
	int baudRate = 38400;
	//37.791166, -122.294209
	Position alamedaFerry = Position.create(37.791166, -122.294209);// = new PositionDTO(latitude, longitude);
	
	
	void testNonBlockingCommPortReads() {
		log.info("testing non-blocking comport reads");
		SerialPort comPort = SerialPort.getCommPorts()[0];
		comPort.openPort();
		try {
		   while (true)
		   {
		      while (comPort.bytesAvailable() == 0)
		         Thread.sleep(20);

		      byte[] readBuffer = new byte[comPort.bytesAvailable()];
		      int numRead = comPort.readBytes(readBuffer, readBuffer.length);
		      log.info("Read " + numRead + " bytes.");
		   }
		} catch (Exception e) { e.printStackTrace(); }
		comPort.closePort();
	}

	void testInputStreamCommPortReads() {
		log.info("testing comport reads");
		SerialPort comPort = SerialPort.getCommPorts()[0];
		comPort.openPort();
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		comPort.setBaudRate(38400);
		InputStream in = comPort.getInputStream();
		try
		{
		   for (int j = 0; j < 1000; ++j)
		      System.out.print((char)in.read());
		   in.close();
		} catch (Exception e) { e.printStackTrace(); }
		comPort.closePort();
	}
	
	
	void testAisMessageDecode() throws InterruptedException {
		log.info("testing ais messages");
		SerialPort comPort = loadSerialPort();
		comPort.openPort();
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		comPort.setBaudRate(baudRate);
		InputStream in = comPort.getInputStream();
		AisReader reader = AisReaders.createReaderFromInputStream( in );
		reader.registerHandler(new Consumer<AisMessage>() {
			@Override
			public void accept(AisMessage aisMessage) {
				log.info("message rcvd: " + aisMessage.toString());
			}
		});
		reader.start();
		reader.join();
	}
	
	@Test
	void testAisPositionDecode() throws InterruptedException {
		log.info("testing ais messages");
		SerialPort comPort = loadSerialPort();
		comPort.openPort();
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		comPort.setBaudRate(baudRate);
		InputStream in = comPort.getInputStream();
		AisReader reader = AisReaders.createReaderFromInputStream( in );
		reader.registerHandler(new Consumer<AisMessage>() {
			@Override
			public void accept(AisMessage aisMessage) {
				if( aisMessage instanceof AisPositionMessage ) {
					handlePosition( aisMessage );
				}
			}

			private void handlePosition(AisMessage aisMessage) {
				AisPositionMessage p = (AisPositionMessage)aisMessage;
				AisPosition pos =  p.getPos();
				Position vpos = Position.create( pos.getLatitudeDouble(), pos.getLongitudeDouble());
				double dist = vpos.distanceTo(alamedaFerry, CoordinateSystem.CARTESIAN);
				String s = String.format("type:%d,id:%d,dist:%3.1f,sog:%3.1f,cog:%3.1f,hdg:%d,lat:%3.5f,lon:%3.5f",
						aisMessage.getMsgId(),aisMessage.getUserId(), dist,(p.getSog() / 10.0f),
						(p.getCog() / 10.0f ), p.getTrueHeading(),pos.getLatitudeDouble(), pos.getLongitudeDouble());
				log.info(s);
			}
		});
		reader.start();
		reader.join();
	}
	SerialPort loadSerialPort() {
		return SerialPort.getCommPort(portDescriptor);
	}
}
