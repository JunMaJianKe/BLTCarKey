package com.neo.bltcarkey;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.neo.bltcarkey.callback.BleGattCallback;
import com.neo.bltcarkey.callback.BleScannerCallBack;
import com.neo.bltcarkey.common.Commons;
import com.neo.bltcarkey.listener.BleReceiveListener;
import com.neo.bltcarkey.listener.IBleStatus;
import com.neo.bltcarkey.listener.IBleConnectListener;
import com.neo.bltcarkey.common.Config;

/**
 * author : SenXia
 * time   : 2020/05/13
 * desc   : This is BleManagerService
 * version: 1.0
 */
public class BleManagerService extends Service implements IBleConnectListener {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BleScannerCallBack mScannerCallBack;
    private BleGattCallback mBleGattCallback;
    private Activity mActivity;
    private IBleStatus mBlStatus;
    private BleReceiveListener mBleReceiver;
    private LocalBinder mLocalBinder;

    private BluetoothGatt mGatt;
    private BluetoothGattService mGattService;
    private BluetoothGattCharacteristic mGattCharacteristic;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.MESSAGE_WHAT_READ_RSSI:
                    if (mGattService != null && mGatt != null) {
                        mGatt.readRemoteRssi();
                        stopReadRssi();
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mScannerCallBack = new BleScannerCallBack();
        mBleGattCallback = new BleGattCallback(this);
    }

    public void setBleStatusListener(Activity activity, IBleStatus bleStatus) {
        mActivity = activity;
        mBlStatus = bleStatus;
        mScannerCallBack.onSetInterface(mBlStatus);
    }

    public void setBleControlListener(BleReceiveListener receiveListener) {
        mBleReceiver = receiveListener;
    }

    @Override
    public IBinder onBind(Intent intent) {
        mLocalBinder = new LocalBinder();
        return mLocalBinder;
    }

    @Override
    public void onUpdateConnectStatus(BluetoothDevice device, int status) {
        if (mBlStatus != null) {
            mBlStatus.onUpdateConnectStatus(device, status);
        }
        if (status != Config.CONNECT_BLE_DISCONNECT) {
            if (mBleReceiver != null) {
                mBleReceiver.onBleDisconnect();
                mGatt = null;
                mGattService = null;
            }
            stopReadRssi();
        }
    }

    @Override
    public void setGatt(BluetoothGatt gatt) {
        mGatt = gatt;
        mGattService = gatt.getService(Commons.BLE_SERVICE_UUID);
        //获取指定uuid的Characteristic
        mGattCharacteristic = mGattService.getCharacteristic(Commons.BLE_CHARACTERISTIC_UUID);

        startReadRssi();

    }

    public class LocalBinder extends Binder {

        public int getBlStatus() {
            if (mBluetoothAdapter != null) {
                if (mBluetoothAdapter.isEnabled()) {
                    return Config.QUERY_BL_STATUS_ON;
                }
            }
            return Config.QUERY_BL_STATUS_OFF;
        }

        public int queryBl() {
            if (mActivity != null && mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                mActivity.startActivityForResult(enableBtIntent, 1);
                return 1;
            } else {
                return 0;
            }
        }

        public void scanBl() {
            if (mBluetoothLeScanner != null && mScannerCallBack != null) {
                mBluetoothLeScanner.startScan(null, getScanSettings(), mScannerCallBack);
                if (mBlStatus != null) {
                    mBlStatus.onUpdateScanStatus(Config.BLE_STATUS_SCANING);
                }
            }
        }

        public void cancleScanBl() {
            if (mBluetoothLeScanner != null && mScannerCallBack != null) {
                mBluetoothLeScanner.stopScan(mScannerCallBack);
            }
            if (mBlStatus != null) {
                mBlStatus.onUpdateScanStatus(Config.BLE_STATUS_NO_SCAN);
            }
        }

        public void connectBle(BluetoothDevice device) {
            device.connectGatt(getApplicationContext(), false, mBleGattCallback);
        }

        public void writeByte(byte[] bytes) {
//获取特定特征成功
            if (mGattCharacteristic != null) {
                //写入你需要传递给外设的特征值（即传递给外设的信息）
                mGattCharacteristic.setValue(bytes);
                //通过GATt实体类将，特征值写入到外设中。
//                gatt.writeCharacteristic(gattCharacteristic);

                //如果只是需要读取外设的特征值：
                //通过Gatt对象读取特定特征（Characteristic）的特征值
//                gatt.readCharacteristic(gattCharacteristic);
            }
        }

        public void activeInterrupt() {
            if (mGatt != null) {
                mGatt.disconnect();
            }
        }

        BleManagerService getService() {
            return BleManagerService.this;
        }
    }

    private ScanSettings getScanSettings() {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
//                .setCallbackType(ScanSettings.CALLBACK_TYPE_MATCH_LOST)
//                .setScanMode(ScanSettings.MATCH_MODE_STICKY)
        ;
        //builder.setReportDelay(100);//设置延迟返回时间
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            builder.setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES);
            if (ActivityCompat.checkSelfPermission(mActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            if (ActivityCompat.checkSelfPermission(mActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        return builder.build();
    }

    private void startReadRssi() {
        stopReadRssi();
        mHandler.sendEmptyMessageDelayed(Config.MESSAGE_WHAT_READ_RSSI, 1000);
    }

    private void stopReadRssi() {
        if (mHandler.hasMessages(Config.MESSAGE_WHAT_READ_RSSI)) {
            mHandler.removeMessages(Config.MESSAGE_WHAT_READ_RSSI);
        }
    }

}
