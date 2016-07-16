package com.jing.app.jjgallery.viewsystem.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jing.app.jjgallery.BaseSlidingActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.res.AppResProvider;
import com.jing.app.jjgallery.res.ColorRes;
import com.jing.app.jjgallery.res.JResource;
import com.jing.app.jjgallery.service.file.EncryptCheckService;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.main.bg.BackgroundManager;
import com.jing.app.jjgallery.viewsystem.sub.dialog.LoadFromDialog;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.king.lib.colorpicker.ColorPicker;
import com.king.lib.colorpicker.ColorPickerSelectionData;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JingYang on 2016/7/7 0007.
 * Description:
 * 将主界面通用的功能在该类实现，比如：检查全部加密文件、导出数据、导入数据、切换主题、actionbar颜色、sliding menu功能
 * 主界面Activity均支持sliding menu模式
 */
public abstract class AbsHomeActivity extends BaseSlidingActivity implements Handler.Callback
    , SlidingMenu.OnOpenedListener, SlidingMenu.OnOpenListener, SlidingMenu.OnClosedListener
    , ColorPicker.OnColorPickerListener {

    private EncryptCheckService mEncryptCheckService;

    private SlidingMenu mSlidingMenu;
    private SlidingViewManager slidingViewManager;

    private long lastTime = 0L;

    @Override
    protected void initView() {

        // initialize sliding menu
        initSlidingMenu();
        // initialize content view
        setUpContentView();
        // custom left sliding menu
        setUpLeftMenu();
        // custom right sliding menu
        setUpRightMenu();
        // apply color saved in disk file
        applyExtendColors();
    }

    private void initSlidingMenu() {
        mSlidingMenu = getSlidingMenu();
        mSlidingMenu.setShadowWidth((int) (getResources().getDisplayMetrics().density * 8));// 设置阴影宽度
        mSlidingMenu.setBehindOffset(getResources().getDisplayMetrics().widthPixels / 3);// 设置菜单宽度
        mSlidingMenu.setShadowDrawable(R.drawable.slidingmenu_left_shadow);
        mSlidingMenu.setSecondaryShadowDrawable(R.drawable.slidingmenu_right_shadow);
        mSlidingMenu.setFadeEnabled(true);
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.setBehindScrollScale(0.333f);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        mSlidingMenu.setOnOpenedListener(this);
        mSlidingMenu.setSecondaryOnOpenListner(this);
        mSlidingMenu.setOnClosedListener(this);
        setSlidingActionBarEnabled(false);
        slidingViewManager = new SlidingViewManager(this, R.layout.layout_sliding_left, R.layout.layout_sliding_right);
        slidingViewManager.setSlidingLeftCallback(new SlidingViewManager.SlidingLeftCallback() {
            @Override
            public void onCheckAllService() {
                checkkAllService();
            }

            @Override
            public void onExport() {
                export();
            }

            @Override
            public void onImport() {
                importFrom();
            }

            @Override
            public void onSetting() {
                openSetting();
            }

            @Override
            public void onExit() {
                exit();
            }
        });
        setBehindContentView(slidingViewManager.getSlidingLeftView());
        setSecondaryMenu(slidingViewManager.getSlidingRightView());
        mSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);

        BackgroundManager.getInstance().addSlidingSubscriber(slidingViewManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BackgroundManager.getInstance().removeSlidingSubscriber(slidingViewManager);
    }

    protected abstract void setUpContentView();
    protected abstract void setUpLeftMenu();
    protected abstract void setUpRightMenu();

    @Override
    public void onBackPressed() {
        if (mSlidingMenu.isMenuShowing()) {
            mSlidingMenu.showContent();
        } else if (mSlidingMenu.isSecondaryMenuShowing()) {
            mSlidingMenu.showContent();
        } else {
            // 双击返回
//            if (System.currentTimeMillis() - lastTime < 1000) {
//                finish();
//            } else {
//                lastTime = System.currentTimeMillis();
//            }
            if (!handleBack()) {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onIconClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_color:
                showColorPicker();
                break;
            default:
                onActionIconClick(view);
                break;
        }
    }

    private void showColorPicker() {
        ColorPicker colorPicker = null;
        if (colorPicker == null) {
            colorPicker = new ColorPicker(this, this);
            colorPicker.setResourceProvider(new AppResProvider(this));
        }
        colorPicker.setSelectionData(getListSelectionData());
        colorPicker.show();
    }

    protected abstract List<ColorPickerSelectionData> getListSelectionData();

    protected abstract void onActionIconClick(View view);

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_check_all_unencrypted:
                checkkAllService();
                break;
            case R.id.menu_export:
                export();
                break;
            case R.id.menu_import:
                importFrom();
                break;
            case R.id.menu_edit:
                openSetting();
                break;
            case R.id.menu_change_theme:
//				new ChangeThemeDialog(context, new CustomDialog.OnCustomDialogActionListener() {
//
//					@Override
//					public boolean onSave(Object object) {
//						reload();
//						return false;
//					}
//
//					@Override
//					public void onLoadData(HashMap<String, Object> data) {
//
//					}
//
//					@Override
//					public boolean onCancel() {
//						return false;
//					}
//				}).show();
                break;
            case R.id.menu_exit:
                exit();
                break;
        }
        return false;
    }

    private void exit() {
        if (needOptionWhenExit()) {

        }
        else {
            onExit();
        }
    }

    private void openSetting() {
        ActivityManager.startSettingActivity(this);
    }

    private void importFrom() {
        openLoadFromDialog();
    }

    private void export() {
        DBInfor.export(this);
    }

    private void checkkAllService() {
        showProgress(getString(R.string.checking));
        if (mEncryptCheckService == null) {
            mEncryptCheckService = new EncryptCheckService(this);
        }
        mEncryptCheckService.check();
    }

    protected abstract boolean handleBack();
    protected abstract boolean needOptionWhenExit();
    protected abstract void onExit();

    private void openLoadFromDialog() {
        new LoadFromDialog(this, LoadFromDialog.DATA_HISTORY, new CustomDialog.OnCustomDialogActionListener() {

            @Override
            public boolean onSave(Object object) {
                if (object != null) {
                    File file = (File) object;
                    DBInfor.replaceDatabase(AbsHomeActivity.this, file.getPath());
                }
                return true;
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

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == EncryptCheckService.SERVICE_CHECK) {
            boolean isExist = msg.getData().getBoolean("existed");
            if (isExist) {
                int size = msg.getData().getInt("size");
                String text = getResources().getString(R.string.encrypt_check_service_isexist);
                text = String.format(text, size);
                new AlertDialog.Builder(AbsHomeActivity.this)
                        .setTitle(R.string.warning)
                        .setMessage(text)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showProgress(getString(R.string.checking));
                                mEncryptCheckService.encrypt();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
            else {
                showToastLong(getString(R.string.encrypt_check_service_isnotexist));
            }
        }
        else if (msg.what == EncryptCheckService.SERVICE_ENCRYPT) {
            showToastLong(getString(R.string.encrypt_check_service_encryptok));
        }

        dismissProgress();
        return false;
    }

    @Override
    public void onClosed() {

    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onOpened() {

    }

    @Override
    protected void onOrentaionChanged(Configuration newConfig) {
        if (slidingViewManager != null) {
            slidingViewManager.onOrentaionChanged(newConfig.orientation);
        }
    }

    @Override
    public void onColorChanged(String key, int newColor) {
        if (key.equals(ColorRes.ACTIONBAR_BK)) {
            mActionBar.updateBackground(newColor);
        }
    }

    @Override
    public void onColorSelected(int color) {

    }

    @Override
    public void onColorSelected(List<ColorPickerSelectionData> list) {
        for (ColorPickerSelectionData data:list) {
            JResource.updateColor(data.getKey(), data.getColor());
        }
        JResource.saveColorUpdate(this);
    }

    @Override
    public void onColorCancleSelect() {
        applyExtendColors();
    }

    @Override
    public void onApplyDefaultColors() {
        JResource.removeColor(ColorRes.ACTIONBAR_BK);
        JResource.saveColorUpdate(this);
        applyExtendColors();
    }

    protected void applyExtendColors() {
        mActionBar.updateBackground(JResource.getColor(this, ColorRes.ACTIONBAR_BK, R.color.actionbar_bk_blue));
    }


}
