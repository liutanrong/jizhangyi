package com.liu.Account.model;

import com.liu.Account.Constants.TagConstats;

/**
 * Created by deonte on 16-1-28.
 */
public class SearchData {
    private String startTime,endTime;
    private String tag= TagConstats.TagTypeSelect[0];
    private String inOrOut=TagConstats.InOrOutSelect[0];
    private String orderBy=TagConstats.OrderBySelet[0];
    private String searchString="";

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getOrderWay() {
        return orderWay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setOrderWay(String orderWay) {
        this.orderWay = orderWay;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(String inOrOut) {
        this.inOrOut = inOrOut;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    private String orderWay=TagConstats.OrderWaySelect[0];//asc升序 desc 降序
    public SearchData(){

    }
}
