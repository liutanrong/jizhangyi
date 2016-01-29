package com.liu.Account.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.liu.Account.Constants.Constants;
import com.liu.Account.R;
import com.liu.Account.commonUtils.PrefsUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import me.zhanghai.android.patternlock.ConfirmPatternActivity;
import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;

/**
 * Created by deonte on 16-1-28.
 */
public class CloseConfirmPatternActivity extends ConfirmPatternActivity {
    @Override
    protected boolean isStealthModeEnabled() {
        //  Return the value from SharedPreferences
        PrefsUtil d=new PrefsUtil(this, Constants.PatternLock, Context.MODE_PRIVATE);

        return d.getBoolean("isPatternOn",false);
    }

    @Override
    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
        //
        String patternSha1 = null;
        PrefsUtil d=new PrefsUtil(this, Constants.PatternLock,Context.MODE_PRIVATE);
        patternSha1=d.getString("sha1");
        boolean i= TextUtils.equals(PatternUtils.patternToSha1String(pattern), patternSha1);
        if (i){
            //// TODO: 16-1-28 提示信息需要更新
            d.putBoolean("isPatternOn",false);
        }else {

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
        MobclickAgent.onPageStart("CloseConfirmPatternActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CloseConfirmPatternActivity");
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
