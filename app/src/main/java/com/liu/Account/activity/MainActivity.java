package com.liu.Account.activity;

import android.app.ActivityManager;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.DownloadListener;
import com.liu.Account.BmobRespose.BmobUsers;
import com.liu.Account.Constants.Constants;
import com.liu.Account.R;
import com.liu.Account.application.ApplicationDatas;
import com.liu.Account.commonUtils.LogUtil;
import com.liu.Account.commonUtils.PrefsUtil;
import com.liu.Account.commonUtils.ToastUtil;
import com.liu.Account.fragment.FragmentFactory;
import com.liu.Account.initUtils.DeviceInformation;
import com.liu.Account.initUtils.StatusBarUtil;
import com.liu.Account.initUtils.Init;
import com.liu.Account.utils.BitmapUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
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

    private ImageView headerIcon;
    private TextView headerText;

    private Context context;

    private MenuItem searchItem;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置沉浸状态栏
        StatusBarUtil.setTransparentStatusBar(this);

        context=MainActivity.this;
        //初始化更新
        Init.autoUpdate(context);
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

        Init.autoUpdateData(context);

    }


    @Override
    protected void onStart() {
        super.onStart();

        headerIcon.setImageResource(R.drawable.iconfont_yonghu128);
        headerText.setText("匿名用户");

        BmobUsers bmobUsers=BmobUser.getCurrentUser(context,BmobUsers.class);
        if (bmobUsers!=null)
            setNameAndPic();

    }
    private void setNameAndPic() {
        BmobUsers users=BmobUser.getCurrentUser(context,BmobUsers.class);
        String fileName=null;
        try{
            fileName=users.getHeaderIconFileName();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            headerText.setText(users.getNickName());
        }catch (Exception e){
            e.printStackTrace();
        }
        if (fileName==null)
            return;
        BmobProFile.getInstance(context).download(users.getHeaderIconFileName(),
                new DownloadListener() {
                    @Override
                    public void onSuccess(String s) {
                        Bitmap bitmap = BitmapUtil.getBitmapFromFile(s);
                        if (bitmap != null)
                            headerIcon.setImageBitmap(bitmap);
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

    private void initView() {
        // 16-1-23 点击用户头像
        headerIcon= (ImageView) headerView.findViewById(R.id.userIcon);
        headerText= (TextView) headerView.findViewById(R.id.userName);



        LinearLayout linearLayout= (LinearLayout) headerView.findViewById(R.id.header_lin);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,AccountActivity.class));

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
        // 16-1-23 左侧选择时

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
            ////  16-1-23 意见反馈
            startActivity(new Intent(context,GiveIdeaActivity.class));
        }else if (id==R.id.menu_about_us){
            ////  16-1-23 关于我们
            startActivity(new Intent(context,AboutUsActivity.class));
        }else if (id==R.id.menu_checkForUpdate){
            final ProgressDialog pro=new ProgressDialog(context);
            pro.setTitle("正在检查更新");
            pro.setMessage("请稍候...");
            pro.show();
            UmengUpdateAgent.forceUpdate(context);
            UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                @Override
                public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                    switch (i) {
                        case UpdateStatus.Yes:
                            break;
                        case UpdateStatus.No:
                            ToastUtil.showShort(context,"暂无更新");
                            break;
                        case UpdateStatus.Timeout:
                            ToastUtil.showShort(context,"超时");
                            break;
                    }
                    pro.dismiss();
                }
            });

        }else if (id==R.id.menu_setting){
            ////  16-1-23 设置
            startActivity(new Intent(context,SettingActivity.class));
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
        if (num==FragmentFactory.SEARCH)
            searchItem.setVisible(true);
        getFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, fragments[num]).commit();


        // 更新currentFragment的值
        currentFragment = num;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchItem=menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
        searchView= (SearchView) MenuItemCompat.getActionView(searchItem);
        ApplicationDatas da= (ApplicationDatas) getApplication();
        da.setSearchView(searchView);
        //// TODO: 16-1-28 搜索框标准写法
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            ToastUtil.showShort(context,"search");
            LogUtil.d("search");
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                getSupportActionBar().setTitle(R.string.menu_home);
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
            //// TODO: 16-1-27 双击退出
            MobclickAgent.onKillProcess(context);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
