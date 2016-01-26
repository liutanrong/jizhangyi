package com.liu.Account.BmobRespose;

import android.content.Context;

/**
 *  @author liutanrong0425@163.com
 * Created by liu on 15-10-12.
 */
public class BmobInstallation extends cn.bmob.v3.BmobInstallation {
    private String phoneType,imei,imsi,number,versionName,androidVersion,androidAPI;
    private int versionCode;
    private int updatetimes=0;
    private String channel;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getUpdatetimes() {
        return updatetimes;
    }

    public void setUpdatetimes(int updatetimes) {
        this.updatetimes = updatetimes;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getAndroidAPI() {
        return androidAPI;
    }

    public void setAndroidAPI(String androidAPI) {
        this.androidAPI = androidAPI;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public BmobInstallation(Context context) {
        super(context);
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
