package com.liu.Account.BmobRespose;

import cn.bmob.v3.BmobUser;

/**
 * @author liutanrong0425@163.com
 * Created by liu on 15-10-16.
 */
public class BmobUsers extends BmobUser {

    private String nickName,passwordd,DBname;
    private String phoneType,imei,imsi,number,versionName,androidVersion,androidAPI;
    private int versionCode;
    private String DBupdateDate;
    private int updatetimes=0;
    private String fileName;
    private String DBMd5=null;
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

    public String getDBMd5() {
        return DBMd5;
    }

    public void setDBMd5(String DBMd5) {
        this.DBMd5 = DBMd5;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getUpdatetimes() {
        return updatetimes;
    }

    public void setUpdatetimes(int updatetimes) {
        this.updatetimes = updatetimes;
    }

    public String getDBupdateDate() {
        return DBupdateDate;
    }

    public void setDBupdateDate(String DBupdateDate) {
        this.DBupdateDate = DBupdateDate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPasswordd() {
        return passwordd;
    }

    public void setPasswordd(String passwordd) {
        this.passwordd = passwordd;
    }


    public String getDBname() {
        return DBname;
    }

    public void setDBname(String DBname) {
        this.DBname = DBname;
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

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
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

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
