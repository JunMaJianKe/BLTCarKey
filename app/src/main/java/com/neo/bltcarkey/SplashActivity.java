package com.neo.bltcarkey;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.neo.bltcarkey.adapter.BleAdapter;
import com.neo.bltcarkey.listener.IBleStatus;
import com.neo.bltcarkey.common.Config;
import com.neo.bltcarkey.common.Utils;

/**
 * author : SenXia
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
    private TextView mConnectlStatus;
    private ListView mListView;
    private BleAdapter mBleAdapter;

    private boolean mItemCanClick;

    private int mCurrentBlStatus;
    private int mCurrentBleScanStatus;

    private ServiceConnection mServiceConnection;
    private BleManagerService.LocalBinder mLocalBinder;

    private static final String SCAN_RESULT_KEY = "scan_result_key";
    private static final int MESSAGE_WHAT_CANCLE_SCAN_BLE = 1;
    private static final int MESSAGE_WHAT_SCAN_BLE_RESULT = 2;
    private static final int MESSAGE_DELAY = 5000;
    int i = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_WHAT_CANCLE_SCAN_BLE:
                    if (mLocalBinder != null) {
                        mLocalBinder.cancleScanBl();
                    }
                    break;
                case MESSAGE_WHAT_SCAN_BLE_RESULT:
                    Bundle bundle = msg.getData();
                    ScanResult result = bundle.getParcelable(SCAN_RESULT_KEY);
                    mBleAdapter.addData(result);
                    i++;
                    break;
            }

        }
    };

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
        } else if (requestCode == 2) {

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
                    Toast.makeText(this, getResources().getString(R.string.bl_null),
                            Toast.LENGTH_SHORT).show();
                } else {
                    mCurrentBlStatus = Config.QUERY_BL_STATUS_QUERYING;
                }
                updateBleStatus();
                break;
            case R.id.scan_ble:
                switch (mCurrentBleScanStatus) {
                    case Config.BLE_STATUS_NO_SCAN:
                        mLocalBinder.scanBl();
                        if (mHandler.hasMessages(MESSAGE_WHAT_CANCLE_SCAN_BLE)) {
                            mHandler.removeMessages(MESSAGE_WHAT_CANCLE_SCAN_BLE);
                        }
                        mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT_CANCLE_SCAN_BLE,
                                MESSAGE_DELAY);
                        break;
                    case Config.BLE_STATUS_SCANING:
                        if (mHandler.hasMessages(MESSAGE_WHAT_CANCLE_SCAN_BLE)) {
                            mHandler.removeMessages(MESSAGE_WHAT_CANCLE_SCAN_BLE);
                        }
                        mHandler.sendEmptyMessage(MESSAGE_WHAT_CANCLE_SCAN_BLE);
                        break;
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mItemCanClick) {
            mItemCanClick = false;
            ScanResult result = mBleAdapter.getItem(i);
            BluetoothDevice devices = result.getDevice();
            mLocalBinder.connectBle(devices);
        }
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
        mBleAdapter = new BleAdapter(this);
        mListView.setAdapter(mBleAdapter);
        mConnectlStatus = findViewById(R.id.connect_ble_status);
    }

    private void resetView() {
        mCurrentBlStatus = Config.QUERY_BL_STATUS_OFF;
        updateBleStatus();
        mConnectlStatus.setText(R.string.bl_connect_no);
        mBleAdapter.resetData();
        mBleAdapter.notifyDataSetChanged();
        mItemCanClick = true;
    }

    private void initBlManager() {
        mCurrentBlStatus = Config.QUERY_BL_STATUS_OFF;
        mCurrentBleScanStatus = Config.BLE_STATUS_NO_SCAN;
        Intent intent = new Intent(this, BleManagerService.class);
        startService(intent);
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mLocalBinder = (BleManagerService.LocalBinder) service;
                BleManagerService mLocalService = mLocalBinder.getService();
                mLocalService.setBleStatusListener(SplashActivity.this, SplashActivity.this);
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

    @Override
    public void onUpdateScanStatus(int status) {
        switch (status) {
            case Config.BLE_STATUS_NO_SCAN:
                mCurrentBleScanStatus = Config.BLE_STATUS_NO_SCAN;
                mScanBlStatus.setText(R.string.bl_scan_stop);
                Log.i(tag, "update.count = " + mBleAdapter.getCount());
                Log.i(tag, "update.i = " + i);
                mBleAdapter.notifyDataSetChanged();
                break;
            case Config.BLE_STATUS_SCANING:
                mCurrentBleScanStatus = Config.BLE_STATUS_SCANING;
                mScanBlStatus.setText(R.string.bl_scanning);
                break;
            case Config.BLE_STATUS_ERROR_SCAN:
                mCurrentBleScanStatus = Config.BLE_STATUS_NO_SCAN;
                mScanBlStatus.setText(R.string.bl_scan_stop);
                mBleAdapter.resetData();
                mBleAdapter.notifyDataSetChanged();
                Utils.showToast(this, R.string.bl_scanning_stop);
                break;
        }
    }

    @Override
    public void onUpdateScanData(ScanResult result) {
        Message message = Message.obtain();
        message.what = MESSAGE_WHAT_SCAN_BLE_RESULT;
        Bundle bundle = new Bundle();
        bundle.putParcelable(SCAN_RESULT_KEY, result);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    @Override
    public void onUpdateConnectStatus(BluetoothDevice device, int status) {
        mItemCanClick = true;
        if (status == Config.CONNECT_BLE_SUCCESS) {
            mConnectlStatus.setText(R.string.bl_connect_success);
            Intent intent = new Intent(this, DigitalKeyActivity.class);
            startActivity(intent);
        } else if(status == Config.CONNECT_BLE_FAILED){
            mConnectlStatus.setText(R.string.bl_connect_failed);
        }else if(status == Config.CONNECT_BLE_DISCONNECT){
            mConnectlStatus.setText(R.string.bl_disconnect);
        }
    }
}
