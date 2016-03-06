package com.liu.Account.initUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.liu.Account.BmobNetwork.BmobNetworkUtils;
import com.liu.Account.Constants.Constants;
import com.liu.Account.commonUtils.AppUtil;
import com.liu.Account.commonUtils.DateUtil;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.commonUtils.PrefsUtil;
import com.liu.Account.commonUtils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.io.File;
import java.util.Calendar;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

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
            Constants.AppSavePath=dir.getPath()+"/";
            LogUtil.i("储存文件夹"+Constants.AppSavePath);
        }else
        {
            LogUtil.i("储存不可用");
        }
    }

    public static String  DatabasePath(Context context,String name) {
        File F=context.getDatabasePath(name);
        return F.getPath();
    }

    public static void autoUpdate(Context context) {
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UmengUpdateAgent.update(context);
        UmengUpdateAgent.setUpdateOnlyWifi(false);//非wifi也提醒
        UmengUpdateAgent.silentUpdate(context);//静默下载更新

    }

    public static void Umeng(Context context) {
        MobclickAgent.openActivityDurationTrack(false);

    }

    public static void autoUpdateData(Context context) {

        LogUtil.i("设置自动同步ing");
        PrefsUtil dd=new PrefsUtil(context,Constants.AutoUpdatePrefsName,Context.MODE_PRIVATE);
        boolean flag=dd.getBoolean("autoUpdateInFirst",false);
        if (!flag){
            LogUtil.i("第一次运行,不自动上传");
            dd.putBoolean("autoUpdateInFirst",true);
            return;
        }
        BmobNetworkUtils bmob=new BmobNetworkUtils(context);
        try {

            PrefsUtil d = new PrefsUtil(context, Constants.AutoUpdatePrefsName, Context.MODE_PRIVATE);
            long gap=d.getLong("gap",24*60*60*1000);
            long lastUpdateTime=d.getLong("autoUpateTime", 0000000);
            long thiss=lastUpdateTime+gap;

            Calendar calendar=Calendar.getInstance();
            long a= calendar.getTimeInMillis();
            if (a>thiss) {
                LogUtil.i("自动同步ing");
                bmob.upDatesToBmob(context, false);
            }else {
                LogUtil.i("未自动同步");
            }
        }catch (Exception e){
           e.printStackTrace();
        }
    }
}
