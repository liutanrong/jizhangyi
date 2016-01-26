package com.liu.Account.BmobRespose;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by deonet on 2015/12/17.
 */
public class BmobNewDatas extends BmobObject {
    private BmobFile file;
    private BmobUsers author;
    private String authorEmail;
    private String fileName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public BmobUsers getAuthor() {
        return author;
    }

    public void setAuthor(BmobUsers author) {
        this.author = author;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }
}
