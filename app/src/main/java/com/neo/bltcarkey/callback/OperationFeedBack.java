package com.neo.bltcarkey.callback;

import java.util.List;

/**
 * author : SenXia
 * e-mail : Sen_Xia@human-horizons.com
 * time   : 2020/05/13
 * desc   : This is OperationFeedBack
 * version: 1.0
 */
public interface OperationFeedBack {
    void onUpdateWindowStatus(int position, int status);

    void onUpdateDoorStatusin(int position, int status);

    void onUpdateKeyDistance(int status);

    void onBLeScan(int status, List<String> list);
}
