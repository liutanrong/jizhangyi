package com.liu.Account.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.Account.Constants.Constants;
import com.liu.Account.R;
import com.liu.Account.commonUtils.AppUtil;
import com.liu.Account.commonUtils.PrefsUtil;
import com.liu.Account.commonUtils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by deonte on 16-1-28.
 */
public class ResetPatternActivity extends AutoLayoutActivity {
    private Context context;
    private ImageView titleBack;
    private TextView topText;

    private EditText mLogin_user_name;
    private EditText mLogin_password;
    private Button mLogin_login;
    private TextView mFindPassword;
    private String userName,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pattern);

        context=ResetPatternActivity.this;
        initTop();
        bindViews();
        mFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //找回密码界面
                startActivity(new Intent(context, RetrievePasswordActivity.class));
                finish();
            }
        });

        mLogin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = mLogin_user_name.getText().toString().trim();
                password = mLogin_password.getText().toString().trim();
                BmobLogin(userName, password);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        BmobUser u=BmobUser.getCurrentUser(context);
        if (u!=null){
            mLogin_user_name.setText(u.getUsername());
        }
    }

    private void BmobLogin(String userName, String password) {
        if (userName==null) {
            ToastUtil.showShort(context, getString(R.string.userNameNull));
            return;
        }else if (password==null){
            ToastUtil.showShort(context, getString(R.string.passwordNull));
            return;
        }else if (!AppUtil.isNetworkOK(context)){
            ToastUtil.showShort(context,getString(R.string.loginNetworkFalse));
            return;
        }else if (!AppUtil.isEmailNO(userName)){
            ToastUtil.showShort(context,getString(R.string.emailIncorrent));
            return;
        }else if (password.length()<6){
            ToastUtil.showShort(context,getString(R.string.passwordTooShort));
            return;
        }
        final ProgressDialog pro = new ProgressDialog(context);
        pro.setTitle("正在验证");
        pro.setMessage("请稍等...");
        pro.show();

        BmobUser user=new BmobUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.login(context, new SaveListener() {
            @Override
            public void onSuccess() {
                //登陆成功
                pro.dismiss();
                PrefsUtil d = new PrefsUtil(ResetPatternActivity.this, Constants.PatternLock, Context.MODE_PRIVATE);
                String objid = d.getString("PatternUserId");
                BmobUser user1 = BmobUser.getCurrentUser(context);
                if (user1.getObjectId().equals(objid)) {
                    d.putBoolean("isPatternOn", false);
                    startActivity(new Intent(context, MainActivity.class));
                    finish();
                } else {
                    ToastUtil.showShort(context, "验证失败");
                }

            }

            @Override
            public void onFailure(int i, String s) {
                //登陆失败
                pro.dismiss();
                ToastUtil.showShort(context, "验证失败\n" + s);
            }
        });
    }

    private void initTop() {
        titleBack = (ImageView) findViewById(R.id.title_back);
        topText= (TextView) findViewById(R.id.title_text);
        topText.setText("验证登陆");
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void bindViews() {

        mLogin_user_name = (EditText) findViewById(R.id.login_user_name);
        mLogin_password = (EditText) findViewById(R.id.login_password);
        mLogin_login = (Button) findViewById(R.id.login_login);
        mFindPassword= (TextView) findViewById(R.id.findPassword);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ResetPatternActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ResetPatternActivity");
        MobclickAgent.onPause(this);
    }
}
