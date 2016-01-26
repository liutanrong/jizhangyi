package com.liu.Account.model;


import java.util.List;

/**
 * Created by deonte on 15-11-23.
 ***/
public class AllBillListGroupData {
    public List<HomeListViewData> child;
    String totalMoney;
    String allTime;
    String moneyType;

    public List<HomeListViewData> getChild() {
        return child;
    }

    public void setChild(List<HomeListViewData> child) {
        this.child = child;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getAllTime() {
        return allTime;
    }

    public void setAllTime(String allTime) {
        this.allTime = allTime;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }
}
