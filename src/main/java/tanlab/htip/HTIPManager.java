package tanlab.htip;

import com.google.gson.Gson;

public class HTIPManager {
	private String managerMAC;
	private String managerPort;
	public HTIPManager() {
		
	}
	public HTIPManager(String port, String MAC) {
		this.managerMAC = MAC;
		this.managerPort = port;
	}
	public String getMAC() {
		return managerMAC;
	}
	public void setMAC(String mAC) {
		managerMAC = mAC;
	}
	public String getPort() {
		return managerPort;
	}
	public void setPort(String port) {
		this.managerPort = port;
	}
	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
