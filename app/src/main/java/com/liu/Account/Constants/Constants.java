package com.liu.Account.Constants;

import com.liu.Account.commonUtils.PrefsUtil;

/**
 * Created by deonte on 16-1-25.
 */
public class Constants {
    public static String DBNAME="UNDEFAED";
    public static final String tableName="billdata";
    public static final String[] column=
            {"_Id","spendMoney","remark","date","unixTime","creatTime","moneyType","Tag","year_date","month_date","day_year"};
    public static final String AutoUpdatePrefsName ="syncFile";
    public static String AppSavePath=null;
    public static String FileName="jizhangyi/";
    public static String DatabasePath="/data/data/com.liu.Account/databases/";

    public static final String PatternLock="patternLock";
}
