package com.jing.app.jjgallery.viewsystem.sub.thumb;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.sub.ThumbPresenter;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.publicview.DragImageView;
import com.jing.app.jjgallery.viewsystem.sub.dialog.FolderDialog;
import com.jing.app.jjgallery.viewsystem.sub.dialog.ShowImageDialog;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public abstract class ThumbPage implements IPage, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    /**
     * 删除时的透明过程
     */
    private final int TIME_GALLERY_ANIM_REMOVE = 200;
    /**
     * 删除时的透明过程结束后后面的item向前挤压的过程
     */
    private final int TIME_GALLERY_ANIM_MOVE = 500;

    private final String TAG = "ThumbPage";
    private Context mContext;

    protected ThumbPresenter mPresenter;

    private boolean isChooserMode;
    private ActionBar actionBar;

    private RecyclerView folderRecyclerView;
    private GridView gridView;

    private ShowImageDialog imageDialog;
    private DragImageView dragView;

    private FolderDialog folderDialog;

    public ThumbPage(Context context, View contentView, boolean isChooserMode) {
        mContext = context;
        this.isChooserMode = isChooserMode;
        folderRecyclerView = (RecyclerView) contentView.findViewById(R.id.thumbfolder_recyclerview);
        gridView = (GridView) contentView.findViewById(R.id.thumbfolder_gridview);
        dragView = (DragImageView) contentView.findViewById(R.id.thumbfolder_indexview_control);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);

        //orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//		layoutManager.setRecycleChildrenOnDetach(true);
        folderRecyclerView.setLayoutManager(layoutManager);
        //animation
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(TIME_GALLERY_ANIM_REMOVE);
        animator.setMoveDuration(TIME_GALLERY_ANIM_MOVE);
        folderRecyclerView.setItemAnimator(animator);
        folderRecyclerView.setAdapter(getFolderAdapter());
    }

    protected abstract ThumbFolderAdapter getFolderAdapter();

    @Override
    public boolean onBack() {
        return false;
    }

    @Override
    public void onExit() {

    }

    @Override
    public void onIconClick(View view) {

    }

    @Override
    public void createMenu(MenuInflater menuInflater, Menu menu) {

    }

    @Override
    public void onPrepareMenu(MenuInflater menuInflater, Menu menu) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onTextChanged(String text, int start, int before, int count) {

    }

    @Override
    public void initActionbar(ActionBar actionBar) {

        if (!isChooserMode) {
            actionBar.addMenuIcon();
            actionBar.addGalleryIcon();
            actionBar.addRefreshIcon();
        }
        actionBar.addSearchIcon();

        if (mContext.getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            actionBar.onLandscape();
        }
        else {
            actionBar.onVertical();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = (ThumbPresenter) presenter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}
