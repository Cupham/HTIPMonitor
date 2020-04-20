package tanlab;

import org.pcap4j.core.BpfProgram.BpfCompileMode;

import java.sql.Timestamp;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.core.PcapStat;

import tanlab.constants.ConstantValues;
import tanlab.htip.HTIPFrameUtil;
import tanlab.htip.HTIPManager;
import tanlab.htip.HTIPObject;

import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;

import com.sun.jna.Platform;

public class test {
	public static void main(String[] args) throws PcapNativeException, NotOpenException {
	    //PcapNetworkInterface nif = Pcaps.getDevByName(args[0]);
		PcapNetworkInterface nif = Pcaps.getDevByName("eth0");
	    System.out.println(nif.getName() + "(" + nif.getDescription() + ")");

	    final PcapHandle handle = nif.openLive(65536, PromiscuousMode.PROMISCUOUS, 10);
	    handle.setFilter(ConstantValues.LLDP_FRAME_FILTER, BpfCompileMode.OPTIMIZE);
	    HTIPManager manager = new HTIPManager(nif.getName(),nif.getLinkLayerAddresses().get(0).toString());
    	App.msgPublisher.publicMessage(manager.toJSON());


	    PacketListener listener =
	        new PacketListener() {
	          @Override
	          public void gotPacket(Packet packet) {
	        	  Timestamp time = handle.getTimestamp();
			    	HTIPObject obj = HTIPFrameUtil.htipFromPacket(time,packet);
			    	if(obj!= null) {
			    		App.msgPublisher.publicMessage(obj.toJson());
			    	}
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
