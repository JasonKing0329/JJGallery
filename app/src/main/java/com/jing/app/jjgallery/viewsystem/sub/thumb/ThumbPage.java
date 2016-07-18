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
import com.jing.app.jjgallery.presenter.main.order.SOrderProvider;
import com.jing.app.jjgallery.presenter.main.order.SOrderProviderCallback;
import com.jing.app.jjgallery.presenter.sub.ThumbPresenter;
import com.jing.app.jjgallery.res.AppResManager;
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
    , View.OnClickListener, IndexView.OnIndexSelectListener, SOrderProviderCallback {

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
    private GridLayoutManager imageLayoutManager;

    private ShowImageDialog imageDialog;
    private DragImageView dragView;
    private ScrollView indexViewParent;
    private IndexView indexView;
    private IndexCreator indexCreator;

    private FolderDialog folderDialog;
    private ThumbImageAdapter mImageAdapter;

    protected SOrderProvider sOrderProvider;

    public ThumbPage(Context context, View contentView, boolean isChooserMode) {
        mContext = context;
        this.isChooserMode = isChooserMode;
        upperView = contentView.findViewById(R.id.thumb_folder_upper);
        folderRecyclerView = (RecyclerView) contentView.findViewById(R.id.thumbfolder_recyclerview);
        imageRecyclerView = (RecyclerView) contentView.findViewById(R.id.thumbfolder_gridview);
        indexView = (IndexView) contentView.findViewById(R.id.thumbfolder_indexview);
        indexViewParent = (ScrollView) contentView.findViewById(R.id.thumbfolder_indexview_parent);
        dragView = (DragImageView) contentView.findViewById(R.id.thumbfolder_indexview_control);

        applyExtendColors();

        upperView.setOnClickListener(this);
        indexView.setOnIndexSelectListener(this);

        sOrderProvider = new SOrderProvider(context, this);
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
        imageLayoutManager = new GridLayoutManager(mContext, column);
        imageRecyclerView.setLayoutManager(imageLayoutManager);
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
            case R.id.actionbar_refresh:
                refreshCurrent();
                break;
        }
    }

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
        if (mImageAdapter.isActionMode()) {
            menuInflater.inflate(R.menu.thumbfolder_actionmode, menu);
            int checkedItemNum = mImageAdapter.getCheckMap().size();
            if (checkedItemNum == 0) {
                menu.findItem(R.id.menu_thumb_addtooder).setVisible(false);
                menu.findItem(R.id.menu_thumb_setascover).setVisible(false);
                menu.findItem(R.id.menu_thumb_viewdetail).setVisible(false);
                menu.findItem(R.id.menu_thumb_delete).setVisible(false);
                menu.findItem(R.id.menu_thumb_selectall).setVisible(true);
                menu.findItem(R.id.menu_thumb_deselectall).setVisible(false);
            }
            else if (checkedItemNum == 1) {
                menu.findItem(R.id.menu_thumb_addtooder).setVisible(true);
                menu.findItem(R.id.menu_thumb_setascover).setVisible(true);
                menu.findItem(R.id.menu_thumb_viewdetail).setVisible(true);
                menu.findItem(R.id.menu_thumb_delete).setVisible(true);
                menu.findItem(R.id.menu_thumb_selectall).setVisible(true);
                menu.findItem(R.id.menu_thumb_deselectall).setVisible(true);
            }
            else if (checkedItemNum == mImageAdapter.getItemCount()) {
                menu.findItem(R.id.menu_thumb_addtooder).setVisible(true);
                menu.findItem(R.id.menu_thumb_setascover).setVisible(false);
                menu.findItem(R.id.menu_thumb_viewdetail).setVisible(false);
                menu.findItem(R.id.menu_thumb_delete).setVisible(true);
                menu.findItem(R.id.menu_thumb_selectall).setVisible(false);
                menu.findItem(R.id.menu_thumb_deselectall).setVisible(true);
            }
            else {
                menu.findItem(R.id.menu_thumb_addtooder).setVisible(true);
                menu.findItem(R.id.menu_thumb_setascover).setVisible(false);
                menu.findItem(R.id.menu_thumb_viewdetail).setVisible(false);
                menu.findItem(R.id.menu_thumb_delete).setVisible(true);
                menu.findItem(R.id.menu_thumb_selectall).setVisible(true);
                menu.findItem(R.id.menu_thumb_deselectall).setVisible(true);
            }
        }
        else {
            if (!isChooserMode) {
                menuInflater.inflate(R.menu.home_file_manager, menu);
                menu.setGroupVisible(R.id.group_file, false);
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_thumb_addtooder:
                sOrderProvider.openOrderChooserToAddItem(mImageAdapter.getSelectedList());
                break;
            case R.id.menu_thumb_setascover:
                sOrderProvider.openOrderChooserToSetCover(mImageAdapter.getSlectedImagePath());
                break;
            case R.id.menu_thumb_viewdetail:
                sOrderProvider.viewDetails(mImageAdapter.getSlectedImagePath());
                break;
            case R.id.menu_move_to_folder:
                sOrderProvider.openFolderDialogToMoveFiles(mImageAdapter.getSelectedList());
                break;
            case R.id.menu_thumb_delete:
                deleteSelectedFiles();
                break;
            case R.id.menu_thumb_selectall:
                mImageAdapter.selectAll();
                mImageAdapter.notifyDataSetChanged();
                break;
            case R.id.menu_thumb_deselectall:
                mImageAdapter.deSelectAll();
                mImageAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return false;
    }

    protected abstract void deleteSelectedFiles();

    protected List<Integer> getSelectedIndex() {
        return mImageAdapter.getSelectedIndex();
    }

    protected List<String> getSelectedPath() {
        return mImageAdapter.getSelectedList();
    }

    @Override
    public void onTextChanged(String text, int start, int before, int count) {
        onTextFilterChanged(text);
    }

    protected abstract void onTextFilterChanged(String text);

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

        initIndexStateController();

        int column = mContext.getResources().getInteger(R.integer.thumb_column);
        imageLayoutManager.setSpanCount(column);
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

    protected void showImages(List<String> pathList) {
        mImageAdapter.setDatas(pathList);
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
            indexView.refresh();
        }
        else if (key.equals(ColorRes.FM_THUMB_TEXT_COLOR)) {
            indexView.updateTextColor(newColor);
            indexView.refresh();
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
        indexView.refresh();
    }

    @Override
    public List<ColorPickerSelectionData> getColorPickerSelectionData() {
        return new AppResManager().getThumbList(mContext);
    }

    @Override
    public void onMoveFinish(String folderPath) {
        mImageAdapter.showActionMode(false);
        refreshCurrent();
    }

    protected abstract void refreshCurrent();

    @Override
    public void onAddToOrderFinished() {

    }

    @Override
    public void onDeleteIndex(int index) {

    }

    @Override
    public void onDeleteFinished(int count) {
        mImageAdapter.showActionMode(false);
        refreshCurrent();
    }

}
