package com.neo.bltcarkey;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.neo.bltcarkey.callback.IBleManager;
import com.neo.bltcarkey.callback.OperationFeedBack;

/**
 * author : SenXia
 * e-mail : Sen_Xia@human-horizons.com
 * time   : 2020/05/13
 * desc   : This is BleManager
 * version: 1.0
 */
public class BleManager implements IBleManager {
    private String tag = getClass().getSimpleName();
    private Activity mActivity;
    private Context mContext;
    private OperationFeedBack mBack;
    private ServiceConnection mServiceConnection;
    private BleManagerService.LocalBinder mLocalBinder;

    public BleManager(Activity activity, OperationFeedBack feedback) {
        mActivity = activity;
        mContext = activity;
        mBack = feedback;
        initBlManager();
    }

    @Override
    public void onQueryBl() {

    }

    @Override
    public void onControlWindows(int position, boolean toOpen) {

    }

    @Override
    public void onControlDoors(int position, boolean toOpen) {

    }

    @Override
    public void onControlLight() {

    }

    @Override
    public void onControlTrunk() {

    }

    @Override
    public void onControlHood() {

    }

    @Override
    public void onPressCarHorn() {

    }

    public void onDestory() {
        mBack = null;
        mContext.unbindService(mServiceConnection);
    }

    private void initBlManager() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mLocalBinder = (BleManagerService.LocalBinder) service;
                Log.d(tag, "LocalService is connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(tag, "LocalService is disconnected");
                mLocalBinder = null;

            }
        };
        Intent bindIntent = new Intent(mContext, BleManagerService.class);
        mContext.bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }
}
