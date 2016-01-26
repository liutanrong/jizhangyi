package com.liu.Account.model;

import com.liu.Account.Constants.TagConstats;

import java.util.Calendar;

/**
 * Created by deonte on 16-1-24.
 */
public class AddBillData {
    private int year,month,dayOfMonth;


    private String money;
    private String remark;
    private String date;

    private String type;
    private String tag;
    private String unixTime;//显示时间
    private String creatTime;//创建时间




    private boolean isSelectTime=false;

    public boolean isSelectTime() {
        return isSelectTime;
    }

    public void setIsSelectTime(boolean isSelectTime) {
        this.isSelectTime = isSelectTime;
    }

    public String getCreatTime() {

        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AddBillData(boolean init){
        if (init) {
            Calendar calendar = Calendar.getInstance();
            year= calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH)+1;
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            type=null;
            money=null;
            unixTime=null;
            tag= TagConstats.tagList[0];
            remark=null;
        }
    }
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(String unixTime) {
        this.unixTime = unixTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
