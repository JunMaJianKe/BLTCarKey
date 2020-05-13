package com.neo.bltcarkey.callback;

/**
 * author : SenXia
 * e-mail : Sen_Xia@human-horizons.com
 * time   : 2020/05/13
 * desc   : This is IBleManager
 * version: 1.0
 */
public interface IBleManager {
    void onQueryBl();
    void onControlWindows(int position, boolean toOpen);
    void onControlDoors(int position, boolean toOpen);
    void onControlLight();
    void onControlTrunk();
    void onControlHood();
    void onPressCarHorn();
}
