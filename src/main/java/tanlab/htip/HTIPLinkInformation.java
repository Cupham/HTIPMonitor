package tanlab.htip;

import java.util.ArrayList;

import tanlab.constants.PortInterfaceType;

public class HTIPLinkInformation {
	
	private PortInterfaceType interfaceType;
	private int portNumber;
	private ArrayList<String> macAddressList;
	
	public HTIPLinkInformation(){
	}
	public HTIPLinkInformation(PortInterfaceType interfaceType, 
							   int portNumber, ArrayList<String> macAddressList) {
		this.interfaceType = interfaceType;
		this.portNumber = portNumber;
		this.macAddressList = macAddressList;
	}

	
	public PortInterfaceType getInterfaceType() {
		return interfaceType;
	}
	public void setInterfaceType(PortInterfaceType interfaceTypeList) {
		this.interfaceType = interfaceTypeList;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumberList) {
		this.portNumber = portNumberList;
	}
	public ArrayList<String> getMacAddressList() {
		return macAddressList;
	}
	public void setMacAddressList(ArrayList<String> macAddressList) {
		this.macAddressList = macAddressList;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Interface Type: ");
		sb.append(interfaceType.name() + "\n");
		
		sb.append("Port Number: ");
		sb.append(portNumber + "\n");
		sb.append("MAC Address List\n");
		for (int i =0; i < this.macAddressList.size(); i ++) {
			sb.append(macAddressList.get(i) + "\n");
		}
		return sb.toString();
	}
	
	

}
