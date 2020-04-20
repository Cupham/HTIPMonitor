package tanlab;

import java.util.List;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import tanlab.htip.HTIPMonitor;
import tanlab.kafka.KProducer;

public class App {
	public static KProducer msgPublisher;
	public static void main(String[] args) throws PcapNativeException, NotOpenException {
		System.out.println("Program Started");
		
		if(args.length == 0) {
			showHelp();
		} else {
			String brokerURL = args[0].trim();
			String topic = args[1].trim();
			msgPublisher = new KProducer(brokerURL, topic);
			List<PcapNetworkInterface> allDevs = Pcaps.findAllDevs();	
			HTIPMonitor.publishHTIPFrame(allDevs);
		}

	}
	private static void showHelp() {
		System.out.println(" Input parameter : [kafka-broker-connection-URL], [topic-name]");
	}

}
