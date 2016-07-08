package com.jing.app.jjgallery.viewsystem.sub.thumb;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.Constants;
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
public abstract class ThumbPage implements IPage, OnThumbImageItemListener, OnThumbFolderItemListener {

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
    private RecyclerView imageRecyclerView;

    private ShowImageDialog imageDialog;
    private DragImageView dragView;

    private FolderDialog folderDialog;
    private ThumbImageAdapter mImageAdapter;

    private View lastChosedFolder;

    public ThumbPage(Context context, View contentView, boolean isChooserMode) {
        mContext = context;
        this.isChooserMode = isChooserMode;
        folderRecyclerView = (RecyclerView) contentView.findViewById(R.id.thumbfolder_recyclerview);
        imageRecyclerView = (RecyclerView) contentView.findViewById(R.id.thumbfolder_gridview);
        dragView = (DragImageView) contentView.findViewById(R.id.thumbfolder_indexview_control);

        //orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//		layoutManager.setRecycleChildrenOnDetach(true);
        folderRecyclerView.setLayoutManager(layoutManager);
        //animation
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(TIME_GALLERY_ANIM_REMOVE);
        animator.setMoveDuration(TIME_GALLERY_ANIM_MOVE);
        folderRecyclerView.setItemAnimator(animator);
        folderRecyclerView.setAdapter(getFolderAdapter());

        int column = mContext.getResources().getInteger(R.integer.thumb_column);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, column);
        imageRecyclerView.setLayoutManager(gridLayoutManager);
        imageRecyclerView.setAdapter(getFolderAdapter());
        mImageAdapter = new ThumbImageAdapter(mContext, this);
        imageRecyclerView.setAdapter(mImageAdapter);
    }

    public Context getContext() {
        return mContext;
    }
    protected abstract ThumbFolderAdapter getFolderAdapter();

    @Override
    public boolean onBack() {
        if (mImageAdapter.isActionMode()) {
            mImageAdapter.showActionMode(false);
            mImageAdapter.notifyDataSetChanged();
            return true;
        }
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

        actionBar.clearActionIcon();
        if (!isChooserMode) {
            actionBar.addMenuIcon();
            actionBar.addGalleryIcon();
            actionBar.addRefreshIcon();
        }
        actionBar.addSearchIcon();
        actionBar.onConfiguration(mContext.getResources().getConfiguration().orientation);
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

    protected void focusFolderItem(View view) {
        if (lastChosedFolder == null) {
            lastChosedFolder = view;
        }
        else {
            if (view != lastChosedFolder) {
                if (mImageAdapter.isActionMode()) {
                    mImageAdapter.showActionMode(false);
                }
            }
            lastChosedFolder.setBackground(null);
        }
        view.setBackgroundResource(R.drawable.gallery_border_choose);
        lastChosedFolder = view;

        view.findViewById(R.id.thumb_folder_item_image).startAnimation(getFolderAnimation());
    }
    protected void showFolderImage(String path) {
        mImageAdapter.setDatas(mPresenter.loadFolderItems(path));
        mImageAdapter.notifyDataSetChanged();
    }

    /**
     * origin codes issue: LinearInterpolator is not working in xml definition
     * must use java codes to set it
     * @return
     */
    private Animation getFolderAnimation() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.thumb_folder_click);
        LinearInterpolator interpolator = new LinearInterpolator();
        animation.setInterpolator(interpolator);
        return animation;
    }

    @Override
    public void onThumbImageItemClick(View view, int position) {

        if (isChooserMode) {
            Activity activity = (Activity) getContext();
            activity.getIntent().putExtra(Constants.KEY_THUMBFOLDER_CHOOSE_CONTENT, mImageAdapter.getImagePath(position));
            activity.setResult(0, activity.getIntent());
            activity.finish();
        }
        else {
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.thumb_item_longclick));
            //showImage(view, position);
            showImage(mImageAdapter.getImagePath(position));
        }
    }

    private void showImage(String path) {
        if (imageDialog == null) {
            imageDialog = new ShowImageDialog(mContext, null, 0);
        }
        imageDialog.setImagePath(path);
        imageDialog.fitImageView();
        imageDialog.show();
    }

    @Override
    public void onThumbImageItemLongClick(View view, int position) {
        if (!isChooserMode) {
            if (mImageAdapter.isActionMode()) {
                mImageAdapter.showActionMode(false);
            }
            else {
                mImageAdapter.notifyShowAnimation(position);
                mImageAdapter.resetMap();
                mImageAdapter.getCheckMap().put(position, true);
                mImageAdapter.showActionMode(true);
            }
            mImageAdapter.notifyDataSetChanged();
        }
    }
}
