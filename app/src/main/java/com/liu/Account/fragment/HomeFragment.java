package com.liu.Account.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.liu.Account.Constants.Constants;
import com.liu.Account.Constants.TagConstats;
import com.liu.Account.R;
import com.liu.Account.activity.AddBillActivity;
import com.liu.Account.activity.LookBillActivity;
import com.liu.Account.adapter.HomeListViewAdapter;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.model.HomeListViewData;
import com.liu.Account.utils.DatabaseUtil;
import com.liu.Account.utils.NumberUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by deonte on 16-1-23.
 */
public class HomeFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{
    private Activity activity;

    View view;
    private FloatingActionButton add;

    private ListView listView;
    private HomeListViewAdapter adapter;
    private List<HomeListViewData> mDataArrays = new ArrayList<>();

    private TextView moneyIn,moneyOut,moneyAll,date;
    private int currentYear,currentMonth;

    DatabaseUtil db;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home, container, false);

        activity=getActivity();
        initView();//初始化控件并设置点击
        return view;
    }

    private void initView() {

        add= (FloatingActionButton) view.findViewById(R.id.add);
        add.setOnClickListener(this);

        moneyAll= (TextView) view.findViewById(R.id.home_money);
        moneyIn= (TextView) view.findViewById(R.id.home_money_in);
        moneyOut= (TextView) view.findViewById(R.id.home_money_out);
        date= (TextView) view.findViewById(R.id.home_date);

        listView= (ListView) view.findViewById(R.id.home_listview);
        adapter=new HomeListViewAdapter(activity,mDataArrays);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.i("OnStart");


        db=new DatabaseUtil(activity,Constants.DBNAME,1);

        mDataArrays.clear();
        Calendar calendar=Calendar.getInstance();
        currentYear=calendar.get(Calendar.YEAR);
        currentMonth=calendar.get(Calendar.MONTH)+1;
        date.setText(currentYear+"."+currentMonth);


        String query;
        query="select * from "+ Constants.tableName+" where year_date=? and month_date=? order by unixtime desc";
        Cursor cursor=db.queryCursor(query, new String[]{"" + currentYear, "" + currentMonth});
        initArray(cursor);

        getMoneyCount(cursor);
    }

    private void getMoneyCount(Cursor cursor) {
        if (cursor.getCount()==0)
            return;
        cursor.moveToFirst();
        float _moneyIn=0,_moneyOut=0,_moneyAll=0;

        do{
            String spendMoney = cursor.getString(cursor.getColumnIndex("spendMoney"));
            String moneyType=cursor.getString(cursor.getColumnIndex("moneyType"));

            if (moneyType.equals(getString(R.string.MoneyIn)))
                _moneyIn=_moneyIn+Float.valueOf(spendMoney);
            else
                _moneyOut=_moneyOut+Float.valueOf(spendMoney);
            _moneyAll=_moneyIn-_moneyOut;
        } while (cursor.moveToNext());


        moneyIn.setText(String.valueOf(NumberUtil.roundHalfUp(_moneyIn)));
        moneyOut.setText(String.valueOf(NumberUtil.roundHalfUp(_moneyOut)));
        moneyAll.setText(String.valueOf(NumberUtil.roundHalfUp(_moneyAll)));
        LogUtil.i("收入:" + _moneyIn + "\n支出：" + _moneyOut + "\n总计:" + _moneyAll);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.i("onResume");
    }

    /**
     * 初始化本地数据库内数据
     * **/
    private void initArray(Cursor cursor) {
        if (cursor.getCount()==0)
            return;
        LogUtil.i(currentYear + "." + currentMonth + "数据共有" + cursor.getCount() + "条");

        //{"_Id","spendMoney","remark","date","unixTime","creatTime","moneyType","Tag","year_date","month_date","day_year"};
        while (cursor.moveToNext()){
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String unixtime = cursor.getString(cursor.getColumnIndex("unixTime"));
            String spendMoney = cursor.getString(cursor.getColumnIndex("spendMoney"));
            String moneyType=cursor.getString(cursor.getColumnIndex("moneyType"));
            String creatTime=cursor.getString(cursor.getColumnIndex("creatTime"));
            String tag=cursor.getString(cursor.getColumnIndex("Tag"));
            addToList(date,remark,spendMoney,unixtime,moneyType,creatTime,tag);
        }
        adapter.notifyDataSetChanged();
    }
    /**
     * 添加数据到列表
     * **/
    private void addToList(String date,String remark,String spendMoney,String unixtime,String moneyType,String creatTime,String tag){
        HomeListViewData data=new HomeListViewData();
        data.setDate(date);
        data.setRemark(remark);
        data.setMoney(spendMoney);
        data.setUnixTime(unixtime);
        data.setCreatTime(creatTime);
        data.setMoneyType(moneyType);
        if (moneyType.equals(getString(R.string.MoneyIn)))
            data.setMoneyType("+");
        else
            data.setMoneyType("-");
        data.setTag(tag);
        for (int i=0;i< TagConstats.tagList.length;i++){
            if (tag.equals(TagConstats.tagList[i]))
                data.set_tagID(TagConstats.tagImage[i]);
        }

        mDataArrays.add(data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:{
                //添加的点击事件
                startActivity(new Intent(activity, AddBillActivity.class));
            }
        }
    }
    String unixTime,datee,rem,money,creatTime,tag,moneyType;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView n = (TextView) view.findViewById(R.id.unixTime);
        TextView da = (TextView) view.findViewById(R.id.dateInList);
        TextView re = (TextView) view.findViewById(R.id.remarkInList);
        TextView mo = (TextView) view.findViewById(R.id.spendMoneyInList);
        TextView sp= (TextView) view.findViewById(R.id.money_type);
        TextView cr= (TextView) view.findViewById(R.id.creatTime);
        TextView ta= (TextView) view.findViewById(R.id.tagText);
        unixTime = n.getText().toString();
        datee = da.getText().toString();
        rem = re.getText().toString();
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
        bundle.putString("remark",rem);
        bundle.putString("date",datee);
        bundle.putString("tag",tag);
        bundle.putString("moneyType",moneyType);
        bundle.putString("creatTime", creatTime);
        bundle.putString("unixTime", unixTime);
        Intent it=new Intent(activity,LookBillActivity.class);
        it.putExtras(bundle);
        activity.startActivity(it);
     }
}
