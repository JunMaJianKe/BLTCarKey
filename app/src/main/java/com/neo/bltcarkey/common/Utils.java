package com.neo.bltcarkey.common;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static void showToast(Context context, int id) {
        Toast.makeText(context, context.getResources().getText(id), Toast.LENGTH_SHORT).show();
    }
}
