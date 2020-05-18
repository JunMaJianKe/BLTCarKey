package com.neo.bltcarkey.common;

public class Commons {

    public static final int STATUS_PB = 0x0000;
    public static final int STATUS_PE = 0x0001;
    public static final int STATUS_PS = 0x0002;
    public static final int STATUS_DEFAULT = 0x0003;

    public static final int CONTROL_POSITION_LEFT_FRONT = 0x0001;
    public static final int CONTROL_POSITION_RIGHT_FRONT = 0x0002;
    public static final int CONTROL_POSITION_LEFT_BACK = 0x0003;
    public static final int CONTROL_POSITION_RIGHT_BACK = 0x0004;

    public static final int BLE_STATUS_SCANING = 0x0001;
    public static final int BLE_STATUS_NO_SCAN = 0x0002;
    public static final int BLE_STATUS_ERROR_SCAN = 0x0003;

    public static final int QUERY_BL_STATUS_OFF = 0x0001;
    public static final int QUERY_BL_STATUS_QUERYING = 0x0002;
    public static final int QUERY_BL_STATUS_ON = 0x0003;

    public static final int CONNECT_BLE_SUCCESS = 0x0001;
    public static final int CONNECT_BLE_DISCONNECT = 0x0001;
    public static final int CONNECT_BLE_FAILED = 0x0002;

    public static final int MESSAGE_WHAT_READ_RSSI = 0x0001;

}
