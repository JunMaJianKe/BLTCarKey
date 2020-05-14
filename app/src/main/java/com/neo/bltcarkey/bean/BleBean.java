package com.neo.bltcarkey.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class BleBean implements Parcelable {

    private String mDevice;
    private String mName;
    private int mRssi;
    private boolean mIsConnect;

    public BleBean() {

    }

    public BleBean(String device, String name, int rssi, boolean isConnect) {
        this.mDevice = device;
        this.mName = name;
        this.mRssi = rssi;
        this.mIsConnect = isConnect;
    }

    public String getDevice() {
        return mDevice;
    }

    public void setDevice(String device) {
        this.mDevice = device;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getRssi() {
        return mRssi;
    }

    public void setRssi(int rssi) {
        this.mRssi = rssi;
    }

    public boolean isConnect() {
        return mIsConnect;
    }

    public void setIsConnect(boolean isConnect) {
        this.mIsConnect = isConnect;
    }


    protected BleBean(Parcel in) {
        mDevice = in.readString();
        mName = in.readString();
        mRssi = in.readInt();
        mIsConnect = in.readByte() != 0;

    }

    public static final Creator<BleBean> CREATOR = new Creator<BleBean>() {
        @Override
        public BleBean createFromParcel(Parcel in) {
            return new BleBean(in);
        }

        @Override
        public BleBean[] newArray(int size) {
            return new BleBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mDevice);
        parcel.writeString(mName);
        parcel.writeInt(mRssi);
        parcel.writeByte((byte) (mIsConnect ? 1 : 0));
    }
}
