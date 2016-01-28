package com.liu.Account.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liu.Account.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by deonte on 16-1-23.
 */
public class AnalysisFragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_analysis,container,false);


        return view;
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AnalysisFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AnalysisFragment");
    }
}
