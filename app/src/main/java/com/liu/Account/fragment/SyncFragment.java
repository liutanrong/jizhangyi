package com.liu.Account.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.liu.Account.BmobRespose.BmobUsers;
import com.liu.Account.R;
import com.liu.Account.activity.LoginActivity;
import com.liu.Account.commonUtils.ToastUtil;
import com.liu.Account.BmobNetwork.BmobNetworkUtils;
import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.BmobUser;

/**
 * Created by deonte on 16-1-23.
 */
public class SyncFragment extends Fragment implements View.OnClickListener {
    View view;
    private Activity activity;
    private TextView lastUpdateTime;
    private Button update;
    private Button downland;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_sync,container,false);

        activity=getActivity();
        initView();

        return view;
    }

    private void initView() {
        lastUpdateTime= (TextView) view.findViewById(R.id.sync_lastUpdateTime);
        update= (Button) view.findViewById(R.id.sync_update);
        downland= (Button) view.findViewById(R.id.sync_downland);
        update.setOnClickListener(this);
        downland.setOnClickListener(this);


    }

    @Override
    public void onStart() {
        super.onStart();
        BmobUsers users=BmobUser.getCurrentUser(activity,BmobUsers.class);
        if (users!=null&&users.getDBupdateDate()!=null){
            lastUpdateTime.setText(users.getDBupdateDate());
        }else {
            lastUpdateTime.setText("---------");
        }
    }

    @Override
    public void onClick(View v) {
        BmobUsers user=BmobUser.getCurrentUser(activity,BmobUsers.class);
        switch (v.getId()){
            case R.id.sync_update:{
                ////  16-1-26 上传数据
                if (user!=null){
                    BmobNetworkUtils d=new BmobNetworkUtils(activity);
                    d.upDatesToBmobWithDialog(activity);
                }else {
                    Dialog dialog =new AlertDialog.Builder(activity)
                            .setTitle("需要登陆")
                            .setMessage("登陆后才能进行同步操作,是否去登陆")
                            .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.startActivity(new Intent(activity, LoginActivity.class));
                                }
                            })
                            .setNegativeButton("取消",null).create();
                    dialog.show();
                }
                onStart();
                break;
            }case R.id.sync_downland:{
                ////  16-1-26 下载数据
                if (user!=null){
                    BmobUsers bmobUser=BmobUser.getCurrentUser(activity,BmobUsers.class);
                    if (bmobUser.getFileName()!=null){
                        BmobNetworkUtils dd=new BmobNetworkUtils(activity);
                        dd.getDatasFromBmob(activity, bmobUser.getFileName());
                    }else {
                        ToastUtil.showShort(activity, "您还没有上传数据呢");
                    }
                }else {
                    Dialog dialog =new AlertDialog.Builder(activity)
                            .setTitle("需要登陆")
                            .setMessage("登陆后才能进行同步操作,是否去登陆")
                            .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.startActivity(new Intent(activity, LoginActivity.class));
                                }
                            })
                            .setNegativeButton("取消",null).create();
                    dialog.show();
                }



                break;

            }
        }
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SyncFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SyncFragment");
    }
}
