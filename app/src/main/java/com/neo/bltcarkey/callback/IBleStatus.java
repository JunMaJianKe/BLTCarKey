package com.neo.bltcarkey.callback;

import java.util.ArrayList;

/**
 * author : SenXia
 * e-mail : Sen_Xia@human-horizons.com
 * time   : 2020/05/13
 * desc   : This is IBleStatus
 * version: 1.0
 */
public interface IBleStatus {
    void onUpdateScanStatus(int status, ArrayList<String> list);
}
