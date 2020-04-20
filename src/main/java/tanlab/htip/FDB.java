package tanlab.htip;

import java.util.ArrayList;

import com.google.gson.Gson;

public class FDB {
	private int port;
	private ArrayList<String> mac;
	
	public FDB() {
		
	}
	public FDB(int p, ArrayList<String> macs) {
		this.port = p;
		this.mac = macs;
		
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public ArrayList<String> getMacList() {
		return mac;
	}
	public void setMacList(ArrayList<String> macList) {
		this.mac = macList;
	}
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
