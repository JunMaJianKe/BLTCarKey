package com.neo.bltcarkey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.neo.bltcarkey.listener.OperationFeedBack;
import com.neo.bltcarkey.common.Config;
import com.neo.bltcarkey.common.Utils;
import com.neo.bltcarkey.widget.BleKeyLayout;

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

    private BleManager mBleManager;

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
        mBleKeyLayout.setStatus(Config.STATUS_PB);
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
                mBleManager.onControlDoors(Config.CONTROL_POSITION_LEFT_FRONT, true);
                break;
            case R.id.rightfront_door:
                mBleManager.onControlDoors(Config.CONTROL_POSITION_RIGHT_FRONT, true);
                break;
            case R.id.leftback_door:
                mBleManager.onControlDoors(Config.CONTROL_POSITION_LEFT_BACK, true);
                break;
            case R.id.rightback_door:
                mBleManager.onControlDoors(Config.CONTROL_POSITION_RIGHT_BACK, true);
                break;
            case R.id.leftfront_window:
                mBleManager.onControlWindows(Config.CONTROL_POSITION_LEFT_FRONT, true);
                break;
            case R.id.rightfront_window:
                mBleManager.onControlWindows(Config.CONTROL_POSITION_RIGHT_FRONT, true);
                break;
            case R.id.leftback_window:
                mBleManager.onControlWindows(Config.CONTROL_POSITION_LEFT_BACK, true);
                break;
            case R.id.rightback_window:
                mBleManager.onControlWindows(Config.CONTROL_POSITION_RIGHT_BACK, true);
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

    private void initBle() {
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
    }
}
