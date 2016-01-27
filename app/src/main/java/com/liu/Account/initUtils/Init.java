package com.liu.Account.initUtils;

import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.liu.Account.Constants.Constants;
import com.liu.Account.commonUtils.AppUtil;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.commonUtils.ToastUtil;

import java.io.File;

import cn.bmob.v3.Bmob;

/**
 * Created by deonte on 16-1-25.
 */
public class Init {
    public static void Bmob(Context context){
        Bmob.initialize(context, "dd7c102003f0523216548d4ae132ad3c");
       /** BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
            @Override
            public void onUpdateReturned(int i, UpdateResponse updateResponse) {

            }
        });
        BmobUpdateAgent.update(this);
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        // 使用推送服务时的初始化操作
        com.liu.Account.util.BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this, "dd7c102003f0523216548d4ae132ad3c");**/
    }
    /**
     * 取得数据库名称 为手机imei号
     * */
    public static void DbName(Context context){
        Constants.DBNAME= AppUtil.getDeviceIMEI(context);

    }

    public static void savePath() {
        if(android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)){
            File dir = new File(Environment.getExternalStorageDirectory(),
                    Constants.FileName == null ? "test" : Constants.FileName);
            if (!dir.exists())
                dir.mkdir();
            Constants.AppSavePath=dir.getPath();
            LogUtil.i("储存文件夹"+Constants.AppSavePath);
        }else
        {
            LogUtil.i("储存不可用");
        }
    }
}
