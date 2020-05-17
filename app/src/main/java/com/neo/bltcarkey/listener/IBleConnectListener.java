package com.neo.bltcarkey.listener;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;

/**
 * author : SenXia
 * time   : 2020/05/14
 * desc   : This is IBleConnectStatus
 * version: 1.0
 */
public interface IBleConnectListener {
    void onUpdateConnectStatus(BluetoothDevice device, int status);
    void setGatt(BluetoothGatt gatt);
    void setRssi(int rssi);
}
