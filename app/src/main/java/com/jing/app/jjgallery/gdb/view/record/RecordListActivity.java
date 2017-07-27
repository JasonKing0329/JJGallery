package com.jing.app.jjgallery.gdb.view.record;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.http.bean.data.DownloadItem;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.gdb.presenter.record.RecordListPresenter;
import com.jing.app.jjgallery.gdb.view.list.GDBListActivity;
import com.jing.app.jjgallery.service.http.Command;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/12 11:20
 */
public class RecordListActivity extends GDBListActivity implements IRecordListHolder {

    private RecordListPresenter recordPresenter;
    private RecordsListFragment recordFragment;

    private ActionBar actionBar;
    
    @Override
    public int getContentView() {
        return R.layout.activity_gdb_record_list;
    }


    @Override
    protected void initController() {
        recordPresenter = new RecordListPresenter(this);
        presenter = recordPresenter;
    }

    @Override
    protected void initView() {
        initActionbar();
        onRecordListPage();
    }

    @Override
    protected void initBackgroundWork() {
        presenter.checkServerStatus();
    }

    private void initActionbar() {
        actionBar = new ActionBar(this, findViewById(R.id.group_actionbar));
        actionBar.setActionIconListener(iconListener);
        actionBar.setActionMenuListener(menuListener);
        actionBar.setActionSearchListener(searchListener);
        actionBar.clearActionIcon();
        actionBar.addSortIcon();
        actionBar.addSearchIcon();
        actionBar.addShowIcon();
        actionBar.addMenuIcon();
        actionBar.addBackIcon();
        actionBar.addPlayIcon();
        actionBar.setTitle(getString(R.string.gdb_title_record));
    }

    public void onRecordListPage() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (recordFragment == null) {
            recordFragment = new RecordsListFragment();
        }

        ft.replace(R.id.group_ft_container, recordFragment, "RecordsListFragment");
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
                    recordFragment.changeSortType();
                    break;
                case R.id.actionbar_show:
                    startActivity(new Intent(RecordListActivity.this, RecordSceneActivity.class));
                    finish();
                    break;
                case R.id.actionbar_play:
                    if (view.isSelected()) {
                        view.setSelected(false);
                        recordFragment.showCanPlayList(false);
                    }
                    else {
                        view.setSelected(true);
                        recordFragment.showCanPlayList(true);
                    }
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
            menuInflater.inflate(R.menu.gdb_record_list, menu);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_gdb_check_server:
                    recordPresenter.checkNewRecordFile();
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
            recordFragment.filterRecord(text);
        }
    };

    @Override
    protected void onServerConnectSuccess() {
        recordPresenter.checkNewRecordFile();
    }

    @Override
    protected void onDownloadFinished() {
        recordFragment.refreshList();
    }

    @Override
    protected String getListType() {
        return Command.TYPE_RECORD;
    }

    @Override
    protected String getSavePath() {
        return Configuration.GDB_IMG_RECORD;
    }

    @Override
    protected List<DownloadItem> getListToDownload(List<DownloadItem> downloadList, List<DownloadItem> repeatList) {
        return recordPresenter.pickRecordToDownload(downloadList, repeatList);
    }

    @Override
    public RecordListPresenter getPresenter() {
        return recordPresenter;
    }
}
