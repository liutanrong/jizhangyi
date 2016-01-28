package com.liu.Account.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.inputmethod.InputMethodManager;

import com.liu.Account.R;
import com.liu.Account.commonUtils.AppUtil;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * Created by deonte on 16-1-28.
 */
public class SearchActivity extends AutoLayoutActivity {
    private Context context;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        context=SearchActivity.this;
        initView();
    }

    private void initView() {
        searchView= (SearchView) findViewById(R.id.search);
        searchView.setSubmitButtonEnabled(true);

        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);
        searchView.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(searchView, 0);
    }
}
