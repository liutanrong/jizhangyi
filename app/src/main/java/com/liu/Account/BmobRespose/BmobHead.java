package com.liu.Account.BmobRespose;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by deonet on 2015/12/17.
 */
public class BmobHead extends BmobObject {
    private BmobFile file;
    private BmobUsers author;
    private String authorEmail;
    private String fileName;


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
