package com.liu.Account.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.liu.Account.Constants.Constants;
import com.liu.Account.Constants.TagConstats;
import com.liu.Account.R;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.commonUtils.PrefsUtil;
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

    private Spinner updateSpinner;
    private Spinner defaultTagSpinner;
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

        updateSpinner= (Spinner) findViewById(R.id.setting_update);
        updateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.i("autoUpdate" + position);
                switch (position) {
                    case 0:
                        //每天
                        LogUtil.i("autoUpdate" + "每天");
                        PrefsUtil d = new PrefsUtil(context, Constants.AutoUpdatePrefsName, Context.MODE_PRIVATE);
                        long temp = 24 * 60 * 60 * 1000;
                        d.putLong("gap", temp);
                        d.putInt("gapNumber", 0);
                        break;
                    case 1:
                        //每三天
                        LogUtil.i("autoUpdate" + "每三天");
                        PrefsUtil dd = new PrefsUtil(context, Constants.AutoUpdatePrefsName, Context.MODE_PRIVATE);
                        long tempp = 3 * 24 * 60 * 60 * 1000;
                        dd.putLong("gap", tempp);
                        dd.putInt("gapNumber", 1);
                        break;
                    case 2:
                        //禁止
                        LogUtil.i("autoUpdate" + "禁止");
                        PrefsUtil ddd = new PrefsUtil(context, Constants.AutoUpdatePrefsName, Context.MODE_PRIVATE);
                        long temppp = 99 * 24 * 60 * 60 * 1000;
                        ddd.putLong("gap", temppp);
                        ddd.putInt("gapNumber", 2);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        defaultTagSpinner= (Spinner) findViewById(R.id.setting_defaultTag);
        defaultTagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PrefsUtil d = new PrefsUtil(context, Constants.DefaultTag, Context.MODE_PRIVATE);
                TagConstats.defaultTag=position;
                switch (position) {
                    case 0:
                        //无分类
                        LogUtil.i("defaultTag:" + TagConstats.tagList[position]);
                        d.putString("tagName", TagConstats.tagList[position]);
                        d.putInt("tagPos",position);
                        break;
                    case 1:
                        //餐饮
                        LogUtil.i("defaultTag:" + TagConstats.tagList[position]);
                        d.putString("tagName", TagConstats.tagList[position]);
                        d.putInt("tagPos",position);
                        break;
                    case 2:
                        //娱乐
                        LogUtil.i("defaultTag:" + TagConstats.tagList[position]);
                        d.putString("tagName", TagConstats.tagList[position]);
                        d.putInt("tagPos",position);
                        break;
                    case 3:
                        //购物
                        LogUtil.i("defaultTag:" + TagConstats.tagList[position]);
                        d.putString("tagName", TagConstats.tagList[position]);
                        d.putInt("tagPos",position);
                        break;
                    case 4:
                        //交通
                        LogUtil.i("defaultTag:" + TagConstats.tagList[position]);
                        d.putString("tagName", TagConstats.tagList[position]);
                        d.putInt("tagPos",position);
                        break;
                    case 5:
                        //工资
                        LogUtil.i("defaultTag:" + TagConstats.tagList[position]);
                        d.putString("tagName", TagConstats.tagList[position]);
                        d.putInt("tagPos",position);
                        break;
                    case 6:
                        //其他
                        LogUtil.i("defaultTag:" + TagConstats.tagList[position]);
                        d.putString("tagName", TagConstats.tagList[position]);
                        d.putInt("tagPos",position);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        PrefsUtil d=new PrefsUtil(context, Constants.PatternLock,Context.MODE_PRIVATE);
        boolean isPatternOn=d.getBoolean("isPatternOn", false);
        patternButton.setChecked(isPatternOn);

        //初始化更新频率的spinner
        PrefsUtil ddd = new PrefsUtil(context, Constants.AutoUpdatePrefsName, Context.MODE_PRIVATE);
        updateSpinner.setSelection(ddd.getInt("gapNumber",0),true);

        //初始化默认Tag的spinner
        PrefsUtil dddd=new PrefsUtil(context,Constants.DefaultTag,Context.MODE_PRIVATE);
        TagConstats.defaultTag=dddd.getInt("tagPos",1);
        defaultTagSpinner.setSelection(TagConstats.defaultTag);
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
