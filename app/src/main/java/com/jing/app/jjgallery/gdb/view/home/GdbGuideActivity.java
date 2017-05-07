package com.jing.app.jjgallery.gdb.view.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.gdb.model.FileService;
import com.jing.app.jjgallery.gdb.presenter.GdbGuidePresenter;
import com.jing.app.jjgallery.gdb.view.pub.AutoScrollView;
import com.jing.app.jjgallery.gdb.view.adapter.GuideScrollAdapter;
import com.jing.app.jjgallery.gdb.view.recommend.RecommendFragment;
import com.jing.app.jjgallery.gdb.view.update.GdbUpdateListener;
import com.jing.app.jjgallery.gdb.view.update.GdbUpdateManager;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.data.SqlConnection;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.toast.TastyToast;
import com.king.service.gdb.bean.Record;

import java.util.List;

/**
 * 可能是由于用到了DrawerLayout，采用BaseActivity运营统一的样式总是隐藏不了actionbar
 * 只能单独继承AppCompatActivity，并在manifest中将theme设置为.NoActionbar的才行
 */
@Deprecated
public class GdbGuideActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ProgressProvider
        , GuideScrollAdapter.OnScrollItemClickListener{

    private ImageView navHeaderView;
    private DrawerLayout drawerLayout;
    private ImageView gameView;
    private ImageView starView;
    private ImageView recordView;
    private AutoScrollView autoScrollView;
    private GuideScrollAdapter scrollAdapter;
    private GdbGuidePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayHelper.disableScreenshot(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gdb_guide);

        mPresenter = new GdbGuidePresenter();
        initView();

        // check database update
        checkUpdate();

        if (!isServiceWork(FileService.class.getName())) {
            // start file check service
            startFileService();
        }
    }

    //    @Override
    public void initView() {

        // public region
        initCommon();
        // nav header
        initNavHeader();
        // recommend region
        initRecommentd();
        // center region: game, star, record
        initCenter();
    }

    /**
     * 加载records的操作由recommend fragment发起，由于想共享数据，
     * auto scroll的初始化就放在record加载完成之后
     */
    public void onRecordsLoaded() {
        // latest records
        initAutoScroll();
    }

    private void initCommon() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        navHeaderView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_bg);
    }

    private void initNavHeader() {
        String path = SettingProperties.getPreference(this, PreferenceKey.PREF_GDB_NAV_HEADER_BG);
        SImageLoader.getInstance().displayImage(path, navHeaderView);
    }

    private void initRecommentd() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.gdb_guide_recommend, new RecommendFragment(), "RecommendFragment");
        ft.commit();
    }

    private void initCenter() {
        starView = (ImageView) findViewById(R.id.gdb_guide_star);
        gameView = (ImageView) findViewById(R.id.gdb_guide_game);
        recordView = (ImageView) findViewById(R.id.gdb_guide_record);
        findViewById(R.id.gdb_guide_star_text).setOnClickListener(this);
        findViewById(R.id.gdb_guide_game_text).setOnClickListener(this);
        findViewById(R.id.gdb_guide_record_text).setOnClickListener(this);

        String path = SettingProperties.getPreference(this, PreferenceKey.PREF_GDB_GAME_BG);
        SImageLoader.getInstance().displayImage(path, gameView);
        path = SettingProperties.getPreference(this, PreferenceKey.PREF_GDB_STAR_BG);
        SImageLoader.getInstance().displayImage(path, starView);
        path = SettingProperties.getPreference(this, PreferenceKey.PREF_GDB_RECORD_BG);
        SImageLoader.getInstance().displayImage(path, recordView);
    }

    private void initAutoScroll() {
        autoScrollView = (AutoScrollView) findViewById(R.id.gdb_guide_autoscroll);
        scrollAdapter = new GuideScrollAdapter(mPresenter.getLatestRecord(
                SettingProperties.getGdbLatestRecordsNumber(this))
                , getResources().getDimensionPixelSize(R.dimen.gdb_guide_scroll_item_width)
                , DisplayHelper.isTabModel(this));
        scrollAdapter.setPresenter(mPresenter);
        scrollAdapter.setOnScrollItemClickListener(this);
        try {
            autoScrollView.setAdapter(scrollAdapter);
            autoScrollView.startScroll();
        } catch (AutoScrollView.ItemNotEnoughException e) {
            findViewById(R.id.gdb_guide_no_latest).setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }

    public GdbGuidePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onStop() {
        if (autoScrollView != null) {
            autoScrollView.stop();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        if (autoScrollView != null) {
            autoScrollView.restart();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        SqlConnection.getInstance().close();
        super.onDestroy();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gdb_guide, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_setting) {
            ActivityManager.startSettingActivity(this);
        }
        else if (id == R.id.nav_exit) {
            finish();
        }
        else if (id == R.id.nav_main) {
            ActivityManager.startFileManagerActivity(this, null);
        }
        else if (id == R.id.nav_timeline) {
            ActivityManager.startTimeLineActivity(this, null);
        }
        else if (id == R.id.nav_waterfall) {
            Bundle bundle = null;
            ActivityManager.startWaterfallActivity(this, bundle);
        }
        else if (id == R.id.nav_update) {
            checkUpdate();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gdb_guide_game_text:
                ActivityManager.startGDBGameActivity(this, null);
                break;
            case R.id.gdb_guide_star_text:
                ActivityManager.startGDBStarListActivity(this, null);
                break;
            case R.id.gdb_guide_record_text:
                ActivityManager.startGDBRecordListActivity(this, null);
                break;
        }
    }

    /**
     * check gdb database update
     */
    private void checkUpdate() {
        GdbUpdateManager manager = new GdbUpdateManager(this, new GdbUpdateListener() {
            @Override
            public void onUpdateFinish() {
                ActivityManager.reload(GdbGuideActivity.this);
            }

            @Override
            public void onUpdateCancel() {

            }
        });
        manager.startCheck();
    }

    @Override
    public void showProgressCycler() {

    }

    @Override
    public boolean dismissProgressCycler() {
        return false;
    }

    @Override
    public void showProgress(String text) {

    }

    @Override
    public boolean dismissProgress() {
        return false;
    }

    @Override
    public void showToastLong(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showToastShort(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToastLong(String text, int type) {
        showToastLib(text, type, TastyToast.LENGTH_LONG);
    }

    @Override
    public void showToastShort(String text, int type) {
        showToastLib(text, type, TastyToast.LENGTH_SHORT);
    }

    public void showToastLib(String text, int type, int time) {
        switch (type) {
            case ProgressProvider.TOAST_SUCCESS:
                TastyToast.makeText(this, text, time, TastyToast.SUCCESS);
                break;
            case ProgressProvider.TOAST_ERROR:
                TastyToast.makeText(this, text, time, TastyToast.ERROR);
                break;
            case ProgressProvider.TOAST_WARNING:
                TastyToast.makeText(this, text, time, TastyToast.WARNING);
                break;
            case ProgressProvider.TOAST_INFOR:
                TastyToast.makeText(this, text, time, TastyToast.INFO);
                break;
            case ProgressProvider.TOAST_DEFAULT:
                TastyToast.makeText(this, text, time, TastyToast.DEFAULT);
                break;
        }
    }

    @Override
    public void onScrollItemClick(View view, Record record) {
        ActivityManager.startGdbRecordActivity(this, record);
    }

    /**
     * 检查文件系统，删除无用文件
     */
    private void startFileService() {
        DebugLog.e("");
        startService(new Intent().setClass(this, FileService.class));
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(String serviceName) {
        boolean isWork = false;
        android.app.ActivityManager myAM = (android.app.ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<android.app.ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
