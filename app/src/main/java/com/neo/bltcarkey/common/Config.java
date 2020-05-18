package com.neo.bltcarkey.common;

import java.util.UUID;

/**
 * author : SenXia
 * time   : 2020/05/13
 * desc   : This is Config
 * version: 1.0
 */
public class Config {

    //定义需要进行通信的ServiceUUID
    public static UUID BLE_SERVICE_UUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
    //定义需要进行通信的CharacteristicUUID
    public static UUID BLE_CHARACTERISTIC_UUID = UUID.fromString("00001803-0000-1000-8000-00805f9b34fb");


    public static final int READ_RSSI_DURATION = 1000;

    public static final String NORMAL_PE = "75";
    public static final String NORMAL_Pb = "90";

}
