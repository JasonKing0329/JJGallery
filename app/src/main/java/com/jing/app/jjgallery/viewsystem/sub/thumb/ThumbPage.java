package com.jing.app.jjgallery.viewsystem.sub.thumb;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ScrollView;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.Constants;
import com.jing.app.jjgallery.model.pub.IndexCreator;
import com.jing.app.jjgallery.presenter.sub.ThumbPresenter;
import com.jing.app.jjgallery.res.AppResManager;
import com.jing.app.jjgallery.res.AppResProvider;
import com.jing.app.jjgallery.res.ColorRes;
import com.jing.app.jjgallery.res.JResource;
import com.jing.app.jjgallery.util.ScreenUtils;
import com.jing.app.jjgallery.viewsystem.IColorPage;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.publicview.DragImageView;
import com.jing.app.jjgallery.viewsystem.publicview.IndexView;
import com.jing.app.jjgallery.viewsystem.sub.dialog.FolderDialog;
import com.jing.app.jjgallery.viewsystem.sub.dialog.ShowImageDialog;
import com.king.lib.colorpicker.ColorPickerSelectionData;

import java.util.List;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public abstract class ThumbPage implements IPage, IColorPage, OnThumbImageItemListener, OnThumbFolderItemListener
    , View.OnClickListener, IndexView.OnIndexSelectListener {

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

    private View upperView;
    private RecyclerView folderRecyclerView;
    private LinearLayoutManager folderLayoutManager;
    private RecyclerView imageRecyclerView;

    private ShowImageDialog imageDialog;
    private DragImageView dragView;
    private ScrollView indexViewParent;
    private IndexView indexView;
    private IndexCreator indexCreator;

    private FolderDialog folderDialog;
    private ThumbImageAdapter mImageAdapter;

    public ThumbPage(Context context, View contentView, boolean isChooserMode) {
        mContext = context;
        this.isChooserMode = isChooserMode;
        upperView = contentView.findViewById(R.id.thumb_folder_upper);
        folderRecyclerView = (RecyclerView) contentView.findViewById(R.id.thumbfolder_recyclerview);
        imageRecyclerView = (RecyclerView) contentView.findViewById(R.id.thumbfolder_gridview);
        indexView = (IndexView) contentView.findViewById(R.id.thumbfolder_indexview);
        indexViewParent = (ScrollView) contentView.findViewById(R.id.thumbfolder_indexview_parent);
        dragView = (DragImageView) contentView.findViewById(R.id.thumbfolder_indexview_control);

        upperView.setOnClickListener(this);
        indexView.setOnIndexSelectListener(this);

        indexCreator = new IndexCreator(indexView);

        int size = mContext.getResources().getDimensionPixelSize(R.dimen.thumbfolder_index_control_width);
        dragView.setImageResource(R.drawable.index_control);
        dragView.fitImageSize(size, size);
        dragView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (indexViewParent.getVisibility() == View.GONE) {
                    indexViewParent.setVisibility(View.VISIBLE);
                }
                else {
                    indexViewParent.setVisibility(View.GONE);
                }
            }
        });

        //orientation
        folderLayoutManager = new LinearLayoutManager(mContext);
        folderLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//		layoutManager.setRecycleChildrenOnDetach(true);
        folderRecyclerView.setLayoutManager(folderLayoutManager);
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

    protected void initIndexStateController() {

        Log.d("ThumbPage", "initIndexStateController");
        //notice use indexViewParent or indexView
        //position should use indexView, but visibility should base on its parent
        if (indexViewParent.getVisibility() != View.VISIBLE) {//when change orientation need consider about this
            //show in the right-bottom corner of screen
            dragView.setPosition(ScreenUtils.getScreenWidth(mContext) - dragView.getImageWidth() - 100
                    , ScreenUtils.getScreenHeight(mContext) - dragView.getImageWidth() - 100);
            return;
        }

        indexView.post(new Runnable() {
            @Override
            public void run() {
                int pos[] = new int[2];
                indexView.getLocationOnScreen(pos);//position should use indexView
                Log.d("ThumbPage", "pos[" + pos[0] + "," + pos[1] + "]");
                int offset = dragView.getImageWidth();
                dragView.setPosition(pos[0] - offset, pos[1]);
                dragView.setVisibility(View.VISIBLE);
            }
        });
    }

    public Context getContext() {
        return mContext;
    }

    public IndexCreator getIndexCreator() {
        return  indexCreator;
    }

    protected void showIndexView() {
        Log.d("ThumbPage", "showIndexView");
        indexViewParent.setVisibility(View.VISIBLE);
    }

    protected abstract ThumbFolderAdapter getFolderAdapter();

    @Override
    public boolean onBack() {
        if (mImageAdapter.isActionMode()) {
            mImageAdapter.showActionMode(false);
            mImageAdapter.notifyDataSetChanged();
            return true;
        }
        //To fix: showImageDialog>click setasslidingmenubk icon>popup listwindow
        //>back>show image dialog again>click setasslidingmenubk, there is no action
        if (imageDialog != null) {
            imageDialog.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onExit() {

    }

    @Override
    public void onIconClick(View view) {
        switch (view.getId()) {
        }
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
            actionBar.addColorIcon();
        }
        actionBar.addSearchIcon();
        actionBar.onConfiguration(mContext.getResources().getConfiguration().orientation);

        applyExtendColors();
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {

        if (imageDialog != null) {
            imageDialog.setOrientationChanged();
            if (imageDialog.isShowing()) {
                imageDialog.onConfigChange();
            }
        }
        if (folderDialog != null) {
            folderDialog.updateHeight();
        }

//        if (newConfig.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
//            actionBar.onLandscape();
//        }
//        else {
//            actionBar.onVertical();
//        }

        initIndexStateController();
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = (ThumbPresenter) presenter;
    }

    protected void focusFolderItem(View view, int position) {
        // no effect with repeat click
        if (position == getFolderAdapter().getFocusPosition()) {
            return;
        }

        getFolderAdapter().setFocusPosition(position);
        getFolderAdapter().notifyDataSetChanged();
        if (mImageAdapter.isActionMode()) {
            mImageAdapter.showActionMode(false);
        }
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

    @Override
    public void onClick(View v) {
        if (v == upperView) {
            onUpperClicked();
        }
    }

    protected void showUpperView(boolean show) {
        upperView.setVisibility(show ? View.VISIBLE:View.GONE);
    }

    protected void scrollFolderToPosition(int position) {
        folderLayoutManager.scrollToPosition(position);
    }

    protected abstract void onUpperClicked();

    @Override
    public void onSelect(String index) {
        folderRecyclerView.scrollToPosition(indexCreator.getIndexPosition(index));
    }

    @Override
    public void onColorChanged(String key, int newColor) {
        if (key.equals(ColorRes.FM_THUMB_INDEX_NORMAL_COLOR)) {
            indexView.updateNormalColor(newColor);
        }
        else if (key.equals(ColorRes.FM_THUMB_TEXT_COLOR)) {
            indexView.updateTextColor(newColor);
        }
    }

    @Override
    public void onApplyDefaultColors() {
        JResource.removeColor(ColorRes.ACTIONBAR_BK);
        JResource.saveColorUpdate(mContext);
        applyExtendColors();
    }

    @Override
    public void applyExtendColors() {
        indexView.updateNormalColor(JResource.getColor(mContext, ColorRes.FM_THUMB_INDEX_NORMAL_COLOR, R.color.actionbar_bk_blue));
        indexView.updateTextColor(JResource.getColor(mContext, ColorRes.FM_THUMB_TEXT_COLOR, R.color.white));
    }

    @Override
    public List<ColorPickerSelectionData> getColorPickerSelectionData() {
        return new AppResManager().getThumbList(mContext);
    }

}
