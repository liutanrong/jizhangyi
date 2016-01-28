package com.liu.Account.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liu.Account.R;
import com.liu.Account.activity.SearchActivity;

/**
 * Created by deonte on 16-1-23.
 */
public class SearchFragment extends Fragment {
    private Activity activity;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_search,container,false);

        activity=getActivity();
        //activity.startActivityFromFragment(this,new Intent(activity, SearchActivity.class),1);

        return view;
    }
}
