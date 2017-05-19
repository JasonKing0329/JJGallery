package com.jing.app.jjgallery.gdb.view.home;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.ImageView;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.gdb.presenter.GdbGuidePresenter;
import com.jing.app.jjgallery.gdb.view.recommend.IRecommendHolder;
import com.jing.app.jjgallery.gdb.view.update.GdbUpdateListener;
import com.jing.app.jjgallery.gdb.view.update.GdbUpdateManager;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.ActivityManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/19 13:33
 */
public class GHomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener
    , IHomeHolder, IRecommendHolder{

    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private ImageView navHeaderView;

    private GdbGuidePresenter mPresenter;

    private GHomeFragment homeFragment;

    @Override
    public boolean isActionBarNeed() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_gdb_home_v4;
    }

    @Override
    public void initController() {
        mPresenter = new GdbGuidePresenter();
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);

        mActionBar.useMenuLeftIcon();
        mActionBar.setTitle("GDB");
        initDrawer();
        initContent();
    }

    @Override
    public void initBackgroundWork() {
        checkUpdate();
    }

    private void initDrawer() {
        navView.setNavigationItemSelectedListener(this);
        navView.setItemIconTintList(null);
        navHeaderView = (ImageView) navView.getHeaderView(0).findViewById(R.id.nav_header_bg);

        String path = SettingProperties.getPreference(this, PreferenceKey.PREF_GDB_NAV_HEADER_BG);
        SImageLoader.getInstance().displayImage(path, navHeaderView);
    }

    private void initContent() {
        homeFragment = new GHomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.group_ft_container, homeFragment, "GHomeFragment");
        ft.commit();
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

    /**
     * check gdb database update
     */
    private void checkUpdate() {
        GdbUpdateManager manager = new GdbUpdateManager(this, new GdbUpdateListener() {
            @Override
            public void onUpdateFinish() {
                ActivityManager.reload(GHomeActivity.this);
            }

            @Override
            public void onUpdateCancel() {

            }
        });
        manager.startCheck();
    }

    @Override
    public GdbGuidePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onRecommendRecordsLoaded() {

    }
}
