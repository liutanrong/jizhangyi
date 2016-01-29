package com.liu.Account.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.Account.BmobRespose.BmobIdeas;
import com.liu.Account.BmobRespose.BmobUsers;
import com.liu.Account.Constants.Constants;
import com.liu.Account.R;
import com.liu.Account.commonUtils.AppUtil;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by deonte on 16-1-28.
 */
public class GiveIdeaActivity extends AutoLayoutActivity implements View.OnClickListener{
    private Context context;
    private EditText edtText,edtPhone;
    private TextView version;
    private Button confirm;


    private ImageView titleBack;
    private TextView topText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_idea);

        context=GiveIdeaActivity.this;
        initTop();
        initView();
    }

    private void initView() {
        edtText= (EditText) findViewById(R.id.giveIdeaText);
        edtPhone= (EditText) findViewById(R.id.giveIdeaPhone);
        version= (TextView) findViewById(R.id.giveIdeaVersionName);
        confirm= (Button) findViewById(R.id.giveIdeaConfirm);
        confirm.setOnClickListener(this);
    }
    private void initTop() {
        titleBack = (ImageView) findViewById(R.id.title_back);
        topText= (TextView) findViewById(R.id.title_text);
        topText.setText(R.string.menu_give_idea);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        version.setText((getResources().getString(R.string.menu_idea_version) + AppUtil.getAppVersionName(context)));
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.giveIdeaConfirm){
            //点击提交
            menuIdea(context,edtText,edtPhone);
        }
    }
    private void menuIdea(final Context context,EditText menuIdeaEdt,EditText menuIdeaEdtPhone){
        String menuIdea,menuIdeaPhone;
        menuIdea= menuIdeaEdt.getText().toString();
        menuIdeaPhone=menuIdeaEdtPhone.getText().toString();
        BmobIdeas ob=new BmobIdeas();
        ob.setIdea(menuIdea);
        ob.setPhoneOrEmail(menuIdeaPhone);
        BmobUser user=BmobUser.getCurrentUser(context,BmobUsers.class);
        try {
            ob.setAuthor(user);
            ob.setAutho(user.getEmail());
        }catch (Exception e){
            e.printStackTrace();
        }
        final ProgressDialog pro=new ProgressDialog(context);
        pro.setTitle("正在上传到服务器");
        pro.setMessage("请耐心等待...");
        pro.show();
        ob.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                pro.dismiss();
                Toast.makeText(context, "成功上传到服务器,感谢您提的意见", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                pro.dismiss();
                Toast.makeText(context,"上传服务器失败,请稍后再试",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("GiveIdeaActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("GiveIdeaActivity");
        MobclickAgent.onPause(this);
    }
    /**
     * 返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return false;
    }
}
