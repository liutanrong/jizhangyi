package com.liu.Account.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.liu.Account.BmobNetwork.BmobNetworkUtils;
import com.liu.Account.Constants.Constants;
import com.liu.Account.R;
import com.liu.Account.commonUtils.DateUtil;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.commonUtils.ToastUtil;
import com.liu.Account.initUtils.Init;
import com.liu.Account.initUtils.StatusBarUtil;
import com.liu.Account.utils.DatabaseUtil;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;

/**
 * Created by deonte on 16-1-25.
 */
public class LookBillActivity extends AutoLayoutActivity {
    private Context context;
    private ImageView titleBack;
    private TextView topText;
    private TextView rightText;

    private TextView mLookBillType;
    private TextView mLookBillMoney;
    private TextView mLookBillCreatTime;
    private TextView mLookBillOutTime;
    private TextView mLookBillTag;
    private TextView mLookBillRemark;
    private Button mLook_bill_modify;

    private DatabaseUtil db;
    private String unixTime;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_bill);

        context=LookBillActivity.this;
        StatusBarUtil.setTransparentStatusBar(this);
        initTop();
        bindViews();
        db=new DatabaseUtil(context, Constants.DBNAME,1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent it=getIntent();
        bundle=it.getExtras();
        mLookBillMoney.setText(bundle.getString("money")+"元");
        mLookBillCreatTime.setText(bundle.getString("creatTime"));
        unixTime=bundle.getString("unixTime");
        long unixTim=Long.parseLong(unixTime);
        mLookBillOutTime.setText(DateUtil.getStringByFormat(unixTim, DateUtil.dateFormatYMDHMSw));
        mLookBillRemark.setText(bundle.getString("remark"));
        mLookBillTag.setText(bundle.getString("tag"));
        mLookBillType.setText(bundle.getString("moneyType"));
        ////  16-1-25 查看账单
        Map<String,String> map = new HashMap<String,String>();
        try{
            BmobUser user=BmobUser.getCurrentUser(context);

            map.put("type",user.getObjectId()+"查看账单");
        }catch (Exception e){
            e.printStackTrace();
            map.put("type","查看账单");
        }
        MobclickAgent.onEventValue(context, "showAccount", map, 0);
    }
/**
 *  bundle.putString("money",money);
 bundle.putString("remark",rem);
 bundle.putString("date",datee);
 bundle.putString("tag",tag);
 bundle.putString("moneyType",moneyType);
 bundle.putString("creatTime",creatTime);
 bundle.putString("unixTime",unixTime);
 *
 * */
    private void initTop() {
        titleBack = (ImageView) findViewById(R.id.title_back);
        topText= (TextView) findViewById(R.id.title_text);
        topText.setText(R.string.lookBill);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rightText= (TextView) findViewById(R.id.title_right);
        rightText.setText(getResources().getText(R.string.lookBillDelete));
        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });
    }

    private void deleteData() {
        ////16-1-25 删除账单
        Map<String,String> map = new HashMap<String,String>();
        try{
            BmobUser user=BmobUser.getCurrentUser(context);

            map.put("type",user.getObjectId()+"删除账单");
        }catch (Exception e){
            e.printStackTrace();
            map.put("type","删除账单");
        }
        MobclickAgent.onEventValue(context, "delAccount", map, 0);


        Dialog dialog =new AlertDialog.Builder(context)
                .setTitle(R.string.deleteBillTitle)
                .setMessage(R.string.deleteBillMessage)
                .setNegativeButton(R.string.deleteBillNegaBtm, null)
                .setPositiveButton(R.string.deleteBillPosBtm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int i = db.delete(Constants.tableName, "unixTime=?", new String[]{unixTime});
                        LogUtil.i("删除影响的记录数:" + i);
                        if (i == 0) {
                            ToastUtil.showShort(context, getString(R.string.deleteBillFailed));
                        } else {
                            ToastUtil.showShort(context, getString(R.string.deleteBillSuccess));
                        }
                        //// 16-1-25 从云端删除 (上传一次)
                        Thread thread=new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                BmobNetworkUtils d = new BmobNetworkUtils(context);
                                d.upDatesToBmob(context,false);
                            }
                        };
                        thread.run();

                        finish();
                    }
                }).create();
        dialog.show();

    }

    private void bindViews() {

        mLookBillType = (TextView) findViewById(R.id.lookBillType);
        mLookBillMoney = (TextView) findViewById(R.id.lookBillMoney);
        mLookBillCreatTime = (TextView) findViewById(R.id.lookBillCreatTime);
        mLookBillOutTime = (TextView) findViewById(R.id.lookBillOutTime);
        mLookBillTag = (TextView) findViewById(R.id.lookBillTag);
        mLookBillRemark = (TextView) findViewById(R.id.lookBillRemark);
        mLook_bill_modify = (Button) findViewById(R.id.look_bill_modify);
        mLook_bill_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, ModifyBillActivity.class);
                it.putExtras(bundle);
                context.startActivity(it);
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("LookBillActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("LookBillActivity");
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
