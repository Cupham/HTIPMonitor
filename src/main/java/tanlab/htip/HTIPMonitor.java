package tanlab.htip;


import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.Packet;

import tanlab.App;
import tanlab.constants.ConstantValues;



public class HTIPMonitor {
	
	public static void publishHTIPFrame(List<PcapNetworkInterface> interfaceList) throws PcapNativeException {
		for(PcapNetworkInterface i  : interfaceList) {
			PcapHandle handle = i.openLive(ConstantValues.SNAPSHOT_LENGTH, PromiscuousMode.PROMISCUOUS, 
					  ConstantValues.READ_TIMEOUT);
			try {
				handle.setFilter("ether proto 0x88cc", BpfCompileMode.OPTIMIZE);
			} catch (Exception e){
				
			}
			
			ExecutorService service = Executors.newFixedThreadPool(interfaceList.size());
		    service.execute(new Task(i,handle, (packet) -> {
		    	Timestamp time = handle.getTimestamp();
		    	HTIPObject obj = HTIPFrameUtil.htipFromPacket(time,packet);
		    	if(obj!= null) {
		    		App.msgPublisher.publicMessage(obj.toJson());
		    	}
		    }));
		}
	}
	public  HTIPObject testFrame(Packet pkg) {
		Timestamp t = new Timestamp(System.currentTimeMillis());
		return HTIPFrameUtil.htipFromPacket(t,pkg);
	}

	

	private static class Task implements Runnable {
		private PcapHandle handle;
        PacketListener listener ;
	    public Task(PcapNetworkInterface i, PcapHandle handle, PacketListener listener) {
	      this.handle = handle;
	      this.listener = listener;
	      
	      if(i.getLinkLayerAddresses().size() > 0) {
	    	  HTIPManager manager = new HTIPManager(i.getName(),i.getLinkLayerAddresses().get(0).toString());
	    	  App.msgPublisher.publicMessage(manager.toJSON());
	      }
	      
	    }
		@Override
		public void run() {
			try {
		        handle.loop(-1, listener);
		      } catch (PcapNativeException e) {
		        e.printStackTrace();
		      } catch (InterruptedException e) {
		        e.printStackTrace();
		      } catch (NotOpenException e) {
		        e.printStackTrace();
		      }
			
		}
		
	}

}
