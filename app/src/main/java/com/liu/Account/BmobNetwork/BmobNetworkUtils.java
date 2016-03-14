package com.liu.Account.BmobNetwork;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.DownloadListener;
import com.bmob.btp.callback.UploadListener;
import com.liu.Account.BmobRespose.BmobNewDatas;
import com.liu.Account.BmobRespose.BmobUsers;
import com.liu.Account.Constants.Constants;
import com.liu.Account.R;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.commonUtils.PrefsUtil;
import com.liu.Account.commonUtils.ToastUtil;
import com.liu.Account.Database.DatabaseUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by deonet on 2015/12/17.
 */
public class BmobNetworkUtils {
    private Context context;
    private String outPath;
    private ProgressDialog pro;

    private String MD5;

    //private static  String PackagePath=Constants.DatabasePath;
    private static String datebasePath=null;

    public BmobNetworkUtils(Context context){
        this.context=context;
         pro= new ProgressDialog(context);

        this.outPath=Constants.AppSavePath;
        print("outpath " + outPath);
        File file=context.getDatabasePath(Constants.DBNAME);
        datebasePath=file.getPath();
        LogUtil.i("database路径：" + datebasePath);
    }

    private void updateToBmobNewDatas(final String fileName, BmobFile file,
                                      final String DBMD5, final boolean isShowToast) {

        BmobNewDatas datas=new BmobNewDatas();
        BmobUsers user=BmobUsers.getCurrentUser(context,BmobUsers.class);
        if (user!=null){
            datas.setAuthor(user);
            datas.setAuthorEmail(user.getEmail());
        }
        datas.setFile(file);
        datas.setFileName(fileName);
        if (isShowToast){
            datas.setType("Update");
        }else {

            datas.setType("autoUpdate");
        }
        datas.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                LogUtil.i("上传到BmobNewDatas成功");
                updateToUsers(fileName, DBMD5,isShowToast);
            }

            @Override
            public void onFailure(int i, String s) {
                if (isShowToast){
                    pro.dismiss();
                    ToastUtil.showShort(context,"同步失败\n"+s);
                }
                LogUtil.i("上传到BmobNewDatas失败：" + s);
            }
        });
    }

    private void updateToUsers(final String fileName, final String dbmd5, final boolean isShowToast) {
        BmobUsers i = BmobUser.getCurrentUser(context, BmobUsers.class);
        if (i != null) {
            i.setFileName(fileName);
            i.setDBMd5(MD5);


            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            String ss = DateFormat.format(new java.util.Date());

            i.setDBupdateDate(ss);

            i.update(context, new UpdateListener() {
                @Override
                public void onSuccess() {
                    LogUtil.i("上传到user表成功,文件名：" + fileName
                            + "\n文件Md5:" + dbmd5);

                    Calendar calendar = Calendar.getInstance();
                    PrefsUtil d = new PrefsUtil(context, Constants.AutoUpdatePrefsName, Context.MODE_PRIVATE);
                    d.putLong("autoUpateTime", calendar.getTimeInMillis());
                    if (isShowToast) {
                        pro.dismiss();
                        ToastUtil.showShort(context,"同步完成");

                    }
                }

                @Override
                public void onFailure(int i, String s) {
                    LogUtil.i("上传到user表失败\n"+s);
                    if (isShowToast) {
                        pro.dismiss();
                        ToastUtil.showShort(context,"同步失败\n"+s);
                    }
                }
            });
        }else {
            LogUtil.i("未登录，不在user表中进行同步");
            pro.dismiss();
        }
    }

    /**
     * 上传数据到Bmob
     * @param context 上下文
     * @param isShow 是否显示通知
     * **/
    BmobFile bmobFile;
    public void upDatesToBmob(final Context context, final boolean isShow) {
        Map<String,String> map = new HashMap<String,String>();
        try{

            BmobUser user=BmobUser.getCurrentUser(context);
            if (isShow)
                map.put("type",user.getObjectId()+"上传数据");
            else
                map.put("type",user.getObjectId()+"自动上传数据");
        }catch (Exception e){
            e.printStackTrace();
            if (isShow)
                map.put("type","上传数据");
            else
                map.put("type","自动上传数据");
        }

        if (isShow)
            MobclickAgent.onEventValue(context, "UpDatas", map, 0);
        else
            MobclickAgent.onEventValue(context, "AutoUpDatas", map, 0);

        if (isShow) {
            pro.setTitle("正在上传");
            pro.setMessage("请稍候...");
            pro.show();
        }

        File file=new File(datebasePath);
        MD5=getFileMD5(file);
        BmobUsers users= BmobUser.getCurrentUser(context, BmobUsers.class);
        LogUtil.i("当前数据库大小：" + getFileSize(file));
        try {
            LogUtil.i("当前MD5："+MD5);
            LogUtil.i("云端MD5："+users.getDBMd5());
            if (users.getDBMd5().equals(MD5)&&getFileSize(file)>20480) {
                pro.dismiss();
                print("MD5码与云端相同，未上传");
                if (isShow) {
                    ToastUtil.showShort(context, "同步完成");
                    pro.dismiss();
                }
                return;
            }
        }catch (Exception e){
            print("还没有云MD5 "+e.toString());

        }
        copyDataBase(datebasePath, outPath + Constants.DBNAME + ".db");

        BTPFileResponse response= BmobProFile.getInstance(context)
                .upload(outPath+Constants.DBNAME+".db", new UploadListener() {
                    @Override
                    public void onSuccess(final String s, String s1, final BmobFile bmobFile) {

                        LogUtil.i("文件上传成功：" + isShow+" "+s);
                        updateToBmobNewDatas(s, bmobFile, MD5, isShow);
                    }
                    @Override
                    public void onProgress(int i) {
                    }

                    @Override
                    public void onError(int i, String s) {
                        if (isShow) {
                            ToastUtil.showShort(context, "同步完成");
                            pro.dismiss();
                        }
                        LogUtil.i("文件上传失败："+isShow+"  "+s);
                    }
                });
    }
    public void getDatasFromBmob(final Context context,String fileName){
        Map<String,String> map = new HashMap<String,String>();
        try{
            BmobUser user=BmobUser.getCurrentUser(context);

            map.put("type",user.getObjectId()+"下载数据");
        }catch (Exception e){
            e.printStackTrace();
            map.put("type", "下载数据");
        }
        MobclickAgent.onEventValue(context, "getDatas", map, 0);

        pro.setTitle("正在下载");
        pro.setMessage("请稍候...");
        pro.show();

        File file=new File(datebasePath);
        MD5=getFileMD5(file);
        BmobUsers users= BmobUsers.getCurrentUser(context, BmobUsers.class);

        try {
            LogUtil.i("当前MD5:" + MD5);
            LogUtil.i("云端MD5:" + users.getDBMd5());
            if (users.getDBMd5().equals(MD5)) {
                pro.dismiss();
                print("MD5码与云端相同，未下载");
                Toast.makeText(context, "您的数据是最新的", Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (Exception e){
            print("还没有云MD5 "+e.toString());
        }

        LogUtil.i("需要下载的文件名："+fileName);
        BmobProFile.getInstance(context).download(fileName, new DownloadListener() {
            @Override
            public void onSuccess(String s) {
                print("文件下载成功,文件路径" + s);
                print("文件复制后,文件路径" + datebasePath);
                copyDataBase(s, datebasePath);
                DatabaseUtil databaseUtil=new DatabaseUtil(context,Constants.DBNAME,1);
                try {
                    databaseUtil.renameTable("date",Constants.tableName);
                }catch (Exception e){
                    e.printStackTrace();
                }
                pro.dismiss();
                ToastUtil.showShort(context, context.getString(R.string.getDatasSuccess));
                //  下载成功
            }

            @Override
            public void onProgress(String s, int i) {
                print("文件下载中...");
            }

            @Override
            public void onError(int i, String s) {
                print("文件下载失败:" + i + "  " + s);
                pro.dismiss();
                ToastUtil.showShort(context,R.string.getDatasFailed);
            ////  16-1-26 下载失败
            }
        });
    }
    public void copyDataBase(String path,String outPath){
        try {
            creatDataBase(path,outPath);
        } catch (IOException e) {
            e.printStackTrace();
            print("拷贝出错");
        }
    }
    //创建一个空的db,然后替换所选db
    public void creatDataBase(String path,String outPath) throws IOException {
            checkDatabase(path);
            try{
                copyDatabase(path,outPath);
            }catch (IOException e){
                e.printStackTrace();
                Log.i("database--->", "Error copying database from assets");
            }

    }
    //检查数据库是否存在
    private boolean checkDatabase(String path){
        SQLiteDatabase checkableDatabase=null;
        try{
            checkableDatabase=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e){
            //数据库不存在，返回false
            LogUtil.i("数据库不存在");
        }
        if (checkableDatabase!=null){
            checkableDatabase.close();
        }
        return checkableDatabase!=null;

    }
    //从应用程序资产中复制数据库
    //越过空使用数据库
    private void copyDatabase(String path,String outPath)throws IOException {
        LogUtil.i("源文件:"+path);
        LogUtil.i("目标路径:"+outPath);

        LogUtil.i("源文件的MD5:"+getFileMD5(new File(path)));
        LogUtil.i("目标路径前MD5:"+getFileMD5(new File(outPath)));


        InputStream myInput=new FileInputStream(path);
        //打开任一目录下数据库
        // InputStream myInput=new FileInputStream(DBPATH+DB_NAME);

        OutputStream myOutput=new FileOutputStream(outPath);

        print("copying....");
        byte[] buffer=new byte[1024];
        int length;
        while ((length=myInput.read(buffer))>0){
            myOutput.write(buffer,0,length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

        File file=new File(datebasePath);

        LogUtil.i("当前数据库路径MD5:"+getFileMD5(file));
    }

    private  void print(String s){
        LogUtil.i("BmobUtils------>"+ s);
    }
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest =MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 获取指定文件大小
     * @param file
     * @return
     * @throws Exception 　　
     */
    public static long getFileSize(File file){
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
            }
        }catch (Exception e){
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }
}
