package com.liu.Account.model;

/**
 *Created by liu on 15-10-11.
 * */
public class AddBillTagData {
    int _id;
    String text;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AddBillTagData() {
    }

    public AddBillTagData(String text, int _id) {
        this._id = _id;
        this.text = text;
    }
}
