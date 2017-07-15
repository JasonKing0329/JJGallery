package com.jing.app.jjgallery.gdb;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.jing.app.jjgallery.JJApplication;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.toast.TastyToast;

/**
 * Created by Administrator on 2017/5/20 0020.
 */

public abstract class GBaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayHelper.enableFullScreen();
        DisplayHelper.disableScreenshot(this);
        
        super.onCreate(savedInstanceState);

        // SImageLoader缓存机制只是根据key进行第一次ImageView大小的缓存
        // 如果先打开了StarListActivity，再跳转到其他界面加载出来的缓存过的图片就太小了，导致严重模糊
        // 因此，在这里删除图片缓存，迫使所有图片重新加载
        SImageLoader.getInstance().removeCache();

        progressDialog = new ProgressDialog(this);

        setContentView(getContentView());
        initController();
        initView();
        initBackgroundWork();
    }

    protected abstract int getContentView();

    protected abstract void initController();

    protected abstract void initView();

    protected abstract void initBackgroundWork();

    public void showProgress(String text) {
        progressDialog.setMessage(text);
        progressDialog.show();
    }

    public boolean dismissProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            return true;
        }
        return  false;
    }

    public void showProgressCycler() {
        showProgress(getString(R.string.loading));
    }

    public boolean dismissProgressCycler() {
        return dismissProgress();
    }

    public void showToastLong(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void showToastShort(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(String text, int type) {
        showToastLib(text, type, TastyToast.LENGTH_LONG);
    }

    public void showToastShort(String text, int type) {
        showToastLib(text, type, TastyToast.LENGTH_SHORT);
    }

    public void showToastLib(String text, int type, int time) {
        switch (type) {
            case ProgressProvider.TOAST_SUCCESS:
                TastyToast.makeText(this, text, time, TastyToast.SUCCESS);
                break;
            case ProgressProvider.TOAST_ERROR:
                TastyToast.makeText(this, text, time, TastyToast.ERROR);
                break;
            case ProgressProvider.TOAST_WARNING:
                TastyToast.makeText(this, text, time, TastyToast.WARNING);
                break;
            case ProgressProvider.TOAST_INFOR:
                TastyToast.makeText(this, text, time, TastyToast.INFO);
                break;
            case ProgressProvider.TOAST_DEFAULT:
                TastyToast.makeText(this, text, time, TastyToast.DEFAULT);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((JJApplication) getApplication()).removeActivity(this);
    }

}
