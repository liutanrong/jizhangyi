package com.liu.Account.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.liu.Account.commonUtils.LogUtil;

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

}
