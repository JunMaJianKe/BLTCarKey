package com.neo.bltcarkey;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.neo.bltcarkey.callback.IBleStatus;
import com.neo.bltcarkey.common.Config;
import com.neo.bltcarkey.common.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * author : SenXia
 * e-mail : Sen_Xia@human-horizons.com
 * time   : 2020/05/13
 * desc   : This is SplashActivity
 * version: 1.0
 */
public class SplashActivity extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener, IBleStatus {

    private String tag = getClass().getSimpleName();
    private Button mQueryBl;
    private Button mScanBle;
    private TextView mQueryBlStatus;
    private TextView mScanBlStatus;
    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;
    private List<String> list;

    private int mCurrentBlStatus;
    private int mCurrentBleScanStatus;

    private ServiceConnection mServiceConnection;
    private BleManagerService.LocalBinder mLocalBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        initView();
        initBlManager();
    }

    @Override
    protected void onStart() {
        Log.i(tag, "onstart");
        super.onStart();
        resetView();
        Intent bindIntent = new Intent(this.getApplicationContext(), BleManagerService.class);
        this.bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Utils.showToast(this, R.string.bl_on);
                mCurrentBlStatus = Config.QUERY_BL_STATUS_ON;
                updateBleStatus();
            } else if (resultCode == RESULT_CANCELED) {
                Utils.showToast(this, R.string.bl_off);
                mCurrentBlStatus = Config.QUERY_BL_STATUS_OFF;
                updateBleStatus();
            }
        }
    }

    @Override
    protected void onStop() {
        Log.i(tag, "onStop");
        super.onStop();
        this.unbindService(mServiceConnection);
    }

    @Override
    public void onClick(View view) {
        if (mLocalBinder == null) {
            Toast.makeText(this, getResources().getString(R.string.bl_null), Toast.LENGTH_SHORT).show();
            return;
        }
        switch (view.getId()) {
            case R.id.query_bl:
                int result = mLocalBinder.queryBl();
                if (result == 0) {
                    mCurrentBlStatus = Config.QUERY_BL_STATUS_OFF;
                    Toast.makeText(this, getResources().getString(R.string.bl_null), Toast.LENGTH_SHORT).show();
                } else {
                    mCurrentBlStatus = Config.QUERY_BL_STATUS_QUERYING;
                }
                updateBleStatus();
                break;
            case R.id.scan_ble:
                switch (mCurrentBleScanStatus) {
                    case Config.BLE_STATUS_NO_SCAN:
                        int scan_result = mLocalBinder.scanBl();
                        if (scan_result == 0) {
                            mCurrentBleScanStatus = Config.BLE_STATUS_NO_SCAN;
                            mScanBlStatus.setText(R.string.bl_scan_stop);
                            Toast.makeText(this, getResources().getString(R.string.bl_null), Toast.LENGTH_SHORT).show();
                        } else {
                            mCurrentBleScanStatus = Config.BLE_STATUS_SCANING;
                            mScanBlStatus.setText(R.string.bl_scanning);
                        }
                        break;
                    case Config.BLE_STATUS_SCANING:
//                        AlertDialog dialog = new AlertDialog.Builder(.setT;
                        break;
                }

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private void initView() {
        mQueryBl = findViewById(R.id.query_bl);
        mQueryBl.setOnClickListener(this);
        mScanBle = findViewById(R.id.scan_ble);
        mScanBle.setOnClickListener(this);
        mQueryBlStatus = findViewById(R.id.query_bl_status);
        mScanBlStatus = findViewById(R.id.scan_ble_status);
        mListView = findViewById(R.id.ble_list_view);
        mListView.setOnItemClickListener(this);

    }

    private void resetView() {
        mCurrentBlStatus = Config.QUERY_BL_STATUS_OFF;
        updateBleStatus();

        if (list != null) {
            list = null;
        }
        list = new ArrayList<String>();
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        mListView.setAdapter(mArrayAdapter);
    }

    private void initBlManager() {
        mCurrentBlStatus = Config.QUERY_BL_STATUS_OFF;
        mCurrentBleScanStatus = Config.BLE_STATUS_NO_SCAN;
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mLocalBinder = (BleManagerService.LocalBinder) service;
                BleManagerService mLocalService = mLocalBinder.getService();
                mLocalService.setBleStatusListener(SplashActivity.this);
                mCurrentBlStatus = mLocalBinder.getBlStatus();
                updateBleStatus();
                Log.d(tag, "LocalService is connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(tag, "LocalService is disconnected");
                mLocalBinder = null;

            }
        };
    }

    @Override
    public void onUpdateScanStatus(int status, ArrayList<String> list) {

    }

    private void updateBleStatus() {
        switch (mCurrentBlStatus) {
            case Config.QUERY_BL_STATUS_OFF:
                mQueryBl.setClickable(true);
                mQueryBlStatus.setText(R.string.bl_off);
                mCurrentBleScanStatus = Config.BLE_STATUS_NO_SCAN;
                mScanBle.setClickable(false);
                mScanBle.setText(R.string.bl_scan_stop);
                break;
            case Config.QUERY_BL_STATUS_QUERYING:
                mQueryBl.setClickable(false);
                mQueryBlStatus.setText(R.string.bl_querying);
                mCurrentBleScanStatus = Config.BLE_STATUS_NO_SCAN;
                mScanBle.setClickable(false);
                mScanBle.setText(R.string.bl_scan_stop);
                break;
            case Config.QUERY_BL_STATUS_ON:
                mQueryBl.setClickable(false);
                mQueryBlStatus.setText(R.string.bl_on);
                mScanBle.setClickable(true);
                mScanBle.setText(R.string.bl_scan_stop);
                break;
        }
    }

}
