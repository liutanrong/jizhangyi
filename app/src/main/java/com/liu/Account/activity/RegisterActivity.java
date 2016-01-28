package com.liu.Account.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.Account.BmobRespose.BmobUsers;
import com.liu.Account.R;
import com.liu.Account.commonUtils.AppUtil;
import com.liu.Account.commonUtils.ToastUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by deonte on 16-1-25.
 */
public class RegisterActivity extends AutoLayoutActivity{
    private Context context;
    private ImageView titleBack;
    private TextView topText;

    private EditText mRegister_user_name;
    private EditText mRegister_password;
    private EditText mRegister_password_t;
    private Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context=RegisterActivity.this;
        initTop();
        bindViews();
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mRegister_user_name.getText().toString().trim();
                String password1 = mRegister_password.getText().toString().trim();
                String password2 = mRegister_password_t.getText().toString().trim();

                register(userName, password1, password2);
            }
        });
    }

    private void register(String userName, String password1, String password2) {

        if (userName==null) {
            ToastUtil.showShort(context, getString(R.string.userNameNull));
            return;
        }else if (password1==null){
            ToastUtil.showShort(context, getString(R.string.passwordNull));
            return;
        }else if (!password1.equals(password2)) {
            ToastUtil.showShort(context, getString(R.string.passwordNotSame));
            return;
        }else if (password1.length()<6){
            ToastUtil.showShort(context,getString(R.string.passwordTooShort));
        }else if (!AppUtil.isNetworkOK(context)){
            ToastUtil.showShort(context,getString(R.string.loginNetworkFalse));
            return;
        }else if (!AppUtil.isEmailNO(userName)){
            ToastUtil.showShort(context,getString(R.string.emailIncorrent));
            return;
        }
        BmobUsers us = new BmobUsers();
        us.setUsername(userName);
        us.setPassword(password1);
        us.setPasswordd(password1);
        us.setEmail(userName);
        final ProgressDialog pro = new ProgressDialog(context);
        pro.setTitle("注册中，请稍候");
        pro.setMessage("请稍等...");
        pro.show();
        us.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
                pro.dismiss();
                Dialog dialog =new AlertDialog.Builder(context)
                        .setTitle("注册成功")
                        .setMessage("请进入您的邮箱完成邮箱验证,以方便您找回密码")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(context,AccountActivity.class));
                                finish();
                            }
                        }).create();
                dialog.show();
            }

            @Override
            public void onFailure(int i, String s) {
                pro.dismiss();
                ToastUtil.showShort(context,"注册失败"+s);
            }
        });
    }

    private void initTop() {
        titleBack = (ImageView) findViewById(R.id.title_back);
        topText= (TextView) findViewById(R.id.title_text);
        topText.setText(R.string.register);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void bindViews() {

        mRegister_user_name = (EditText) findViewById(R.id.register_user_name);
        mRegister_password = (EditText) findViewById(R.id.register_password);
        mRegister_password_t = (EditText) findViewById(R.id.register_password_t);
        mRegister = (Button) findViewById(R.id.register);
    }
}
