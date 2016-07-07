package com.jing.app.jjgallery.viewsystem.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.Toast;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.DBInfor;
import com.jing.app.jjgallery.service.file.EncryptCheckService;
import com.jing.app.jjgallery.viewsystem.main.filesystem.LoadFromDialog;
import com.jing.app.jjgallery.viewsystem.main.settings.SettingsActivity;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;

import java.io.File;
import java.util.HashMap;

/**
 * Created by JingYang on 2016/7/7 0007.
 * Description:
 * 将主界面通用的功能在该类实现，比如：检查全部加密文件、导出数据、导入数据、切换主题
 */
public abstract class AbsHomeActivity extends BaseActivity implements Handler.Callback {

    private EncryptCheckService mEncryptCheckService;
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_check_all_unencrypted:
                showProgress(getString(R.string.checking));
                if (mEncryptCheckService == null) {
                    mEncryptCheckService = new EncryptCheckService(this);
                }
                mEncryptCheckService.check();
                break;
            case R.id.menu_export:
                DBInfor.export(this);
                break;
            case R.id.menu_import:
                openLoadFromDialog();
                break;
            case R.id.menu_edit:
                startActivity(new Intent().setClass(this, SettingsActivity.class));
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
                if (needOptionWhenExit()) {

                }
                else {
                    onExit();
                }
                break;
        }
        return false;
    }

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
}
