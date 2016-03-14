package com.liu.Account.application;

import android.app.Application;
import android.support.v7.widget.SearchView;

import com.liu.Account.commonUtils.CrashHandler;

/**
 * Created by deonte on 16-1-28.
 */
public class ApplicationDatas extends Application {
    SearchView searchView;

    private static ApplicationDatas sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        //在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

    public static ApplicationDatas getInstance() {
        return sInstance;
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }
}
