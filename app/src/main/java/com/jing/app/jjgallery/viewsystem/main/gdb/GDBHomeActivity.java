package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.HomeSelecter;
import com.jing.app.jjgallery.viewsystem.publicview.ChangeThemeDialog;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;

import java.util.HashMap;

public class GDBHomeActivity extends BaseActivity {

    private Fragment currentFragment;
    private StarListFragment starFragment;
    private RecordListFragment recordFragment;
    private RecordSceneListFragment sceneListFragment;
    @Override
    protected boolean isActionBarNeed() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_gdb_home;
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        onStarListPage();
    }

    public void onStarListPage() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (starFragment == null) {
            starFragment = new StarListFragment();
            starFragment.setActionbar(mActionBar);
        }
        currentFragment = starFragment;

        ft.replace(R.id.gdb_fragment_container, currentFragment, "StarListFragment");
        ft.commit();
    }

    public void onRecordListPage() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (recordFragment == null) {
            recordFragment = new RecordListFragment();
            recordFragment.setActionbar(mActionBar);
        }
        currentFragment = recordFragment;

        ft.replace(R.id.gdb_fragment_container, currentFragment, "RecordListFragment");
        ft.commit();
    }

    public void onRecordSceneListPage() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (sceneListFragment == null) {
            sceneListFragment = new RecordSceneListFragment();
            sceneListFragment.setActionbar(mActionBar);
        }
        currentFragment = sceneListFragment;

        ft.replace(R.id.gdb_fragment_container, sceneListFragment, "RecordSceneListFragment");
        ft.commit();
    }

    @Override
    protected void initBackgroundWork() {

    }

    @Override
    public void onIconClick(View view) {
        super.onIconClick(view);
        if (view.getId() == R.id.actionbar_home) {
            new HomeSelecter(this).startDefaultHome(this, null);
            finish();
        }
        else {
            if (currentFragment == recordFragment) {
                recordFragment.onIconClick(view);
            }
            else if (currentFragment == sceneListFragment) {
                sceneListFragment.onIconClick(view);
            }
        }
    }

    @Override
    public void onTextChanged(String text, int start, int before, int count) {
        super.onTextChanged(text, start, before, count);
        if (currentFragment == starFragment) {
             starFragment.onTextChanged(text, start, before, count);
        }
        else if (currentFragment == recordFragment) {
            recordFragment.onTextChanged(text, start, before, count);
        }
        else if (currentFragment == sceneListFragment) {
            sceneListFragment.onTextChanged(text, start, before, count);
        }
    }

    @Override
    public void createMenu(MenuInflater menuInflater, Menu menu) {
        super.createMenu(menuInflater, menu);
        loadMenu(menuInflater, menu);
    }

    @Override
    public void onPrepareMenu(MenuInflater menuInflater, Menu menu) {
        super.onPrepareMenu(menuInflater, menu);
        loadMenu(menuInflater, menu);
    }

    private void loadMenu(MenuInflater menuInflater, Menu menu) {
        menu.clear();
        menuInflater.inflate(R.menu.gdb_main, menu);
        if (currentFragment instanceof StarListFragment) {
            menu.findItem(R.id.menu_gdb_star).setVisible(false);
        }
        else if (currentFragment instanceof RecordListFragment) {
            menu.findItem(R.id.menu_gdb_record).setVisible(false);
        }
        else if (currentFragment instanceof RecordSceneListFragment) {
            menu.findItem(R.id.menu_gdb_record).setVisible(false);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_gdb_record:
                onRecordListPage();
                break;
            case R.id.menu_gdb_star:
                onStarListPage();
                break;
            case R.id.menu_change_theme:
                openChangeThemeDialog();
                break;
        }
        return super.onMenuItemClick(item);
    }

    private void openChangeThemeDialog() {
        new ChangeThemeDialog(this, new CustomDialog.OnCustomDialogActionListener() {

            @Override
            public boolean onSave(Object object) {
                ActivityManager.reload(GDBHomeActivity.this);
                return false;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {

            }

            @Override
            public boolean onCancel() {
                return false;
            }
        }).show();
    }
}
