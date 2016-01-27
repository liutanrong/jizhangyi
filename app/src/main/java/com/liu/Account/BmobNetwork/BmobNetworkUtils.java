package com.liu.Account.BmobNetwork;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
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
import com.liu.Account.commonUtils.PrefsUtil;
import com.liu.Account.commonUtils.ToastUtil;
import com.liu.Account.utils.DatabaseUtil;

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
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by deonet on 2015/12/17.
 */
public class BmobNetworkUtils {
    private Context context;
    private String outPath;
    private  String DBNAME;
    private ProgressDialog pro;

    private String MD5;
    private static final String PackagePath="/data/data/com.liu.Account/databases/";
    public BmobNetworkUtils(Context context){
        this.context=context;
         pro= new ProgressDialog(context);

        this.outPath=Constants.AppSavePath;
        print("outpath " + outPath);
    }
    /**
     * 上传数据到Bmob
     * **/
    public void upDatesToBmobWithDialog(final Context context) {
        Map<String,String> map = new HashMap<String,String>();
        try{
            BmobUser user= BmobUser.getCurrentUser(context);
            map.put("email",user.getEmail());
        }catch (Exception e){
            e.printStackTrace();
        }
        pro.setTitle("正在上传");
        pro.setMessage("请稍候...");
        pro.show();
        File file=new File(PackagePath+Constants.DBNAME);
        MD5=getFileMD5(file);
        BmobUsers users= BmobUser.getCurrentUser(context, BmobUsers.class);

        try {
            if (users.getDBMd5().equals(MD5)) {
                pro.dismiss();
                print("MD5码与云端相同，未上传");
                Toast.makeText(context,"同步完成",Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (Exception e){
            print("还没有云MD5 "+e.toString());

        }
        copyDataBase(PackagePath+Constants.DBNAME,outPath+Constants.DBNAME);
        BTPFileResponse response= BmobProFile.getInstance(context)
                .upload(outPath+Constants.DBNAME, new UploadListener() {
                    @Override
                    public void onSuccess(final String s, String s1, final BmobFile bmobFile) {

                        final BmobNewDatas datas=new BmobNewDatas();
                        final BmobUsers user=BmobUsers.getCurrentUser(context,BmobUsers.class);
                        BmobQuery<BmobNewDatas> query=new BmobQuery<BmobNewDatas>();
                        query.addWhereEqualTo("authorEmail", user.getEmail());
                        query.findObjects(context, new FindListener<BmobNewDatas>() {
                            @Override
                            public void onSuccess(List<BmobNewDatas> list) {
                                BmobNewDatas datas=new BmobNewDatas();
                                BmobUsers user=BmobUsers.getCurrentUser(context,BmobUsers.class);

                                if (list.size() > 0) {
                                    list.get(0).getObjectId();

                                    datas.setAuthor(user);
                                    datas.setAuthorEmail(user.getEmail());
                                    datas.setFile(bmobFile);
                                    datas.setFileName(s);
                                    datas.setType("update");
                                    datas.update(context, list.get(0).getObjectId(), new UpdateListener() {
                                        @Override
                                        public void onSuccess() {
                                            BmobUsers i = BmobUser.getCurrentUser(context, BmobUsers.class);
                                            i.setFileName(s);
                                            i.setDBMd5(MD5);
                                            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                                            String ss = DateFormat.format(new java.util.Date());
                                            i.setDBupdateDate(ss);
                                            i.update(context, new UpdateListener() {
                                                @Override
                                                public void onSuccess() {
                                                    print("上传成功,文件名：" + s);
                                                    pro.dismiss();
                                                    Calendar calendar=Calendar.getInstance();
                                                    PrefsUtil d=new PrefsUtil(context,Constants.PrefsName,Context.MODE_PRIVATE);
                                                    d.putLong("autoUpateTime", calendar.getTimeInMillis());
                                                    ToastUtil.showShort(context, R.string.updateSuccess);
                                                }

                                                @Override
                                                public void onFailure(int i, String s) {

                                                    print("上传失败 账户信息更新失败");
                                                    Toast.makeText(context, "同步失败\n" + s, Toast.LENGTH_SHORT).show();
                                                    pro.dismiss();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            print("上传失败  文件信息更新失败");
                                            ToastUtil.showShort(context, R.string.updateFailed);
                                            pro.dismiss();

                                        }
                                    });

                                } else {
                                    datas.setAuthor(user);
                                    datas.setAuthorEmail(user.getEmail());
                                    datas.setFile(bmobFile);
                                    datas.setFileName(s);
                                    datas.setType("update");
                                    datas.save(context, new SaveListener() {
                                        @Override
                                        public void onSuccess() {
                                            BmobUsers i = BmobUser.getCurrentUser(context, BmobUsers.class);
                                            i.setFileName(s);
                                            i.setDBMd5(MD5);
                                            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                                            String ss = DateFormat.format(new java.util.Date());
                                            i.setDBupdateDate(ss);
                                            i.update(context, new UpdateListener() {
                                                @Override
                                                public void onSuccess() {
                                                    print("上传成功,文件名：" + s);
                                                    pro.dismiss();
                                                    ToastUtil.showShort(context, R.string.updateSuccess);
                                                    Calendar calendar=Calendar.getInstance();
                                                    PrefsUtil d=new PrefsUtil(context,Constants.PrefsName,Context.MODE_PRIVATE);
                                                    d.putLong("autoUpateTime", calendar.getTimeInMillis());

                                                }

                                                @Override
                                                public void onFailure(int i, String s) {

                                                    print("上传失败 账户信息更新失败");
                                                    Toast.makeText(context, "同步失败\n" + s, Toast.LENGTH_SHORT).show();
                                                    pro.dismiss();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {

                                            print("上传失败  文件信息更新失败");
                                            Toast.makeText(context, "同步失败\n" + s, Toast.LENGTH_SHORT).show();
                                            pro.dismiss();
                                       }
                                    });
                                }
                            }

                            @Override
                            public void onError(int i, String s) {

                            }
                        });

                    }

                    @Override
                    public void onProgress(int i) {
                        print("上传中");
                    }

                    @Override
                    public void onError(int i, String s) {

                        pro.dismiss();
                        print("上传失败 文件上传失败\n" + i + " " + s);
                        Toast.makeText(context,"同步失败\n"+s,Toast.LENGTH_SHORT).show();

                    }
                });
    }
    /**
     * 上传数据到Bmob
     * **/
    public void upDatesToBmob(final Context context) {
        Map<String,String> map = new HashMap<String,String>();
        try{
            BmobUser user= BmobUser.getCurrentUser(context);
            map.put("email",user.getEmail());
        }catch (Exception e){
            e.printStackTrace();
        }

        File file=new File(PackagePath+Constants.DBNAME);
        MD5=getFileMD5(file);
        BmobUsers users= BmobUser.getCurrentUser(context, BmobUsers.class);
        print("当前数据库大小："+getFileSize(file));
        try {
            if (users.getDBMd5().equals(MD5)&&getFileSize(file)>20480) {
                pro.dismiss();
                print("MD5码与云端相同，未上传");
                return;
            }
        }catch (Exception e){
            print("还没有云MD5 "+e.toString());

        }
        copyDataBase(PackagePath + Constants.DBNAME, outPath + Constants.DBNAME);

        BTPFileResponse response= BmobProFile.getInstance(context)
                .upload(outPath+Constants.DBNAME, new UploadListener() {
                    @Override
                    public void onSuccess(final String s, String s1, final BmobFile bmobFile) {

                        final BmobNewDatas datas=new BmobNewDatas();
                        final BmobUsers user=BmobUsers.getCurrentUser(context,BmobUsers.class);
                        BmobQuery<BmobNewDatas> query=new BmobQuery<BmobNewDatas>();
                        query.addWhereEqualTo("authorEmail", user.getEmail());
                        query.findObjects(context, new FindListener<BmobNewDatas>() {
                            @Override
                            public void onSuccess(List<BmobNewDatas> list) {
                                BmobNewDatas datas=new BmobNewDatas();
                                BmobUsers user=BmobUsers.getCurrentUser(context,BmobUsers.class);

                                if (list.size() > 0) {
                                    list.get(0).getObjectId();

                                    datas.setAuthor(user);
                                    datas.setAuthorEmail(user.getEmail());
                                    datas.setFile(bmobFile);
                                    datas.setFileName(s);
                                    datas.setType("update");
                                    datas.update(context, list.get(0).getObjectId(), new UpdateListener() {
                                        @Override
                                        public void onSuccess() {
                                            BmobUsers i = BmobUser.getCurrentUser(context, BmobUsers.class);
                                            i.setFileName(s);
                                            i.setDBMd5(MD5);
                                            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                                            String ss = DateFormat.format(new java.util.Date());
                                            i.setDBupdateDate(ss);
                                            i.update(context, new UpdateListener() {
                                                @Override
                                                public void onSuccess() {
                                                    print("上传成功,文件名：" + s);
                                                    pro.dismiss();
                                             }

                                                @Override
                                                public void onFailure(int i, String s) {

                                                    print("上传失败 账户信息更新失败");
                                                    pro.dismiss();
                                              }
                                            });
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            print("上传失败  文件信息更新失败");
                                            pro.dismiss();

                                        }
                                    });

                                } else {
                                    datas.setAuthor(user);
                                    datas.setAuthorEmail(user.getEmail());
                                    datas.setFile(bmobFile);
                                    datas.setFileName(s);
                                    datas.setType("update");
                                    datas.save(context, new SaveListener() {
                                        @Override
                                        public void onSuccess() {
                                            BmobUsers i = BmobUser.getCurrentUser(context, BmobUsers.class);
                                            i.setFileName(s);
                                            i.setDBMd5(MD5);
                                            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                                            String ss = DateFormat.format(new java.util.Date());
                                            i.setDBupdateDate(ss);
                                            i.update(context, new UpdateListener() {
                                                @Override
                                                public void onSuccess() {
                                                    print("上传成功,文件名：" + s);
                                                    pro.dismiss();
                                                    Calendar calendar=Calendar.getInstance();
                                                    PrefsUtil d=new PrefsUtil(context,Constants.PrefsName,Context.MODE_PRIVATE);
                                                    d.putLong("autoUpateTime", calendar.getTimeInMillis());

                                                }

                                                @Override
                                                public void onFailure(int i, String s) {

                                                    print("上传失败 账户信息更新失败");
                                                    pro.dismiss();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {

                                            print("上传失败  文件信息更新失败");
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError(int i, String s) {

                            }
                        });

                    }

                    @Override
                    public void onProgress(int i) {
                    }

                    @Override
                    public void onError(int i, String s) {

                        pro.dismiss();
                        print("上传失败 文件上传失败\n" + i + " " + s);
                    }
                });
    }
    public void getDatasFromBmob(final Context context,String fileName){
        Map<String,String> map = new HashMap<String,String>();
        try{
            BmobUser user= BmobUser.getCurrentUser(context);
            map.put("email",user.getEmail());
        }catch (Exception e){
            e.printStackTrace();
        }

        pro.setTitle("正在下载");
        pro.setMessage("请稍候...");
        pro.show();
        File file=new File(PackagePath+Constants.DBNAME);
        MD5=getFileMD5(file);
        BmobUsers users= BmobUser.getCurrentUser(context, BmobUsers.class);

        try {
            if (users.getDBMd5().equals(MD5)) {
                pro.dismiss();
                print("MD5码与云端相同，未下载");
                Toast.makeText(context, "您的数据是最新的", Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (Exception e){
            print("还没有云MD5 "+e.toString());
        }

        BmobProFile.getInstance(context).download(fileName, new DownloadListener() {
            @Override
            public void onSuccess(String s) {
                copyDataBase(s, PackagePath + Constants.DBNAME);
                DatabaseUtil databaseUtil=new DatabaseUtil(context,Constants.DBNAME,1);
                try {
                    databaseUtil.renameTable("date",Constants.tableName);
                }catch (Exception e){
                    e.printStackTrace();
                }
                print("文件下载成功,文件路径" + s);
                pro.dismiss();
                ToastUtil.showShort(context, context.getString(R.string.getDatasSuccess));
                //// TODO: 16-1-26 下载成功
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
            //// TODO: 16-1-26 下载失败
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
        }
        if (checkableDatabase!=null){
            checkableDatabase.close();
        }
        return checkableDatabase!=null;

    }
    //从应用程序资产中复制数据库
    //越过空使用数据库
    private void copyDatabase(String path,String outPath)throws IOException {

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
    }

    private  void print(String s){
        Log.i("BmobUtils------>", s);
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
