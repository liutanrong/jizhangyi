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
import com.liu.Account.Database.DatabaseUtil;
import com.liu.Account.R;
import com.liu.Account.activity.LoginActivity;
import com.liu.Account.commonUtils.DateUtil;
import com.liu.Account.commonUtils.IntentUtil;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.commonUtils.ToastUtil;
import com.liu.Account.BmobNetwork.BmobNetworkUtils;
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
import com.liu.Account.Database.billdate;
/**
 * Created by deonte on 16-1-23.
 */
public class SyncFragment extends Fragment implements View.OnClickListener {

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
                String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};
                int permsRequestCode = 200;
                requestPermissions(perms, permsRequestCode);
                break;
            }case R.id.csv2t:{
                LogUtil.i("从CSV导入数据");
                String[] perms = {"android.permission.READ_EXTERNAL_STORAGE"};
                int permsRequestCode = 201;
                requestPermissions(perms, permsRequestCode);
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
            case 200:
                //请求读写sd卡权限
                boolean writeAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                LogUtil.i("权限:"+writeAccepted);
                if (!writeAccepted){
                    new AlertDialog.Builder(activity).setTitle("缺少权限")
                            .setMessage("缺少运行该功能必须的读写SD卡权限,请检查权限设置")
                            .setPositiveButton("确定",null)
                            .show();
                }
                writeToCsv();

                break;
            case 201:
                //读取sd卡权限
                boolean Accepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                LogUtil.i("权限:"+Accepted);
                if (!Accepted){
                    new AlertDialog.Builder(activity).setTitle("缺少权限")
                            .setMessage("缺少运行该功能必须的读写SD卡权限,请检查权限设置")
                            .setPositiveButton("确定",null)
                            .show();
                }

                Intent it= IntentUtil.newPickFileIntent();
                activity.startActivityForResult(it,100);

                break;
        }
    }

    private void writeToCsv() {
        DatabaseUtil db=new DatabaseUtil(getActivity(), Constants.DBNAME,1);
        List<billdate> list = db.exportToList();

        StringBuffer buffer = new StringBuffer();
      //  buffer.append("id,订单金额,订单备注,订单时间,订单时间戳,订单创建时间,订单类型,订单标签,订单年,订单月,订单日\r\n");
        for(billdate u:list){
            buffer.append(u.get_Id()+","
                    +u.getSpendMoney()+","
                    +u.getRemark()+","
                    +u.getDate()+","
                    +u.getUnixTime()+","
                    +u.getCreatTime()+","
                    +u.getMoneyType()+","
                    +u.getTag()+","
                    +u.getYear_date()+","
                    +u.getMonth_date()+","
                    +u.getDay_year()
                    +"\r\n");
        }

        try {
//				String data =new String(buffer.toString().getBytes("utf-8"), "ansi") ;
            String data = buffer.toString();

            String filename = "jizhangyi_"+ DateUtil.getCurrentDate(DateUtil.dateFormatYMDHMS)+".csv";

            String path = Environment.getExternalStorageDirectory()+Constants.FileName;

            if (!new File(path).exists()) {
                new File(path).mkdirs();
            }

            File file = new File(path, filename);
            ToastUtil.showShort(activity, file.getAbsolutePath());
            OutputStream out=new FileOutputStream(file);
            // excel需要BOM签名才能解析utf-8格式的编码

            ToastUtil.showShort(activity, "文件路径" + path);
            byte b[] = {(byte)0xEF, (byte)0xBB, (byte)0xBF};

            out.write(b);
            out.write(data.getBytes());
            out.close();
            new AlertDialog.Builder(activity).setTitle("导出成功")
                    .setMessage("数据导出成功,可在sd卡下jizhangyi目录下查看")
                    .setPositiveButton("确定",null)
                    .show();
        } catch (Exception e) {
            LogUtil.i("导出文件失败:" + e.toString());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i("activityResult:"+requestCode+"  "+resultCode);
        if (!(resultCode==Activity.RESULT_OK)||data==null){
            LogUtil.i("选取失败");
            return;
        }
        switch (requestCode){
            case 100:
                //选取文件
                DatabaseUtil db=new DatabaseUtil(activity,Constants.DBNAME,1);
                db.exportFromList(csvToList(data));
                break;
        }
    }

    private List<billdate> csvToList(Intent data) {
        Uri uri=data.getData();
        File file=new File(uri.getPath());
        List<billdate> list=new ArrayList<billdate>();
        try {

            BufferedReader br = new BufferedReader(new FileReader(file));
            // 读取直到最后一行
            String line = "";
            while ((line = br.readLine()) != null) {
                // 把一行数据分割成多个字段
                StringTokenizer st = new StringTokenizer(line, ",");
                String[] colums=line.split(",");
                if (colums.length!=11){
                    LogUtil.d("此行为坏行 跳过");
                    continue;
                }
                billdate bt =new billdate();
                bt.setSpendMoney(colums[1]);
                bt.setRemark(colums[2]);
                bt.setDate(colums[3]);
                bt.setUnixTime(colums[4]);
                bt.setCreatTime(colums[5]);
                bt.setMoneyType(colums[6]);
                bt.setTag(colums[7]);
                bt.setYear_date(colums[8]);
                bt.setMonth_date(colums[9]);
                bt.setDay_year(colums[10]);
                list.add(bt);
                LogUtil.i("账单id:"+colums[0]+"账单备注:"+colums[2]);
            }
            br.close();

        } catch (FileNotFoundException e) {
            // 捕获File对象生成时的异常
            e.printStackTrace();
        } catch (IOException e) {
            // 捕获BufferedReader对象关闭时的异常
            e.printStackTrace();
        }
        return list;
    }
}

