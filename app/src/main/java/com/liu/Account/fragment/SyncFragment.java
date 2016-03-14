package com.liu.Account.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.liu.Account.BmobRespose.BmobUsers;
import com.liu.Account.Constants.Constants;
import com.liu.Account.Constants.Permissions;
import com.liu.Account.Database.DatabaseUtil;
import com.liu.Account.R;
import com.liu.Account.activity.LoginActivity;
import com.liu.Account.commonUtils.DateUtil;
import com.liu.Account.commonUtils.IntentUtil;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.commonUtils.ToastUtil;
import com.liu.Account.BmobNetwork.BmobNetworkUtils;
import com.liu.Account.utils.CSVUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import cn.bmob.v3.BmobUser;
import com.liu.Account.Database.Billdate;
/**
 * Created by deonte on 16-1-23.
 */
public class SyncFragment extends Fragment implements View.OnClickListener {

    public static final int PICK_FILE=36;
    View view;
    private Activity activity;
    private TextView lastUpdateTime;
    private Button update;
    private Button downland;
    private Button sync2csv;
    private Button csv2this;
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
        sync2csv= (Button) view.findViewById(R.id.sync2csv);
        csv2this= (Button) view.findViewById(R.id.csv2t);
        update.setOnClickListener(this);
        downland.setOnClickListener(this);
        sync2csv.setOnClickListener(this);
        csv2this.setOnClickListener(this);


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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        BmobUsers user=BmobUser.getCurrentUser(activity,BmobUsers.class);
        switch (v.getId()){
            case R.id.sync_update:{
                ////  16-1-26 上传数据
                if (user!=null){
                    BmobNetworkUtils d=new BmobNetworkUtils(activity);
                    d.upDatesToBmob(activity,true);
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
            }case R.id.sync_downland:{
                ////  16-1-26 下载数据
                if (user!=null){
                    BmobUsers bmobUser=BmobUsers.getCurrentUser(activity,BmobUsers.class);
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
            }case R.id.sync2csv: {
                LogUtil.i("导出到CSV文件");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    LogUtil.i(Build.VERSION.SDK_INT+"  "+Build.VERSION_CODES.M);
                    String[] perms = {Permissions.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(perms, Permissions.WRITE_EXTERNAL_STORAGE_CODE);
                }else {
                    LogUtil.i(Build.VERSION.SDK_INT+" sd "+Build.VERSION_CODES.M);
                    DatabaseUtil db=new DatabaseUtil(activity, Constants.DBNAME,1);
                    List<Billdate> list = db.exportToList();
                    CSVUtils.writeToCsv(list,activity);
                }
                break;
            }case R.id.csv2t:{
                LogUtil.i("从CSV导入数据");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    String[] perms = {Permissions.READ_EXTERNAL_STORAGE};
                    requestPermissions(perms, Permissions.READ_EXTERNAL_STORAGE_CODE);
                }else {
                    Intent it = IntentUtil.newPickFileIntent();
                    activity.startActivityForResult(it, PICK_FILE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Permissions.WRITE_EXTERNAL_STORAGE_CODE:
                //写SD卡权限
                boolean writeAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                LogUtil.i("权限:" + writeAccepted);
                if (!writeAccepted){
                    new AlertDialog.Builder(activity).setTitle("缺少权限")
                            .setMessage("缺少运行该功能必须的读写SD卡权限,请检查权限设置")
                            .setPositiveButton("确定",null)
                            .show();
                }

                DatabaseUtil db=new DatabaseUtil(activity, Constants.DBNAME,1);
                List<Billdate> list = db.exportToList();
                CSVUtils.writeToCsv(list, activity);
                break;
            case Permissions.READ_EXTERNAL_STORAGE_CODE:
                //读SD卡权限
                boolean Accepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                LogUtil.i("权限:"+Accepted);
                if (!Accepted){
                    new AlertDialog.Builder(activity).setTitle("缺少权限")
                            .setMessage("缺少运行该功能必须的读写SD卡权限,请检查权限设置")
                            .setPositiveButton("确定",null)
                            .show();
                }
                Intent it= IntentUtil.newPickFileIntent();

                startActivityForResult(it, Activity.RESULT_FIRST_USER);
               // SyncFragment.this.startActivityForResult(it, PICK_FILE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i("activityResult1:" + requestCode + "  " + resultCode);
        if (!(resultCode== Activity.RESULT_OK)){
            new AlertDialog.Builder(activity).setTitle("文件选取失败")
                    .setMessage("由于未知原因,文件选取失败.请重试")
                    .setPositiveButton("确定",null)
                    .show();
            return;
        }
        switch (requestCode){
            case Activity.RESULT_FIRST_USER:
                //选取文件
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        DatabaseUtil db=new DatabaseUtil(activity,Constants.DBNAME,1);

                        Uri uri=data.getData();
                        LogUtil.i(uri.getPath());
//                        db.exportFromList(CSVUtils.csvToList(data));
                    }
                }.run();
                break;
        }
    }
}

