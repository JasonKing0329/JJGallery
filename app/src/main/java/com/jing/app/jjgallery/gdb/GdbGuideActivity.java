package com.jing.app.jjgallery.gdb;

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

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceKey;
import com.jing.app.jjgallery.gdb.view.recommend.RecommendFragment;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.ActivityManager;

/**
 * 可能是由于用到了DrawerLayout，采用BaseActivity运营统一的样式总是隐藏不了actionbar
 * 只能单独继承AppCompatActivity，并在manifest中将theme设置为.NoActionbar的才行
 */
public class GdbGuideActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout drawerLayout;
    private ImageView gameView;
    private ImageView starView;
    private ImageView recordView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayHelper.disableScreenshot(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdb_guide);
        initView();
    }

    //    @Override
    public void initView() {
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

        initRecommentd();
        initCenter();
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

        if (id == R.id.nav_camera) {
            ActivityManager.startFileManagerActivity(this, null);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gdb_guide_game_text:
                break;
            case R.id.gdb_guide_star_text:
                ActivityManager.startGDBMainActivity(this, null);
                break;
            case R.id.gdb_guide_record_text:
                break;
        }
    }
}
