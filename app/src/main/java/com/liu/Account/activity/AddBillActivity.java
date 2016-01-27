package com.liu.Account.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.liu.Account.Constants.Constants;
import com.liu.Account.Constants.TagConstats;
import com.liu.Account.R;
import com.liu.Account.adapter.AddBillTagAdapter;
import com.liu.Account.commonUtils.DateUtil;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.commonUtils.ToastUtil;
import com.liu.Account.model.AddBillData;
import com.liu.Account.model.AddBillTagData;
import com.liu.Account.utils.DatabaseUtil;
import com.liu.Account.utils.NumberUtil;
import com.liu.Account.initUtils.StatusBarUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by deonte on 16-1-24.
 */
public class AddBillActivity extends AutoLayoutActivity {
    private Context context;
    private ImageView titleBack;
    private TextView topText;

    private RadioGroup typeRadio;
    private EditText moneyEdt;
    private EditText remarkEdt;
    private LinearLayout dateLin;
    private LinearLayout tagLin;
    private TextView dateText;
    private TextView tagText;
    private ImageView tagImage;
    private Button confirmBtn;


    AddBillData data=new AddBillData(true);

    private AlertDialog tagDialog;
    private ListView tagList;

    private DatabaseUtil db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);


        StatusBarUtil.setTransparentStatusBar(this);

        context = AddBillActivity.this;
        initTop();//初始化顶部栏
        initTagDialog();
        initView();
        initArray();
        db=new DatabaseUtil(context,Constants.DBNAME,1);
    }
    //选择tag的弹出框
    private void initTagDialog() {
        List<AddBillTagData> datas=new ArrayList<>();
        AddBillTagAdapter adapter=new AddBillTagAdapter(context,datas);

        LinearLayout tagSelectLayout= (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_add_bill_tag, null);
        tagList= (ListView) tagSelectLayout.findViewById(R.id.list_add_bill_tag);
        tagList.setAdapter(adapter);
        for (int i=0;i<TagConstats.tagList.length;i++){
            AddBillTagData data=new AddBillTagData();
            data.setText(TagConstats.tagList[i]);
            data.set_id(TagConstats.tagImage[i]);
            datas.add(data);
        }
        adapter.notifyDataSetChanged();

        tagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.item_add_bill_tag_list_text);
                ImageView imageView = (ImageView) view.findViewById(R.id.item_add_bill_tag_list_image);
                data.setTag(textView.getText().toString());

                tagText.setText(textView.getText().toString());
                tagImage.setImageDrawable(imageView.getDrawable());
                tagDialog.dismiss();
            }
        });

        tagDialog=new AlertDialog.Builder(context).setView(tagSelectLayout).create();
    }

    private void initArray() {
        String temp=data.getYear()+"/"+data.getMonth()+"/"+data.getDayOfMonth();
        dateText.setText(temp);

        changeTag(TagConstats.defaultTag);
    }

    /**
     * 将tag更改为指定位置的tag
     * */
    private void changeTag(int position) {
        data.setTag(TagConstats.tagList[position]);
        tagText.setText(TagConstats.tagList[position]);
        tagImage.setImageResource(TagConstats.tagImage[position]);
    }

    private void initTop() {
        titleBack = (ImageView) findViewById(R.id.title_back);
        topText= (TextView) findViewById(R.id.title_text);
        topText.setText(R.string.addBill);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {

        typeRadio= (RadioGroup) findViewById(R.id.add_bill_type);
        moneyEdt= (EditText) findViewById(R.id.add_bill_money);
        remarkEdt= (EditText) findViewById(R.id.add_bill_remark);
        dateLin= (LinearLayout) findViewById(R.id.add_bill_date_lin);
        tagLin= (LinearLayout) findViewById(R.id.add_bill_tag_lin);
        dateText= (TextView) findViewById(R.id.add_bill_date);
        tagText= (TextView) findViewById(R.id.add_bill_tag);
        tagImage= (ImageView) findViewById(R.id.add_bill_tag_pic);
        confirmBtn= (Button) findViewById(R.id.add_bill_confirm);


    }
    public void click(View v){
        switch (v.getId()){
            case R.id.add_bill_date_lin:{
                // 点击日期框事件 选择日期
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        //参数选择结果：年 月 日

                        int monthhhhhhh = monthOfYear + 1;
                        data.setYear(year);
                        data.setDayOfMonth(dayOfMonth);
                        data.setMonth(monthhhhhhh);
                        data.setIsSelectTime(true);
                        String temp=year+"/"+monthhhhhhh+"/"+dayOfMonth;
                        dateText.setText(temp);
                    }
                };
                //日期选择对话框：参数1上下文   参数2：监听器    参数...默认显示日期 实际显示的月份比这里设置的月份数会多1个月
                Calendar calendar=Calendar.getInstance();
                int yearr = calendar.get(Calendar.YEAR);
                int monthh = calendar.get(Calendar.MONTH);
                int dayy = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datee = new DatePickerDialog(context, date, yearr, monthh, dayy);
                //显示
                datee.show();
                break;
            }case R.id.add_bill_tag_lin:{
                // 16-1-24 点击标签对话框事件 选择标签
                tagDialog.show();//弹出对话框解决
                break;
            }case R.id.add_bill_confirm:{
                //  点击确定事件 提交
                confirm();
                break;
            }
        }
    }

    private void confirm() {
        Calendar calendar=Calendar.getInstance();

        //选择账单类别 收入还是支出
        if (typeRadio.getCheckedRadioButtonId()==R.id.add_bill_money_in)//收入
            data.setType(getResources().getString(R.string.MoneyIn));
        else//支出
            data.setType(getResources().getString(R.string.MoneyOut));

        //账单金额
        if (moneyEdt.getText().toString().length()==0){
            ToastUtil.showShort(context, R.string.addBillMoneyNull);
            return;
        }else {
            String temp=moneyEdt.getText().toString().trim();
            data.setMoney(String.valueOf(NumberUtil.roundHalfUp(temp)));
        }

        //账单备注
        data.setRemark(remarkEdt.getText().toString().trim());

        //creatTime
        data.setCreatTime(DateUtil.getStringByFormat(calendar.getTimeInMillis(),DateUtil.dateFormatYMDHMSw));

        //当前日期是否为选择日期
        if (data.isSelectTime())
        {//选择过日期
            Calendar c=Calendar.getInstance();
            c.set(Calendar.YEAR,data.getYear());
            c.set(Calendar.MONTH,data.getMonth()-1);
            c.set(Calendar.DAY_OF_MONTH,data.getDayOfMonth());
            data.setUnixTime(String.valueOf(c.getTimeInMillis()));
            data.setDate(DateUtil.getStringByFormat(c.getTimeInMillis(), DateUtil.dateFormatYMDHMSw));
        }else {//没有选择日期
            data.setUnixTime(String.valueOf(calendar.getTimeInMillis()));
            data.setDate(DateUtil.getStringByFormat(calendar.getTimeInMillis(),DateUtil.dateFormatYMDHMSw));
        }

        LogUtil.i("创建时间:"+data.getCreatTime()+"" +
                "\n选择时间:"+data.getUnixTime()+
                "\n发生时间:"+data.getDate()+
                "\n年："+data.getYear()+"  月:"+data.getMonth()+"  日："+data.getDayOfMonth()+
                "\n账单类型:"+data.getType()+
                "\n账单标签:"+data.getTag() +
                "\n账单金额:"+data.getMoney()+
                "\n账单备注:"+data.getRemark());
        //{"_Id","spendMoney","remark","date","unixTime","creatTime","moneyType","Tag","year_date","month_date","day_year"};
        ////  16-1-25 写入数据库
        ContentValues cv=new ContentValues();
        cv.put(Constants.column[1],data.getMoney());
        cv.put(Constants.column[2],data.getRemark());
        cv.put(Constants.column[3],data.getDate());
        cv.put(Constants.column[4],data.getUnixTime());
        cv.put(Constants.column[5],data.getCreatTime());
        cv.put(Constants.column[6],data.getType());
        cv.put(Constants.column[7],data.getTag());
        cv.put(Constants.column[8],data.getYear());
        cv.put(Constants.column[9],data.getMonth());
        cv.put(Constants.column[10], data.getDayOfMonth());
        db.insert(Constants.tableName, cv);
        //// TODO: 16-1-25 添加账单 统计数据
        this.startActivity(new Intent(context,MainActivity.class));
        AddBillActivity.this.finish();
    }
}
