package com.liu.Account.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.DownloadListener;
import com.bmob.btp.callback.UploadListener;
import com.liu.Account.BmobRespose.BmobHead;
import com.liu.Account.BmobRespose.BmobNewDatas;
import com.liu.Account.BmobRespose.BmobUsers;
import com.liu.Account.Constants.Constants;
import com.liu.Account.R;
import com.liu.Account.commonUtils.AppUtil;
import com.liu.Account.commonUtils.IntentUtil;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.commonUtils.PrefsUtil;
import com.liu.Account.commonUtils.ToastUtil;
import com.liu.Account.utils.BitmapUtil;
import com.liu.Account.utils.DatabaseUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by deonte on 16-1-25.
 */
public class AccountActivity extends AutoLayoutActivity implements View.OnClickListener{
    private Context context;
    private ImageView titleBack;
    private TextView topText;
    private TextView rightText;

    private Button saveDatas;
    private ImageView userIcon;
    private EditText nickName;
    private  TextView email;
    private Bitmap photo;

    private ProgressDialog pro;

    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private static String PicName="head.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        context=AccountActivity.this;
        initTop();
        initView();

        pro=new ProgressDialog(context);


    }

    private void initView() {


        saveDatas= (Button) findViewById(R.id.account_save);
        saveDatas.setOnClickListener(this);

        userIcon= (ImageView) findViewById(R.id.account_icon);
        userIcon.setOnClickListener(this);

        nickName= (EditText) findViewById(R.id.account_nickname);
        email= (TextView) findViewById(R.id.account_email);

        email.setFocusable(true);
        email.setFocusableInTouchMode(true);
        email.requestFocus();
    }

    private void initTop() {
        titleBack = (ImageView) findViewById(R.id.title_back);
        topText= (TextView) findViewById(R.id.title_text);
        topText.setText("编辑资料");
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rightText= (TextView) findViewById(R.id.title_right);
        rightText.setText("注销");
        rightText.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setNameAndPic();

    }

    private void setNameAndPic() {
        final BmobUsers users=BmobUser.getCurrentUser(context,BmobUsers.class);
        String fileName=null;
        try{
            fileName=users.getHeaderIconFileName();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (fileName==null)
            return;
        LogUtil.i("用户头像file名称"+fileName);
        BmobProFile.getInstance(context).download(users.getHeaderIconFileName(),
                new DownloadListener() {
                    @Override
                    public void onSuccess(String s) {
                        Bitmap bitmap = BitmapUtil.getBitmapFromFile(s);
                        if (bitmap != null)
                            userIcon.setImageBitmap(bitmap);
                        String temp = null;
                        String e=null;
                        try {
                            e=users.getEmail();
                            temp = users.getNickName();
                        } catch (Exception a) {
                            a.printStackTrace();
                        }
                        if (temp != null)
                            nickName.setText(users.getNickName());
                        if (e!=null)
                            email.setText(e);

                    }

                    @Override
                    public void onProgress(String s, int i) {

                    }

                    @Override
                    public void onError(int i, String s) {
                        LogUtil.i("头像下载失败");
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_right:{
                new AlertDialog.Builder(context).setTitle("退出")
                        .setMessage("退出后，将清除所有本地记录,确定退出?")
                        .setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    DatabaseUtil db =new DatabaseUtil(context,Constants.DBNAME,1);

                                    db.delete(Constants.tableName, "1", null);
                                    db.close();
                                    AccountActivity.this.finish();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                BmobUser.logOut(context);   //清除缓存用户对象
                            }
                        }).setNegativeButton("取消",null)
                        .show();
                break;
            }case R.id.account_save:{
                String nick=nickName.getText().toString().trim();
                if (nick.isEmpty()){
                    ToastUtil.showShort(context,"用户名不得为空");
                    return;
                }else {

                    pro.setTitle("正在保存");
                    pro.setMessage("请稍候...");
                    pro.show();
                    BmobUsers users = BmobUser.getCurrentUser(context, BmobUsers.class);
                    users.setNickName(nick);
                    users.update(context, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            LogUtil.i("保存用户名成功");
                            pro.dismiss();
                            AccountActivity.this.finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            pro.dismiss();
                            ToastUtil.showShort(context,"保存信息失败\n"+s);
                        }
                    });
                }
                break;
            }case R.id.account_icon:{
                startActivityForResult(IntentUtil.newSelectPictureIntent(), REQUESTCODE_PICK);
                break;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== RESULT_OK){
            switch (requestCode){
                case REQUESTCODE_PICK:{
                    //相册选取返回
                    LogUtil.i("从相册选取返回成功");
                    try {
                        startPhotoZoom(data.getData());
                    } catch (NullPointerException e) {
                        e.printStackTrace();// 用户点击取消操作
                    }
                    break;
                }case REQUESTCODE_CUTTING:{
                    //裁剪图片返回
                    LogUtil.i("裁剪图片返回成功");
                    if (data!=null){
                        setPicToView(data);
                        pro.setTitle("正在保存");
                        pro.setMessage("请稍候...");
                        pro.show();
                    }
                    break;
                }
            }
        }else{
            ToastUtil.showShort(context, "设置失败,请重试");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {

        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            photo = extras.getParcelable("data");
            try {
                BitmapUtil.saveImage(photo, Constants.AppSavePath + PicName, 300);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (AppUtil.isNetworkOK(context)) {
                BTPFileResponse response = BmobProFile.getInstance(context)
                        .upload(Constants.AppSavePath + PicName, new UploadListener() {
                            @Override
                            public void onSuccess(final String s, String s1, final BmobFile bmobFile) {
                                LogUtil.i("头像上传成功:" + s);
                                BmobUsers user1=BmobUsers.getCurrentUser(context,BmobUsers.class);
                                BmobHead head1=new BmobHead();
                                head1.setAuthor(user1);
                                head1.setAuthorEmail(user1.getEmail());
                                head1.setFile(bmobFile);
                                head1.setFileName(s);
                                head1.save(context, new SaveListener() {
                                    @Override
                                    public void onSuccess() {
                                        LogUtil.i("头像 在BmobHead表更新成功" + s);
                                        BmobUsers users = BmobUser.getCurrentUser(context, BmobUsers.class);
                                        users.setHeaderIconFileName(s);
                                        users.update(context, new UpdateListener() {
                                            @Override
                                            public void onSuccess() {
                                                LogUtil.i("头像在user表中更新数据成功");
                                                if (photo != null) {
                                                    userIcon.setImageBitmap(photo);
                                                    ToastUtil.showShort(context, "头像设置成功");
                                                    pro.dismiss();
                                                }

                                            }

                                            @Override
                                            public void onFailure(int i, String s) {
                                                LogUtil.i("头像在user表中更新数据失败" + s);
                                                pro.dismiss();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        pro.dismiss();
                                        LogUtil.i("用户头像在BmobHead表中保存失败");
                                    }
                                });
                            }

                            @Override
                            public void onProgress(int i) {

                            }

                            @Override
                            public void onError(int i, String s) {
                                LogUtil.i("头像设置失败:" + s);
                                pro.dismiss();
                            }
                        });

            }
        }
    }

}
