package com.neo.bltcarkey;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.neo.bltcarkey.callback.IBleStatus;
import com.neo.bltcarkey.common.Config;

/**
 * author : SenXia
 * e-mail : Sen_Xia@human-horizons.com
 * time   : 2020/05/13
 * desc   : This is BleManagerService
 * version: 1.0
 */
public class BleManagerService extends Service {

    private BluetoothAdapter mBluetoothAdapter;
    private Activity mActivity;
    private IBleStatus mBlStatus;

    @Override
    public void onCreate() {
        super.onCreate();
        mBluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
    }

    public void setBleStatusListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
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

        public int scanBl() {
            if (mActivity != null && mBluetoothAdapter != null) {
//                if(){
//
//                }
            }
            return 0;
        }


        BleManagerService getService() {
            return BleManagerService.this;
        }
    }
}
