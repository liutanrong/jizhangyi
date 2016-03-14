package com.liu.Account.Database;

/**
 * Created by deonte on 16-3-11.
 */
public class Billdate {
    private String _Id;
    private String spendMoney;
    private String remark;
    private String date;
    private String unixTime;
    private String creatTime;
    private String moneyType;
    private String Tag;

    private String year_date;
    private String month_date;

    private String day_year;

    public String get_Id() {
        return _Id;
    }

    public void set_Id(String _Id) {
        this._Id = _Id;
    }

    public String getSpendMoney() {
        return spendMoney;
    }

    public void setSpendMoney(String spendMoney) {
        this.spendMoney = spendMoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(String unixTime) {
        this.unixTime = unixTime;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getYear_date() {
        return year_date;
    }

    public void setYear_date(String year_date) {
        this.year_date = year_date;
    }

    public String getMonth_date() {
        return month_date;
    }

    public void setMonth_date(String month_date) {
        this.month_date = month_date;
    }

    public String getDay_year() {
        return day_year;
    }

    public void setDay_year(String day_year) {
        this.day_year = day_year;
    }

}
