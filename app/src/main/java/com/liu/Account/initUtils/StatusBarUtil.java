package com.liu.Account.initUtils;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

import com.liu.Account.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * 设置状态栏的工具类
 * @author mtxc
 *
 */
public class StatusBarUtil {

    /**
     * 设置灰色沉浸式状态栏
     * @param activity
     */
    public static final void setTransparentStatusBar(Activity activity){
        // TODO: 16-1-23 设置沉浸状态栏     //
        //判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
        /**if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }**/
     }

}
