package com.neo.bltcarkey.adapter;

import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neo.bltcarkey.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author : SenXia
 * time   : 2020/05/14
 * desc   : This is BleAdapter
 * version: 1.0
 */
public class BleAdapter extends BaseAdapter {

    private List<ScanResult> mList;
    private Context mContext;

    public BleAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<ScanResult>();
    }

    public void resetData() {
        mList.clear();
    }

    public void addData(ScanResult result) {
        synchronized (mList) {
            if (mList == null) {
                mList = new ArrayList<ScanResult>();
            }
            String key = result.getDevice().toString();
            int j = -1;
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getDevice().toString().equals(key)) {
                    j = i;
//                    mList.remove(i);
                    mList.set(i, result);
                }
            }
            if (j == -1) {
                mList.add(result);
            }
        }

        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public ScanResult getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_ble_layout,
                    viewGroup, false);
            holder = new ViewHolder();
            holder.mDevicesView = (TextView) convertView.findViewById(R.id.device);
            holder.mNameView = (TextView) convertView.findViewById(R.id.name);
            holder.mRssi = (TextView) convertView.findViewById(R.id.rssi);
            holder.mConnectView = (TextView) convertView.findViewById(R.id.isconnect);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ScanResult result = mList.get(i);
        if (result.getDevice() != null) {
            holder.mDevicesView.setText(setTexts(R.string.bl_devices,
                    result.getDevice().toString()));
        }
        holder.mNameView.setText(setTexts(R.string.bl_name,
                result.getScanRecord().getDeviceName()));
        holder.mRssi.setText(setTexts(R.string.bl_rssi, result.getRssi()));
        if (result.isConnectable()) {
            holder.mConnectView.setText(R.string.bl_connected);
        } else {
            holder.mConnectView.setText(R.string.bl_not_connect);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView mDevicesView;
        TextView mNameView;
        TextView mRssi;
        TextView mConnectView;
    }

    private String setTexts(int res, String string) {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(mContext.getResources().getString(res)).append(string);
        return stringBuffer.toString();
    }

    private String setTexts(int res, boolean string) {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(mContext.getResources().getString(res)).append(string);
        return stringBuffer.toString();
    }

    private String setTexts(int res, int string) {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(mContext.getResources().getString(res)).append(string);
        return stringBuffer.toString();
    }
}
