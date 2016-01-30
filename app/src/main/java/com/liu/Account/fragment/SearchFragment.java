package com.liu.Account.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.liu.Account.Constants.Constants;
import com.liu.Account.Constants.TagConstats;
import com.liu.Account.R;
import com.liu.Account.activity.LookBillActivity;
import com.liu.Account.adapter.HomeListViewAdapter;
import com.liu.Account.application.ApplicationDatas;
import com.liu.Account.commonUtils.DateUtil;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.model.HomeListViewData;
import com.liu.Account.model.SearchData;
import com.liu.Account.utils.DatabaseUtil;
import com.liu.Account.utils.NumberUtil;
import com.squareup.timessquare.CalendarPickerView;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by deonte on 16-1-23.
 */
public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Activity activity;
    private View view;
    private SearchView searchView;


    private ListView listView;
    private HomeListViewAdapter adapter;
    private List<HomeListViewData> mDataArrays = new ArrayList<>();
    private TextView accountSize,accountMoney;
    private Button time;

    private Spinner mTagType;
    private Spinner mInOrOut;
    private Spinner mOrderBy;
    private Button mOrderWay;
    private ImageView mOrderWayI;
    private ArrayAdapter<String> mTagTypeAdapter;
    private ArrayAdapter<String> mInOrOutAdapter;
    private ArrayAdapter<String> mOrderByAdapter;
    private ArrayAdapter<String> mOrderWayAdapter;



    private SearchData data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_search, container, false);

        activity=getActivity();
        initView();
        return view;
    }

    private void initView() {
        //数量和金额
        accountMoney= (TextView) view.findViewById(R.id.search_money);
        accountSize= (TextView) view.findViewById(R.id.search_size);
        time= (Button) view.findViewById(R.id.search_Time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CalendarPickerView dialogView= (CalendarPickerView) activity.getLayoutInflater().inflate(R.layout.dialog_timepick,null,false);

                Calendar start=Calendar.getInstance();
                start.add(Calendar.YEAR,-100);
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 1);

                final ArrayList<Date> dates=new ArrayList<Date>();
                Calendar startTime=Calendar.getInstance();
                startTime.setTimeInMillis(Long.parseLong(data.getStartTime()));

                Calendar endTime = Calendar.getInstance();
                endTime.setTimeInMillis(Long.parseLong(data.getEndTime()));

                dates.add(startTime.getTime());
                dates.add(endTime.getTime());


                dialogView.init(start.getTime(),
                        end.getTime()) //
                        .inMode(CalendarPickerView.SelectionMode.RANGE)
                        .withSelectedDates(dates);
                //
                AlertDialog theDialog = new AlertDialog.Builder(activity) //
                        .setTitle("请选取日期范围")
                        .setView(dialogView)
                        .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<Date> dates1=dialogView.getSelectedDates();
                                LogUtil.i("获取的日期数" + dates1.size());
                                long st=dates1.get(0).getTime();
                                long en=dates1.get(dates1.size()-1).getTime();
                                Calendar calendar=Calendar.getInstance();
                                calendar.setTimeInMillis(en);
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                                LogUtil.i("startTime:" + st);
                                LogUtil.i("endTime:" + calendar.getTimeInMillis());
                                String temp=DateUtil.getStringByFormat(st, DateUtil.dateFormatYMDD)
                                        +"——"+DateUtil.getStringByFormat(en, DateUtil.dateFormatYMD);
                                time.setText(temp);
                                data.setStartTime(String.valueOf(st));
                                data.setEndTime(String.valueOf(calendar.getTimeInMillis()));
                                queryString();
                            }
                        })
                        .create();
                theDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        //Log.d(TAG, "onShow: fix the dimens!");
                        dialogView.fixDialogDimens();
                    }
                });
                theDialog.show();

            }
        });

        //几个spinner
        mTagType = (Spinner) view.findViewById(R.id.search_tagType);
        mInOrOut = (Spinner) view.findViewById(R.id.search_inOrOut);
        mOrderBy = (Spinner) view.findViewById(R.id.search_orderBy);
        mOrderWay = (Button) view.findViewById(R.id.search_orderWay);
        mOrderWayI= (ImageView) view.findViewById(R.id.search_orderWayi);

        //选择排序方式
        mOrderWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean is = data.getOrderWay().endsWith(TagConstats.OrderWaySelect[0]);
                if (is) {
                    //降序
                    ObjectAnimator
                            .ofFloat(mOrderWayI, "rotation", 0.0f, 180.0f)
                            .setDuration(100).start();
                    data.setOrderWay(TagConstats.OrderWaySelect[1]);
                    mOrderWay.setText(TagConstats.OrderWayShow[1]);

                    queryString();
                } else {
                    //升序
                    data.setOrderWay(TagConstats.OrderWaySelect[0]);
                    mOrderWay.setText(TagConstats.OrderWayShow[0]);
                    ObjectAnimator
                            .ofFloat(mOrderWayI, "rotation", 180.0f, 360.0f)
                            .setDuration(100).start();
                    queryString();
                }
            }
        });
        //为spinner绑定数据
        //将可选内容与ArrayAdapter连接起来
        mTagTypeAdapter= new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,TagConstats.TagTypeShow);
        //设置下拉列表的风格
        mTagTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        mTagType.setAdapter(mTagTypeAdapter);
        mTagType.setOnItemSelectedListener(this);

        //收入或支出
        mInOrOutAdapter= new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,TagConstats.InOrOutShow);
        mInOrOutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mInOrOut.setAdapter(mInOrOutAdapter);
        mInOrOut.setOnItemSelectedListener(this);

        //排序依据
        mOrderByAdapter= new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,TagConstats.OrderByShow);
        mOrderByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOrderBy.setAdapter(mOrderByAdapter);
        mOrderBy.setOnItemSelectedListener(this);


        listView= (ListView) view.findViewById(R.id.searchList);
        adapter=new HomeListViewAdapter(activity,mDataArrays);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });
    }


    @Override
    public void onStart() {
        super.onStart();

        data=new SearchData();
        Calendar calendar=Calendar.getInstance();
        data.setStartTime(String.valueOf(DateUtil.getFirstDayOfMonth()));
        data.setEndTime(String.valueOf(calendar.getTimeInMillis()));

        String temp=DateUtil.getStringByFormat(DateUtil.getFirstDayOfMonth(), DateUtil.dateFormatYMDD)
                +"——"+DateUtil.getStringByFormat(calendar.getTimeInMillis(), DateUtil.dateFormatYMD);
        time.setText(temp);
        LogUtil.i("startTime" +DateUtil.getStringByFormat(DateUtil.getFirstDayOfMonth(), DateUtil.dateFormatYMD) );
        LogUtil.i("endTime"+DateUtil.getStringByFormat(calendar.getTimeInMillis(), DateUtil.dateFormatYMD));


        final ApplicationDatas datas= (ApplicationDatas) activity.getApplication();
        searchView=datas.getSearchView();
        searchView.setQueryHint("搜索账单备注...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LogUtil.i("输入的搜索词" + query);
                data.setSearchString(query);
                queryString();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LogUtil.i("2");
                data.setSearchString(newText);
                queryString();
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //// TODO: 16-1-28 搜索的标准写法
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.search_tagType:
                ////// TODO: 2015/12/13 tag
                //  datas.setTag(aOrderBy[position]);
                data.setTag(TagConstats.TagTypeSelect[position]);
                LogUtil.i("选择了tag:" + data.getTag());
                queryString();
                break;
            case R.id.search_inOrOut:
                data.setInOrOut(TagConstats.InOrOutSelect[position]);
                LogUtil.i("选择了收入或支出:" + data.getInOrOut());
                queryString();
                break;
            case R.id.search_orderBy:
                data.setOrderBy(TagConstats.OrderBySelet[position]);
                queryString();
                break;
        }
    }

    private void queryString() {
        String query=null;
        if (data.getInOrOut().equals(TagConstats.InOrOutSelect[TagConstats.InOrOutPosition])) {
            //如果选取 收入 支出 类型为全部
            if (data.getTag().equals(TagConstats.TagTypeSelect[TagConstats.TagTypePosition])) {
                //选取的标签也是全部
                query = "select * from " + Constants.tableName + " where unixTime BETWEEN '" + data.getStartTime() + "' AND " +
                        "' " + data.getEndTime() +
                        "'and remark like '%" + data.getSearchString() + "%" +
                        "' order by  " + data.getOrderBy() + " " + data.getOrderWay();
            } else {
                //选取的标签不是全部
                query = "select * from " + Constants.tableName +
                        " where unixTime BETWEEN '" + data.getStartTime() + "' AND " + "' " + data.getEndTime() +
                        "'and remark like '%" + data.getSearchString() + "%" +
                        "'and Tag='" + data.getTag() +
                        "'order by  " + data.getOrderBy() + " "
                        + data.getOrderWay();
            }
        } else {
            //如果选取 收入 支出 类型不是全部
            if (data.getTag().equals(TagConstats.TagTypeSelect[TagConstats.TagTypePosition])) {
                //选取的标签时全部
                query = "select * from " + Constants.tableName +
                        " where unixTime  BETWEEN '" + data.getStartTime() + "' AND " + "'" + data.getEndTime() +
                        "'and remark like '%" + data.getSearchString() + "%" +
                        "' and  moneyType='" + data.getInOrOut() +
                        "' order by " + data.getOrderBy() + " "
                        + data.getOrderWay();
            } else {
                //选取的标签不是全部
                query = "select * from " + Constants.tableName +
                        " where unixTime  BETWEEN '" + data.getStartTime() + "' AND " + "'" + data.getEndTime() +
                        "' and remark like '%" + data.getSearchString() + "%" +
                        "' and  moneyType='" + data.getInOrOut() +
                        "' and Tag='" + data.getTag() + "' " +
                        " order by " + data.getOrderBy() + " "
                        + data.getOrderWay();
            }
        }

        LogUtil.i("查询语句"+query);
        query(query);
    }

    private void query(String query) {
        mDataArrays.clear();
        adapter.notifyDataSetChanged();
        accountMoney.setText("账单金额: ---");
        float  money=0;
        DatabaseUtil databaseUtil=new DatabaseUtil(activity,Constants.DBNAME,1);
        Cursor cursor=databaseUtil.queryCursor(query, null);
        LogUtil.i("搜索数目" + cursor.getCount());
        accountSize.setText("账单数目: " + cursor.getCount() + "笔");
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

                if (moneyType.equals("收入")){
                    money = money + Float.parseFloat(spendMoney);
                }else {
                    money = money - Float.parseFloat(spendMoney);
                }

                money = NumberUtil.roundHalfUp(money);
                if (creatTime==null){
                    creatTime="---------";
                }
                addToList(date, remark, spendMoney, unixtime, moneyType, creatTime, tag);
            } catch (Exception e) {
                LogUtil.i(e.toString());
            }
        }
        if (money!=0) {
            String te=null;
            if (money>10000||money<-10000){
                te=NumberUtil.roundHalfUp(money/10000)+"万";
            }else {
                te=NumberUtil.roundHalfUp(money)+"元";
            }
            accountMoney.setText("账单金额: " + te);
        }
        databaseUtil.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SearchFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SearchFragment");
    }
}
