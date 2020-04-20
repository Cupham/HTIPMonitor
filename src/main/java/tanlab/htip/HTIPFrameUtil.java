package tanlab.htip;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;

import tanlab.constants.DeviceInformationID;
import tanlab.constants.FrameIndex;
import tanlab.constants.PortInterfaceType;
import tanlab.constants.TLVType;
import tanlab.constants.TTCSubtyle;

public class HTIPFrameUtil {
	
	public static String getMacAddressFromTLV(byte[] input) {
		
		String macAddr = MACFromByte(input);
		System.out.println("Mac Address: " + macAddr);
		return macAddr;	
	}
	public static String getPortIDFromTLV(byte[] input) {
		String portID = new String(Arrays.copyOfRange(input, 1, input.length));
		System.out.println("Port ID: " + portID);
		return portID;	
	}
	public static Short getTimeToLiveFromTLV(byte[] input) {
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.put(input[0]);
		bb.put(input[1]);
		short ttl = bb.getShort(0);
		System.out.println("Time To Live: " + ttl );
		return ttl;	
	}
	public static String getPortDescriptionFromTLV(byte[] input) {
		String portDescription = new String(input);
		System.out.println("Port Description: " + portDescription);
		return 	portDescription;
	}
	public static HTIPLinkInformation getLinkInformationFromHTIPFrame(byte[] frame) {
		
		// Get Port Interface Types
		int startIndex = 4;
		int interfaceTypeLen = frame[startIndex];
		int interfaceInt = integerFromByteArray(Arrays.copyOfRange(frame, startIndex+1, startIndex + 1 +interfaceTypeLen));
		PortInterfaceType interfaceType = PortInterfaceType.fromInt(interfaceInt);
		
		// Get Port Numbers 
		startIndex = startIndex + interfaceTypeLen + 1;
		int portNumberLen = frame[startIndex];
		int portNums = integerFromByteArray(Arrays.copyOfRange(frame, startIndex+1, startIndex + 1 +portNumberLen));	
		// Get MAC Addresses	
		startIndex = startIndex + portNumberLen + 1;
		int macLen = frame[startIndex];
		byte[] lk = Arrays.copyOfRange(frame, startIndex+1, startIndex + macLen*6 + 1);
		ArrayList<String> MACAddr = MACsFromByte(lk, macLen);
		return new HTIPLinkInformation(interfaceType, portNums, MACAddr);
		
	}
	public static ArrayList<String> getMacListFromHTIPFrame(byte[] frame) {
		
		// Get Port Interface Types
		ArrayList<String> rs = new ArrayList<String>();

		int numberOfMac = frame[4];
		int startIndex = 5;
		for(int i = 0; i< numberOfMac; i++) {
			//getMac
			int len = frame[startIndex];
			String mac = MACFromByte(Arrays.copyOfRange(frame, startIndex + 1, startIndex + len));
			rs.add(mac);
			startIndex = startIndex + len + 1;
		}
		if(rs.size() > 0) {
			return rs;
		} else  {
			return null;
		}
	}

	public static boolean isTTCOUI(byte b1, byte b2, byte b3) {
		boolean rs;
		if(b1 == (byte) 0xE0 && b2 == (byte) 0x27 && b3 == (byte) 0x1A) {
			rs = true;
		} else {
			rs = false;
		}
		return rs;	
	}
	public static HTIPObject htipFromPacket(Timestamp timeStamp, Packet pkg) {
 		byte[] payload = pkg.get(EthernetPacket.class).getPayload().getRawData();
 		
 		HTIPObject htipOBJ = new HTIPObject();
 		htipOBJ.setFrameLength(pkg.length());
 		htipOBJ.setRawFrame(pkg.getRawData());
 		HTIPDeviceInformation deviceInfor = new HTIPDeviceInformation();
 		ArrayList<HTIPLinkInformation> linkInforList = new ArrayList<HTIPLinkInformation>();
 		ArrayList<String> macList = new ArrayList<String>();
 		int i = 0;
 		htipOBJ.setTimeStamp(timeStamp);
 		while(i < payload.length) {	
	 		if(payload[i] == TLVType.END_OF_FRAME_1 || payload[i] == TLVType.END_OF_FRAME_2) {
	 			System.out.println("end of frame");
	 			break;
	 		} else if(payload[i] == TLVType.DEVICE_ID) {
				int dataFrameLastPosition= getDataFramePosition(payload,i);				
				String equipmentID = HTIPFrameUtil.getMacAddressFromTLV(dataFromFrame(payload, i, dataFrameLastPosition));		
				htipOBJ.setEquipmentID(equipmentID);
				i = dataFrameLastPosition;
	 		} else if (payload[i] == TLVType.PORT_ID) {
	 			int dataFrameLastPosition= getDataFramePosition(payload,i);
	 			String portID = HTIPFrameUtil.getPortIDFromTLV(dataFromFrame(payload, i, dataFrameLastPosition));		
				htipOBJ.setPortID(portID);
	 			i = dataFrameLastPosition;
	 		} else if (payload[i] == TLVType.TIME_TO_LIVE) {
				int dataFrameLastPosition= getDataFramePosition(payload,i);
				int timeToLive = HTIPFrameUtil.getTimeToLiveFromTLV(dataFromFrame(payload, i, dataFrameLastPosition));		
				htipOBJ.setTimeToLive(timeToLive);
				i = dataFrameLastPosition;
			} else if (payload[i] == TLVType.PORT_DESCRIPTION) {
				int dataFrameLastPosition= getDataFramePosition(payload,i);
				String portDescription = HTIPFrameUtil.getPortDescriptionFromTLV(dataFromFrame(payload, i, dataFrameLastPosition));		
				htipOBJ.setPortDescription(portDescription);
				i = dataFrameLastPosition;
				System.out.println(i);
			} else if ((payload[i]&0xFF) == TLVType.DEVICE_INFORMATION) {
				
				// get the payload length
				int dataFrameLastPosition= getDataFramePosition(payload,i);
				// copy the payload
				byte[] htipFrame = dataFromFrame(payload, i, dataFrameLastPosition);
				i = dataFrameLastPosition;
				if(HTIPFrameUtil.isTTCOUI((htipFrame[FrameIndex.OUI_1]),htipFrame[FrameIndex.OUI_2],
											htipFrame[FrameIndex.OUI_3])) {
					switch (htipFrame[FrameIndex.TTC_SUBTYLE]) {
					case TTCSubtyle.DEVICE_INFORMATION:
						//=====
						switch (htipFrame[FrameIndex.DEVICE_INFORMATION_ID]) { 
						case (byte) DeviceInformationID.DEVICE_CATEGORY :
							String deviceCategory = getDeviceCategoryFromTLV(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setDeviceCategory(deviceCategory);
							break;
						case (byte) DeviceInformationID.MANUFACTURER_CODE :
							String manufacturerCode = getManufactuterCodeFromTLV(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setManufacturerCode(manufacturerCode.trim());
							break;
						case (byte) DeviceInformationID.MODEL_NAME :
							String modelName = getModelNameFromTLV(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setModelName(modelName);
							break;
						case (byte) DeviceInformationID.MODEL_NUMBER :
							String modelNumber = getModelNumberFromTLV(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setModelNumber(modelNumber);
							break;
						case (byte) DeviceInformationID.CHANNEL_USAGE_INFORMATION  :
							int channelUsageInformation = getChannelUsageInfor(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setChannelUsageInformation(channelUsageInformation);
							break;
						case (byte) DeviceInformationID.RADIO_SIGNAL_STRENGTH:
							int rssi = getRSSI(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setRadioSignalStrength(rssi);
							break;
						case (byte) DeviceInformationID.COMMUNICATION_ERROR_RATE_INFORMATION :
							int communicationErrorRate = getErrorRateInfor(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setCommunicationErrorRate(communicationErrorRate);
							break;
						case (byte) DeviceInformationID.RESPONSE_TIME :
							int responseTime = getResponseTime(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setResponseTime(responseTime);
							break;
						case (byte) DeviceInformationID.NUMBER_OF_ASSOCIATED_DEVICE :
							int numberOfAssociatedDevice = getNumberOfAssociatedDevice(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setNumberOfAssociatedDevice(numberOfAssociatedDevice);
							break;
						case (byte) DeviceInformationID.NUMBER_OF_ACTIVE_NODES :
							int numberOfActiveNode = getNumberOfActiveNode(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setNumberOfActiveNode(numberOfActiveNode);
							break;
						case (byte) DeviceInformationID.LINK_QUALITY :
							int linkQuality = getLinkQuality(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setLinkQuality(linkQuality);
							break;
						case (byte) DeviceInformationID.NUMBER_OF_RETRANSMISSION :
							int numberOfRetransmission = getNumberOfRetransmission(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setNumberOfRetransmission(numberOfRetransmission);
							break;
						case (byte) DeviceInformationID.STATUS_INFORMATION :
							String status = getStatusInformation(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setStatus(status);
							break;
						case (byte) DeviceInformationID.CPU_USAGE_RATE :
							int cpuUsageRate = getCPUUsangeRate(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setCpuUsageRate(cpuUsageRate);
							break;
						case (byte) DeviceInformationID.MEMORY_USAGE_RATE :
							int memoryUsageRate = getMemoryUsangeRate(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setMemoryUsageRate(memoryUsageRate);
							break;
						case (byte) DeviceInformationID.HDD_USAGE_RATE :
							int hddUsageRate = getHDDUsangeRate(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setHddUsageRate(hddUsageRate);
							break;
						case (byte) DeviceInformationID.REMAINING_BATTERY_LEVEL :
							int remainingBatteryLevel = getRemainingBatteryLevel(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setRemainingBatteryLevel(remainingBatteryLevel);
							break;
						case (byte) DeviceInformationID.LLDPDU_TRANSMISSION_INTERVAL :
							int transmissionInterval = getTransmissionInterval(Arrays.copyOfRange(htipFrame, 6, htipFrame.length));
							deviceInfor.setTransmissionInterval(transmissionInterval);
							break;
						case (byte) DeviceInformationID.VENDOR_SPECIFIC :
							System.out.println("Vendor Property...");
							break;
						default :
							System.out.println("Unknow Property...");
						break;	
					}
						
						//=====
						break;
					case TTCSubtyle.LINK_INFORMATION:
						linkInforList.add(HTIPFrameUtil.getLinkInformationFromHTIPFrame(htipFrame));
						break;
					case TTCSubtyle.MAC_ADDRESS_LIST:
						macList = getMacListFromHTIPFrame(htipFrame);
					default:
						
					}
				} else {
					System.out.println("It is not TTC OUI");
				}
				// update index
				
				
			}else {
	 			System.out.println("Unknown");
	 			
	 			//update index
	 			i = getDataFramePosition(payload,i) +1;
	 		}
 		}
 		if (deviceInfor != null) {
 			htipOBJ.setHtipDeviceInformation(deviceInfor);
 		}
 		if(linkInforList.size() > 0) {
 			htipOBJ.setHtipLinkInformation(linkInforList);
 			htipOBJ.setContainLinkInformation(true);
 		}	
 		if(macList != null) {
 			htipOBJ.setMacAddressList(null);
 		}
 		return htipOBJ;	
	}

	//-------------------------Private Functions--------------------------------------
	private static ArrayList<String> MACsFromByte(byte[] input, int numberOfMAC) {
		StringBuilder sb = new StringBuilder();
		ArrayList<String> rs = new ArrayList<String>();
		int i = 0;
		while (i < numberOfMAC*6) {
			if(i % 6 ==5) {
				sb.append(String.format("%02x", input[i]));
				rs.add(sb.toString());
				System.out.println("MAC: " + sb.toString());
				sb = new StringBuilder();
				
			} else {
				sb.append(String.format("%02x:", input[i]));
			}
			
			i++;
		}
		return rs;
		
	}
	private static String MACFromByte(byte[] input) {
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < input.length;i++)
			if(i == input.length-1) {
				sb.append(String.format("%02x", input[i]));
			} else {
				sb.append(String.format("%02x:", input[i]));
			}	
		return sb.toString();
		
	}
	
	
	private static  int getDataFramePosition(byte[] payload, int currentIndex) {
		int theLastBit = ((byte) payload[currentIndex] >> 0) & 1;
		int lastPos = currentIndex;
		if(theLastBit == 0) {
			lastPos = currentIndex + payload[currentIndex + 1] + 2;
		} else {
			lastPos = currentIndex + payload[currentIndex + 1] + 258;
		}
		return lastPos;
	}
	private static  byte[] dataFromFrame(byte[] payload, int i, int lastposition) {
		return Arrays.copyOfRange(payload, i + 2, lastposition);
	}
	
	
	private static int integerFromByteArray(byte[] input) {
		int rs = new BigInteger(input).intValue();
		return rs;
	}
	private static String getDeviceCategoryFromTLV(byte[] input) {
		String deviceCategory = new String(input);
		return deviceCategory;	
	}
	private static String getManufactuterCodeFromTLV(byte[] input) {
		String val = new String(input);
		return val;	
	}
	private static String getModelNameFromTLV(byte[] input) {
		String val = new String(input);
		return val;	
	}
	private static String getModelNumberFromTLV(byte[] input) {
		String val = new String(input);
		return val;	
	}
	private static int getChannelUsageInfor(byte[] input) {
		int channelUsageInfor = new BigInteger(input).intValue();
		return channelUsageInfor;	
	}
	private static int getRSSI(byte[] input) {
		int val = new BigInteger(input).intValue();
		return val;	
	}
	private static int getErrorRateInfor(byte[] input) {
		int val = new BigInteger(input).intValue();
		return val;	
	}
	private static int getResponseTime(byte[] input) {
		int val = new BigInteger(input).intValue();
		return val;	
	}
	private static int getNumberOfAssociatedDevice(byte[] input) {
		int val = new BigInteger(input).intValue();
		return val;	
	}
	private static int getNumberOfActiveNode(byte[] input) {
		int val = new BigInteger(input).intValue();
		return val;	
	}
	private static int getLinkQuality(byte[] input) {
		int val = new BigInteger(input).intValue();
		return val;	
	}
	private static int getNumberOfRetransmission(byte[] input) {
		int val = new BigInteger(input).intValue();
		return val;	
	}
	private static String getStatusInformation(byte[] input) {
		String val = new String(input);
		return val;	
	}
	private static int getCPUUsangeRate(byte[] input) {
		int val = new BigInteger(input).intValue();
		return val;	
	}
	private static int getMemoryUsangeRate(byte[] input) {
		int val = new BigInteger(input).intValue();
		return val;	
	}
	private static int getHDDUsangeRate(byte[] input) {
		int val = new BigInteger(input).intValue();
		return val;	
	}
	private static int getRemainingBatteryLevel(byte[] input) {
		int val = new BigInteger(input).intValue();
		return val;	
	}
	private static Short getTransmissionInterval(byte[] input) {
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.put(input[0]);
		bb.put(input[1]);
		short ttl = bb.getShort(0);
		return ttl;	
	}


}
