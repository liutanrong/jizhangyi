package com.liu.Account.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.liu.Account.Constants.Constants;
import com.liu.Account.R;
import com.liu.Account.commonUtils.PrefsUtil;
import com.liu.Account.utils.DatabaseUtil;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import cn.bmob.v3.BmobUser;

/**
 * Created by deonte on 16-1-28.
 */
public class SettingActivity extends AutoLayoutActivity implements CompoundButton.OnCheckedChangeListener{
    private Context context;
    private ImageView titleBack;
    private TextView topText;

    private SwitchButton patternButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        context=SettingActivity.this;
        initTop();
        initView();
    }

    private void initView() {
        patternButton= (SwitchButton) findViewById(R.id.settingPattern);
        patternButton.setChecked(false);
        patternButton.setOnCheckedChangeListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        PrefsUtil d=new PrefsUtil(context, Constants.PatternLock,Context.MODE_PRIVATE);
        boolean isPatternOn=d.getBoolean("isPatternOn",false);
        patternButton.setChecked(isPatternOn);
    }

    private void initTop() {
        titleBack = (ImageView) findViewById(R.id.title_back);
        topText= (TextView) findViewById(R.id.title_text);
        topText.setText(R.string.menu_setting);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //isChecked  代表 开关 开/关
        switch (buttonView.getId()){
            case R.id.settingPattern:{
                //手势是否开
                PrefsUtil d=new PrefsUtil(context, Constants.PatternLock,Context.MODE_PRIVATE);
                boolean isPatternOn=d.getBoolean("isPatternOn",false);
                if (isPatternOn&&isChecked){
                    //原本是开着手势密码的 并且开关为开
                }else if (isPatternOn&&!isChecked){
                    //原本是开着手势密码的 并且现在要关
                    startActivity(new Intent(context,CloseConfirmPatternActivity.class));

                }else if (!isPatternOn&&isChecked){
                    //原本时关着手势密码的，现在要开
                    //设置手势密码
                    //设置手势密码成功后将 isPatternOn 设为true
                    BmobUser user=BmobUser.getCurrentUser(context);
                    if (user==null){
                        new AlertDialog.Builder(context).setTitle("您尚未登陆")
                                .setMessage("为确保您忘记图案后仍可使用本应用，只有登陆的用户才能使用图形密码.\n当前您未登录,是否去登陆?")
                                .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(context,LoginActivity.class));
                                      }
                                }).setNegativeButton("取消",null)
                                .show();
                    }else {
                        startActivity(new Intent(context, SetPatternLockActivity.class));
                    }
                }else if (!isPatternOn&&!isChecked){
                    //原本是关着手势密码的，现在要关
                }
                break;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SettingActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SettingActivity");
        MobclickAgent.onPause(this);
    }
}
