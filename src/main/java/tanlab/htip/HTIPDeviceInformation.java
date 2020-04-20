package tanlab.htip;

public class HTIPDeviceInformation {
	private String deviceCategory;
	private String manufacturerCode;
	private String modelName;
	private String modelNumber;
	private int channelUsageInformation;
	private int radioSignalStrength;
	private int communicationErrorRate;
	private String status;
	private int transmissionInterval;
	private int responseTime;
	private int numberOfAssociatedDevice;
	private int numberOfActiveNode;
	private int linkQuality;
	private int numberOfRetransmission;
	private int cpuUsageRate;
	private int memoryUsageRate;
	private int hddUsageRate;
	private int remainingBatteryLevel;

	
	public HTIPDeviceInformation() {
	}
	
	public String getDeviceCategory() {
		return deviceCategory;
	}
	public void setDeviceCategory(String deviceCategory) {
		this.deviceCategory = deviceCategory.trim();
	}
	public String getManufacturerCode() {
		return manufacturerCode;
	}
	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode.trim();
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName.trim();
	}
	public String getModelNumber() {
		return modelNumber;
	}
	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber.trim();
	}
	public int getChannelUsageInformation() {
		return channelUsageInformation;
	}
	public void setChannelUsageInformation(int channelUsageInformation) {
		this.channelUsageInformation = channelUsageInformation;
	}
	public int getRadioSignalStrength() {
		return radioSignalStrength;
	}
	public void setRadioSignalStrength(int radioSignalStrength) {
		this.radioSignalStrength = radioSignalStrength;
	}
	public int getCommunicationErrorRate() {
		return communicationErrorRate;
	}
	public void setCommunicationErrorRate(int communicationErrorRate) {
		this.communicationErrorRate = communicationErrorRate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status.trim();
	}
	public int getTransmissionInterval() {
		return transmissionInterval;
	}
	public void setTransmissionInterval(int transmissionInterval) {
		this.transmissionInterval = transmissionInterval;
	}
	public int getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}
	public int getNumberOfAssociatedDevice() {
		return numberOfAssociatedDevice;
	}
	public void setNumberOfAssociatedDevice(int numberOfAssociatedDevice) {
		this.numberOfAssociatedDevice = numberOfAssociatedDevice;
	}
	public int getNumberOfActiveNode() {
		return numberOfActiveNode;
	}
	public void setNumberOfActiveNode(int numberOfActiveNode) {
		this.numberOfActiveNode = numberOfActiveNode;
	}
	public int getLinkQuality() {
		return linkQuality;
	}
	public void setLinkQuality(int linkQuality) {
		this.linkQuality = linkQuality;
	}
	public int getNumberOfRetransmission() {
		return numberOfRetransmission;
	}
	public void setNumberOfRetransmission(int numberOfRetransmission) {
		this.numberOfRetransmission = numberOfRetransmission;
	}
	public int getCpuUsageRate() {
		return cpuUsageRate;
	}
	public void setCpuUsageRate(int cpuUsageRate) {
		this.cpuUsageRate = cpuUsageRate;
	}
	public int getMemoryUsageRate() {
		return memoryUsageRate;
	}
	public void setMemoryUsageRate(int memoryUsageRate) {
		this.memoryUsageRate = memoryUsageRate;
	}
	public int getHddUsageRate() {
		return hddUsageRate;
	}
	public void setHddUsageRate(int hddUsageRate) {
		this.hddUsageRate = hddUsageRate;
	}
	public int getRemainingBatteryLevel() {
		return remainingBatteryLevel;
	}
	public void setRemainingBatteryLevel(int remainingBatteryLevel) {
		this.remainingBatteryLevel = remainingBatteryLevel;
	}

	@Override
	public String toString() {
		String rs = String.format("deviceCategory : %s manufacturerCode : %s "
								+ "modelName : %s modelNumber : %s "
								+ "channelUsageInformation : %d radioSignalStrength : %d "
								+ "communicationErrorRate : %d status : %s "
								+ "transmissionInterval : %d responseTime : %d "
								+ "numberOfAssociatedDevice : %d numberOfActiveNode : %d "
								+ "linkQuality : %d numberOfRetransmission : %d "
								+ "cpuUsageRate : %d memoryUsageRate : %d "
								+ "hddUsageRate : %d remainingBatteryLevel : %d ",
								getDeviceCategory(),getManufacturerCode(),
								getModelName(),getModelNumber(),
								getChannelUsageInformation(),getRadioSignalStrength(),
								getCommunicationErrorRate(),getStatus(),
								getTransmissionInterval(),getResponseTime(),
								getNumberOfAssociatedDevice(),getNumberOfActiveNode(),
								getLinkQuality(),getNumberOfRetransmission(),
								getCpuUsageRate(),getMemoryUsageRate(),
								getHddUsageRate(),getRemainingBatteryLevel());
		return rs;
	}
	
}
