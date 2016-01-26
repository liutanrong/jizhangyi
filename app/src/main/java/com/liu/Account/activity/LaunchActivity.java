package com.liu.Account.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.liu.Account.Constants.Constants;
import com.liu.Account.R;
import com.liu.Account.commonUtils.AppUtil;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.commonUtils.PrefsUtil;
import com.liu.Account.initUtils.Init;
import com.liu.Account.utils.DatabaseUtil;

/**
 *  @author liutanrong0425@163.com
 * Created by deonte on 15-11-4.
 */
public class LaunchActivity extends Activity {
    private Context context;
    private DatabaseUtil db;

    public final int MSG_FINISH_LAUNCHERACTIVITY = 500;

    public Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FINISH_LAUNCHERACTIVITY:
                    //跳转到MainActivity，并结束当前的LauncherActivity
                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);
        mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 1000);

        context=LaunchActivity.this;
        Init.Bmob(context);//初始化bmob
        Init.DbName(context);//获得数据库名称，为手机imei号
        initDB();
    }

    private void initDB() {

        String CREATE_CLASS = "Create Table If Not Exists "+ Constants.tableName+" ("
                +Constants.column[0]+" integer primary key,"
                +Constants.column[1]+" String,"
                +Constants.column[2]+" String,"
                +Constants.column[3]+" String,"
                +Constants.column[4]+" String UNIQUE,"
                +Constants.column[5]+" String,"
                +Constants.column[6]+" String,"
                +Constants.column[7]+" String,"
                +Constants.column[8]+" String,"
                +Constants.column[9]+" String,"
                +Constants.column[10]+" String )";

        PrefsUtil pr=new PrefsUtil(context,"isFirst",Context.MODE_PRIVATE);
        boolean flag=pr.getBoolean("isFirst",true);
        pr.putBoolean("isFirst",false);
        db = new DatabaseUtil(context, Constants.DBNAME, 1);
        if (AppUtil.getAppVersionCode(context)==17){
            try {
                db.renameTable("date", Constants.tableName);
            }catch (Exception e){
                e.printStackTrace();
                if (flag){
                    db.creatTables(CREATE_CLASS);
                }
            }
        }else {
            if (flag){
                db.creatTables(CREATE_CLASS);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }
/**
    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
**/
}
