package com.liu.Account.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.liu.Account.Constants.Constants;
import com.liu.Account.R;
import com.liu.Account.commonUtils.AppUtil;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.commonUtils.PrefsUtil;
import com.liu.Account.initUtils.Init;
import com.liu.Account.utils.DatabaseUtil;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.List;

import me.zhanghai.android.patternlock.ConfirmPatternActivity;
import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;

/**
 *  @author liutanrong0425@163.com
 * Created by deonte on 15-11-4.
 */
public class LaunchActivity extends ConfirmPatternActivity {
    private Context context;
    private DatabaseUtil db;

    public final int MSG_FINISH_LAUNCHERACTIVITY = 500;
    public final int MSG_FINISH_LAUNCHERACTIVITY2 = 700;

    public Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FINISH_LAUNCHERACTIVITY:
                    //跳转到MainActivity，并结束当前的LauncherActivity
                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case MSG_FINISH_LAUNCHERACTIVITY2:
                    Intent intt = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intt);
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


        context=LaunchActivity.this;
        Init.Bmob(context);//初始化bmob
        Init.DbName(context);//获得数据库名称，为手机imei号
        Init.savePath();
        Init.Umeng(context);
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

        db = new DatabaseUtil(context, Constants.DBNAME, 1);
        if (AppUtil.getAppVersionCode(context)==16||AppUtil.getAppVersionCode(context)==17){
            try {
                db.renameTable("date", Constants.tableName);
            }catch (Exception e){
                e.printStackTrace();

                    db.creatTables(CREATE_CLASS);
            }
        }else {
                db.creatTables(CREATE_CLASS);
        }
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
    @Override
    protected boolean isStealthModeEnabled() {
    // Return the value from SharedPreferences
        PrefsUtil d=new PrefsUtil(LaunchActivity.this, Constants.PatternLock,Context.MODE_PRIVATE);
        if (!d.getBoolean("isPatternOn",false)) {
            //不开着屏幕锁
            setContentView(R.layout.activity_launch);
            mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 1000);
        }
        return d.getBoolean("isPatternOn",false);
}

    @Override
    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
        //
        String patternSha1 = null;
        PrefsUtil d=new PrefsUtil(LaunchActivity.this, Constants.PatternLock,Context.MODE_PRIVATE);
        patternSha1=d.getString("sha1");
        boolean i=TextUtils.equals(PatternUtils.patternToSha1String(pattern), patternSha1);
        if (i){
            mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY2, 0);
        }else {
            //// TODO: 16-1-28 Bug待解决
            return false;
        }
        return true;
    }

    @Override
    protected void onForgotPassword() {

        startActivity(new Intent(this, ResetPatternActivity.class));

        // Finish with RESULT_FORGOT_PASSWORD.
        super.onForgotPassword();
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("LaunchActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("LaunchActivity");
        MobclickAgent.onPause(this);
    }
    /**
     * 返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return false;
    }
}
