package com.liu.Account.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.Account.Constants.Constants;
import com.liu.Account.R;
import com.liu.Account.utils.DatabaseUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import cn.bmob.v3.BmobUser;

/**
 * Created by deonte on 16-1-25.
 */
public class AccountActivity extends AutoLayoutActivity implements View.OnClickListener{
    private Context context;
    private ImageView titleBack;
    private TextView topText;

    private Button logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        context=AccountActivity.this;
        initTop();
        initView();
    }

    private void initView() {
        logOut= (Button) findViewById(R.id.account_logOut);
        logOut.setOnClickListener(this);
    }

    private void initTop() {
        titleBack = (ImageView) findViewById(R.id.title_back);
        topText= (TextView) findViewById(R.id.title_text);
        topText.setText("编辑资料");
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_logOut:{
                new AlertDialog.Builder(context).setTitle("退出")
                        .setMessage("退出后，将清除所有本地记录,确定退出?")
                        .setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    DatabaseUtil db =new DatabaseUtil(context,Constants.DBNAME,1);

                                    db.delete(Constants.tableName, "1", null);
                                    db.close();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                BmobUser.logOut(context);   //清除缓存用户对象
                            }
                        }).setNegativeButton("取消",null)
                        .show();
                break;
            }
        }
    }
}
