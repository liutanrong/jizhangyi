package com.liu.Account.activity;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.Account.R;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.fragment.FragmentFactory;
import com.liu.Account.initUtils.DeviceInformation;
import com.liu.Account.initUtils.StatusBarUtil;
import com.liu.Account.initUtils.Init;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;

public class MainActivity extends AutoLayoutActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment[] fragments=new Fragment[FragmentFactory.TOTAL];
    // 当前显示的Fragment
    private int currentFragment = -1;
    private int position=1;

    private View headerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置沉浸状态栏
        StatusBarUtil.setTransparentStatusBar(this);

        //初始化toolb左滑菜单ar和
        initToolbarAndNavigation();

        initView();

        //将默认页面设为添加账单页
        replaceFragment(FragmentFactory.HOME);
        getSupportActionBar().setTitle(R.string.menu_home);


        //为取得设备信息做准备
        TelephonyManager mTm= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        DeviceInformation get;
        //取得设备信息
        get=new DeviceInformation(MainActivity.this,mTm,packInfo);
        final Bundle bundle=get.getInformation();

        get.upInfoToBmob(bundle);//上传设备信息到bmob

    }

    private void initView() {
        
        //// TODO: 16-1-23 点击用户头像
        ImageView headerIcon= (ImageView) headerView.findViewById(R.id.userIcon);
        TextView headerText= (TextView) headerView.findViewById(R.id.userName);



        LinearLayout linearLayout= (LinearLayout) headerView.findViewById(R.id.header_lin);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser bmobUser = BmobUser.getCurrentUser(MainActivity.this);
                if(bmobUser == null){
                    //未登录
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }else {//已登陆
                    startActivity(new Intent(MainActivity.this,AccountActivity.class));
                }
             }
        });
    }

    private void initToolbarAndNavigation() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView=navigationView.getHeaderView(0);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //// TODO: 16-1-23 左侧选择时

        if (id == R.id.menu_home) {
            position=1;
            replaceFragment(FragmentFactory.HOME);
            getSupportActionBar().setTitle(R.string.menu_home);
        } else if (id == R.id.menu_sync) {
            position=2;
            replaceFragment(FragmentFactory.SYNC);
            getSupportActionBar().setTitle(R.string.menu_sync);
        } else if (id == R.id.menu_all_bill) {
            position=3;
            replaceFragment(FragmentFactory.ALLBILL);
            getSupportActionBar().setTitle(R.string.menu_all_bill);
        } else if (id == R.id.menu_search) {
            position=4;
            replaceFragment(FragmentFactory.SEARCH);
            getSupportActionBar().setTitle(R.string.menu_search);
        } else if (id == R.id.menu_analysis) {
            position=5;
            replaceFragment(FragmentFactory.ANALYSIS);
            getSupportActionBar().setTitle(R.string.menu_analysis);
        } else if (id == R.id.menu_give_idea) {
            //// TODO: 16-1-23 意见反馈
        }else if (id==R.id.menu_about_us){
            //// TODO: 16-1-23 关于我们
        }else if (id==R.id.menu_checkForUpdate){
            //// TODO: 16-1-23 检查更新
        }else if (id==R.id.menu_setting){
            //// TODO: 16-1-23 设置
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 切换到指定的fragment
     *
     * @param num
     */
    private void replaceFragment(int num) {
        if (num < FragmentFactory.HOME || num >= FragmentFactory.TOTAL)
            return;
        // 如果已经是当前显示的fragment，不做任何操作
        if (currentFragment == num)
            return;
        // 如果还未创建该fragment实例，new一个
        if (null == fragments[num]) {
            fragments[num] = FragmentFactory.createFragment(num);
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, fragments[num]).commit();


        // 更新currentFragment的值
        currentFragment = num;

    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(position!=1){
//				如果当前界面不是主界面，就先回到主界面
                position=1;
                replaceFragment(FragmentFactory.HOME);
            }
            else{
                exitBy2Click(); // 调用双击退出函数
            }

        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
            System.exit(0);
        }
    }
}
