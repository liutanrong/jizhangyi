package com.liu.Account.initUtils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;


import com.liu.Account.BmobRespose.BmobInstallation;
import com.liu.Account.BmobRespose.BmobUsers;
import com.liu.Account.utils.LocationUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by deonte on 15-10-23.
 *
 * 调用方式
 *  TelephonyManager mTm= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
 *  PackageManager packageManager = getPackageManager();
 *   PackageInfo packInfo = null;
 *   try {
 *   packInfo = packageManager.getPackageInfo(getPackageName(), 0);
 *} catch (PackageManager.NameNotFoundException e) {
 *   e.printStackTrace();
 *   }
 *   get=new getInfo(MainActivity.this,mTm,packInfo);
 *   Bundle bundle= get.getInformation();
 * **/
public class DeviceInformation extends Activity{
    Context context;
    TelephonyManager mTm;
    PackageInfo packInfo;
    public DeviceInformation(Context context, TelephonyManager mTm, PackageInfo packInfo){
        this.context=context;
        this.mTm=mTm;
        this.packInfo=packInfo;
    }
    public Bundle getInformation ( ) {
        String phoneType, imei, imsi, phoneNumber, versionName, androidVersion, androidAPI;
        int versionCode;
        String channel;
        Bundle bundle=new Bundle();
        phoneType = Build.MODEL; // 手机型号
        channel=getMetaData(context, "CHANNEL");

        androidAPI = String.valueOf(Build.VERSION.SDK_INT);
        androidVersion = Build.VERSION.RELEASE;

        imei = mTm.getDeviceId();
        imsi = mTm.getSubscriberId();
        phoneNumber = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得

        versionName = packInfo.versionName;
        versionCode = packInfo.versionCode;

        bundle.putString("phoneType",phoneType);
        bundle.putString("androidVersion",androidVersion);
        bundle.putString("androidAPI",androidAPI);
        bundle.putString("imei",imei);
        bundle.putString("imsi",imsi);
        bundle.putString("phoneNumber",phoneNumber);
        bundle.putString("versionName",versionName);
        bundle.putString("channel",channel);
        bundle.putString("versionCode",String.valueOf(versionCode));
        String location;
        Location location1=LocationUtils.getLocation(context);
        if (location1!=null){
            location=location1.getLatitude()+","+location1.getLongitude();
        }else {
            location="0,0";
        }
        bundle.putString("location", location);
        return bundle;
    }

    public void upInfoToBmob(final Bundle bundle){
        cn.bmob.v3.BmobInstallation.getCurrentInstallation(context).save();
        BmobQuery<BmobInstallation> query=new BmobQuery<>();
        query.addWhereEqualTo("installationId", cn.bmob.v3.BmobInstallation.getInstallationId(context));
        query.findObjects(context, new FindListener<BmobInstallation>() {
            @Override
            public void onSuccess(List<BmobInstallation> list) {
                String phoneType,imei,imsi,number,versionName,androidVersion,androidAPI,versionCode;
                String channel;
                String location;
                if (list.size() > 0) {
                    phoneType=bundle.getString("phoneType");
                    androidVersion=bundle.getString("androidVersion");
                    androidAPI=bundle.getString("androidAPI");
                    imei=bundle.getString("imei");
                    imsi=bundle.getString("imsi");
                    channel=bundle.getString("UMENG_CHANNEL");
                    number=bundle.getString("phoneNumber");
                    versionName=bundle.getString("versionName");
                    versionCode=bundle.getString("versionCode");
                    location=bundle.getString("location");
                    BmobUsers us=BmobUsers.getCurrentUser(context,BmobUsers.class);
                    if(us!= null){
                        // 允许用户使用应用
                        us.setUpdatetimes(us.getUpdatetimes()+1);
                        us.setImei(imei);
                        us.setImsi(imsi);
                        us.setNumber(number);
                        us.setPhoneType(phoneType);
                        us.setVersionCode(Integer.valueOf(versionCode));
                        us.setVersionName(versionName);
                        us.setAndroidAPI(androidAPI);
                        us.setAndroidVersion(androidVersion);
                        us.setChannel(channel);
                        us.setLocation(location);
                        us.update(context);
                    }
                    BmobInstallation mbi = list.get(0);
                    mbi.setUpdatetimes(mbi.getUpdatetimes()+1);
                    mbi.setImei(imei);
                    mbi.setImsi(imsi);
                    mbi.setNumber(number);
                    mbi.setPhoneType(phoneType);
                    mbi.setVersionCode(Integer.valueOf(versionCode));
                    mbi.setVersionName(versionName);
                    mbi.setAndroidAPI(androidAPI);
                    mbi.setAndroidVersion(androidVersion);
                    mbi.setChannel(channel);
                    mbi.setLocation(location);
                    mbi.update(context);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    private static String getMetaData(Context context, String key) {

        try {

            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(

                    context.getPackageName(), PackageManager.GET_META_DATA);

            Object value = ai.metaData.get(key);

            if (value != null) {

                return value.toString();

            }

        } catch (Exception e) {

            //

        }

        return null;

    }
}
