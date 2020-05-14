package com.neo.bltcarkey.callback;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.util.Log;

import com.neo.bltcarkey.listener.IBleStatus;
import com.neo.bltcarkey.common.Config;

import java.util.List;

/**
 * author : SenXia
 * time   : 2020/05/14
 * desc   : This is BleScannerCallBack
 * version: 1.0
 */
public class BleScannerCallBack extends ScanCallback {
    private String tag = getClass().getSimpleName();
    private IBleStatus mBleStatus;

    public BleScannerCallBack() {
        super();
    }

    public void onSetInterface(IBleStatus bleStatus) {
        mBleStatus = bleStatus;
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);
        Log.i(tag, "onScanResult, result = " + result.toString());
        if (mBleStatus != null) {
            mBleStatus.onUpdateScanData(result);
        }
    }

    @Override
    public void onBatchScanResults(List<ScanResult> results) {
        super.onBatchScanResults(results);
        Log.i(tag, "onBatchScanResults");
    }

    @Override
    public void onScanFailed(int errorCode) {
        super.onScanFailed(errorCode);
        Log.i(tag, "onScanFailed");
        if (mBleStatus != null) {
            mBleStatus.onUpdateScanStatus(Config.BLE_STATUS_ERROR_SCAN);
        }
    }
}
