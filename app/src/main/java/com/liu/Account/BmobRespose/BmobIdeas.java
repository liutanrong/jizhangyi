package com.liu.Account.BmobRespose;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 *  @author liutanrong0425@163.com
 * Created by liu on 15-10-16.
 */
public class BmobIdeas extends BmobObject {
    private String idea,phoneOrEmail;
    private String autho;
    private BmobUser author;

    public String getAutho() {
        return autho;
    }

    public void setAutho(String autho) {
        this.autho = autho;
    }

    public BmobUser getAuthor() {
        return author;
    }

    public void setAuthor(BmobUser author) {
        this.author = author;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public String getPhoneOrEmail() {
        return phoneOrEmail;
    }

    public void setPhoneOrEmail(String phoneOrEmail) {
        this.phoneOrEmail = phoneOrEmail;
    }
}
