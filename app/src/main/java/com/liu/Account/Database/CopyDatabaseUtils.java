package com.liu.Account.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by liutanrong on 16-3-14.
 * 此类可将资源文件中的数据库拷贝至指定位置
 * 使用前先将资源置于 /app/src/main/assets/目录下
 */
public class CopyDatabaseUtils extends SQLiteOpenHelper {
    private String DBNameInResoures;
    private String DBNameInProject;
    private String Path2Copy2;
    private Context context;

    public CopyDatabaseUtils(Context cont, String DBNameInProject, String DBNameInResoures, String path2Copy2) {
        super(cont, DBNameInProject, null, 1);
        this.context=cont;
        this.DBNameInProject=DBNameInProject;
        this.DBNameInResoures=DBNameInResoures;
        this.Path2Copy2=path2Copy2;

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //创建一个空的db,然后替换所选db
    public void copyDataBase() throws IOException {
        if (!checkDatabase()){
            this.getWritableDatabase();
            try{
                copyDatabase();
            }catch (IOException e){
                Log.i("database--->", "Error copying database from assets");
            }
        }
    }
    //检查数据库是否存在
    private boolean checkDatabase(){
        SQLiteDatabase checkableDatabase=null;
        try{
            checkableDatabase=SQLiteDatabase.openDatabase(Path2Copy2+DBNameInProject,null,SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e){
            //数据库不存在，返回false
        }
        if (checkableDatabase!=null){
            checkableDatabase.close();
        }
        return checkableDatabase!=null;

    }
    //从应用程序资产中复制数据库
    //越过空使用数据库
    private void copyDatabase()throws IOException{
        InputStream myInput=context.getAssets().open(DBNameInResoures);
        //打开任一目录下数据库
        // InputStream myInput=new FileInputStream(DBPATH+DB_NAME);

        OutputStream myOutput=new FileOutputStream(Path2Copy2+DBNameInProject);

        byte[] buffer=new byte[1024];
        int length;
        while ((length=myInput.read(buffer))>0){
            myOutput.write(buffer,0,length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


}
