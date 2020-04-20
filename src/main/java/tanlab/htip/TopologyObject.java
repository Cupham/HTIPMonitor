package tanlab.htip;

import java.util.ArrayList;

import com.google.gson.Gson;

public class TopologyObject {
	private String ip;
	private String type;
	private String chassisID;
	private String portID;
	private int ttl;
	private String portDescription;
	private String deviceCategory;
	private String manufacturerCode;
	private String modelName;
	private String modelNumber;
	private ArrayList<FDB> fdb;
	
	
	TopologyObject() {
		
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getChassisID() {
		return chassisID;
	}
	public void setChassisID(String chassisID) {
		this.chassisID = chassisID;
	}
	public String getPortID() {
		return portID;
	}
	public void setPortID(String portID) {
		this.portID = portID;
	}
	public int getTtl() {
		return ttl;
	}
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}
	public String getPortDescription() {
		return portDescription;
	}
	public void setPortDescription(String portDescription) {
		this.portDescription = portDescription;
	}
	public String getDeviceCategory() {
		return deviceCategory;
	}
	public void setDeviceCategory(String deviceCategory) {
		this.deviceCategory = deviceCategory;
	}
	public String getManufacturerCode() {
		return manufacturerCode;
	}
	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getModelNumber() {
		return modelNumber;
	}
	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}
	public ArrayList<FDB> getFdb() {
		return fdb;
	}
	public void setFdb(ArrayList<FDB> fdb) {
		this.fdb = fdb;
	}
	
	public  String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}
