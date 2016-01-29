package com.liu.Account.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.Account.Constants.Constants;
import com.liu.Account.R;
import com.liu.Account.commonUtils.AppUtil;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.commonUtils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;

/**
 * Created by deonte on 16-1-25.
 */
public class RetrievePasswordActivity extends AutoLayoutActivity{
    private Context context;
    private ImageView titleBack;
    private TextView topText;

    private EditText mUser_name;
    private Button mRetrieve;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);
        context=RetrievePasswordActivity.this;
        initTop();
        bindViews();
        mRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName=mUser_name.getText().toString().trim();
                retrievePassword(userName);
            }
        });
    }

    private void retrievePassword(String userName) {
        if (userName==null) {
            ToastUtil.showShort(context, getString(R.string.userNameNull));
            return;
        }else if (!AppUtil.isNetworkOK(context)){
            ToastUtil.showShort(context,getString(R.string.loginNetworkFalse));
            return;
        }else if (!AppUtil.isEmailNO(userName)){
            ToastUtil.showShort(context,getString(R.string.emailIncorrent));
            return;
        }
        BmobUser.resetPasswordByEmail(context, userName, new ResetPasswordByEmailListener() {
            @Override
            public void onSuccess() {
                Dialog dialog = new AlertDialog.Builder(context)
                        .setTitle("重置密码成功")
                        .setMessage("重置密码成功,请进入您的邮箱进行后续操作")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
                dialog.show();
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.showShort(context, "重置密码失败\n" + s);
            }
        });
    }

    private void bindViews() {
        mUser_name= (EditText) findViewById(R.id.retrieve_user_name);
        mRetrieve= (Button) findViewById(R.id.retrieve_password);
        AppUtil.requestFocus(mRetrieve);
    }

    private void initTop() {
        titleBack = (ImageView) findViewById(R.id.title_back);
        topText= (TextView) findViewById(R.id.title_text);
        topText.setText(R.string.retrieve_password);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("RetrievePasswordActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("RetrievePasswordActivity");
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
