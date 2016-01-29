package com.liu.Account.commonUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;


/**
 * 当前程序是否后台运行 
 * 当前手机是否处于睡眠 
 * 当前网络是否已连接 
 * 当前网络是否wifi状态 
 * 安装apk 
 * 初始化view的高度 
 * 初始化view的高度后不可见 
 * 判断是否为手机 
 * 获取屏幕宽度 
 * 获取屏幕高度 
 * 获取设备的IMEI 
 * 获取设备的mac地址 
 * 获取当前应用的版本号 
 * 收集设备信息并以Properties返回 
 * 收集设备信息并以String返回 
 *
 * Created with IntelliJ IDEA.
 *  Author: wangjie 
 *  email:tiantian.china.2@gmail.com 
 *  Date: 13-6-5 Time: 下午3:14
 */
public class AppUtil {
    public static final int NETWORK_CLASS_2_G=2;
    public static final int NETWORK_CLASS_3_G=3;
    public static final int NETWORK_CLASS_4_G=4;
    public static final int NETWORK_CLASS_UNKNOWN=-1;
    public static final int NETWORK_WIFI=5;

    /**
     * 判断当前应用程序是否后台运行 在android5.0以上失效！请使用isApplicationBackground()方法代替！
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Deprecated
    public static boolean isBackground(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return false;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.d("AppUtils:","后台程序: " + appProcess.processName);
                    return true;
                } else {
                    Log.d("AppUtils:","前台程序: " + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
    /**
     * 判断当前应用程序处于前台还是后台 需要添加权限: <uses-permission android:name="android.permission.GET_TASKS" />
     */
    public static boolean isApplicationBackground(final Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                Log.d("AppUtils:","isBackground: " + true);
                return true;
            }
        }
        Log.d("AppUtils:","isBackground: " + false);
        return false;
    }

    /**
     * 判断手机是否处理睡眠
     *
     * @param context
     * @return
     */
    public static boolean isSleeping(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return false;
        }
        KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        Log.d("AppUtils:",isSleeping ? "手机睡眠中.." : "手机未睡眠...");
        return isSleeping;
    }


    /**
     * 判断当前是否是wifi状态
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 安装apk
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return;
        }
        if(file.length()==0){
            Log.e("AppUtils:", "传入file为空");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 安装apk
     */
    public static void installAPK(Context context, String apkFile) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return;
        }
        if(apkFile==null){
            Log.e("AppUtils:", "传入file为空");
            return;
        }
        File file = new File(apkFile);
        if (file.exists()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }

    /**
     * 初始化View的高宽
     *
     * @param view
     */
    @Deprecated
    public static void initViewWH(final View view) {
        if(view==null){
            Log.e("AppUtils:", "传入view为空");
            return;
        }
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                System.out.println(view + ", width: " + view.getWidth() + "; height: " + view.getHeight());
            }
        });
    }

    /**
     * 初始化View的高宽并显示不可见
     *
     * @param view
     */
    @Deprecated
    public static void initViewWHAndGone(final View view) {
        if(view==null){
            Log.e("AppUtils:", "传入view为空");
            return;
        }
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                view.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 使用Properties来保存设备的信息和错误堆栈信息
     */
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";

    /**
     * 判断是否为手机
     *
     * @param context
     * @return
     * @author liutanrong
     */
    public static boolean isPhone(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return false;
        }
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int type = telephony.getPhoneType();
        if (type == TelephonyManager.PHONE_TYPE_NONE) {
            Log.d("AppUtils","Current device is Tablet!");
            return false;
        } else {
            Log.d("AppUtils","Current device is phone!");
            return true;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        if(mobiles==null){
            Log.e("AppUtils:", "传入号码为空");
            return false;
        }
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    虚拟运营商：170
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "^(0|86|17951)?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[07])[0-9]{8}$";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }
    /**
     * 判断是否为Email格式
     * @param emails email的字符串
     * */
    public static boolean isEmailNO(String emails){
        if(emails==null){
            Log.e("AppUtils:", "传入邮箱为空");
            return false;
        }
        String mailRegex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (TextUtils.isEmpty(emails)) return false;
        else return emails.matches(mailRegex);
    }
    /**
     * 获得设备的屏幕宽度(px)
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getDeviceWidth(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return 0;
        }
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕尺寸与密度.
     *
     * @param context the context
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return null;
        }
        Resources mResources;
        if (context == null) {
            mResources = Resources.getSystem();

        } else {
            mResources = context.getResources();
        }
        //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
        //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }

    /**
     * 获得设备的屏幕高度(px)
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getDeviceHeight(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return 0;
        }
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getHeight();
    }

    /**
     * 获取设备id（IMEI）
     *
     * @param context
     * @return
     * @author wangjie
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static String getDeviceIMEI(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return null;
        }
        String deviceId;
        if (isPhone(context)) {
            TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephony.getDeviceId();
        } else {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        Log.d("AppUtils","当前设备IMEI码: " + deviceId);
        return deviceId;
    }

    /**
     * 获取设备mac地址
     *
     * @param context
     * @return
     * @author wangjie
     */
    public static String getDeviceMacAddress(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return null;
        }
        String macAddress;
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        macAddress = info.getMacAddress();
        Log.d("AppUtils","当前mac地址: " + (null == macAddress ? "null" : macAddress));
        if (null == macAddress) {
            return "";
        }
        macAddress = macAddress.replace(":", "");
        return macAddress;
    }

    /**
     * 获取当前应用程序的版本名
     *
     * @return
     */
    public static String getAppVersionName(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return null;
        }
        String version = "0";
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d("AppUtils","getAppVersion");
        }
        Log.d("AppUtils","该应用的版本号: " + version);
        return version;
    }

    /**
     * 获取当前应用程序的版本号
     *
     * @return
     */
    public static int getAppVersionCode(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return -1;
        }
        int version = 0;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("AppUtils", "getAppVersion");
        }
        Log.d("AppUtils","该应用的版本号: " + version);
        return version;
    }

    /**
     * 收集设备信息
     *
     * @param context
     */
    public static Properties collectDeviceInfo(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return null;
        }
        Properties mDeviceCrashInfo = new Properties();
        try {
            // Class for retrieving various kinds of information related to the
            // application packages that are currently installed on the device.
            // You can find this class through getPackageManager().
            PackageManager pm = context.getPackageManager();
            // getPackageInfo(String packageName, int flags)
            // Retrieve overall information about an application package that is installed on the system.
            // public static final int GET_ACTIVITIES
            // Since: API Level 1 PackageInfo flag: return information about activities in the package in activities.
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                // public String versionName The version name of this package,
                // as specified by the <manifest> tag's versionName attribute.
                mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
                // public int versionCode The version number of this package,
                // as specified by the <manifest> tag's versionCode attribute.
                mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AppUtils","Error while collect package info");
        }
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
        // 返回 Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                // setAccessible(boolean flag)
                // 将此对象的 accessible 标志设置为指示的布尔值。
                // 通过设置Accessible属性为true,才能对私有变量进行访问，不然会得到一个IllegalAccessException的异常
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName(), field.get(null));
                //                if (DEBUG) {
                //                    Logger.d( field.getName() + " : " + field.get(null));
                //                }
            } catch (Exception e) {
                Log.e("AppUtils","Error while collect crash info");
            }
        }
        return mDeviceCrashInfo;
    }

    /**
     * 获取包信息.
     *
     * @param context the context
     */
    public static PackageInfo getPackageInfo(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return null;
        }
        PackageInfo info = null;

        try {
            String packageName = context.getPackageName();
            info = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 是否有SDCard
     *
     * @return
     */
    public static boolean haveSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 隐藏软键盘
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void hideSoftInput(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入context为空");
            return;
        }
        View view = ((Activity) context).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 隐藏软键盘
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void hideSoftInput(Context context, EditText edit) {
        if(context==null||edit==null){
            Log.e("AppUtils:", "传入参数为空");
            return;
        }
        edit.clearFocus();
        InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    /**
     * 隐藏软键盘
     **/
    public static void closeSoftKeyboard(Activity context) {
        if(context==null){
            Log.e("AppUtils:", "传入参数为空");
            return;
        }
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void showSoftInput(Context context, EditText edit) {
        if(context==null||edit==null){
            Log.e("AppUtils:", "传入参数为空");
            return;
        }
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(edit, 0);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void toggleSoftInput(Context context, EditText edit) {
        if(context==null||edit==null){
            Log.e("AppUtils:", "传入参数为空");
            return;
        }
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    public static int getNetworkType(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入参数为空");
            return -1;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getNetworkType();
    }

    /**
     * MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码) 仅当用户已在网络注册时有效, CDMA 可能会无效
     *
     * @return （中国移动：46000 46002, 中国联通：46001,中国电信：46003）
     */
    public static String getNetworkOperator(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入参数为空");
            return null;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getNetworkOperator();
    }

    /**
     * 返回移动网络运营商的名字(例：中国联通、中国移动、中国电信) 仅当用户已在网络注册时有效, CDMA 可能会无效
     *
     * @return
     */
    public static String getNetworkOperatorName(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入参数为空");
            return null;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getNetworkOperatorName();
    }

    /**
     * 返回移动终端类型 PHONE_TYPE_NONE :0手机制式未知 PHONE_TYPE_GSM :1手机制式为GSM，移动和联通 PHONE_TYPE_CDMA :2手机制式为CDMA，电信 PHONE_TYPE_SIP:3
     *
     * @return
     */
    public static int getPhoneNetType(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入参数为空");
            return -1;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getPhoneType();
    }


    /**
     * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
     *
     * @return 2G、3G、4G、未知四种状态
     */
    public static int getNetWorkClass(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入参数为空");
            return -1;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    /**
     * 返回状态：当前的网络链接状态
     *
     * @return 没有网络，2G，3G，4G，WIFI
     */
    public static int getCurrentNetWorkStatus(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入参数为空");
            return -1;
        }
        int netWorkType = NETWORK_CLASS_UNKNOWN;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            int type = networkInfo.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                netWorkType = NETWORK_WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                netWorkType = getNetWorkClass(context);
            }
        }
        return netWorkType;
    }

    /**
     * 检查网络连接
     */
    public static boolean isNetworkOK(Context context) {
        if(context==null){
            Log.e("AppUtils:", "传入参数为空");
            return false;
        }
        if (context == null) {
            return false;
        }
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }
    /**
     * 取得手机型号
     * */
    public static String getPhoneModel(){
        return android.os.Build.MODEL;
    }

    /**
     * 打开移动数据设置界面
     */
    public static void openMoblieDataSetting(Activity activity) {
        activity.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));

    }

    /**
     * 打开WIFI设置界面
     *
     * @param activity
     */
    public static void openWifiSetting(Activity activity) {
        activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    /**
     * 设置WIFI状态，权限 ACCESS_WIFI_STATE，CHANGE_WIFI_STATE
     *
     * @param context
     * @param enabled
     */
    public static void setWifiEnabled(Context context, boolean enabled) {
        WifiManager manager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (manager != null)
            manager.setWifiEnabled(enabled);

    }
    public static void requestFocus(View v){
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
    }
}