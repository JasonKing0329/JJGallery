package com.jing.app.jjgallery.viewsystem.sub.thumb;

import android.support.v4.app.FragmentTransaction;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.Constants;
import com.jing.app.jjgallery.presenter.sub.ThumbPresenter;
import com.jing.app.jjgallery.viewsystem.main.filesystem.FileManagerThumbFragment;
import com.jing.app.jjgallery.viewsystem.main.order.SOrderThumbFragment;

public class ThumbActivity extends BaseActivity implements IThumbSelector {

    private ThumbTypeFragment typeFragment;
    private FileManagerThumbFragment folderFragment;
    private SOrderThumbFragment orderFragment;
    private ThumbPresenter mPresenter;

    @Override
    public boolean isActionBarNeed() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_thumb;
    }

    @Override
    public void initController() {
        mPresenter = new ThumbPresenter();
    }

    @Override
    public void initView() {
        mActionBar.addBackIcon();
        mActionBar.setTitle(getString(R.string.thumb_select_title));
        showTypePage();
    }

    @Override
    public void initBackgroundWork() {

    }

    private void showTypePage() {
        typeFragment = new ThumbTypeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.thumb_fragment_container, typeFragment, "ThumbTypeFragment");
        ft.commit();
    }

    @Override
    public void onSelectFolderThumb() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        folderFragment = new FileManagerThumbFragment();
        folderFragment.setActionbar(mActionBar);
        folderFragment.setPresenter(mPresenter);
        ft.replace(R.id.thumb_fragment_container, folderFragment, "FileManagerThumbFragment");
        ft.commit();
    }

    @Override
    public void onSelectOrderThumb() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        orderFragment = new SOrderThumbFragment();
        orderFragment.setActionbar(mActionBar);
        orderFragment.setPresenter(mPresenter);
        ft.replace(R.id.thumb_fragment_container, orderFragment, "SOrderThumbFragment");
        ft.commit();
    }

    @Override
    public void onSelectImage(String imagePath) {
        getIntent().putExtra(Constants.KEY_THUMBFOLDER_CHOOSE_CONTENT, imagePath);
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    public void onCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
