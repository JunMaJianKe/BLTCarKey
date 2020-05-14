package com.neo.bltcarkey.callback;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.neo.bltcarkey.common.Commons;
import com.neo.bltcarkey.common.Config;
import com.neo.bltcarkey.listener.IBleConnectListener;

import java.util.UUID;

/**
 * author : SenXia
 * time   : 2020/05/14
 * desc   : This is BleGattCallback
 * version: 1.0
 */
public class BleGattCallback extends BluetoothGattCallback {
    private String tag = getClass().getSimpleName();
    private IBleConnectListener mIBleConnectStatus;

    public BleGattCallback(IBleConnectListener connectStatus) {
        mIBleConnectStatus = connectStatus;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            //连接成功
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                Log.i("Bluetooth", "连接成功");
                mIBleConnectStatus.onUpdateConnectStatus(gatt.getDevice(), Config.CONNECT_BLE_SUCCESS);
            } else if(newState == BluetoothGatt.STATE_DISCONNECTED){
                Log.i("Bluetooth", "断开连接");
                gatt.close();
                mIBleConnectStatus.onUpdateConnectStatus(gatt.getDevice(), Config.CONNECT_BLE_DISCONNECT);
            }
        } else {
            //连接失败
            Log.w("Bluetooth", "失败==" + status);
            gatt.close();
            mIBleConnectStatus.onUpdateConnectStatus(gatt.getDevice(), Config.CONNECT_BLE_FAILED);
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            //获取指定uuid的service
            BluetoothGattService gattService = gatt.getService(Commons.BLE_SERVICE_UUID);
            //获取到特定的服务不为空
            if (gattService != null) {
                mIBleConnectStatus.setGatt(gatt);
            }
        } else {
            //获取特定服务失败
            mIBleConnectStatus.onUpdateConnectStatus(gatt.getDevice(), Config.CONNECT_BLE_DISCONNECT);
        }

    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            //获取读取到的特征值
            characteristic.getValue();
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            //获取写入到外设的特征值
            characteristic.getValue();
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
    }
}
