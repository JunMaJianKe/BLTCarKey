package com.neo.bltcarkey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.neo.bltcarkey.common.Commons;
import com.neo.bltcarkey.common.Config;
import com.neo.bltcarkey.listener.OperationFeedBack;
import com.neo.bltcarkey.common.Utils;
import com.neo.bltcarkey.widget.BleKeyLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * author : SenXia
 * time   : 2020/01/08
 * desc   : This is DigitalKeyActivity
 * version: 1.0
 */
public class DigitalKeyActivity extends Activity implements OperationFeedBack,
        View.OnClickListener {

    private BleKeyLayout mBleKeyLayout;
    private Button mLeftFrontDoor;
    private Button mRightFrontDoor;
    private Button mLeftBackDoor;
    private Button mRightBackDoor;
    private Button mLeftFrontWindow;
    private Button mRightFrontWindow;
    private Button mLeftBackWindow;
    private Button mRightBackWindow;

    private Button mCarHood;
    private Button mCarTruck;
    private Button mCarHorn;
    private Button mCarLight;
    private Button mSwithcRssi;

    private TextView mTvRssi;
    private TextView mAverageRssi;
    private EditText mEtvPb;
    private EditText mEtvPe;
    private Button pbtn;
    private int mCurrentPe;
    private int mCurrentPb;

    private boolean isAveRssi;
    private List<Integer> mRssiArray;

    private BleManager mBleManager;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int rssi = msg.arg1;
                mRssiArray.add(rssi);
                while (mRssiArray.size() > 10) {
                    mRssiArray.remove(0);
                }
                int currentRssi = 0;
                if (mRssiArray.size() > 3) {
                    int max = 0;
                    int min = 0;
                    int all = 0;
                    for (int i : mRssiArray) {
                        if (i > max) {
                            max = i;
                        }
                        if (i < min) {
                            min = i;
                        }
                        all += i;
                    }
                    int current = all - max - min;
                    currentRssi = current / (mRssiArray.size() - 2);
                } else {
                    currentRssi = rssi;
                }
                mTvRssi.setText("rssi:" + rssi);
                mAverageRssi.setText("average rssi:" + currentRssi);
                if (mBleKeyLayout == null) {
                    return;
                }
                int AbsRssi;
                if (isAveRssi) {
                    AbsRssi = Math.abs(currentRssi);
                } else {
                    AbsRssi = Math.abs(rssi);
                }

                if (AbsRssi <= mCurrentPe) {
                    mBleKeyLayout.setStatus(Commons.STATUS_PS);
                } else if (AbsRssi > mCurrentPe && AbsRssi <= mCurrentPb) {
                    mBleKeyLayout.setStatus(Commons.STATUS_PE);
                } else if (AbsRssi > mCurrentPb) {
                    mBleKeyLayout.setStatus(Commons.STATUS_PB);
                }
            } else if (msg.what == 2) {

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_key_layout);
        initView();
        initBle();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mBleKeyLayout.setStatus(Commons.STATUS_PB);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        if (mBleManager == null) {
            Utils.showToast(this, R.string.bl_null);
            return;
        }
        switch (view.getId()) {
            case R.id.leftfront_door:
                mBleManager.onControlDoors(Commons.CONTROL_POSITION_LEFT_FRONT, true);
                break;
            case R.id.rightfront_door:
                mBleManager.onControlDoors(Commons.CONTROL_POSITION_RIGHT_FRONT, true);
                break;
            case R.id.leftback_door:
                mBleManager.onControlDoors(Commons.CONTROL_POSITION_LEFT_BACK, true);
                break;
            case R.id.rightback_door:
                mBleManager.onControlDoors(Commons.CONTROL_POSITION_RIGHT_BACK, true);
                break;
            case R.id.leftfront_window:
                mBleManager.onControlWindows(Commons.CONTROL_POSITION_LEFT_FRONT, true);
                break;
            case R.id.rightfront_window:
                mBleManager.onControlWindows(Commons.CONTROL_POSITION_RIGHT_FRONT, true);
                break;
            case R.id.leftback_window:
                mBleManager.onControlWindows(Commons.CONTROL_POSITION_LEFT_BACK, true);
                break;
            case R.id.rightback_window:
                mBleManager.onControlWindows(Commons.CONTROL_POSITION_RIGHT_BACK, true);
                break;
            case R.id.car_hood:
                mBleManager.onControlHood();
                break;
            case R.id.car_trunk:
                mBleManager.onControlTrunk();
                break;
            case R.id.car_horn:
                mBleManager.onPressCarHorn();
                break;
            case R.id.car_light:
                mBleManager.onControlLight();
                break;
            case R.id.btn_p:
                String pe = mEtvPe.getText().toString();
                String pb = mEtvPb.getText().toString();
                if (pe == null || pe.equals("")) {
                    Utils.showToast(this, R.string.error_pe_null);
                    return;
                }
                if (pb == null || pb.equals("")) {
                    Utils.showToast(this, R.string.error_pb_null);
                    return;
                }
                int pei = Integer.valueOf(pe);
                int pbi = Integer.valueOf(pb);
                if (pei >= pbi) {
                    Utils.showToast(this, R.string.error_pe_more_than_pb);
                } else {
                    mCurrentPe = pei;
                    mCurrentPb = pbi;
                }
                break;
            case R.id.switch_rssi:
                if (isAveRssi) {
                    isAveRssi = false;
                    mSwithcRssi.setText(getResources().getText(R.string.use_ave_rssi));
                    mTvRssi.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
                    mAverageRssi.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                } else {
                    isAveRssi = true;
                    mSwithcRssi.setText(getResources().getText(R.string.use_rssi));
                    mAverageRssi.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
                    mTvRssi.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBleManager != null) {
            mBleManager.onDestory();
            mBleManager = null;
        }
    }

    @Override
    public void onUpdateWindowStatus(int position, int status) {

    }

    @Override
    public void onUpdateDoorStatusin(int position, int status) {

    }

    @Override
    public void onUpdateKeyDistance(int status) {

    }

    @Override
    public void onBLeScan(int status, List<String> list) {

    }

    @Override
    public void onUpdateRssi(int rssi) {
        Message msg = Message.obtain();
        msg.what = 1;
        msg.arg1 = rssi;
        mHandler.sendMessage(msg);
    }

    private void initBle() {
        mRssiArray = new ArrayList<>();
        mBleManager = new BleManager(this, this);
    }


    private void initView() {
        mBleKeyLayout = findViewById(R.id.ble_key_layout);
        mLeftFrontDoor = findViewById(R.id.leftfront_door);
        mRightFrontDoor = findViewById(R.id.rightfront_door);
        mLeftBackDoor = findViewById(R.id.leftback_door);
        mRightBackDoor = findViewById(R.id.rightback_door);

        mLeftFrontWindow = findViewById(R.id.leftfront_window);
        mRightFrontWindow = findViewById(R.id.rightfront_window);
        mLeftBackWindow = findViewById(R.id.leftback_window);
        mRightBackWindow = findViewById(R.id.rightback_window);

        mCarHood = findViewById(R.id.car_hood);
        mCarTruck = findViewById(R.id.car_trunk);
        mCarHorn = findViewById(R.id.car_horn);
        mCarLight = findViewById(R.id.car_light);

        mTvRssi = findViewById(R.id.tv_rssi);
        mAverageRssi = findViewById(R.id.tv_ave_rssi);
        mSwithcRssi = findViewById(R.id.switch_rssi);

        mLeftFrontDoor.setOnClickListener(this);
        mRightFrontDoor.setOnClickListener(this);
        mLeftBackDoor.setOnClickListener(this);
        mRightBackDoor.setOnClickListener(this);
        mLeftFrontWindow.setOnClickListener(this);
        mRightFrontWindow.setOnClickListener(this);
        mLeftBackWindow.setOnClickListener(this);
        mRightBackWindow.setOnClickListener(this);
        mCarLight.setOnClickListener(this);
        mCarHorn.setOnClickListener(this);
        mCarTruck.setOnClickListener(this);
        mCarHood.setOnClickListener(this);
        mSwithcRssi.setOnClickListener(this);
        mEtvPb = findViewById(R.id.tv_pb);
        mEtvPb.setText(Config.NORMAL_Pb);
        mCurrentPb = Integer.valueOf(Config.NORMAL_Pb);
        mEtvPe = findViewById(R.id.tv_pe);
        mEtvPe.setText(Config.NORMAL_PE);
        mCurrentPe = Integer.valueOf(Config.NORMAL_PE);
        pbtn = findViewById(R.id.btn_p);
        pbtn.setOnClickListener(this);

        isAveRssi = false;
        mTvRssi.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        mAverageRssi.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }
}
