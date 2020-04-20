package tanlab;

import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.PcapStat;

import tanlab.constants.ConstantValues;

import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;

import com.sun.jna.Platform;

public class test {
	public static void main(String[] args) throws PcapNativeException, NotOpenException {
	    //PcapNetworkInterface nif = Pcaps.getDevByName(args[0]);
		PcapNetworkInterface nif = Pcaps.getDevByName("en7");
	    System.out.println(nif.getName() + "(" + nif.getDescription() + ")");

	    final PcapHandle handle = nif.openLive(65536, PromiscuousMode.PROMISCUOUS, 10);
	    handle.setFilter(ConstantValues.LLDP_FRAME_FILTER, BpfCompileMode.OPTIMIZE);
	   

	    PacketListener listener =
	        new PacketListener() {
	          @Override
	          public void gotPacket(Packet packet) {
	            System.out.println(handle.getTimestamp());
	            System.out.println(packet);
	          }
	        };

	    try {
	      handle.loop(-1, listener);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }

	    PcapStat ps = handle.getStats();
	    System.out.println("ps_recv: " + ps.getNumPacketsReceived());
	    System.out.println("ps_drop: " + ps.getNumPacketsDropped());
	    System.out.println("ps_ifdrop: " + ps.getNumPacketsDroppedByIf());
	    if (Platform.isWindows()) {
	      System.out.println("bs_capt: " + ps.getNumPacketsCaptured());
	    }

	    handle.close();
	  }
}
