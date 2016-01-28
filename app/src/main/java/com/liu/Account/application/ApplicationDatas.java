package com.liu.Account.application;

import android.app.Application;
import android.support.v7.widget.SearchView;

/**
 * Created by deonte on 16-1-28.
 */
public class ApplicationDatas extends Application {
    SearchView searchView;

    public SearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }
}
