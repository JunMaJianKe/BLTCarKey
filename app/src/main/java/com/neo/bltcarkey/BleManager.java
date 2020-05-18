package com.neo.bltcarkey;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.neo.bltcarkey.listener.BleReceiveListener;
import com.neo.bltcarkey.listener.IBleManager;
import com.neo.bltcarkey.listener.OperationFeedBack;

/**
 * author : SenXia
 * time   : 2020/05/13
 * desc   : This is BleManager
 * version: 1.0
 */
public class BleManager implements IBleManager, BleReceiveListener {
    private String tag = getClass().getSimpleName();
    private Activity mActivity;
    private Context mContext;
    private OperationFeedBack mBack;
    private ServiceConnection mServiceConnection;
    private BleManagerService.LocalBinder mLocalBinder;
    private BleManagerService mLocalService;

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
        mLocalBinder.cancleReadRssi();
        mContext.unbindService(mServiceConnection);
    }

    private void initBlManager() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mLocalBinder = (BleManagerService.LocalBinder) service;
                mLocalService = mLocalBinder.getService();
                mLocalService.setBleControlListener(BleManager.this);
                Log.d(tag, "LocalService is connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(tag, "LocalService is disconnected");
                mLocalService.setBleControlListener(null);
                mLocalService = null;
                mLocalBinder = null;

            }
        };
        Intent bindIntent = new Intent(mContext, BleManagerService.class);
        mContext.bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onBleDisconnect() {
        if (mActivity != null) {
            mActivity.finish();
        }
    }

    @Override
    public void onUpdateRssi(int rssi) {
        if(mBack!=null){
            mBack.onUpdateRssi(rssi);
        }
    }
}
