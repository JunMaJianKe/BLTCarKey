package com.neo.bltcarkey.common;

/**
 * author : SenXia
 * e-mail : Sen_Xia@human-horizons.com
 * time   : 2020/05/13
 * desc   : This is Config
 * version: 1.0
 */
public class Config {

    public static final int STATUS_PB = 0x0000;
    public static final int STATUS_PE = 0x0001;
    public static final int STATUS_PS = 0x0002;

    public static final int CONTROL_POSITION_LEFT_FRONT = 0x0001;
    public static final int CONTROL_POSITION_RIGHT_FRONT = 0x0002;
    public static final int CONTROL_POSITION_LEFT_BACK = 0x0003;
    public static final int CONTROL_POSITION_RIGHT_BACK = 0x0004;

    public static final int BLE_STATUS_SCANING = 0x0001;
    public static final int BLE_STATUS_NO_SCAN = 0x0002;

    public static final int QUERY_BL_STATUS_OFF = 0x0001;
    public static final int QUERY_BL_STATUS_QUERYING = 0x0002;
    public static final int QUERY_BL_STATUS_ON = 0x0003;

}
