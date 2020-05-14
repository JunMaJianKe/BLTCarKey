package com.neo.bltcarkey.listener;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;

/**
 * author : SenXia
 * time   : 2020/05/13
 * desc   : This is IBleStatus
 * version: 1.0
 */
public interface IBleStatus {
    void onUpdateScanStatus(int status);

    void onUpdateScanData(ScanResult result);

    void onUpdateConnectStatus(BluetoothDevice device, int status);
}
