package com.jing.app.jjgallery.gdb.view.star;

import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.gdb.presenter.star.StarListPresenter;
import com.jing.app.jjgallery.gdb.view.list.GDBListActivity;
import com.jing.app.jjgallery.service.http.Command;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/12 9:33
 */
public class StarListActivity extends GDBListActivity implements IStarListHolder {

    private StarListFragment starFragment;
    private StarListPresenter starPresenter;

    private ActionBar actionBar;

    @Override
    public int getContentView() {
        return R.layout.activity_gdb_star_list;
    }

    @Override
    protected void initController() {
        starPresenter = new StarListPresenter(this);
        presenter = starPresenter;
    }

    @Override
    protected void initView() {
        initActionbar();
        onStarListPage();
    }

    @Override
    protected void initBackgroundWork() {
        starPresenter.checkNewStarFile();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (starFragment != null) {
            starFragment.dispatchTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    private void initActionbar() {
        actionBar = new ActionBar(this, findViewById(R.id.group_actionbar));
        actionBar.setActionIconListener(iconListener);
        actionBar.setActionMenuListener(menuListener);
        actionBar.setActionSearchListener(searchListener);
        actionBar.clearActionIcon();
        actionBar.addMenuIcon();
        actionBar.addSearchIcon();
        actionBar.addBackIcon();
        actionBar.addSortIcon();
        actionBar.addFavorIcon();
        actionBar.addIndexIcon();
        actionBar.setTitle(getString(R.string.gdb_title_star));
    }

    public void onStarListPage() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        starFragment = new StarListFragment();
        ft.replace(R.id.group_ft_container, starFragment, "StarListFragment");
        ft.commit();
    }

    private ActionBar.ActionIconListener iconListener = new ActionBar.ActionIconListener() {
        @Override
        public void onBack() {
            finish();
        }

        @Override
        public void onIconClick(View view) {
            switch (view.getId()) {
                case R.id.actionbar_sort:
                    starFragment.changeSortType();
                    break;
                case R.id.actionbar_index:
                    starFragment.changeSideBarVisible();
                    break;
                case R.id.actionbar_favor:
                    starFragment.changeFavorList();
                    break;
            }
        }
    };
    
    private ActionBar.ActionMenuListener menuListener = new ActionBar.ActionMenuListener() {
        @Override
        public void createMenu(MenuInflater menuInflater, Menu menu) {
            loadMenu(menuInflater, menu);
        }

        @Override
        public void onPrepareMenu(MenuInflater menuInflater, Menu menu) {
            loadMenu(menuInflater, menu);
        }

        private void loadMenu(MenuInflater menuInflater, Menu menu) {
            menu.clear();
            menuInflater.inflate(R.menu.gdb_star_list, menu);
        }
        
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_gdb_check_server:
                    starPresenter.checkNewStarFile();
                    break;
                case R.id.menu_gdb_recommend:
                    showRecommendDialog();
                    break;
                case R.id.menu_gdb_download:
                    showDownloadDialog();
                    break;
            }
            return false;
        }
    };

    private ActionBar.ActionSearchListener searchListener = new ActionBar.ActionSearchListener() {
        @Override
        public void onTextChanged(String text, int start, int before, int count) {
            starFragment.filterStar(text);
        }
    };

    @Override
    public void onServerConnectSuccess() {
        starPresenter.checkNewStarFile();
    }

    @Override
    public void onDownloadFinished() {
        starFragment.refreshList();
    }

    @Override
    protected String getListType() {
        return Command.TYPE_STAR;
    }

    @Override
    protected String getSavePath() {
        return Configuration.GDB_IMG_STAR;
    }

    @Override
    protected List<DownloadItem> getListToDownload(List<DownloadItem> downloadList, List<DownloadItem> repeatList) {
        List<DownloadItem> newList = starPresenter.pickStarToDownload(downloadList, repeatList);
        return newList;
    }

    @Override
    public StarListPresenter getPresenter() {
        return starPresenter;
    }
}
