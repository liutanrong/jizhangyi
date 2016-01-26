package com.liu.Account.fragment;

import android.app.Fragment;

/**
 * 生成首页Tab中Fragment的工厂类
 */
public class FragmentFactory {

    public static final int HOME= 0;
    public static final int SYNC = 1;
    public static final int ALLBILL = 2;
    public static final int SEARCH = 3;
    public static final int ANALYSIS = 4;

    public static final int TOTAL = ANALYSIS + 1;

    public static final Fragment createFragment(int index) {
        switch (index) {
            case HOME:
                return new HomeFragment();
            case SYNC:
                return new SyncFragment();
            case ALLBILL:
                return new AllBillFragment();
            case SEARCH:
                return new SearchFragment();
            case ANALYSIS:
                return new AnalysisFragment();
        }
        return null;
    }

}
