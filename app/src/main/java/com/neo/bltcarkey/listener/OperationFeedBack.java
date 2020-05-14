package com.neo.bltcarkey.listener;

import java.util.List;

/**
 * author : SenXia
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
