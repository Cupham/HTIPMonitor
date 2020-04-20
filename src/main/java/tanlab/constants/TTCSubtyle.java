package tanlab.constants;


public class TTCSubtyle {
	public static final int NOT_TTC_SUBTYLE = 0;
	public static final int DEVICE_INFORMATION = 1;
	public static final int LINK_INFORMATION = 2;
	public static final int MAC_ADDRESS_LIST = 3;
	public static final int EXTENSION_LINK_INFORMATION = 4;
	public static final int EXTENSION_MAC_ADDRESS_LIST = 5;
	public static final int SETTING_INFORMATION = 6;
	

	private static final String[] names = {"NotTTCSubtyle","DeviceInformation","LinkInformation","MacAddressList",
										   "ExtensionLinkInformation","ExtensionMacAddressList","SettingInformation"};
	public static final TTCSubtyle NotTTCSubtyle = new TTCSubtyle(NOT_TTC_SUBTYLE);
	public static final TTCSubtyle DeviceInformation = new TTCSubtyle(DEVICE_INFORMATION);
	public static final TTCSubtyle LinkInformation = new TTCSubtyle(LINK_INFORMATION);
	public static final TTCSubtyle MacAddressList = new TTCSubtyle(MAC_ADDRESS_LIST);
	public static final TTCSubtyle ExtensionLinkInformation = new TTCSubtyle(EXTENSION_LINK_INFORMATION);
	public static final TTCSubtyle ExtensionMacAddressList = new TTCSubtyle(EXTENSION_MAC_ADDRESS_LIST);
	public static final TTCSubtyle SettingInformation = new TTCSubtyle(SETTING_INFORMATION);

	private int order;

	private TTCSubtyle(int order) {
		this.order = order;
	}

	public static TTCSubtyle fromInt(int order) {
		switch (order) {
		case NOT_TTC_SUBTYLE:
			return NotTTCSubtyle;
		case DEVICE_INFORMATION:
			return DeviceInformation;
		case LINK_INFORMATION:
			return LinkInformation;
		case MAC_ADDRESS_LIST:
			return MacAddressList;
		case EXTENSION_LINK_INFORMATION:
			return ExtensionLinkInformation;
		case EXTENSION_MAC_ADDRESS_LIST:
			return ExtensionMacAddressList;
		case SETTING_INFORMATION:
			return SettingInformation;
		default:
			return null;
		}
	}

	public static final TTCSubtyle fromString(String name) {
		if (name == null)
			return null;

		for (int i = NOT_TTC_SUBTYLE; i <= SETTING_INFORMATION; i++)
			if (names[i].equalsIgnoreCase(name.trim()))
				return fromInt(i);
		return null;
	}
	public String name() {
		return names[order];
	}

	public int ord() {
		return order;
	}

}
