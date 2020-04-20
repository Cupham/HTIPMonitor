package tanlab;

import java.util.List;

import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import tanlab.htip.HTIPMonitor;
import tanlab.kafka.KProducer;

public class App {
	public static KProducer msgPublisher;
	public static void main(String[] args) throws PcapNativeException {
		System.out.println("Program Started");
		
		if(args.length == 0) {
			showHelp();
		} else {
			String brokerURL = args[0].trim();
			String topic = args[1].trim();
			// 1. Get all available network interfaces
			List<PcapNetworkInterface> allDevs = Pcaps.findAllDevs();	
			if(allDevs.size() > 0) {
				// 2. Initialize Message Publisher
				msgPublisher = new KProducer(brokerURL,topic);
				// 3. Monitor and publish HTIP Frame
				HTIPMonitor.publishHTIPFrame(allDevs);
			} else {
				System.out.println("No Network Interface is available");
			}
		}

	}
	private static void showHelp() {
		System.out.println(" Input parameter : [kafka-broker-connection-URL], [topic-name]");
	}

}
