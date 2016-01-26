package com.liu.Account.model;

/**
 *Created by liu on 15-10-11.
 * */
public class HomeListViewData {
    String remark,
            money,
            date,
            unixTime,
            moneyType,
            creatTime,
            tag;

    int _tagID;
    String allInMoney,allOutMoney,totalMoney;
    public String getAllInMoney() {
        return allInMoney;
    }

    public void setAllInMoney(String allInMoney) {
        this.allInMoney = allInMoney;
    }

    public String getAllOutMoney() {
        return allOutMoney;
    }

    public void setAllOutMoney(String allOutMoney) {
        this.allOutMoney = allOutMoney;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
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

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int get_tagID() {
        return _tagID;
    }

    public void set_tagID(int _tagID) {
        this._tagID = _tagID;
    }


    public HomeListViewData() {
    }

    public HomeListViewData(String remark, String date, String money) {
        this.remark = remark;
        this.date = date;
        this.money = money;
        this.date = date;
    }
}
