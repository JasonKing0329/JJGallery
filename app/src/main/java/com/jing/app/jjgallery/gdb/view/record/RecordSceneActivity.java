package com.jing.app.jjgallery.gdb.view.record;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.GBaseActivity;
import com.jing.app.jjgallery.gdb.presenter.record.RecordListPresenter;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/12 16:18
 */
public class RecordSceneActivity extends GBaseActivity implements IRecordSceneHolder, IRecordListHolder {

    private ActionBar actionBar;

    private RecordSceneFragment ftScene;
    private RecordsListFragment ftList;
    private Fragment ftCurrent;

    private RecordListPresenter recordPresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_gdb_record_scene;
    }

    @Override
    protected void initController() {
        recordPresenter = new RecordListPresenter(null);
    }

    @Override
    protected void initView() {
        initActionbar();
        onScenePage();
    }

    @Override
    protected void initBackgroundWork() {

    }

    private void initActionbar() {
        getSupportActionBar().hide();
        actionBar = new ActionBar(this, findViewById(R.id.group_actionbar));
        actionBar.setActionIconListener(iconListener);
        actionBar.setActionSearchListener(searchListener);
        actionBar.setTitle(getString(R.string.gdb_title_scene));
    }

    private void onScenePage() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (ftScene == null) {
            ftScene = new RecordSceneFragment();
            ft.add(R.id.group_ft_container, ftScene, "RecordSceneFragment");
        }
        else {
            ft.setCustomAnimations(R.anim.activity_left_in, R.anim.activity_right_out);
            ft.hide(ftList).show(ftScene);
        }
        ft.commit();

        ftCurrent = ftScene;
        actionBar.clearActionIcon();
        actionBar.addSortIcon();
        actionBar.addBackIcon();
        actionBar.addColorIcon();
    }

    private void onListPage(String scene) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (ftList == null) {
            ftList = new RecordsListFragment();
            ftList.setScene(scene);
            ft.setCustomAnimations(R.anim.activity_right_in, R.anim.activity_left_out);
            ft.add(R.id.group_ft_container, ftList, "RecordsListFragment");
        }
        else {
            ftList.setScene(scene);
            ft.setCustomAnimations(R.anim.activity_right_in, R.anim.activity_left_out);
            ft.hide(ftScene).show(ftList);
        }
        ft.commit();

        ftCurrent = ftList;
        actionBar.clearActionIcon();
        actionBar.addSortIcon();
        actionBar.addSearchIcon();
        actionBar.addBackIcon();
        actionBar.addPlayIcon();
    }

    @Override
    public RecordListPresenter getPresenter() {
        return recordPresenter;
    }

    @Override
    public void onSelectScene(String scene) {
        onListPage(scene);
    }

    @Override
    public void onBackPressed() {
        if (ftCurrent == ftList) {
            if (actionBar != null && actionBar.isSearchVisible()) {
                actionBar.closeSearch();
            }
            else {
                onScenePage();
            }
        }
        else {
            super.onBackPressed();
        }
    }

    private ActionBar.ActionIconListener iconListener = new ActionBar.ActionIconListener() {
        @Override
        public void onBack() {
            onBackPressed();
        }

        @Override
        public void onIconClick(View view) {
            switch (view.getId()) {
                case R.id.actionbar_sort:
                    if (ftCurrent == ftScene) {
                        showSortPopup(RecordSceneActivity.this, view);
                    }
                    else if (ftCurrent == ftList) {
                        ftList.changeSortType();
                    }
                    break;
                case R.id.actionbar_color:
                    if (ftCurrent == ftScene) {
                        ftScene.editColor();
                    }
                    break;
                case R.id.actionbar_play:
                    if (view.isSelected()) {
                        view.setSelected(false);
                        ftList.showCanPlayList(false);
                    }
                    else {
                        view.setSelected(true);
                        ftList.showCanPlayList(true);
                    }
                    break;
            }
        }
    };

    private ActionBar.ActionSearchListener searchListener = new ActionBar.ActionSearchListener() {
        @Override
        public void onTextChanged(String text, int start, int before, int count) {
            ftList.filterRecord(text);
        }
    };

    public void showSortPopup(Context context, View anchor) {
        PopupMenu menu = new PopupMenu(context, anchor);
        menu.getMenuInflater().inflate(R.menu.sort_gdb_record_scene, menu.getMenu());
        menu.show();
        menu.setOnMenuItemClickListener(sortListener);
    }

    PopupMenu.OnMenuItemClickListener sortListener = new PopupMenu.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_sort_by_name:
                    ftScene.sortByName();
                    break;
                case R.id.menu_sort_by_avg:
                    ftScene.sortByAvg();
                    break;
                case R.id.menu_sort_by_number:
                    ftScene.sortByNumber();
                    break;
                case R.id.menu_sort_by_max:
                    ftScene.sortByMax();
                    break;
            }
            return true;
        }
    };
}
