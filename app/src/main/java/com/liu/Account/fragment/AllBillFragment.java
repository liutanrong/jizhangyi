package com.liu.Account.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.liu.Account.Constants.Constants;
import com.liu.Account.Constants.TagConstats;
import com.liu.Account.R;
import com.liu.Account.activity.LookBillActivity;
import com.liu.Account.adapter.AllBillListAdapter;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.model.AllBillListGroupData;
import com.liu.Account.model.HomeListViewData;
import com.liu.Account.utils.DatabaseUtil;
import com.liu.Account.utils.NumberUtil;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deonte on 16-1-23.
 */
public class AllBillFragment extends Fragment implements ExpandableListView.OnChildClickListener{
    View view;
    private Activity activity;
    private ExpandableListView listView;
    private AllBillListAdapter adapter;
    private List<AllBillListGroupData> groupDatas = new ArrayList<>();

    private DatabaseUtil db;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_all_bill,container,false);
        activity = getActivity();

        listView= (ExpandableListView) view.findViewById(R.id.allBill_list);
        adapter = new AllBillListAdapter(activity,groupDatas);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initArray();
    }

    private void initArray() {
        groupDatas.clear();
        adapter.notifyDataSetChanged();
        db=new DatabaseUtil(activity,Constants.DBNAME,1);
        String query;
        query="select distinct year_date,month_date from "+Constants.tableName+" order by unixtime desc";

        try {
            Cursor cursor = db.queryCursor(query,null);
            while (cursor.moveToNext()){
                String year = cursor.getString(cursor.getColumnIndex("year_date"));
                String month = cursor.getString(cursor.getColumnIndex("month_date"));
                String[] count=getCount(year, month);
                //返回 count0 是收支金额  count1 是收入或支出 count2 为总支出 count3 为总收入

                List<HomeListViewData> child=new ArrayList<>();

                HomeListViewData d=new HomeListViewData();
                d.setTotalMoney(count[0]);
                d.setAllInMoney(count[3]);
                d.setAllOutMoney(count[2]);
                child.add(d);
                // child.add(d);
                initChild(year, month, child);

                AllBillListGroupData entity =new AllBillListGroupData();
                entity.setMoneyType(count[1]);
                entity.setTotalMoney(count[0]);
                entity.setAllTime(year + "年" + month + "月");
                entity.setChild(child);
                groupDatas.add(entity);
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
    }
    /**
     * @param year,month,child,order,way 参数
     *
     * **/

    private void initChild(String year, String month, List<HomeListViewData> child) {
        String query = "select  * from "+Constants.tableName+" where year_date='" + year + "' and month_date='" + month + "' order by unixtime desc";

        db=new DatabaseUtil(activity,Constants.DBNAME,1);
        try{
            Cursor cursor = db.queryCursor(query,null);
            while (cursor.moveToNext()) {
                //遍历
                try {
                    String remark = cursor.getString(cursor.getColumnIndex("remark"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String unixtime = cursor.getString(cursor.getColumnIndex("unixTime"));
                    String spendMoney = cursor.getString(cursor.getColumnIndex("spendMoney"));
                    String moneyType=cursor.getString(cursor.getColumnIndex("moneyType"));
                    String creatTime=cursor.getString(cursor.getColumnIndex("creatTime"));
                    String tag=cursor.getString(cursor.getColumnIndex("Tag"));

                    if (creatTime==null){
                        creatTime = "---------";
                    }
                    HomeListViewData d = new HomeListViewData();
                    d.setCreatTime(creatTime);
                    d.setMoney(spendMoney);
                    d.setUnixTime(unixtime);
                    d.setDate(date);
                    d.setRemark(remark);
                    d.setTag(tag);
                    for (int i=0;i< TagConstats.tagList.length;i++){
                        if (TagConstats.tagList[i].equals(tag))
                            d.set_tagID(TagConstats.tagImage[i]);
                    }
                    if (moneyType.equals(getString(R.string.MoneyIn)))
                        d.setMoneyType("+");
                    else if (moneyType.equals(getString(R.string.MoneyOut)))
                        d.setMoneyType("-");
                    child.add(d);
                } catch (Exception e) {
                    print(e.toString());
                }
            }

        } catch (Exception e) {
            print(e.toString());
        }
        db.close();
    }

    /**
     * @param month 要查询的月份
     * @param year 要查询的年份
     * @return count[4]  0 总收支 1 收支类别(收入或支出) 2 总支出 3 总收入
     * **/
    private String[] getCount(String year,String month) {
        float InCount = 0;
        float OutCount = 0;
        float allCount;
        String[] count = new String[4];
        String query = "select  * from "+Constants.tableName+" where year_date='" + year + "' and month_date='" + month + "' order by unixtime desc";


        db=new DatabaseUtil(activity,Constants.DBNAME,1);
        try {
            Cursor cursor = db.queryCursor(query,null);
            while (cursor.moveToNext()) {
                //遍历
                try {
                    String spendMoney = cursor.getString(cursor.getColumnIndex("spendMoney"));
                    String moneyType=cursor.getString(cursor.getColumnIndex("moneyType"));
                    if (moneyType.equals("收入")) {
                        InCount = InCount + Float.parseFloat(spendMoney);
                    } else {
                        OutCount = OutCount + Float.parseFloat(spendMoney);
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        allCount = OutCount - InCount;


        OutCount=NumberUtil.roundHalfUp(OutCount);
        allCount=NumberUtil.roundHalfUp(allCount);

        if (allCount>10000){
            allCount= NumberUtil.roundHalfUp(allCount/10000);
            count[0] = String.valueOf(allCount)+"万";
        }else {
            allCount= NumberUtil.roundHalfUp(allCount);
            count[0] = String.valueOf(allCount);
        }

        if (OutCount>10000){
            OutCount= NumberUtil.roundHalfUp(OutCount/10000);
            count[2] = String.valueOf(OutCount)+"万";
        }else {
            OutCount= NumberUtil.roundHalfUp(OutCount);
            count[2]= String.valueOf(OutCount);
        }

        if (InCount>10000){
            InCount= NumberUtil.roundHalfUp(InCount/10000);
            count[3] = String.valueOf(InCount)+"万";
        }else {
            InCount= NumberUtil.roundHalfUp(InCount);
            count[3]= String.valueOf(InCount);
        }

        if (allCount > 0){
            count[1] = getString(R.string.MoneyOut);
        }else {
            count[1]=getString(R.string.MoneyIn);
        }

        return count;
    }

    String unixTime,tag;
    String  date, remark, money,moneyType,creatTime=null;
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (childPosition==0)
            return false;
        print("当前点击了第" + groupPosition + " "+childPosition);
        TextView n = (TextView) view.findViewById(R.id.unixTime);
        TextView da = (TextView) view.findViewById(R.id.dateInList);
        TextView re = (TextView) view.findViewById(R.id.remarkInList);
        TextView mo = (TextView) view.findViewById(R.id.spendMoneyInList);
        TextView sp= (TextView) view.findViewById(R.id.money_type);
        TextView cr= (TextView) view.findViewById(R.id.creatTime);
        TextView ta= (TextView) view.findViewById(R.id.tagText);
        unixTime = n.getText().toString();
        date = da.getText().toString();
        remark = re.getText().toString();
        money = mo.getText().toString();
        creatTime=cr.getText().toString();
        tag=ta.getText().toString();
        String mon=sp.getText().toString();
        if (mon.equals("+")){
            moneyType="收入";
        }else {
            moneyType="支出";
        }
        Bundle bundle=new Bundle();
        bundle.putString("money",money);
        bundle.putString("remark",remark);
        bundle.putString("date",date);
        bundle.putString("tag",tag);
        bundle.putString("moneyType",moneyType);
        bundle.putString("creatTime", creatTime);
        bundle.putString("unixTime", unixTime);
        Intent it=new Intent(activity,LookBillActivity.class);
        it.putExtras(bundle);
        activity.startActivity(it);

        return false;
    }
    private void print(String s){
        LogUtil.i(s);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AllBillFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AllBillFragment");
    }
}
