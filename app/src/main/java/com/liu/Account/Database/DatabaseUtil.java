package com.liu.Account.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.liu.Account.Constants.Constants;
import com.liu.Account.commonUtils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deonte on 16-1-25.
 */
public class DatabaseUtil {
    private Context context;
    private SQLiteDatabase readOnlyDatabase;
    private SQLiteDatabase writeableDatabase;
    private SQLiteHelper sqLiteHelper;
    private int version;
    public DatabaseUtil(Context contex,String dbName,int version){
        this.context=contex;

        sqLiteHelper=new SQLiteHelper(contex,dbName,null,version);
        readOnlyDatabase=sqLiteHelper.getReadableDatabase();
        writeableDatabase =sqLiteHelper.getWritableDatabase();
    }
    public SQLiteDatabase getReadableDatabase(){
        return sqLiteHelper.getReadableDatabase();
    }
    public SQLiteDatabase getWriteableDatabase(){
        return  sqLiteHelper.getWritableDatabase();
    }
    public SQLiteHelper getSqLiteHelper(){
        return this.sqLiteHelper;
    }
    /***
     * 创建表
     * **/
    public void creatTables(String sql){
        doSql(sql);
    }
    /**
     * 插入数据
     * **/
    public long insert(String tableName, ContentValues contentValues){
        long temp;
        if (contentValues==null||tableName==null){
            LogUtil.e("数据为空");
            return -1;
        }
        temp=writeableDatabase.insert(tableName,null,contentValues);//执行插入操作
        return temp;
    }
    public void insert(String sqlInsert){
        doSql(sqlInsert);
    }
    /**
     * 更新数据
     * */
    public int update(String tableName, ContentValues contentValues,String whereClause, String [] whereArgs){
        if (tableName==null||contentValues==null||whereArgs==null||whereClause==null){
            LogUtil.e("数据为空");
            return -1;
        }
        int temp=writeableDatabase.update(tableName,contentValues,whereClause,whereArgs);
        return temp;
    }
    public void update(String sqlUpdate){
        doSql(sqlUpdate);
    }

    /**
     * 删除数据
     */
    public int delete(String tableName, String whereClause, String [] whereArgs){
        int temp;
        if (tableName==null||whereArgs==null||whereClause==null){
            LogUtil.e("数据为空");
            return -1;
        }
        temp=writeableDatabase.delete(tableName,whereClause,whereArgs);
        return temp;
    }
    public void delete(String sqlDelete){
        doSql(sqlDelete);
    }
    /**
     * 查询数据
     * */
    public Cursor queryCursor(String sql, String [] whereArgs){
        Cursor cursor;
        if (sql==null){
            LogUtil.e("数据为空");
            return null;
        }
        cursor=readOnlyDatabase.rawQuery(sql,whereArgs);
        return cursor;
    }

    /**
     * sql语句涉及的条数
     * */
    public int getCount(String sql,String[] whereArgs){
        Cursor cursor=queryCursor(sql,whereArgs);
        return cursor.getCount();
    }

    public void deleteAll(String tableName){
        writeableDatabase.delete(tableName,null,null);
    }
    /*
    删除表
    * **/
    public void dropTable(String tableName){
        String sql="DROP TABLE "+tableName;
        doSql(sql);
    }
    //更改table名称
    public void renameTable(String oldTableName,String newTableName){
        String sql="ALTER TABLE "+oldTableName+" RENAME To "+newTableName;
        doSql(sql);
    }
    public void close(){
        if (writeableDatabase!=null&&readOnlyDatabase!=null) {
            writeableDatabase.close();
            readOnlyDatabase.close();
        }
    }
    private void doSql(String sql){
        if (sql==null){
            return;
        }
        writeableDatabase.execSQL(sql);
    }

    /**
     * 查询数据库中的用户
     * @return
     */
    public List<Billdate> exportToList() {
        String sql = "select * from billdata";
        Cursor cursor = this.queryCursor(sql,null);

        List<Billdate> list = new ArrayList<Billdate>();

      //  ToastUtil.showShort(activity, "导出CSV文件");
        while(cursor.moveToNext()){
            Billdate user = new Billdate();
            String id=String.valueOf(cursor.getInt(cursor.getColumnIndex("_Id")));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String unixtime = cursor.getString(cursor.getColumnIndex("unixTime"));
            String spendMoney = cursor.getString(cursor.getColumnIndex("spendMoney"));
            String moneyType=cursor.getString(cursor.getColumnIndex("moneyType"));
            String creatTime=cursor.getString(cursor.getColumnIndex("creatTime"));
            String tag=cursor.getString(cursor.getColumnIndex("Tag"));
            String year_date=cursor.getString(cursor.getColumnIndex("year_date"));
            String month_date=cursor.getString(cursor.getColumnIndex("month_date"));
            String day_year=cursor.getString(cursor.getColumnIndex("day_year"));

            user.set_Id(id);
            user.setCreatTime(creatTime);
            user.setDate(date);
            user.setRemark(remark);
            user.setDay_year(day_year);
            user.setMoneyType(moneyType);
            user.setYear_date(year_date);
            user.setUnixTime(unixtime);
            user.setSpendMoney(spendMoney);
            user.setTag(tag);
            user.setMonth_date(month_date);
            list.add(user);
        }
        cursor.close();
        writeableDatabase.close();
        return list;
    }
    public void insert(Billdate bt){


    //{"_Id","spendMoney","remark","date","unixTime","creatTime",
    // "moneyType","Tag","year_date","month_date","day_year"};

        ContentValues cv=new ContentValues();
        cv.put(Constants.column[1],bt.getSpendMoney());
        cv.put(Constants.column[2],bt.getRemark());
        cv.put(Constants.column[3],bt.getDate());
        cv.put(Constants.column[4],bt.getUnixTime());
        cv.put(Constants.column[5],bt.getCreatTime());
        cv.put(Constants.column[6],bt.getMoneyType());
        cv.put(Constants.column[7],bt.getTag());
        cv.put(Constants.column[8],bt.getYear_date());
        cv.put(Constants.column[9],bt.getMonth_date());
        cv.put(Constants.column[10],bt.getDay_year());
        insert(Constants.tableName, cv);
    }
    public void exportFromList(List<Billdate> list){
        for(Billdate u:list){
            insert(u);

        }

    }
}
