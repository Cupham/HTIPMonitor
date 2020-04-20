package tanlab.htip;

import java.sql.Timestamp;
import java.util.ArrayList;

import com.google.gson.Gson;

public class HTIPObject {
	// Required TLV
	private Timestamp timeStamp;
	private int frameLength;
	private byte[] rawFrame;
	private String equipmentID;
	private String portID;
	private int timeToLive;
	private String portDescription;
	private HTIPDeviceInformation htipDeviceInformation;
	private boolean containLinkInformation;
	private ArrayList<HTIPLinkInformation> htipLinkInformation;
	private ArrayList<String> macAddressList;
	
	
	public boolean isContainLinkInformation() {
		return containLinkInformation;
	}
	public void setContainLinkInformation(boolean containLinkInformation) {
		this.containLinkInformation = containLinkInformation;
	}
	public int getFrameLength() {
		return frameLength;
	}
	public void setFrameLength(int frameLength) {
		this.frameLength = frameLength;
	}
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	public HTIPObject() {
	}
	public String getEquipmentID() {
		return equipmentID;
	}
	public void setEquipmentID(String equipmentID) {
		this.equipmentID = equipmentID.trim();
	}
	public String getPortID() {
		return portID;
	}
	public void setPortID(String portID) {
		this.portID = portID.trim();
	}
	public int getTimeToLive() {
		return timeToLive;
	}
	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}
	public String getPortDescription() {
		return portDescription;
	}
	public void setPortDescription(String portDescription) {
		this.portDescription = portDescription.trim();
	}
	public HTIPDeviceInformation getHtipDeviceInformation() {
		return htipDeviceInformation;
	}
	public void setHtipDeviceInformation(HTIPDeviceInformation htipDeviceInformation) {
		this.htipDeviceInformation = htipDeviceInformation;
	}
	public ArrayList<HTIPLinkInformation> getHtipLinkInformation() {
		return htipLinkInformation;
	}
	public void setHtipLinkInformation(ArrayList<HTIPLinkInformation> htipLinkInformation) {
		this.htipLinkInformation = htipLinkInformation;
	}
	@Override
	public String toString() {
		String rs  = String.format("Time: %s {equipmentID: %s "
								 + "PortID: %s, PortDescription: %s, TTL = %d}", 
								    getTimeStamp(), getEquipmentID(),
								    getPortID(),getPortDescription(),getTimeToLive());
		rs = rs + "\n===============Device Infor==============\n";
		rs = rs + getHtipDeviceInformation().toString();
		rs = rs + "=========================================\n";
		for(int i = 0; i < getHtipLinkInformation().size();i++) {
			rs = rs + "---------------------Link Infor ---------------------\n";
			rs = rs + getHtipLinkInformation().get(i);
			rs = rs + "---------------------------------------------------\n";
		}
		return rs;
	}
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	public byte[] getRawFrame() {
		return rawFrame;
	}
	public void setRawFrame(byte[] rawFrame) {
		this.rawFrame = rawFrame;
	}
	
	public TopologyObject toTopologyObject() {
		TopologyObject obj = new TopologyObject();
		if(this.isContainLinkInformation() ) {
			obj.setType("HTIP_NW");
		} else {
			obj.setType("HTIP_L2_Agent");
		}
		obj.setTtl(this.timeToLive);
		obj.setChassisID(this.getEquipmentID());
		obj.setPortID(this.portID);
		obj.setPortDescription(this.portDescription);
		obj.setDeviceCategory(this.htipDeviceInformation.getDeviceCategory());
		obj.setDeviceCategory(this.htipDeviceInformation.getManufacturerCode());
		obj.setDeviceCategory(this.htipDeviceInformation.getModelName());
		obj.setDeviceCategory(this.htipDeviceInformation.getModelNumber());
		ArrayList<FDB> fdbs = new ArrayList<FDB>();
		for(int i = 0; i <this.getHtipLinkInformation().size();i++) {
			HTIPLinkInformation link = this.getHtipLinkInformation().get(i);
			FDB fdb = new FDB(link.getPortNumber(),link.getMacAddressList());
			fdbs.add(fdb);
		}
		obj.setFdb(fdbs);
		return obj;
	}
	public ArrayList<String> getMacAddressList() {
		return macAddressList;
	}
	public void setMacAddressList(ArrayList<String> macAddressList) {
		this.macAddressList = macAddressList;
	}
}
