package com.liu.Account.activity;

import android.content.Context;
import android.view.KeyEvent;

import com.liu.Account.BmobRespose.BmobUsers;
import com.liu.Account.Constants.Constants;
import com.liu.Account.commonUtils.PrefsUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.bmob.v3.BmobUser;
import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;
import me.zhanghai.android.patternlock.SetPatternActivity;

/**
 * Created by deonte on 16-1-28.
 */
public class SetPatternLockActivity extends me.zhanghai.android.patternlock.SetPatternActivity {
    @Override
    protected void onSetPattern(List<PatternView.Cell> pattern) {
        super.onSetPattern(pattern);
        String patternSha1 = PatternUtils.patternToSha1String(pattern);
        PrefsUtil d=new PrefsUtil(SetPatternLockActivity.this, Constants.PatternLock, Context.MODE_PRIVATE);
        d.putString("sha1", patternSha1);
        d.putBoolean("isPatternOn", true);
        BmobUser user= BmobUser.getCurrentUser(SetPatternLockActivity.this);
        d.putString("PatternUserId",user.getObjectId());
        // Save patternSha1 in SharedPreferences.
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SetPatternLockActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SetPatternLockActivity");
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
