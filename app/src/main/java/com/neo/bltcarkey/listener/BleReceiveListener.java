package com.neo.bltcarkey.listener;

public interface BleReceiveListener {
    void onBleDisconnect();
    void onUpdateRssi(int rssi);
}
